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
import org.openapitools.client.api.PdsApi;
import org.openapitools.client.model.TPDataShareSuccessData;
import org.openapitools.client.model.TpDataShare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mastercard.dis.mids.reference.constants.Constants.PDS;
import static com.mastercard.dis.mids.reference.constants.Constants.USER_PROFILE_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.X_MIDS_USERAUTH_SESSIONID;
import static com.mastercard.dis.mids.reference.constants.Constants.X_USER_IDENTITY;
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
import static org.openapitools.client.model.UserConsent.ACCEPT;

@ExtendWith(MockitoExtension.class)
class TPDataSharesServiceImplTest {
    @InjectMocks
    private TPDataSharesServiceImpl tpDataSharesService;

    @Mock
    private ApiClientConfiguration apiClientConfigurationMock;

    @Mock
    private ApiClient apiClientMock;

    @Mock
    private ExceptionUtil exceptionUtilMock;

    @Mock
    private PdsApi pdsApi;

    Map<String, List<String>> headers;

    List<String> headersList;

    @BeforeEach
    void setUp() throws Exception {
        headersList = new ArrayList<>();
        headersList.add(X_MIDS_USERAUTH_SESSIONID);
        headersList.add(X_USER_IDENTITY);

        headers = new HashMap<>();
        headers.put(X_MIDS_USERAUTH_SESSIONID, headersList);
        headers.put(X_USER_IDENTITY, headersList);
    }

    @Test
    void testTpDataShares_updatePdsData_Success() throws ServiceException, ApiException {
        doReturn(new ApiResponse<>(200, headers, getTpDataShareSuccessData())).when(apiClientMock).execute(any(),any());
        TPDataShareSuccessData result = tpDataSharesService.updatePdsData(getTpDataShare());
        verify(apiClientMock, times(1)).execute(any(), any());
        assertAll(() -> assertNotNull(result), () -> assertEquals(PDS, result.getPds()));
    }

    @Test
    void testTpDataShares_updatePdsData_Error() throws ServiceException, ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("exception converted to ServiceException")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));
        TpDataShare tpDataShare = getTpDataShare();
        try{
             tpDataSharesService.updatePdsData(tpDataShare);
        }catch  (ServiceException exp) {
            Assertions.assertEquals("exception converted to ServiceException", exp.getMessage());
        }
        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(), any());
    }

    private TPDataShareSuccessData getTpDataShareSuccessData() {
        TPDataShareSuccessData data = new TPDataShareSuccessData();
        data.setPds(PDS);
        return data;
    }

    private TpDataShare getTpDataShare() {
        TpDataShare tpDataShare = new TpDataShare();
        tpDataShare.setPds(PDS);
        tpDataShare.setUserProfileId(USER_PROFILE_ID);
        tpDataShare.setUserConsent(ACCEPT);
        return tpDataShare;
    }
}
