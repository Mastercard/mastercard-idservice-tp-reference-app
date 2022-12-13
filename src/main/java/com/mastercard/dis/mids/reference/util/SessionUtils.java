package com.mastercard.dis.mids.reference.util;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.session.SessionContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.ApiClient;
import org.openapitools.client.model.TpAuditMetadata;

import static com.mastercard.dis.mids.reference.util.Constants.X_USER_IDENTITY;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtils {

    public static void setupUserIdTokens(ApiClient apiClient) {
        apiClient.addDefaultHeader(X_USER_IDENTITY, SessionContext.get().getUserIdentityToken());
    }

    public static TpAuditMetadata getTpAuditMetadata(ApiClientConfiguration apiClientConfiguration) {
        TpAuditMetadata tpAuditMetadata = new TpAuditMetadata();
        tpAuditMetadata.setSessionId(apiClientConfiguration.getSessionId());
        tpAuditMetadata.setTransactionGroupId(apiClientConfiguration.getTransactionGroupId());
        return tpAuditMetadata;
    }

}