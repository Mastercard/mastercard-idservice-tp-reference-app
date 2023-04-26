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
import com.mastercard.dis.mids.reference.constants.Constants;
import com.mastercard.dis.mids.reference.example.DocumentDataRetrievalExample;
import com.mastercard.dis.mids.reference.example.DocumentVerificationTokenExample;
import com.mastercard.dis.mids.reference.example.dto.DocumentVerificationToken;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.session.SessionContext;
import com.mastercard.dis.mids.reference.util.DocumentVerificationUtil;
import okhttp3.Call;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.openapitools.client.api.DocumentVerificationApi;
import org.openapitools.client.model.AccessToken;
import org.openapitools.client.model.ConfirmDocumentData;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.DocumentDataRetrieval;
import org.openapitools.client.model.DocumentDataStatus;
import org.openapitools.client.model.DocumentIdConfirmation;
import org.openapitools.client.model.DocumentVerificationConfirmData;
import org.openapitools.client.model.DocumentVerificationExtractedData;
import org.openapitools.client.model.FraudDetection;
import org.openapitools.client.model.RetrieveAccessToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.SDK_VERSION;
import static com.mastercard.dis.mids.reference.constants.Constants.VISA_SUPPORTED_COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.X_MIDS_USERAUTH_SESSIONID;
import static com.mastercard.dis.mids.reference.constants.Constants.X_USER_IDENTITY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DocumentVerificationServiceImplTest {

    @InjectMocks
    private DocumentVerificationServiceImpl documentVerificationService;

    @Mock
    private ApiClientConfiguration apiClientConfigurationMock;

    @Mock
    private  DocumentVerificationApi documentVerificationApi;

    @Mock
    private ApiClient apiClientMock;

    @Mock
    private ExceptionUtil exceptionUtil;
    @Mock
    private DocumentVerificationUtil util;

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
        Constants.setAridValue("7ec89f22-8b4c-44ad-80a5-088c87bd61df");
    }

    @Test
    void testDocumentVerificationToken_GenerateTokenExample_Success() throws ServiceException, ApiException {
       doReturn(new ApiResponse<>(200, headers, getAccessToken())).when(apiClientMock).execute(any(), any());
        AccessToken result = documentVerificationService.retrieveDocumentVerificationToken(getDocumentVerificationToken());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void testDocumentVerificationToken_GenerateTokenExample_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("Error while processing request")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));

        DocumentVerificationToken documentVerificationToken = DocumentVerificationTokenExample.getDocumentVerificationTokenObject();
        Assertions.assertThrows(ServiceException.class, () -> documentVerificationService.retrieveDocumentVerificationToken(documentVerificationToken));

        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(Call.class), any(Type.class));
    }

    @ParameterizedTest
    @CsvSource({"true, true", "false, false", "true, false", "false, true"})
    @DisplayName("Data Confirmation API - Success - Visa Verify and Claim Sharing Combination and Without")
    void documentVerificationDataConfirmations_DataConfirmations_Success(boolean visaVerify, boolean claimSharingFlow) throws ApiException {
        doReturn(new ApiResponse<>(200, headers, getConfirmedDataUpdatedPDS())).when(apiClientMock).execute(any(), any());
        documentVerificationService.confirmDocumentData(getDocumentVerificationConfirmData(visaVerify, claimSharingFlow));
        verify(apiClientMock, times(1)).execute(any(), any());
    }

    @Test
    void testDocumentVerificationDataConfirmations_DataConfirmations_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("Error while processing request")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));
        DocumentVerificationConfirmData documentVerificationConfirmData = getDocumentVerificationConfirmData(false, false);
        Assertions.assertThrows(ServiceException.class, () -> documentVerificationService.confirmDocumentData(documentVerificationConfirmData));

        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(Call.class), any(Type.class));
    }

    @Test
    void testDocumentVerificationUpdateIdConfirmations_Success() throws ApiException {
        doReturn(new ApiResponse<>(200, headers)).when(apiClientMock).execute(any());
        documentVerificationService.updateIdConfirmations(new DocumentIdConfirmation());
        verify(apiClientMock, times(1)).execute(any());
    }

    @Test
    void testDocumentVerificationUpdateIdConfirmations_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any());
        doThrow(new ServiceException("Error while processing request")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));

        DocumentIdConfirmation documentIdConfirmation = new DocumentIdConfirmation();
        Assertions.assertThrows(ServiceException.class, () -> documentVerificationService.updateIdConfirmations(documentIdConfirmation));

        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(Call.class));
    }

    @Test
    void getDocumentVerificationToken_CreateDocumentVerificationStatus_Success() throws ServiceException, ApiException {
          doReturn(new ApiResponse<>(200, headers, getDocumentVerificationExtractedData())).when(apiClientMock).execute(any(), any());

        DocumentVerificationExtractedData result = documentVerificationService.retrieveDocument(new DocumentDataRetrieval());

        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void getDocumentVerificationToken_CreateDocumentVerificationStatusWithOptionalFields_Success() throws ServiceException, ApiException {

        doReturn(new ApiResponse<>(200, headers, getDocumentVerificationExtractedDataWithOptionalFields())).when(apiClientMock).execute(any(), any());
        DocumentVerificationExtractedData result = documentVerificationService.retrieveDocument(new DocumentDataRetrieval());
        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void getDocumentVerificationToken_CreateDocumentVerificationStatus_Error() throws ApiException {
        doThrow(new ApiException()).when(apiClientMock).execute(any(), any());
        doThrow(new ServiceException("")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));

        DocumentDataRetrieval documentDataRetrievalExample = DocumentDataRetrievalExample.getDocumentDataRetrievalExample();
        Assertions.assertThrows(ServiceException.class, () -> documentVerificationService.retrieveDocument(documentDataRetrievalExample));

        verify(apiClientMock, atLeastOnce()).execute(any(), any());
    }

    @Test
    void getDocumentVerificationToken_CreateDocumentVerificationStatus_WithCreateSessionContext_Success() throws ServiceException, ApiException {
        headers.put(X_USER_IDENTITY, Collections.singletonList("test-user-identity-token"));
        headers.put(X_MIDS_USERAUTH_SESSIONID, Collections.singletonList("test-user-auth-token"));
        doReturn(new ApiResponse<>(200, headers, getDocumentVerificationExtractedData())).when(apiClientMock).execute(any(), any());

        DocumentVerificationExtractedData result = documentVerificationService.retrieveDocument(new DocumentDataRetrieval());
        verify(apiClientMock, times(1)).execute(any(), any());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("transactionId", result.getTransactionId())
        );
    }

    @Test
    void getDocumentVerificationToken_CreateDocumentVerificationStatus_WithCreateSessionContext_Failure() throws ServiceException, ApiException {
        headers.clear();
        doThrow(new ServiceException("Exception occurred while creating session context"))
                .when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));

        doThrow(new ApiException("Api Exception")).when(apiClientMock).execute(any(), any());
        DocumentDataRetrieval documentDataRetrieval = new DocumentDataRetrieval();
        try {
            documentVerificationService.retrieveDocument(documentDataRetrieval);
            fail();
        } catch (ServiceException exp) {
            Assertions.assertEquals("Exception occurred while creating session context", exp.getMessage());
        }
    }

    @Test
    void getDocumentVerificationToken_CreateDocumentVerificationStatus_WithCreateSessionContext_UserAuth_Header_Null() throws ServiceException, ApiException {
        headers.put(X_MIDS_USERAUTH_SESSIONID, null);

        doReturn(new ServiceException("Exception occurred while creating session context"))
                .when(exceptionUtil)
                .logAndConvertToServiceException(any());


        doThrow(new ApiException("Api Exception")).when(apiClientMock).execute(any(), any());
        DocumentDataRetrieval documentDataRetrieval = new DocumentDataRetrieval();
        try {
            documentVerificationService.retrieveDocument(documentDataRetrieval);
            fail();
        } catch (ServiceException exp) {
            Assertions.assertEquals("Exception occurred while creating session context", exp.getMessage());
        }
    }

    @Test
    void getDocumentVerificationToken_CreateDocumentVerificationStatus_WithCreateSessionContext_UserIdentity_Header_Null() throws ServiceException, ApiException {
        headers.clear();
        headers.put(X_MIDS_USERAUTH_SESSIONID, Collections.singletonList("test-user-auth-token"));
        headers.put(X_USER_IDENTITY, null);
        doReturn(new ServiceException("Exception occurred while creating session context")).when(exceptionUtil).logAndConvertToServiceException(any());

        doThrow(new ApiException("Api Exception")).when(apiClientMock).execute(any(), any());
        DocumentDataRetrieval documentDataRetrieval = new DocumentDataRetrieval();
        try {
            documentVerificationService.retrieveDocument(documentDataRetrieval);
            fail();
        } catch (ServiceException exp) {
            Assertions.assertEquals("Exception occurred while creating session context", exp.getMessage());
        }
    }

    @Test
    void testDocumentVerificationDataConfirmations_DataConfirmations_Failure_WithCreateSessionContext() throws ApiException {
        SessionContext.create("test-user-identity-token");

        doReturn(new ServiceException("Error while processing request")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));
      //  doThrow(new ApiException()).when(documentVerificationApi).confirmDocumentDataWithHttpInfo(any(),any(),any());
        doThrow(new ApiException()).when(apiClientMock).execute(any(),any());

        DocumentVerificationConfirmData documentVerificationConfirmData = getDocumentVerificationConfirmData(false, true);
        Assertions.assertThrows(ServiceException.class, () -> documentVerificationService.confirmDocumentData(documentVerificationConfirmData));

        verify(apiClientMock, times(1)).execute(any(), any());
    }

    @Test
    void testDocumentVerificationUpdateIdConfirmations_Error_WithCreateSessionContext() throws ApiException {
        SessionContext.create("test-user-identity-token");
        doReturn(new ServiceException("Error while processing request")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));
        doThrow(new ApiException()).when(apiClientMock).execute(any());

        DocumentIdConfirmation documentIdConfirmation = new DocumentIdConfirmation();
        Assertions.assertThrows(ServiceException.class, () -> documentVerificationService.updateIdConfirmations(documentIdConfirmation));

        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(Call.class));
    }

    @Test
    void testDocumentVerificationUpdateIdConfirmations_Error_WithCreateSessionContext_null() throws ApiException {
        SessionContext.create("test-user-identity-token");
        doReturn(new ServiceException("Error while processing request")).when(exceptionUtil).logAndConvertToServiceException(any(ApiException.class));
        doThrow(new ApiException()).when(apiClientMock).execute(any());


        DocumentIdConfirmation documentIdConfirmation = new DocumentIdConfirmation();
        Assertions.assertThrows(ServiceException.class, () -> documentVerificationService.updateIdConfirmations(documentIdConfirmation));

        verify(apiClientMock, atMostOnce()).buildCall(anyString(),anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(), any());
        verify(apiClientMock, atMostOnce()).execute(any(Call.class));
    }

    private AccessToken getAccessToken() {
        AccessToken accessToken = new AccessToken();
        accessToken.setApiDataCenter("apiDataCenter");
        accessToken.sdkToken("sdkToken");
        accessToken.setTransactionId("transactionId");
        return accessToken;
    }

    private DocumentVerificationToken getDocumentVerificationToken() {
        DocumentVerificationToken token = new DocumentVerificationToken();
        token.setSdkVersion(SDK_VERSION);
        token.setCountryCode(COUNTRY_CODE);
        token.setChannelTypeEnum(RetrieveAccessToken.ChannelTypeEnum.SDK);
        return token;
    }

    private ConfirmedPDS getConfirmedDataUpdatedPDS() {
        ConfirmedPDS confirmedDataUpdatedPDS = new ConfirmedPDS();
        confirmedDataUpdatedPDS.setPds("pds");
        confirmedDataUpdatedPDS.setTransactionId("transactionId");
        return confirmedDataUpdatedPDS;
    }

    private DocumentVerificationExtractedData getDocumentVerificationExtractedData() {
        DocumentVerificationExtractedData extractedData = new DocumentVerificationExtractedData();
        extractedData.setDocumentData(getDocumentData());
        extractedData.setStatus("PENDING");
        extractedData.setTransactionId("transactionId");
        return extractedData;
    }

    private DocumentDataStatus getDocumentData() {
        DocumentDataStatus documentData = new DocumentDataStatus();
        documentData.setAddressCity("New York");
        documentData.setAddressCountry("USA");
        documentData.setAddressLine1("123 Main St.");
        documentData.setAddressLine2("New York");
        documentData.setAddressZipCode("10021");
        documentData.setDateOfBirth("2020-09-09");
        documentData.setDocumentNumber("1LViI488YkFZh8YjNlLf61BMn29cmQn");
        documentData.setDocumentType("passport");
        documentData.setFirstName("John");
        documentData.setLastName("Smith");
        documentData.setExpiryDate("2020-10-10");
        documentData.setGender("M");
        documentData.setIssuingAuthority("United States");
        documentData.placeOfBirth("Boston");
        documentData.setIssuingCountry("USA");
        documentData.setIssuingDate("2020-09-09");
        documentData.setIssuingPlace("New York");
        documentData.setAddressSubdivision("MO");
        documentData.setFormattedAddress("220 BLVD O FALLON MO");
        documentData.setRgNumber("F204050123");
        documentData.setCpf("000.000.000-00");
        documentData.setFathersName("WILLIAM SMITH");
        documentData.setMothersName("JANE SMITH");
        return documentData;
    }

    private DocumentVerificationExtractedData getDocumentVerificationExtractedDataWithOptionalFields() {
        DocumentVerificationExtractedData extractedData = new DocumentVerificationExtractedData();
        extractedData.setDocumentData(getDocumentDataWithOptionalFields());
        extractedData.setStatus("PENDING");
        extractedData.setTransactionId("transactionId");
        return extractedData;
    }

    private DocumentDataStatus getDocumentDataWithOptionalFields() {
        DocumentDataStatus documentData = new DocumentDataStatus();
        documentData.setAddressCity("New York");
        documentData.setAddressCountry("USA");
        documentData.setAddressLine1("123 Main St.");
        documentData.setAddressLine2("New York");
        documentData.setAddressZipCode("10021");
        documentData.setDateOfBirth("2020-09-09");
        documentData.setDocumentNumber("1LViI488YkFZh8YjNlLf61BMn29cmQn");
        documentData.setDocumentStatus("SUCCESS");
        documentData.setDocumentType("passport");
        documentData.setFirstName("John");
        documentData.setLastName("Smith");
        documentData.setExpiryDate("2020-10-10");
        documentData.setGender("M");
        documentData.setIssuingAuthority("United States");
        documentData.placeOfBirth("Boston");
        documentData.setIssuingCountry("USA");
        documentData.setIssuingDate(null);
        documentData.setIssuingPlace("New York");
        documentData.setAddressSubdivision("MO");
        documentData.setFormattedAddress("220 BLVD O FALLON MO");
        documentData.setRgNumber("F204050123");
        documentData.setCpf("000.000.000-00");
        documentData.setFathersName("WILLIAM SMITH");
        documentData.setMothersName("JANE SMITH");
        return documentData;
    }

    private DocumentVerificationConfirmData getDocumentVerificationConfirmData(boolean verifyIfVisaMatched, boolean claimSharingFlow) {
        DocumentVerificationConfirmData documentVerificationConfirmData = new DocumentVerificationConfirmData();
        documentVerificationConfirmData.setDocumentData(new ConfirmDocumentData());
        documentVerificationConfirmData.setFraudDetection(new FraudDetection());
        if (claimSharingFlow) {
            documentVerificationConfirmData.setArid(UUID.fromString(Constants.getAridValue()));
        }
        if (verifyIfVisaMatched) {
            documentVerificationConfirmData.setVisaMatched(verifyIfVisaMatched);
            documentVerificationConfirmData.setCountryCode(VISA_SUPPORTED_COUNTRY_CODE);
        }
        return documentVerificationConfirmData;
    }
}
