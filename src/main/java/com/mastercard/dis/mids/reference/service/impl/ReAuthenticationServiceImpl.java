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
import com.mastercard.dis.mids.reference.constants.TpVariables;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.ReAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ReAuthenticationApi;
import org.openapitools.client.model.AuthenticationDecisions;
import org.openapitools.client.model.AuthenticationResults;
import org.openapitools.client.model.Authentications;
import org.openapitools.client.model.InitializeAuthentications;
import org.openapitools.client.model.TpAuditMetadata;
import org.openapitools.client.model.VerifyAuthentication;
import org.openapitools.client.model.VerifyAuthenticationDecisions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class ReAuthenticationServiceImpl implements ReAuthenticationService {

    private final ReAuthenticationApi reAuthenticationApi;
    private final ExceptionUtil exceptionUtil;
    private final ApiClientConfiguration apiClientConfiguration;

    @Autowired
    public ReAuthenticationServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil, ApiClientConfiguration apiClientConfiguration) {
        reAuthenticationApi = new ReAuthenticationApi(apiClient);
        this.exceptionUtil = exceptionUtil;
        this.apiClientConfiguration = apiClientConfiguration;
    }

    @Override
    public Authentications initiateAuthentication(final InitializeAuthentications initializeAuthentications) {
        try {
            initializeAuthentications.setUserProfileId(TpVariables.getUserProfileId());
            initializeAuthentications.setTpAuditMetadata(getTpAuditMetadata());
            return reAuthenticationApi.initializeAuthentication(initializeAuthentications);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public AuthenticationDecisions initiateAuthenticationDecisions(String scanId, final VerifyAuthenticationDecisions verifyAuthenticationDecisions) {
        try {
            verifyAuthenticationDecisions.setUserProfileId(TpVariables.getUserProfileId());
            return reAuthenticationApi.authenticationdecisions(scanId, verifyAuthenticationDecisions);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public AuthenticationResults authenticationResults(final VerifyAuthentication verifyAuthentication) {
        try {
            verifyAuthentication.setUserProfileId(TpVariables.getUserProfileId());
            verifyAuthentication.setWorkflowId(TpVariables.getWorkflowIdReAuth());
            verifyAuthentication.setTpAuditMetadata(getTpAuditMetadata());
            return reAuthenticationApi.getAuthenticationResults(verifyAuthentication);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    private TpAuditMetadata getTpAuditMetadata() {
        TpAuditMetadata tpAuditMetadata = new TpAuditMetadata();
        tpAuditMetadata.setSessionId(apiClientConfiguration.getSessionId());
        tpAuditMetadata.setTransactionGroupId(apiClientConfiguration.getTransactionGroupId());
        return tpAuditMetadata;
    }
}