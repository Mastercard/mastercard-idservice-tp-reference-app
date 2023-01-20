package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
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
import org.openapitools.client.api.UserProfileApi;
import org.openapitools.client.model.ClientIdentities;
import org.openapitools.client.model.IdentityAttributeDeleted;
import org.openapitools.client.model.IdentityAttributeDeletions;
import org.openapitools.client.model.IdentitySearch;
import org.openapitools.client.model.UserProfile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mastercard.dis.mids.reference.constants.Constants.ATTRIBUTE_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.PDS;
import static com.mastercard.dis.mids.reference.constants.Constants.X_MIDS_USERAUTH_SESSIONID;
import static com.mastercard.dis.mids.reference.constants.Constants.X_USER_IDENTITY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    public static final String COUNTRY_CODE = "countryCode";
    public static final String USER_PROFILE_ID = "userProfileId";
    public static final String USER_CONSENT = "ACCEPT";
    private static final String SDK_VERSION = "sdkVersion";

    @InjectMocks
    private UserProfileServiceImpl userProfileServiceImpl;

    @Mock
    private ApiClientConfiguration apiClientConfigurationMock;

    @Mock
    private ApiClient apiClientMock;

    @Mock
    private ExceptionUtil exceptionUtilMock;

    @Mock
    private UserProfileApi userProfileApi;

    Map<String, List<String>> headers;

    List<String> headersList;

    @BeforeEach
    void setUp() {
        headers = new HashMap<>();
        headersList = new ArrayList<>();
        headersList.add(X_MIDS_USERAUTH_SESSIONID);
        headersList.add(X_USER_IDENTITY);
        headers.put(X_MIDS_USERAUTH_SESSIONID, headersList);
        headers.put(X_USER_IDENTITY, headersList);
    }

    @Test
    void testRetrieveIdentities_Success() throws ApiException {
        doReturn(new ApiResponse<>(200, headers, getClientIdentities())).when(apiClientMock).execute(any(), any());

        ClientIdentities clientIdentities = userProfileServiceImpl.retrieveIdentities(getIdentitySearch());
        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(clientIdentities)
        );
    }

    @Test
    void testInitializeReAuthentication_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("Error while processing request")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));

        IdentitySearch identitySearch = new IdentitySearch();
        Assertions.assertThrows(ServiceException.class, () -> userProfileServiceImpl.retrieveIdentities(identitySearch));
        verify(apiClientMock, atMostOnce()).buildCall(anyString(), anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(Call.class), any(Type.class));

    }

    private ClientIdentities getClientIdentities() {
        ClientIdentities clientIdentities = new ClientIdentities();
        clientIdentities.setAttributes(new HashMap<>());
        return clientIdentities;
    }

    private IdentitySearch getIdentitySearch() {
        IdentitySearch identitySearch = new IdentitySearch();
        identitySearch.setUserConsent(ACCEPT);
        identitySearch.setScopedFields(Collections.singletonList(IdentitySearch.ScopedFieldsEnum.ALL));
        identitySearch.setPds(PDS);
        return identitySearch;
    }

    private IdentityAttributeDeletions getIdentityAttributeDeletions() {
        IdentityAttributeDeletions identityAttributeDeletions = new IdentityAttributeDeletions();
        identityAttributeDeletions.setPds(PDS);
        identityAttributeDeletions.setUserConsent(ACCEPT);
        identityAttributeDeletions.setAttributeName(IdentityAttributeDeletions.AttributeNameEnum.DRIVER_LICENSE);
        identityAttributeDeletions.setAttributeId(ATTRIBUTE_ID);
        return identityAttributeDeletions;
    }

    private IdentityAttributeDeleted getIdentityAttributeDeleted() {
        IdentityAttributeDeleted identityAttributeDeleted = new IdentityAttributeDeleted();
        identityAttributeDeleted.setPds(PDS);
        return identityAttributeDeleted;
    }

    @Test
    void userProfileRegistration_Test() throws ApiException {
        UserProfile userProfile = new UserProfile();
        userProfile.setCountryCode(COUNTRY_CODE);
        userProfile.setUserProfileId(USER_PROFILE_ID);
        Call call = mock(Call.class);
        doReturn(new ApiResponse<>(200, null, null)).when(apiClientMock).execute(any());

        userProfileServiceImpl.userProfileRegistration(userProfile);
        verify(apiClientMock, atMostOnce()).execute(any(Call.class));
        verify(apiClientMock, atMostOnce()).buildCall(anyString(), anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
    }

    @Test
    void userProfileRegistration_exception_Test() {
        UserProfile userProfile = new UserProfile();
        userProfile.setCountryCode(COUNTRY_CODE);
        userProfile.setUserProfileId(USER_PROFILE_ID);
        when(exceptionUtilMock.logAndConvertToServiceException(any(ApiException.class)))
                .thenThrow(new ServiceException("Error while processing request"));
        Assertions.assertThrows(ServiceException.class,
                () -> userProfileServiceImpl.userProfileRegistration(any(UserProfile.class)));
    }

    @Test
    void userProfile_Delete_successful_Test() throws ApiException {
        when(apiClientMock.execute(any())).thenReturn(new ApiResponse<>(200, null, null));
        userProfileServiceImpl.userProfileDelete(USER_PROFILE_ID, USER_CONSENT);
        verify(apiClientMock, atMostOnce()).execute(any(Call.class));
        verify(apiClientMock, atMostOnce()).buildCall(anyString(), anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
    }

    @Test
    void userProfile_Delete_exception_Test() {
        when(exceptionUtilMock.logAndConvertToServiceException(any(ApiException.class)))
                .thenThrow(new ServiceException("Error while processing request"));
        Assertions.assertThrows(ServiceException.class,
                () -> userProfileServiceImpl.userProfileDelete(any(), USER_CONSENT));
    }

    @Test
    void identityAttributeDelete_successful() throws ApiException {
        doReturn(new ApiResponse<>(200, headers, getIdentityAttributeDeleted())).when(apiClientMock).execute(any(), any());
        IdentityAttributeDeleted identityAttributeDeleted = userProfileServiceImpl.deleteIdentityAttribute(getIdentityAttributeDeletions());
        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(identityAttributeDeleted)
        );
    }

    @Test
    void identityAttributeDelete_error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("Error while processing request")).when(exceptionUtilMock).logAndConvertToServiceException(any(ApiException.class));

        Assertions.assertThrows(ServiceException.class, () -> userProfileServiceImpl.deleteIdentityAttribute(getIdentityAttributeDeletions()));
        verify(apiClientMock, atMostOnce()).buildCall(anyString(), anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(Call.class), any(Type.class));
    }












}
