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

package com.mastercard.dis.mids.reference.config;

import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.interceptor.EncryptionDecryptionInterceptor;
import com.mastercard.dis.mids.reference.interceptor.SDKVersionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;

import static com.mastercard.dis.mids.reference.constants.Constants.X_ENCRYPTED_HEADER;

/**
 * This is ApiClient configuration, it will read properties from application.properties and create instance of ApiClient.
 */
@Slf4j
@Configuration
public class ApiClientConfiguration {

    @Value("${mastercard.api.base.path}")
    private String basePath;

    @Value("${mastercard.api.consumer.key}")
    private String consumerKey;

    @Value("${mastercard.api.keystore.alias}")
    private String keystoreAlias;

    @Value("${mastercard.api.keystore.password}")
    private String keystorePassword;

    @Value("${mastercard.api.key.file}")
    private Resource keyFile;

    @Value("${mastercard.client.userProfileId}")
    private String userProfileId;

    @Value("${mastercard.client.enrollment.workflowId}")
    private String workflowId;

    @Value("${mastercard.client.sessionId}")
    private String sessionId;

    @Value("${mastercard.client.transactionGroupId}")
    private String transactionGroupId;

    @Value("${mastercard.client.authentication.workflowId}")
    private String authenticationWorkflowId;

    @Value("${mastercard.client.multidoc.workflowId}")
    private String multidocWorkFlowId;

    @Value("${mastercard.user.selectedCountry}")
    private String selectedUserCountry;

    //this header needs to be enabled to test encryption, use the same property for encryptionHeader
    @Value("${mastercard.client.encryption.enable}")
    private boolean encryptionEnabled;

    private String clientId;

    private static final String ERROR_MSG_CONFIGURING_CLIENT = "Error occurred while configuring ApiClient";

    @PostConstruct
    public void initialize() {
        if (null == keyFile || StringUtils.isEmpty(consumerKey)) {
            throw new ServiceException(".p12 file or consumerKey does not exist, please add details in application.properties");
        }
        clientId = (consumerKey.split("!"))[0];
        if (StringUtils.isBlank(clientId)){
            throw new ServiceException("Invalid Client configuration");
        }
    }

    @Bean
    public ApiClient apiClient(EncryptionDecryptionInterceptor encryptionDecryptionInterceptor, SDKVersionInterceptor sdkVersionInterceptor) {
        ApiClient client = new ApiClient();
        try {
            PrivateKey signingKey = AuthenticationUtils.loadSigningKey(keyFile.getFile().getAbsolutePath(), keystoreAlias, keystorePassword);
            client.setBasePath(basePath);
            client.setDebugging(true);
            client.setReadTimeout(40000);
            client.addDefaultHeader(X_ENCRYPTED_HEADER, encryptionEnabled? Boolean.TRUE.toString() : Boolean.FALSE.toString());

            return client.setHttpClient(client.getHttpClient()
                    .newBuilder()
                    .addInterceptor(sdkVersionInterceptor)
                    .addInterceptor(encryptionDecryptionInterceptor) // This interceptor will encrypt and decrypt the payload
                  .addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, signingKey))
                    .build()
            );
        } catch (Exception e) {
            log.error(ERROR_MSG_CONFIGURING_CLIENT, e);
            throw new ServiceException(ERROR_MSG_CONFIGURING_CLIENT);
        }
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTransactionGroupId() {
        return transactionGroupId;
    }

    public String getAuthenticationWorkflowId() {
        return authenticationWorkflowId;
    }

    public String getMultidocWorkFlowId() {
        return multidocWorkFlowId;
    }

    public String getSelectedUserCountry() {
        return selectedUserCountry;
    }

    public boolean isEncryptionEnabled() {
        return encryptionEnabled;
    }

    public String getClientId(){
        return clientId;
    }
}
