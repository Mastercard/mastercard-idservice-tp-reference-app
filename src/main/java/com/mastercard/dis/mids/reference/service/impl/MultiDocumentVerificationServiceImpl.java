package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.example.dto.MultiDocumentVerificationToken;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.MultiDocumentVerificationService;
import com.mastercard.dis.mids.reference.util.DocumentVerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MultiDocumentVerificationApi;
import org.openapitools.client.model.AccessToken;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.DocumentVerificationExtractedData;
import org.openapitools.client.model.FraudDetection;
import org.openapitools.client.model.MultiDocConfirmData;
import org.openapitools.client.model.MultiDocumentConfirmedPDS;
import org.openapitools.client.model.MultiDocumentDataRetrieval;
import org.openapitools.client.model.MultiRetrieveAccessToken;
import org.openapitools.client.model.UpdateIdentityAttributesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mastercard.dis.mids.reference.util.Constants.X_USER_IDENTITY;

@Slf4j
@Service
public class MultiDocumentVerificationServiceImpl implements MultiDocumentVerificationService {

    private final MultiDocumentVerificationApi multiDocumentVerificationApi;
    private final ExceptionUtil exceptionUtil;
    private final ApiClientConfiguration apiClientConfiguration;
    private final DocumentVerificationUtil util;

    @Autowired
    public MultiDocumentVerificationServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil, ApiClientConfiguration apiClientConfiguration, DocumentVerificationUtil util) {
        multiDocumentVerificationApi = new MultiDocumentVerificationApi(apiClient);
        this.exceptionUtil = exceptionUtil;
        this.apiClientConfiguration = apiClientConfiguration;
        this.util = util;
    }

    @Override
    public AccessToken documentVerificationMultiAccessSDKToken(MultiDocumentVerificationToken multiDocumentVerificationToken) {
        try {
            util.setupUserIdentityTokens();
            MultiRetrieveAccessToken multiRetrieveAccessToken = new MultiRetrieveAccessToken()
                    .channelType(multiDocumentVerificationToken.getChannelTypeEnum())
                    .countryCode(multiDocumentVerificationToken.getCountryCode())
                    .sdkVersion(multiDocumentVerificationToken.getSdkVersion())
                    .userProfileId(apiClientConfiguration.getUserProfileId())
                    .pds(multiDocumentVerificationToken.getPds());
            return multiDocumentVerificationApi.getMultiAccessToken(multiRetrieveAccessToken, X_USER_IDENTITY);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public DocumentVerificationExtractedData multiDocumentVerificationStatus(MultiDocumentDataRetrieval multiDocumentDataRetrieval) {
        try {
            util.setupUserIdentityTokens();
            multiDocumentDataRetrieval.userProfileId(apiClientConfiguration.getUserProfileId())
                    .workflowId(apiClientConfiguration.getMultidocWorkFlowId())
                    .tpAuditMetadata(util.getTpAuditMetadata())
                    .userSelectedCountry(apiClientConfiguration.getSelectedUserCountry())
                    .pds(multiDocumentDataRetrieval.getPds());
            ApiResponse<DocumentVerificationExtractedData> multiDocumentVerificationStatus = multiDocumentVerificationApi.retrieveMultiDocumentDataWithHttpInfo(multiDocumentDataRetrieval, X_USER_IDENTITY);
            util.createSessionContext(multiDocumentVerificationStatus.getHeaders());
            return multiDocumentVerificationStatus.getData();
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public MultiDocumentConfirmedPDS multiDocumentVerificationConfirmation(MultiDocConfirmData multiDocConfirmData) {
        try {
            util.setupUserIdentityTokens();
            multiDocConfirmData.setUserProfileId(apiClientConfiguration.getUserProfileId());
            multiDocConfirmData.getDocumentData().setWorkflowId(apiClientConfiguration.getMultidocWorkFlowId());
            multiDocConfirmData.setTpAuditMetadata(util.getTpAuditMetadata());
            FraudDetection fraudDetection = new FraudDetection();
            fraudDetection.setNuDetectMeta(util.createFraudDetectMeta());
            multiDocConfirmData.setFraudDetection(fraudDetection);
            ApiResponse<MultiDocumentConfirmedPDS> confirmedDataUpdatedPDS = multiDocumentVerificationApi.updatePDSForAdditionalDocumentDataWithHttpInfo(multiDocConfirmData, X_USER_IDENTITY);
            util.createSessionContext(confirmedDataUpdatedPDS.getHeaders());
            return confirmedDataUpdatedPDS.getData();
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public ConfirmedPDS updateIdentityAttributes(UpdateIdentityAttributesData updateIdentityAttributesData) {
        try {
            util.setupUserIdentityTokens();
            updateIdentityAttributesData.setTpAuditMetadata(util.getTpAuditMetadata());
            ApiResponse<ConfirmedPDS> confirmedDataUpdatedPDS = multiDocumentVerificationApi.updateIdentityAttributeWithHttpInfo(updateIdentityAttributesData, X_USER_IDENTITY, apiClientConfiguration.isEncryptionEnabled());
            util.createSessionContext(confirmedDataUpdatedPDS.getHeaders());
            return confirmedDataUpdatedPDS.getData();
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

}
