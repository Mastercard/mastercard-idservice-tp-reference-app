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

import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TpScopesRequestApi;
import org.openapitools.client.model.RPScopes;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TPScopesServiceImplTest {

    private final String ARID = "5d9140df-4fe5-4ff7-8317-dc4e85f409b5";
    public static final String X_MIDS_USERAUTH_SESSIONID = "x-mids-userauth-sessionid";

    @InjectMocks
    private TPScopesServiceImpl tpScopesService;

    @Mock
    private ExceptionUtil exceptionUtil;

    private final TpScopesRequestApi tpScopesRequestApi = new TpScopesRequestApi();

    Map<String, List<String>> headers;

    List<String> headersList;

    @BeforeEach
    void setUp() throws Exception {
        headers = new HashMap<>();
        headersList = new ArrayList<>();
        headersList.add(X_MIDS_USERAUTH_SESSIONID);
        headers.put(X_MIDS_USERAUTH_SESSIONID, headersList);
        ReflectionTestUtils.setField(tpScopesService, "tpScopesRequestApi", tpScopesRequestApi);
    }

    @Test
    public void retrieveRpScopes_validRpScopes_successResponse() throws ApiException {

        TPScopesServiceImpl tpScopesServiceMock = mock(TPScopesServiceImpl.class);
        RPScopes rpScopesReturn = getRpScopes();
        doReturn(rpScopesReturn).when(tpScopesServiceMock).getRpScopes(anyString());
        RPScopes response = tpScopesServiceMock.getRpScopes(ARID);
        assertNotNull(response);
        verify(tpScopesServiceMock, times(1)).getRpScopes(anyString());
    }

    @Test
    public void retrieveRpScopes_validRpScopes_exceptionResponse() throws ApiException {
        doReturn(new ServiceException("Test")).when(exceptionUtil).logAndConvertToServiceException(any());
        try {
            tpScopesService.getRpScopes(ARID);
            fail("Expecting ServiceException");
        } catch (ServiceException e) {
            verify(exceptionUtil, times(1)).logAndConvertToServiceException(any());
        }
    }

    private RPScopes getRpScopes() {
        RPScopes responseData = new RPScopes();
        responseData.setRpLogoUrl("url");
        responseData.setRpName("rpName");
        responseData.setScopes(Arrays.asList("Name:0:20"));
        return responseData;
    }
}