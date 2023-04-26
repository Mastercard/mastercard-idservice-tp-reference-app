package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.constants.Constants;
import com.mastercard.dis.mids.reference.example.ClaimsApiExample;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ClaimsSharingApi;
import org.openapitools.client.model.RPClaimsUserConsent;
import org.openapitools.client.model.RPClaimsUserConsentData;
import org.openapitools.client.model.RPClaimsUserData;
import org.openapitools.client.model.RPClaimsUserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimsApiServiceImplTest {

    @InjectMocks
    private ClaimsApiServiceImpl claimsApiService;

    @Mock
    private ClaimsSharingApi claimsSharingApi;

    @Mock
    private ApiClient apiClient;

    @Mock
    private ExceptionUtil exceptionUtil;

    @Test
    void getUserConsentStatusTest() throws ApiException {
        ReflectionTestUtils.setField(claimsApiService, "claimsSharingApi", claimsSharingApi);
        RPClaimsUserConsent rpClaimsUserConsent = new RPClaimsUserConsent();
        rpClaimsUserConsent.setArid(UUID.fromString("df52649e-4096-456a-bca0-751ee470009f"));
        RPClaimsUserConsentData rpClaimsUserConsentData = new RPClaimsUserConsentData();
        rpClaimsUserConsentData.setPds("ZGZnZGVmZ2RnZGVnZXJnZXJncmRnZXJ5aGdld3J0eWJld3J5dHdleXd5d3l3cmFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFh");
        when(claimsSharingApi.getUserConsentStatus(any(RPClaimsUserConsent.class))).thenReturn(rpClaimsUserConsentData);
        RPClaimsUserConsentData rpClaimsUserConsentDataActual = claimsApiService.getUserConsentStatus(rpClaimsUserConsent);
        Assertions.assertEquals("ZGZnZGVmZ2RnZGVnZXJnZXJncmRnZXJ5aGdld3J0eWJld3J5dHdleXd5d3l3cmFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFh", rpClaimsUserConsentDataActual.getPds());
    }

    @Test
    void getUserConsentStatusTest_Exception() throws ApiException {
        ReflectionTestUtils.setField(claimsApiService, "claimsSharingApi", claimsSharingApi);
        Constants.setAridValue("7ec89f22-8b4c-44ad-80a5-088c87bd61df");
        RPClaimsUserConsent rpClaimsUserConsent = ClaimsApiExample.getUserConsentStatusExample();
        RPClaimsUserConsentData rpClaimsUserConsentData = new RPClaimsUserConsentData();
        rpClaimsUserConsentData.setPds("ZGZnZGVmZ2RnZGVnZXJnZXJncmRnZXJ5aGdld3J0eWJld3J5dHdleXd5d3l3cmFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFh");
        when(claimsSharingApi.getUserConsentStatus(any(RPClaimsUserConsent.class))).thenThrow(new ApiException());
        when(exceptionUtil.logAndConvertToServiceException(any(ApiException.class))).thenThrow(new ServiceException("Error while processing request"));
        Assertions.assertThrows(ServiceException.class, () -> claimsApiService.getUserConsentStatus(rpClaimsUserConsent));
    }

    @Test
    void extractClaimsUserDataTest() throws ApiException {
        ReflectionTestUtils.setField(claimsApiService, "claimsSharingApi", claimsSharingApi);
        RPClaimsUserDetails rpClaimsUserDetails = new RPClaimsUserDetails();
        rpClaimsUserDetails.setPds("ZGZnZGVmZ2RnZGVnZXJnZXJncmRnZXJ5aGdld3J0eWJld3J5dHdleXd5d3l3cmFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFh");
        RPClaimsUserData rpClaimsUserData = new RPClaimsUserData();
        rpClaimsUserData.setTransactionId("df52649e-4096-456a-bca0-751ee470009f");
        when(claimsSharingApi.extractClaimsUserData(any(RPClaimsUserDetails.class))).thenReturn(rpClaimsUserData);
        RPClaimsUserData rpClaimsUserDataActual = claimsApiService.extractClaimsUserData(rpClaimsUserDetails);
        Assertions.assertEquals("df52649e-4096-456a-bca0-751ee470009f", rpClaimsUserDataActual.getTransactionId());
    }

    @Test
    void extractClaimsUserDataTest_Exception() throws ApiException {
        ReflectionTestUtils.setField(claimsApiService, "claimsSharingApi", claimsSharingApi);
        RPClaimsUserDetails rpClaimsUserDetails = ClaimsApiExample.extractClaimsUserDataExample();
        RPClaimsUserData rpClaimsUserData = new RPClaimsUserData();
        rpClaimsUserData.setTransactionId("df52649e-4096-456a-bca0-751ee470009f");
        when(claimsSharingApi.extractClaimsUserData(any(RPClaimsUserDetails.class))).thenThrow(new ApiException());
        when(exceptionUtil.logAndConvertToServiceException(any(ApiException.class))).thenThrow(new ServiceException("Error while processing request"));
        Assertions.assertThrows(ServiceException.class, () -> claimsApiService.extractClaimsUserData(rpClaimsUserDetails));
    }

}