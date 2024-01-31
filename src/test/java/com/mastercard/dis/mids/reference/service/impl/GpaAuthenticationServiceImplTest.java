package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.constants.TpVariables;
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
import org.openapitools.client.model.Authentications;
import org.openapitools.client.model.InitPremiumAuthentications;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.PDS;
import static com.mastercard.dis.mids.reference.constants.Constants.SDK_VERSION;
import static com.mastercard.dis.mids.reference.constants.Constants.USER_PROFILE_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.X_MIDS_USERAUTH_SESSIONID;
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
class GpaAuthenticationServiceImplTest {

    @InjectMocks
    GpaAuthenticationServiceImpl gpaAuthenticationServiceImpl;

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
        TpVariables.setUserProfileId("17005442-51cd-4e46-ab74-7a3a25c503ec");
    }


    @Test
    void initPremiumAuthentications_Success() throws ApiException {
        doReturn(new ApiResponse<>(200, headers, getAuthentications())).when(apiClientMock).execute(any(),any());
        Authentications result = gpaAuthenticationServiceImpl.initPremiumAuthentications(getInitPremiumAuthenticationToken());
        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void testInitPremiumAuthentications_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("exception converted to ServiceException")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));
        InitPremiumAuthentications initPremiumAuthentications = new InitPremiumAuthentications();
        Assertions.assertThrows(ServiceException.class, () -> gpaAuthenticationServiceImpl.initPremiumAuthentications(initPremiumAuthentications));
        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(Call.class), any(Type.class));

    }

    private Authentications getAuthentications() {
        Authentications authentications = new Authentications();
        authentications.setTransactionId("transactionId");
        return authentications;
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
}