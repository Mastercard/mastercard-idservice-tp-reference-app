package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.TPDataSharesService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PdsApi;
import org.openapitools.client.model.TPDataShareSuccessData;
import org.openapitools.client.model.TpDataShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TPDataSharesServiceImpl implements TPDataSharesService {

    private final PdsApi updatePdsApi;
    private final ExceptionUtil exceptionUtil;

    @Autowired
    public TPDataSharesServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil) {
        this.updatePdsApi = new PdsApi(apiClient);
        this.exceptionUtil = exceptionUtil;
    }

    @Override
    public TPDataShareSuccessData updatePdsData(TpDataShare tpDataShare) {
        try {
            return updatePdsApi.updatePdsData(tpDataShare);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }
}
