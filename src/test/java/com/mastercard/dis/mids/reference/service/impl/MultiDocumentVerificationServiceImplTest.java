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
import com.mastercard.dis.mids.reference.example.MultiDocumentVerificationExample;
import com.mastercard.dis.mids.reference.example.MultiDocumentVerificationTokenExample;
import com.mastercard.dis.mids.reference.example.dto.MultiDocumentVerificationToken;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.session.SessionContext;
import com.mastercard.dis.mids.reference.util.DocumentVerificationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MultiDocumentVerificationApi;
import org.openapitools.client.model.AccessToken;
import org.openapitools.client.model.ConfirmDocumentData;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.DocumentVerificationConfirmData;
import org.openapitools.client.model.DocumentVerificationExtractedData;
import org.openapitools.client.model.FraudDetection;
import org.openapitools.client.model.MultiDocConfirmData;
import org.openapitools.client.model.MultiDocumentConfirmedPDS;
import org.openapitools.client.model.MultiDocumentDataRetrieval;
import org.openapitools.client.model.MultiRetrieveAccessToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.PDS;
import static com.mastercard.dis.mids.reference.constants.Constants.SDK_VERSION;
import static com.mastercard.dis.mids.reference.constants.Constants.USER_PROFILE_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.VISA_SUPPORTED_COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.X_MIDS_USERAUTH_SESSIONID;
import static com.mastercard.dis.mids.reference.constants.Constants.X_USER_IDENTITY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MultiDocumentVerificationServiceImplTest {

    @InjectMocks
    private MultiDocumentVerificationServiceImpl multiDocumentVerificationService;
    @Mock
    private ApiClient apiClientMock;
    @Mock
    private ApiClientConfiguration apiClientConfigurationMock;

    @Mock
    private DocumentVerificationUtil util;

    @Mock
    private MultiDocumentVerificationApi multiDocumentVerificationApi;

    @Mock
    private  ExceptionUtil exceptionUtil;

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
    void testMultiDocumentVerificationToken_GenerateTokenExample_Success() throws ServiceException, ApiException {

        MultiDocumentVerificationServiceImpl multiDocumentVerificationServiceMock = mock(MultiDocumentVerificationServiceImpl.class);
        AccessToken accessTokenReturn = getAccessToken();
        doReturn(accessTokenReturn).when(multiDocumentVerificationServiceMock).documentVerificationMultiAccessSDKToken(any());
        AccessToken result = multiDocumentVerificationServiceMock.documentVerificationMultiAccessSDKToken(getMultiDocumentVerificationToken());

        verify(multiDocumentVerificationServiceMock, times(1)).documentVerificationMultiAccessSDKToken(any());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void testDocumentVerificationToken_GenerateTokenExample_Error() throws ApiException {
        try{
        MultiDocumentVerificationToken multiDocumentVerificationToken = MultiDocumentVerificationTokenExample.getMultiDocumentVerificationToken();
        MultiDocumentVerificationServiceImpl multiDocumentVerificationServiceMock = mock(MultiDocumentVerificationServiceImpl.class);


            multiDocumentVerificationServiceMock.documentVerificationMultiAccessSDKToken(multiDocumentVerificationToken);
            multiDocumentVerificationService.documentVerificationMultiAccessSDKToken(multiDocumentVerificationToken);
        }catch (Exception serviceException){
            Assertions.assertNotNull(serviceException);

        }


    }

    @Test
    void testMultiDocumentVerificationStatus_success() throws ServiceException, ApiException {

       doReturn(new ApiResponse<>(200, headers, getDocumentVerificationExtractedData())).when(apiClientMock).execute(any(), any());

        DocumentVerificationExtractedData result = multiDocumentVerificationService.multiDocumentVerificationStatus(new MultiDocumentDataRetrieval());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @ParameterizedTest
    @CsvSource({"true", "false"})
    void testMultiDocumentVerificationConfirmation_success(boolean visaVerify) throws ServiceException, ApiException {

        doReturn(new ApiResponse<>(200, headers, MultiDocConfirmedDataUpdatedPDS())).when(apiClientMock).execute(any(), any());
        MultiDocumentConfirmedPDS multiDocumentConfirmedPDSRes = multiDocumentVerificationService.multiDocumentVerificationConfirmation(getDocumentVerificationConfirmData(visaVerify));

        Assertions.assertNotNull(multiDocumentConfirmedPDSRes);
        verify(apiClientMock, times(1)).execute(any(), any());
    }

    @Test
    void testUpdateIdentityAttributes_success() throws ServiceException, ApiException {
       doReturn(new ApiResponse<>(200, headers, getConfirmedDataUpdatedPDS())).when(apiClientMock).execute(any(), any());

        ConfirmedPDS confirmedPDS = multiDocumentVerificationService.updateIdentityAttributes(MultiDocumentVerificationExample.getUpdateIdentityAttributesData());
        Assertions.assertNotNull(confirmedPDS);
        Assertions.assertEquals("pds",confirmedPDS.getPds());

    }


    private MultiDocumentConfirmedPDS MultiDocConfirmedDataUpdatedPDS() {
        MultiDocumentConfirmedPDS confirmedDataUpdatedPDS = new MultiDocumentConfirmedPDS();
        confirmedDataUpdatedPDS.setPds("pds");
        confirmedDataUpdatedPDS.setTransactionId("transactionId");
        return confirmedDataUpdatedPDS;
    }


    private ConfirmedPDS getConfirmedDataUpdatedPDS() {
        ConfirmedPDS confirmedDataUpdatedPDS = new ConfirmedPDS();
        confirmedDataUpdatedPDS.setPds("pds");
        confirmedDataUpdatedPDS.setTransactionId("transactionId");
        return confirmedDataUpdatedPDS;
    }

    private MultiDocConfirmData getDocumentVerificationConfirmData(boolean verifyIfVisaMatched) {
        MultiDocConfirmData documentVerificationConfirmData = new MultiDocConfirmData();
        documentVerificationConfirmData.setDocumentData(new ConfirmDocumentData());
        documentVerificationConfirmData.setFraudDetection(new FraudDetection());

        if (verifyIfVisaMatched) {
            documentVerificationConfirmData.setVisaMatched(verifyIfVisaMatched);
            documentVerificationConfirmData.setCountryCode(VISA_SUPPORTED_COUNTRY_CODE);
        }
        return documentVerificationConfirmData;
    }


    private DocumentVerificationExtractedData getDocumentVerificationExtractedData() {
        DocumentVerificationExtractedData extractedData = new DocumentVerificationExtractedData();
        extractedData.setDocumentData(extractedData.getDocumentData());
        extractedData.setStatus("PENDING");
        extractedData.setTransactionId("transactionId");
        return extractedData;
    }

    private DocumentVerificationConfirmData getDocumentData() {
        DocumentVerificationConfirmData documentData = new DocumentVerificationConfirmData();
        documentData.getDocumentData().setAddressCity("New York");
        documentData.getDocumentData().setAddressCountry("USA");
        documentData.getDocumentData().setAddressLine1("123 Main St.");
        documentData.getDocumentData().setAddressLine2("New York");
        documentData.getDocumentData().setAddressZipCode("10021");
        documentData.getDocumentData().setDateOfBirth("2020-09-09");
        documentData.getDocumentData().setDocumentNumber("1LViI488YkFZh8YjNlLf61BMn29cmQn");
        documentData.getDocumentData().setDocumentType("passport");
        documentData.getDocumentData().setFirstName("John");
        documentData.getDocumentData().setLastName("Smith");
        documentData.getDocumentData().setExpiryDate("2020-10-10");
        documentData.getDocumentData().setGender("M");
        documentData.getDocumentData().setIssuingAuthority("United States");
        documentData.getDocumentData().placeOfBirth("Boston");
        documentData.getDocumentData().setIssuingCountry("USA");
        documentData.getDocumentData().setIssuingDate("2020-09-09");
        documentData.getDocumentData().setIssuingPlace("New York");
        documentData.getDocumentData().setAddressSubdivision("MO");
        documentData.getDocumentData().setFormattedAddress("220 BLVD O FALLON MO");
        return documentData;
    }

    private AccessToken getAccessToken() {
        AccessToken accessToken = new AccessToken();
        accessToken.setApiDataCenter("apiDataCenter");
        accessToken.sdkToken("sdkToken");
        accessToken.setTransactionId("transactionId");
        return accessToken;
    }

    private MultiDocumentVerificationToken getMultiDocumentVerificationToken() {
        MultiDocumentVerificationToken token = new MultiDocumentVerificationToken();
        token.setSdkVersion(SDK_VERSION);
        token.setCountryCode(COUNTRY_CODE);
        token.setChannelTypeEnum(MultiRetrieveAccessToken.ChannelTypeEnum.SDK);
        token.setUserProfileId(USER_PROFILE_ID);
        token.setPds(PDS);
        return token;
    }

}