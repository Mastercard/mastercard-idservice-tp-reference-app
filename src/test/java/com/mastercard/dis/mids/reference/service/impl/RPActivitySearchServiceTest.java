package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.example.RPActivitySearchTokenExample;
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
import org.openapitools.client.model.RPActivities;
import org.openapitools.client.model.RPActivitySearch;

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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@ExtendWith(MockitoExtension.class)
class RPActivitySearchServiceTest {

    @InjectMocks
    private RPActivitySearchServiceImpl rPActivitySearchService;
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
    void testRPActivitySearch_Success() throws ServiceException, ApiException {
        doReturn(new ApiResponse<>(200, headers, getRPActivities())).when(apiClientMock).execute(any(), any());

        RPActivities result = rPActivitySearchService.rpActivitySearch(rPActivitySearchToken());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("rotatedPds", result.getRotatedPds())
        );
    }

    private RPActivities getRPActivities() {
        RPActivities rPActivities = new RPActivities();
        rPActivities.setRotatedPds("rotatedPds");
        return rPActivities;
    }

    @Test
    void testRPActivitySearch_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("exception converted to ServiceException")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));

        RPActivitySearch rPActivitySearchToken = RPActivitySearchTokenExample.getRPActivitySearchToken();
        Assertions.assertThrows(ServiceException.class, () -> rPActivitySearchService.rpActivitySearch(rPActivitySearchToken));
    }

    private RPActivitySearch rPActivitySearchToken() {
        RPActivitySearch rPActivitySearch = new RPActivitySearch();
        rPActivitySearch.setStartIndex(1);
        rPActivitySearch.setEndIndex(10);
        rPActivitySearch.setPds("PDS");
        rPActivitySearch.setUserConsent(ACCEPT);
        return rPActivitySearch;
    }
}
