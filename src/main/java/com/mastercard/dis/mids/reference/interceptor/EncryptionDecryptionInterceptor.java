/*
 Copyright (c) 2023 Mastercard

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.mastercard.dis.mids.reference.interceptor;

import com.mastercard.developer.utils.AuthenticationUtils;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.util.EncryptionUtils;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class EncryptionDecryptionInterceptor extends BaseInterceptor implements Interceptor {

    private static final String PDS_JSON_KEY = "pds";
    private static final String ROTATED_PDS_JSON_KEY = "rotatedPds";

    @Value("${mastercard.api.encryption.certificateFile}")
    private Resource encryptionCertificateFile;

    @Value("${mastercard.api.encryption.fingerPrint}")
    private String encryptionCertificateFingerPrint;

    @Value("${mastercard.api.decryption.keystore}")
    private Resource decryptionKeystore;

    @Value("${mastercard.api.decryption.alias}")
    private String decryptionKeystoreAlias;

    @Value("${mastercard.api.decryption.keystore.password}")
    private String decryptionKeystorePassword;

    @Value("${mastercard.client.encryption.enable}")
    private boolean isEncryptionEnable;

    @Value("${mastercard.client.decryption.enable}")
    private boolean isDecryptionEnable;

    private static PrivateKey signingKey;

    private final Map<String,Boolean> encryptionRequiredEndpoints = new HashMap<>();

    private final Map<String,Boolean> decryptionRequiredEndpoints = new HashMap<>();

    @Nonnull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = handleRequest(chain.request());
        Response response = chain.proceed(request);
        return handleResponse(request, response);
    }

    private Request handleRequest(Request request) {
        log.info("request.url(): {}", request.url().uri().getPath());
        if (isEncryptionEnable && isEncryptionRequired(request)) {
            try {
                String body = bodyToString(request);
                JSONObject requestJson = (JSONObject) JSONValue.parse(body);
                String pds = requestJson.getAsString(PDS_JSON_KEY);
                requestJson.remove(PDS_JSON_KEY);

                String encryptedData = EncryptionUtils.jweEncrypt(requestJson.toJSONString(), encryptionCertificateFile, encryptionCertificateFingerPrint);
                JSONObject encryptedRequestJson = new JSONObject();
                encryptedRequestJson.put("encryptedData", encryptedData);
                if (StringUtils.isNotBlank(pds)) {
                    encryptedRequestJson.put(PDS_JSON_KEY, pds);
                }
                log.info("Encrypted Payload sending to server: {}", encryptedRequestJson);
                Request.Builder post = request
                        .newBuilder()
                        .headers(request.headers())
                        .post(RequestBody.create(encryptedRequestJson.toJSONString(), MediaType.parse("application/json")));
                return post.build();

            } catch (Exception e) {
                log.error("Unable to encrypt request data to server", e);
                throw new ServiceException("Unable to encrypt request data to server", e);
            }
        }

        return request;
    }

    private synchronized Response handleResponse(Request request, Response encryptedResponse) {
        if (isDecryptionEnable && isDecryptionRequired(request)) {
            try {
                if (encryptedResponse.code() != 200) {
                    return encryptedResponse; // We will receive encrypted payload only for 200 response
                }
                ResponseBody responseBody = encryptedResponse.body();
                String encryptedResponseStr = Objects.requireNonNull(responseBody).string();
                log.info("Encrypted Payload received from server: {}", encryptedResponseStr);
                JSONObject encryptedResponseJson = (JSONObject) JSONValue.parse(encryptedResponseStr);
                if(signingKey == null){
                    String filePath = "src/main/resources/";
                    String[] fileName = decryptionKeystore.getURI().toString().split("/");
                    String finalFile = filePath+fileName[fileName.length-1];
                    signingKey = AuthenticationUtils.loadSigningKey(finalFile, decryptionKeystoreAlias, decryptionKeystorePassword);
                }
                String decryptedPayload = EncryptionUtils.jweDecrypt(encryptedResponseJson.getAsString("encryptedData"), (RSAPrivateKey) signingKey);

                JSONObject responseJson = (JSONObject) JSONValue.parse(decryptedPayload);
                String pds = encryptedResponseJson.getAsString(PDS_JSON_KEY);
                if (StringUtils.isNotBlank(pds)) {
                    responseJson.put(PDS_JSON_KEY, pds);
                }

                String rotatedPds = encryptedResponseJson.getAsString(ROTATED_PDS_JSON_KEY);
                if (StringUtils.isNotBlank(rotatedPds)) {
                    responseJson.put(ROTATED_PDS_JSON_KEY, rotatedPds);
                }

                Response.Builder responseBuilder = encryptedResponse.newBuilder();
                ResponseBody decryptedBody = ResponseBody.create(responseJson.toJSONString(), responseBody.contentType());

                return responseBuilder
                        .body(decryptedBody)
                        .header("Content-Length", String.valueOf(decryptedBody.contentLength()))
                        .build();
            } catch (Exception e) {
                log.error("Unable to decrypt response from server", e);
                throw new ServiceException("Unable to process response from server", e);
            }
        }

        return encryptedResponse;
    }

    private boolean isEncryptionRequired(Request request) {
        return encryptionRequiredEndpoints.getOrDefault(request.url().uri().getPath(),false);
    }

    private boolean isDecryptionRequired(Request request) {
        return decryptionRequiredEndpoints.getOrDefault(request.url().uri().getPath(),false);
    }

    @PostConstruct
    private void  initEncryptionRequiredEndpointsAndDecryptionRequired(){

        encryptionRequiredEndpoints.put("/idservice/claims/user-consents",true);
        encryptionRequiredEndpoints.put("/idservice/document-verifications/document-data-confirmations",true);
        encryptionRequiredEndpoints.put("/idservice/sms-otps",true);
        encryptionRequiredEndpoints.put("/idservice/email-otps",true);
        encryptionRequiredEndpoints.put("/idservice/sms-otp-verifications",true);
        encryptionRequiredEndpoints.put("/idservice/email-otp-verifications",true);

        encryptionRequiredEndpoints.put("/idservice/access-tokens",false);
        encryptionRequiredEndpoints.put("/idservice/document-verifications/document-data-retrievals",false);
        encryptionRequiredEndpoints.put("/idservice/multi-access-tokens",false);

        decryptionRequiredEndpoints.put("/idservice/document-verifications/document-data-retrievals",true);
        decryptionRequiredEndpoints.put("/idservice/initiate-authentications",true);
        decryptionRequiredEndpoints.put("/idservice/retrieve-rp-activities",true);
        decryptionRequiredEndpoints.put("/idservice/user-profiles/retrieve-identities",true);
        decryptionRequiredEndpoints.put("/idservice/tprp-claims",true);

        decryptionRequiredEndpoints.put("/idservice/access-tokens",false);
    }


}