package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.ClaimsApiService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ClaimsSharingApi;
import org.openapitools.client.model.RPClaimsUserConsent;
import org.openapitools.client.model.RPClaimsUserConsentData;
import org.openapitools.client.model.RPClaimsUserData;
import org.openapitools.client.model.RPClaimsUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ClaimsApiServiceImpl implements ClaimsApiService {

    private final ClaimsSharingApi claimsSharingApi;
    private final ExceptionUtil exceptionUtil;

    @Autowired
    public ClaimsApiServiceImpl(ExceptionUtil exceptionUtil, ApiClient apiClient) {
        this.claimsSharingApi = new ClaimsSharingApi(apiClient);
        this.exceptionUtil = exceptionUtil;
    }

    @Override
    public RPClaimsUserConsentData getUserConsentStatus(RPClaimsUserConsent rpClaimsUserConsent) {
        try {
            return claimsSharingApi.getUserConsentStatus(rpClaimsUserConsent);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public RPClaimsUserData extractClaimsUserData(RPClaimsUserDetails rpClaimsUserDetails) {
        try {
            return claimsSharingApi.extractClaimsUserData(rpClaimsUserDetails);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }
}