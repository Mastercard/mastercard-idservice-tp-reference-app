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
import org.openapitools.client.model.UserAccountActivities;
import org.openapitools.client.model.UserAccountActivityItemContents;
import org.openapitools.client.model.UserAccountActivityItems;
import org.openapitools.client.model.UserAccountActivitySearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mastercard.dis.mids.reference.util.Constants.X_MIDS_USERAUTH_SESSIONID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserAccountActivitySearchServiceImplTest {

    public static final String CREATED_DATE = "2021-04-09T18:55:35.944Z";
    public static final String ACTION_SAMPLE = "ID Created";
    public static final String CONTENT_VALUE = "5ed2cf35-a125-aaaaaaaaaa-bbbbbb-ccc";
    public static final String ROTATE_PDS = "test-value";

    @InjectMocks
    private UserAccountActivitySearchServiceImpl userAccountActivitySearchService;

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
    }

    @Test
    void testRetrieveUserActivities_Success() throws ApiException {
       doReturn(new ApiResponse<>(200, headers, createRetrieveUserActivities())).when(apiClientMock).execute(any(),any());
        UserAccountActivities result = userAccountActivitySearchService.retrieveUserActivities(new UserAccountActivitySearch());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("test-value", result.getRotatedPds()));
    }

    @Test
    void testRetrieveUserActivities_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("exception converted to ServiceException")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));
        UserAccountActivitySearch userAccountActivitySearch = new UserAccountActivitySearch();
        Assertions.assertThrows(ServiceException.class, () -> userAccountActivitySearchService.retrieveUserActivities(userAccountActivitySearch));
        verify(apiClientMock, atMostOnce()).execute(any(), any());
    }

    private UserAccountActivities createRetrieveUserActivities(){
        UserAccountActivities userAccountActivities = new UserAccountActivities();
        List<UserAccountActivityItems> userAccountActivitiesList = new ArrayList<>();
        List<UserAccountActivityItemContents> userAccountActivityItemContentsList = new ArrayList<>();
        UserAccountActivityItems userAccountActivityItems = new UserAccountActivityItems();
        UserAccountActivityItemContents userAccountActivityItemContents = new UserAccountActivityItemContents();
        userAccountActivityItems.setAction(ACTION_SAMPLE);
        userAccountActivityItems.setCreatedDate(CREATED_DATE);
        userAccountActivityItemContents.setAttributeName(null);
        userAccountActivityItemContents.setPreviousValue(null);
        userAccountActivityItemContents.setValue(CONTENT_VALUE);
        userAccountActivityItemContents.setSource(null);
        userAccountActivityItemContentsList.add(userAccountActivityItemContents);
        userAccountActivityItems.setValues(userAccountActivityItemContentsList);
        userAccountActivitiesList.add(userAccountActivityItems);
        userAccountActivities.setUserAccountActivities(userAccountActivitiesList);
        userAccountActivities.setRotatedPds(ROTATE_PDS);
        return userAccountActivities;
    }
}