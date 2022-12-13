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
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.session.SessionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.model.CreatedEmailOtp;
import org.openapitools.client.model.EmailOtp;
import org.openapitools.client.model.OtpVerification;
import org.openapitools.client.model.OtpVerificationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mastercard.dis.mids.reference.util.Constants.X_MIDS_USERAUTH_SESSIONID;
import static com.mastercard.dis.mids.reference.util.Constants.X_USER_IDENTITY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailOtpServiceImplTest {

    @InjectMocks
    private EmailOtpServiceImpl emailOtpService;

    @Mock
    private ApiClientConfiguration apiClientConfigurationMock;

    @Mock
    private ApiClient apiClientMock;

    @Mock
    private ExceptionUtil exceptionUtilMock;

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
    void testCreateEmailOtp() throws ServiceException, ApiException {
        doReturn(new ApiResponse<>(201, headers, getOtp())).when(apiClientMock).execute(any(),any());

        CreatedEmailOtp result = emailOtpService.createEmailOtp(new EmailOtp());
        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );

    }

    @Test
    void testCreateOtpEmailError() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));

        EmailOtp emailOtp = new EmailOtp();
        Assertions.assertThrows(ServiceException.class, () -> emailOtpService.createEmailOtp(emailOtp));

        verify(apiClientMock, atLeastOnce()).execute(any(), any());
    }

    @Test
    void testCreateVerifyEmailOtp() throws ServiceException, ApiException {
      doReturn(new ApiResponse<>(201, headers, getVerifyOtpResult())).when(apiClientMock).execute(any(),any());

        OtpVerificationResult result = emailOtpService.verifyEmailOtp(new OtpVerification());

        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void testCreateVerifyEmailOtpError() throws ServiceException, ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));
        OtpVerification emailOtpVerification = new OtpVerification();
        Assertions.assertThrows(ServiceException.class, () -> emailOtpService.verifyEmailOtp(emailOtpVerification));
        verify(apiClientMock, atLeastOnce()).execute(any(), any());
    }

    private CreatedEmailOtp getOtp() {
        CreatedEmailOtp otp = new CreatedEmailOtp();
        otp.setTransactionId("transactionId");
        return otp;
    }

    private OtpVerificationResult getVerifyOtpResult() {
        OtpVerificationResult verifyOtpResult = new OtpVerificationResult();
        verifyOtpResult.setTransactionId("transactionId");
        return verifyOtpResult;
    }
}
