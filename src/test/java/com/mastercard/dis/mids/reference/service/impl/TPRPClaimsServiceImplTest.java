/*
 Copyright (c) 2023 Mastercard

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
import com.mastercard.dis.mids.reference.constants.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ClaimsSharingApi;
import org.openapitools.client.model.ClaimIdentities;
import org.openapitools.client.model.ClaimScopes;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.mastercard.dis.mids.reference.constants.Constants.X_USER_IDENTITY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TPRPClaimsServiceImplTest {

    @InjectMocks
    TPRPClaimsServiceImpl TPRPClaimsServiceImpl;

    @Mock
    ExceptionUtil exceptionUtil;

    @Mock
    ApiClientConfiguration apiClientConfiguration;

    Map<String, List<String>> headers;

    List<String> headersList;

    @BeforeEach
    void setUp() throws Exception {
        headers = new HashMap<>();
        headersList = new ArrayList<>();
        headersList.add(Constants.X_MIDS_USERAUTH_SESSIONID);
        headersList.add(X_USER_IDENTITY);
        headers.put(Constants.X_MIDS_USERAUTH_SESSIONID, headersList);
        headers.put(X_USER_IDENTITY, headersList);
        SessionContext.create(X_USER_IDENTITY);

        ClaimsSharingApi claimsSharingApi = new ClaimsSharingApi();
        claimsSharingApi.setApiClient(new ApiClient());
        claimsSharingApi.getApiClient().addDefaultHeader(X_USER_IDENTITY, SessionContext.get().getUserIdentityToken());
        ReflectionTestUtils.setField(TPRPClaimsServiceImpl, "claimsSharingApi", claimsSharingApi);

    }

    @Test
    void testTPRPClaimSharing_Success() throws ApiException {

        ClaimIdentities resultExpect = getClaimIdentities();
        TPRPClaimsServiceImpl mockTPRPClaimsServiceImpl = mock(TPRPClaimsServiceImpl.class);
        doReturn(resultExpect).when(mockTPRPClaimsServiceImpl).claimsSharing(any());
        ClaimIdentities result = mockTPRPClaimsServiceImpl.claimsSharing(getClaimScopes());
        assertAll(() -> assertNotNull(result), () -> assertEquals("transactionId", result.getTransactionId()));
    }

    @Test
    void testTPRPClaimSharing_Error()  {

        ClaimScopes claimScopes = getClaimScopes();

        doThrow(ServiceException.class).when(exceptionUtil).logAndConvertToServiceException(any());

        Assertions.assertThrows(ServiceException.class, ()-> TPRPClaimsServiceImpl.claimsSharing(claimScopes) );
    }

    private ClaimScopes getClaimScopes() {
        ClaimScopes claimScopes = new ClaimScopes();
        return claimScopes;
    }

    private ClaimIdentities getClaimIdentities() {
        ClaimIdentities claimIdentities = new ClaimIdentities();
        claimIdentities.setTransactionId("transactionId");
        claimIdentities.setRpSpecificIdentifier("rpSpecificId");
        return claimIdentities;
    }

}
