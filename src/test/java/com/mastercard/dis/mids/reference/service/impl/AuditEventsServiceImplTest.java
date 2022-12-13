package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import okhttp3.Call;
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
import org.openapitools.client.model.AuditEvents;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.mastercard.dis.mids.reference.util.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.util.Constants.USER_PROFILE_ID;
import static com.mastercard.dis.mids.reference.util.Constants.X_MIDS_USERAUTH_SESSIONID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditEventsServiceImplTest {

    @InjectMocks
    private AuditEventsServiceImpl auditEventsServiceImpl;


    @Mock
    private ApiClient apiClientMock;

    @Mock
    private ExceptionUtil exceptionUtilMock;

    Map<String, List<String>> headers;

    List<String> headersList;

    @BeforeEach
    void setUp() {
        headers = new HashMap<>();
        headersList = new ArrayList<>();
        headersList.add(X_MIDS_USERAUTH_SESSIONID);
        headers.put(X_MIDS_USERAUTH_SESSIONID, headersList);
    }

    @Test
    void testAuditEvents_Test() throws ApiException {
        AuditEvents auditEvents = new AuditEvents();
        auditEvents.setSdkAuditEvents(new ArrayList<>());
        auditEvents.setUserProfileId(USER_PROFILE_ID);
        auditEvents.setCountryCode(COUNTRY_CODE);
        Call call = mock(Call.class);
        doReturn(new ApiResponse<>(200, null, null)).when(apiClientMock).execute(any());
        auditEventsServiceImpl.auditEvents(auditEvents);
        verify(apiClientMock, atMostOnce()).execute(any());
        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());

    }

    @Test
    void auditEvents_exception_Test() throws ServiceException {
        when(exceptionUtilMock.logAndConvertToServiceException(any(ApiException.class)))
                .thenThrow(new ServiceException("Error while processing request"));
        Assertions.assertThrows(ServiceException.class,
                () -> auditEventsServiceImpl.auditEvents(any(AuditEvents.class)));

    }
}
