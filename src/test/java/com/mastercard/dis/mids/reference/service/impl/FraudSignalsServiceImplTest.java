package com.mastercard.dis.mids.reference.service.impl;


import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.FraudDataApi;
import org.openapitools.client.model.FraudDataResultSearch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FraudSignalsServiceImplTest {


    @InjectMocks
    private  FraudSignalsServiceImpl fraudSignalsServiceImpl;

    @Mock
    private  FraudDataApi fraudDataApi;

    @Mock
    private  ExceptionUtil exceptionUtil;

    @Mock
    private ApiClient apiClientMock;


    @Test
    void performFraudDataSearchSignals_ok() throws ApiException {

        FraudDataResultSearch sampleFraudDataResultSearch = createFraudDataResultSearch();
        doReturn(sampleFraudDataResultSearch).when(fraudDataApi).searchFraudData(any());
        fraudSignalsServiceImpl.performFraudDataSearchSignals(any());
        verify(fraudDataApi, times(1)).searchFraudData(any());

    }

    @Test
    void createFraudSignal_ok() throws ApiException {

        doNothing().when(fraudDataApi).createFraudData(any());
        fraudSignalsServiceImpl.createFraudSignal(any());
        verify(fraudDataApi, times(1)).createFraudData(any());
    }

    @Test
    void performFraudDataSearchSignals_error() throws ApiException {

        doThrow(new ServiceException("Error while processing request")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));
        doThrow(new ApiException()).when(fraudDataApi).searchFraudData(any());

        try{
            fraudSignalsServiceImpl.performFraudDataSearchSignals(any());
        }catch (ServiceException serviceException){
            Assertions.assertNotNull(serviceException);
        }

    }

    @Test
    void createFraudSignal_Error() throws ApiException {

        doThrow(new ServiceException("Error while processing request")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));
        doThrow(new ApiException()).when(fraudDataApi).createFraudData(any());

        try {
            fraudSignalsServiceImpl.createFraudSignal(any());
        }catch (ServiceException serviceException){
            Assertions.assertNotNull(serviceException);
        }

    }



     public FraudDataResultSearch createFraudDataResultSearch(){
         FraudDataResultSearch fraudDataResultSearch = new FraudDataResultSearch();
         fraudDataResultSearch.setScoreBand("YELLOW");
         fraudDataResultSearch.fraudDateTime("2021-12-02T21:47:03.575Z");

         return fraudDataResultSearch;
     }


}
