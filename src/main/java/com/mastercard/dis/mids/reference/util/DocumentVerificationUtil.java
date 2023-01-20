package com.mastercard.dis.mids.reference.util;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.session.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.EmailOtpApi;
import org.openapitools.client.model.FraudDetection;
import org.openapitools.client.model.FraudDetectionNuDetectMeta;
import org.openapitools.client.model.TpAuditMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.mastercard.dis.mids.reference.constants.Constants.BEHAVIOUR_DATA;
import static com.mastercard.dis.mids.reference.constants.Constants.REMOTE_IP;
import static com.mastercard.dis.mids.reference.constants.Constants.REQUEST_URL;
import static com.mastercard.dis.mids.reference.constants.Constants.SESSION_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.USER_AGENT;
import static com.mastercard.dis.mids.reference.constants.Constants.X_FORWARDED_FOR;
import static com.mastercard.dis.mids.reference.constants.Constants.X_USER_IDENTITY;

@Slf4j
@Service
public class DocumentVerificationUtil {

    private final EmailOtpApi otpApi;
    private final ApiClientConfiguration apiClientConfiguration;

    @Autowired
    public DocumentVerificationUtil(ApiClient apiClient, ApiClientConfiguration apiClientConfiguration) {
        this.otpApi = new EmailOtpApi(apiClient);
        this.apiClientConfiguration = apiClientConfiguration;
    }

    public void setupUserIdentityTokens() {
        otpApi.getApiClient().addDefaultHeader(X_USER_IDENTITY, SessionContext.get().getUserIdentityToken());
    }

    public TpAuditMetadata getTpAuditMetadata() {
        TpAuditMetadata tpAuditMetadata = new TpAuditMetadata();
        tpAuditMetadata.setSessionId(apiClientConfiguration.getSessionId());
        tpAuditMetadata.setTransactionGroupId(apiClientConfiguration.getTransactionGroupId());
        return tpAuditMetadata;
    }

    public void createSessionContext(Map<String, List<String>> headers) {
        try {
            List<String> userIdentityToken = headers.get(X_USER_IDENTITY);
            if (userIdentityToken != null) {
                SessionContext.create(userIdentityToken.stream().iterator().next());
            } else {
                throw new ServiceException("Unable to createSessionContext");
            }
        } catch (Exception e) {
            throw new ServiceException("Exception occurred while creating session context");
        }
    }

    public FraudDetectionNuDetectMeta createFraudDetectMeta() {
        FraudDetectionNuDetectMeta fraudDetectionNuDetectMeta = new FraudDetectionNuDetectMeta();
        fraudDetectionNuDetectMeta.setUserAgent(USER_AGENT);
        fraudDetectionNuDetectMeta.setSessionId(SESSION_ID);
        fraudDetectionNuDetectMeta.setBehaviourData(BEHAVIOUR_DATA);
        fraudDetectionNuDetectMeta.setRemoteIp(REMOTE_IP);
        fraudDetectionNuDetectMeta.setRequestUrl(REQUEST_URL);
        fraudDetectionNuDetectMeta.setxForwardedFor(X_FORWARDED_FOR);
        return fraudDetectionNuDetectMeta;
    }

    public FraudDetection getFraudDetection() {
        FraudDetection fraudDetection = new FraudDetection();
        FraudDetectionNuDetectMeta fraudDetectionNuDetectMeta = createFraudDetectMeta();
        fraudDetection.setNuDetectMeta(fraudDetectionNuDetectMeta);
        return fraudDetection;
    }
}
