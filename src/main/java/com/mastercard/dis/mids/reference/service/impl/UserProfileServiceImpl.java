package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DeleteIdApi;
import org.openapitools.client.api.PdsApi;
import org.openapitools.client.api.UserProfileApi;
import org.openapitools.client.model.ClientIdentities;
import org.openapitools.client.model.IdentityAttributeDeleted;
import org.openapitools.client.model.IdentityAttributeDeletions;
import org.openapitools.client.model.IdentitySearch;
import org.openapitools.client.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mastercard.dis.mids.reference.util.Constants.X_USER_IDENTITY;

@Slf4j
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final PdsApi pdsApi;
    private final UserProfileApi userProfileApi;
    private final DeleteIdApi deleteIdApi;
    private final ExceptionUtil exceptionUtil;
    private final ApiClientConfiguration apiClientConfiguration;

    @Autowired
    public UserProfileServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil, ApiClientConfiguration apiClientConfiguration) {
        pdsApi = new PdsApi(apiClient);
        this.exceptionUtil = exceptionUtil;
        userProfileApi = new UserProfileApi(apiClient);
        deleteIdApi = new DeleteIdApi(apiClient);
        this.apiClientConfiguration = apiClientConfiguration;
    }

    @Override
    public ClientIdentities retrieveIdentities(IdentitySearch identitySearch) {

        try {
            return pdsApi.retrieveIdentities(identitySearch, X_USER_IDENTITY, apiClientConfiguration.isEncryptionEnabled());
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public void userProfileRegistration(UserProfile userProfile) {
        try {
            userProfileApi.createUserProfile(userProfile);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public void userProfileDelete(String userProfileId, String userConsent) {
        try {
            deleteIdApi.deleteUserProfile(userProfileId, userConsent);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public IdentityAttributeDeleted deleteIdentityAttribute(IdentityAttributeDeletions identityAttributeDeletions) {
        try {
            return pdsApi.deleteIdentityAttribute(identityAttributeDeletions, X_USER_IDENTITY, apiClientConfiguration.isEncryptionEnabled());
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }


}