package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.RPActivitySearchService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PdsApi;
import org.openapitools.client.model.RPActivities;
import org.openapitools.client.model.RPActivitySearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mastercard.dis.mids.reference.util.Constants.X_USER_IDENTITY;

@Slf4j
@Service
public class RPActivitySearchServiceImpl implements RPActivitySearchService {

    private final PdsApi pdsApi;
    private final ExceptionUtil exceptionUtil;
    private final ApiClientConfiguration apiClientConfiguration;

    @Autowired
    public RPActivitySearchServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil, ApiClientConfiguration apiClientConfiguration) {
        pdsApi = new PdsApi(apiClient);
        this.exceptionUtil = exceptionUtil;
        this.apiClientConfiguration = apiClientConfiguration;

    }

    @Override
    public RPActivities rpActivitySearch(RPActivitySearch rPActivitySearch) {
        try {
            return pdsApi.searchRpActivity(rPActivitySearch, X_USER_IDENTITY, apiClientConfiguration.isEncryptionEnabled());
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }
}