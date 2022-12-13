package com.mastercard.dis.mids.reference.service.impl;



import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.FraudSignalsService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.FraudDataApi;
import org.openapitools.client.model.FraudData;
import org.openapitools.client.model.FraudSearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FraudSignalsServiceImpl implements FraudSignalsService {

    private final FraudDataApi fraudDataApi;
    private final ExceptionUtil exceptionUtil;

    @Autowired
    public FraudSignalsServiceImpl(FraudDataApi fraudDataApi, ExceptionUtil exceptionUtil) {
        this.fraudDataApi = fraudDataApi;
        this.exceptionUtil = exceptionUtil;
    }

    @Override
    public void createFraudSignal(FraudData fraudData) {
        try{
            fraudDataApi.createFraudData(fraudData);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public void performFraudDataSearchSignals(FraudSearchData fraudSearchData) {
        try{
            fraudDataApi.searchFraudData(fraudSearchData);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }


}
