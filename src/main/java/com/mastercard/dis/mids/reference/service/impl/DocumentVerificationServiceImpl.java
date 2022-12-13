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
import com.mastercard.dis.mids.reference.example.dto.DocumentVerificationToken;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.DocumentVerificationService;
import com.mastercard.dis.mids.reference.util.DocumentVerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.DocumentVerificationApi;
import org.openapitools.client.model.AccessToken;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.DocumentDataRetrieval;
import org.openapitools.client.model.DocumentIdConfirmation;
import org.openapitools.client.model.DocumentVerificationConfirmData;
import org.openapitools.client.model.DocumentVerificationExtractedData;
import org.openapitools.client.model.FraudDetection;
import org.openapitools.client.model.RetrieveAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mastercard.dis.mids.reference.util.Constants.X_USER_IDENTITY;

@Slf4j
@Service
public class DocumentVerificationServiceImpl implements DocumentVerificationService {

    private final DocumentVerificationApi documentVerificationApi;
    private final ExceptionUtil exceptionUtil;
    private final ApiClientConfiguration apiClientConfiguration;

    private final DocumentVerificationUtil util;

    @Autowired
    public DocumentVerificationServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil, ApiClientConfiguration apiClientConfiguration, DocumentVerificationUtil util) {
        documentVerificationApi = new DocumentVerificationApi(apiClient);
        this.exceptionUtil = exceptionUtil;
        this.apiClientConfiguration = apiClientConfiguration;
        this.util = util;
    }

    @Override
    public AccessToken retrieveDocumentVerificationToken(DocumentVerificationToken documentVerificationToken) {
        try {
            RetrieveAccessToken retrieveAccessToken = new RetrieveAccessToken()
                    .channelType(documentVerificationToken.getChannelTypeEnum())
                    .countryCode(documentVerificationToken.getCountryCode())
                    .sdkVersion(documentVerificationToken.getSdkVersion())
                    .enrollmentOrigin(documentVerificationToken.getEnrollmentOrigin())
                    .userProfileId(apiClientConfiguration.getUserProfileId());
            return documentVerificationApi.getAccessToken(retrieveAccessToken);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public DocumentVerificationExtractedData retrieveDocument(DocumentDataRetrieval documentDataRetrieval) {
        try {
            documentDataRetrieval.setUserProfileId(apiClientConfiguration.getUserProfileId());
            documentDataRetrieval.setWorkflowId(apiClientConfiguration.getWorkflowId());
            documentDataRetrieval.setTpAuditMetadata(util.getTpAuditMetadata());
            documentDataRetrieval.setUserSelectedCountry(apiClientConfiguration.getSelectedUserCountry());

            ApiResponse<DocumentVerificationExtractedData> documentVerificationStatusResponse = documentVerificationApi.retrieveDocumentDataWithHttpInfo(documentDataRetrieval, apiClientConfiguration.isEncryptionEnabled());
            util.createSessionContext(documentVerificationStatusResponse.getHeaders());

            return documentVerificationStatusResponse.getData();
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public ConfirmedPDS confirmDocumentData(DocumentVerificationConfirmData documentVerificationConfirmData) {
        try {
            util.setupUserIdentityTokens();
            documentVerificationConfirmData.setUserProfileId(apiClientConfiguration.getUserProfileId());
            documentVerificationConfirmData.getDocumentData().setWorkflowId(apiClientConfiguration.getWorkflowId());
            documentVerificationConfirmData.setTpAuditMetadata(util.getTpAuditMetadata());
            FraudDetection fraudDetection = new FraudDetection();
            fraudDetection.setNuDetectMeta(util.createFraudDetectMeta());
            documentVerificationConfirmData.setFraudDetection(fraudDetection);
            ApiResponse<ConfirmedPDS> confirmedDataUpdatedPDS = documentVerificationApi.confirmDocumentDataWithHttpInfo(documentVerificationConfirmData, X_USER_IDENTITY, apiClientConfiguration.isEncryptionEnabled());
            util.createSessionContext(confirmedDataUpdatedPDS.getHeaders());
            return confirmedDataUpdatedPDS.getData();
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public void updateIdConfirmations(DocumentIdConfirmation documentIdConfirmation) {
        try {
            util.setupUserIdentityTokens();
            documentIdConfirmation.setUserProfileId(apiClientConfiguration.getUserProfileId());
            documentIdConfirmation.setWorkflowId(apiClientConfiguration.getWorkflowId());
            documentIdConfirmation.setTpAuditMetadata(util.getTpAuditMetadata());
            documentVerificationApi.updateIdConfirmation(documentIdConfirmation, X_USER_IDENTITY);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }
}
