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
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.EmailOtpService;
import com.mastercard.dis.mids.reference.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.EmailOtpApi;
import org.openapitools.client.model.CreatedEmailOtp;
import org.openapitools.client.model.EmailOtp;
import org.openapitools.client.model.OtpVerification;
import org.openapitools.client.model.OtpVerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mastercard.dis.mids.reference.constants.Constants.X_USER_IDENTITY;

@Slf4j
@Service
public class EmailOtpServiceImpl implements EmailOtpService {

    private final EmailOtpApi otpApi;
    private final ExceptionUtil exceptionUtil;
    private final ApiClientConfiguration apiClientConfiguration;

    @Autowired
    public EmailOtpServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil, ApiClientConfiguration apiClientConfiguration) {
        otpApi = new EmailOtpApi(apiClient);
        this.exceptionUtil = exceptionUtil;
        this.apiClientConfiguration = apiClientConfiguration;
    }

    @Override
    public CreatedEmailOtp createEmailOtp(EmailOtp emailOtp) {
        try {
            SessionUtils.setupUserIdTokens(otpApi.getApiClient());
            emailOtp.setTpAuditMetadata(SessionUtils.getTpAuditMetadata(apiClientConfiguration));
            return otpApi.createEmailOTP(emailOtp, X_USER_IDENTITY, apiClientConfiguration.isEncryptionEnabled());
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public OtpVerificationResult verifyEmailOtp(OtpVerification emailOtpVerification) {
        try {
            SessionUtils.setupUserIdTokens(otpApi.getApiClient());
            emailOtpVerification.setTpAuditMetadata(SessionUtils.getTpAuditMetadata(apiClientConfiguration));
            return otpApi.verifyEmailOTP(emailOtpVerification, X_USER_IDENTITY, apiClientConfiguration.isEncryptionEnabled());
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }
}