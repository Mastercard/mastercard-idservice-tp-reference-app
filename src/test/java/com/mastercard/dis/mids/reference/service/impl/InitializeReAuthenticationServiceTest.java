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
import org.openapitools.client.model.AuthenticationResults;
import org.openapitools.client.model.Authentications;
import org.openapitools.client.model.InitPremiumAuthentications;
import org.openapitools.client.model.InitializeAuthentications;
import org.openapitools.client.model.VerifyAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.mastercard.dis.mids.reference.util.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.util.Constants.PDS;
import static com.mastercard.dis.mids.reference.util.Constants.PRIVACY_POLICY_VERSION;
import static com.mastercard.dis.mids.reference.util.Constants.SDK_VERSION;
import static com.mastercard.dis.mids.reference.util.Constants.USER_PROFILE_ID;
import static com.mastercard.dis.mids.reference.util.Constants.X_MIDS_USERAUTH_SESSIONID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@ExtendWith(MockitoExtension.class)
class InitializeReAuthenticationServiceTest {

    @InjectMocks
    private ReAuthenticationServiceImpl initializeReAuthenticationServiceImpl;
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
        headers.put(X_MIDS_USERAUTH_SESSIONID, headersList);
        when(apiClientConfigurationMock.getUserProfileId()).thenReturn(USER_PROFILE_ID);
    }

    @Test
    void testInitializeReAuthentication_Success() throws ApiException {
        doReturn(new ApiResponse<>(200, headers, getAuthentications())).when(apiClientMock).execute(any(), any());
        Authentications result = initializeReAuthenticationServiceImpl.initiateAuthentication(getInitializeReAuthenticationToken());
        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void testInitializeReAuthentication_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("Error while processing request")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));
        InitializeAuthentications initializeAuthentications = new InitializeAuthentications();
        Assertions.assertThrows(ServiceException.class, () -> initializeReAuthenticationServiceImpl.initiateAuthentication(initializeAuthentications));
        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(), any());

    }

    @Test
    void testAuthenticationResults_Success() throws ApiException {

        doReturn(new ApiResponse<>(200, headers, getAuthenticationsResults())).when(apiClientMock).execute(any(), any());
        AuthenticationResults result = initializeReAuthenticationServiceImpl.authenticationResults(getVerifyAuthenticationsToken());
        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void testAuthenticationResults_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("exception converted to ServiceException")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));

        VerifyAuthentication verifyAuthentication = new VerifyAuthentication();
        Assertions.assertThrows(ServiceException.class, () -> initializeReAuthenticationServiceImpl.authenticationResults(verifyAuthentication));
        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(), any());

    }

    private Authentications getAuthentications() {
        Authentications authentications = new Authentications();
        authentications.setTransactionId("transactionId");
        return authentications;
    }

    private AuthenticationResults getAuthenticationsResults() {
        AuthenticationResults authenticationResults = new AuthenticationResults();
        authenticationResults.setTransactionId("transactionId");
        return authenticationResults;
    }

    private InitializeAuthentications getInitializeReAuthenticationToken() {
        InitializeAuthentications initializeAuthentications = new InitializeAuthentications();
        initializeAuthentications.setChannelType(InitializeAuthentications.ChannelTypeEnum.WEB);
        initializeAuthentications.setCountryCode(COUNTRY_CODE);
        initializeAuthentications.sdkVersion(SDK_VERSION);
        initializeAuthentications.setUserProfileId(USER_PROFILE_ID);
        initializeAuthentications.setUserConsent(ACCEPT);
        initializeAuthentications.setPds(PDS);
        return initializeAuthentications;
    }

    private InitPremiumAuthentications getInitPremiumAuthenticationToken() {
        InitPremiumAuthentications initPremiumAuthentications = new InitPremiumAuthentications();
        initPremiumAuthentications.setChannelType(InitPremiumAuthentications.ChannelTypeEnum.SDK);
        initPremiumAuthentications.setCountryCode(COUNTRY_CODE);
        initPremiumAuthentications.sdkVersion(SDK_VERSION);
        initPremiumAuthentications.setUserProfileId(USER_PROFILE_ID);
        initPremiumAuthentications.setUserConsent(ACCEPT);
        initPremiumAuthentications.setPds(PDS);
        return initPremiumAuthentications;
    }

    private VerifyAuthentication getVerifyAuthenticationsToken() {
        VerifyAuthentication verifyAuthentication = new VerifyAuthentication();
        verifyAuthentication.setCountryCode(COUNTRY_CODE);
        verifyAuthentication.setPrivacyPolicyVersion(PRIVACY_POLICY_VERSION);
        return verifyAuthentication;
    }
}