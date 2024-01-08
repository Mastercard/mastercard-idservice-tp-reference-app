package com.mastercard.dis.mids.reference.service.impl;


import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.constants.TpVariables;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.GpaAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.GpaAuthenticationApi;
import org.openapitools.client.model.Authentications;
import org.openapitools.client.model.InitPremiumAuthentications;
import org.openapitools.client.model.TpAuditMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GpaAuthenticationServiceImpl implements GpaAuthenticationService {

    private final GpaAuthenticationApi gpaAuthenticationApi;
    private final ExceptionUtil exceptionUtil;
    private final ApiClientConfiguration apiClientConfiguration;

    @Autowired
    public GpaAuthenticationServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil, ApiClientConfiguration apiClientConfiguration) {
        gpaAuthenticationApi = new GpaAuthenticationApi(apiClient);
        this.exceptionUtil = exceptionUtil;
        this.apiClientConfiguration = apiClientConfiguration;
    }

    @Override
    public Authentications initPremiumAuthentications(final InitPremiumAuthentications initPremiumAuthentications) {
        try {
            initPremiumAuthentications.setUserProfileId(TpVariables.getUserProfileId());
            initPremiumAuthentications.setTpAuditMetadata(getTpAuditMetadata());
            return gpaAuthenticationApi.initiateStrongerAuthentication(initPremiumAuthentications);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    private TpAuditMetadata getTpAuditMetadata() {
        TpAuditMetadata tpAuditMetadata = new TpAuditMetadata();
        tpAuditMetadata.setSessionId(apiClientConfiguration.getSessionId());
        tpAuditMetadata.setTransactionGroupId(apiClientConfiguration.getTransactionGroupId());
        return tpAuditMetadata;
    }
}
