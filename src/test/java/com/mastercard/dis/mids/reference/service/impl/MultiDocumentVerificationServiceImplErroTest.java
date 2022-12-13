/*
 Copyright (c) 2021 Mastercard

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

package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.example.MultiDocumentVerificationExample;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.session.SessionContext;
import com.mastercard.dis.mids.reference.util.DocumentVerificationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MultiDocumentVerificationApi;
import org.openapitools.client.model.ConfirmDocumentData;
import org.openapitools.client.model.FraudDetection;
import org.openapitools.client.model.MultiDocConfirmData;
import org.openapitools.client.model.MultiDocumentDataRetrieval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mastercard.dis.mids.reference.util.Constants.VISA_SUPPORTED_COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.util.Constants.X_MIDS_USERAUTH_SESSIONID;
import static com.mastercard.dis.mids.reference.util.Constants.X_USER_IDENTITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
class MultiDocumentVerificationServiceImplErroTest {

    @InjectMocks
    private MultiDocumentVerificationServiceImpl multiDocumentVerificationService;
    @Mock
    private ApiClient apiClientMock;
    @Mock
    private ApiClientConfiguration apiClientConfigurationMock;

    @Mock
    private DocumentVerificationUtil util;

    @Mock
    private MultiDocumentVerificationApi multiDocumentVerificationApi;

    @Mock
    private  ExceptionUtil exceptionUtil;

    Map<String, List<String>> headers;

    List<String> headersList;

    @BeforeEach
    void setUp() throws Exception {
        headers = new HashMap<>();
        headersList = new ArrayList<>();
        headersList.add(X_MIDS_USERAUTH_SESSIONID);
        headersList.add(X_USER_IDENTITY);
        headers.put(X_MIDS_USERAUTH_SESSIONID, headersList);
        headers.put(X_USER_IDENTITY, headersList);
        SessionContext.create(X_USER_IDENTITY);

    }


    @Test
    void testMultiDocumentVerificationConfirmation_error() throws ServiceException, ApiException {

        try {
            ApiException apiException = new ApiException();
            ServiceException serviceExceptionReturn    = new ServiceException("test Exception");
            doThrow(apiException).when(apiClientMock).execute(any(), any());
            doThrow(serviceExceptionReturn).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));
            MultiDocConfirmData multiDocConfirmData = getDocumentVerificationConfirmData(true);
            multiDocumentVerificationService.multiDocumentVerificationConfirmation(multiDocConfirmData); // mover
        }catch (ServiceException serviceException){
            Assertions.assertNotNull(serviceException);
            Assertions.assertEquals("test Exception",serviceException.getMessage());
        }

    }


    @Test
    void testUpdateIdentityAttributes_error() throws ServiceException, ApiException {

        try {
            doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
            doThrow(new ServiceException("test Exception")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class)); // erro

            multiDocumentVerificationService.updateIdentityAttributes(MultiDocumentVerificationExample.getUpdateIdentityAttributesData());
        }catch (ServiceException serviceException){
            Assertions.assertNotNull(serviceException);
        }

    }

    @Test
    void testMultiDocumentVerificationStatus_error() throws ServiceException, ApiException {

        try {
            MultiDocumentDataRetrieval documentDataRetrievalExample = MultiDocumentVerificationExample.getMultiDocumentDataRetrieval();
            doThrow(ApiException.class).when(apiClientMock).execute(any(), any());
            doThrow(new ServiceException("test Exception")).when(exceptionUtil).logAndConvertToServiceException(any());

            multiDocumentVerificationService.multiDocumentVerificationStatus(documentDataRetrievalExample);
        }catch (ServiceException serviceException){
            Assertions.assertNotNull(serviceException);
            Assertions.assertEquals("test Exception",serviceException.getMessage());
        }

    }


    private MultiDocConfirmData getDocumentVerificationConfirmData(boolean verifyIfVisaMatched) {
        MultiDocConfirmData documentVerificationConfirmData = new MultiDocConfirmData();
        documentVerificationConfirmData.setDocumentData(new ConfirmDocumentData());
        documentVerificationConfirmData.setFraudDetection(new FraudDetection());

        if (verifyIfVisaMatched) {
            documentVerificationConfirmData.setVisaMatched(verifyIfVisaMatched);
            documentVerificationConfirmData.setCountryCode(VISA_SUPPORTED_COUNTRY_CODE);
        }
        return documentVerificationConfirmData;
    }


}