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

package com.mastercard.dis.mids.reference.example;

import com.mastercard.dis.mids.reference.constants.Constants;
import com.mastercard.dis.mids.reference.util.FraudDetectionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.DocumentVerificationConfirmData;

import java.util.UUID;

import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.LOCALE;
import static com.mastercard.dis.mids.reference.constants.Constants.VISA_SUPPORTED_COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.VISA_VERIFY_REQUIRED;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentVerificationDataConfirmationsExample {

    public static DocumentVerificationConfirmData getDocumentVerificationDataConfirmations(boolean claimSharingFlow) {
        DocumentVerificationConfirmData documentVerificationConfirmData = new DocumentVerificationConfirmData();
        documentVerificationConfirmData.setUserConsent(ACCEPT);
        documentVerificationConfirmData.setCountryCode(COUNTRY_CODE);
        documentVerificationConfirmData.setLocale(LOCALE);
        documentVerificationConfirmData.setFraudDetection(FraudDetectionUtil.getFraudDetection());
        // Only set for Claim Sharing flow.
        if (claimSharingFlow) {
            documentVerificationConfirmData.setArid(UUID.fromString(Constants.getAridValue()));
        }
        //currently only Australian passports are supported for visa verification
        if (VISA_VERIFY_REQUIRED) {
            documentVerificationConfirmData.setVisaMatched(VISA_VERIFY_REQUIRED);
            documentVerificationConfirmData.setCountryCode(VISA_SUPPORTED_COUNTRY_CODE);
        }
        return documentVerificationConfirmData;
    }
}