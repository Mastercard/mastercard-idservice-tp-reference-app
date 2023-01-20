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

import com.mastercard.dis.mids.reference.util.FraudDetectionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.DocumentDataRetrieval;
import org.openapitools.client.model.IdentityAttributeItem;
import org.openapitools.client.model.MultiDocConfirmData;
import org.openapitools.client.model.MultiDocumentDataRetrieval;
import org.openapitools.client.model.TpAuditMetadata;
import org.openapitools.client.model.UpdateIdentityAttributesData;

import java.util.ArrayList;
import java.util.Collections;

import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.LOCALE;
import static com.mastercard.dis.mids.reference.constants.Constants.NEW_LEGAL_NAME;
import static com.mastercard.dis.mids.reference.constants.Constants.PRIVACY_POLICY_VERSION;
import static com.mastercard.dis.mids.reference.constants.Constants.SESSION_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.USER_PROFILE_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.VISA_SUPPORTED_COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.VISA_VERIFY_REQUIRED;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiDocumentVerificationExample {

    public static MultiDocumentDataRetrieval getMultiDocumentDataRetrieval() {
        MultiDocumentDataRetrieval documentDataRetrieval = new MultiDocumentDataRetrieval();
        documentDataRetrieval.setCountryCode(COUNTRY_CODE);
        documentDataRetrieval.setUserConsent(ACCEPT);
        documentDataRetrieval.setUserProfileId(USER_PROFILE_ID);
        documentDataRetrieval.setWorkflowId(DocumentDataRetrieval.SERIALIZED_NAME_WORKFLOW_ID);
        documentDataRetrieval.setPrivacyPolicyVersion(PRIVACY_POLICY_VERSION);
        documentDataRetrieval.setSdkAuditEvents(new ArrayList<>());
        documentDataRetrieval.setLocale(LOCALE);
        documentDataRetrieval.setDocumentType(MultiDocumentDataRetrieval.DocumentTypeEnum.DRIVING_LICENSE);
        TpAuditMetadata tpAuditMetadata = new TpAuditMetadata();
        tpAuditMetadata.setSessionId(SESSION_ID);
        tpAuditMetadata.setTransactionGroupId(TpAuditMetadata.SERIALIZED_NAME_TRANSACTION_GROUP_ID);
        documentDataRetrieval.setTpAuditMetadata(tpAuditMetadata);

        return documentDataRetrieval;
    }

    public static MultiDocConfirmData getMultiDocConfirmData() {
        MultiDocConfirmData documentVerificationConfirmData = new MultiDocConfirmData();
        documentVerificationConfirmData.setUserConsent(ACCEPT);
        documentVerificationConfirmData.setCountryCode(COUNTRY_CODE);
        documentVerificationConfirmData.setLocale(LOCALE);
        documentVerificationConfirmData.setFraudDetection(FraudDetectionUtil.getFraudDetection());

        //currently only Australian passports are supported for visa verification
        if (VISA_VERIFY_REQUIRED) {
            documentVerificationConfirmData.setVisaMatched(VISA_VERIFY_REQUIRED);
            documentVerificationConfirmData.setCountryCode(VISA_SUPPORTED_COUNTRY_CODE);
        }
        return documentVerificationConfirmData;
    }

    public static UpdateIdentityAttributesData getUpdateIdentityAttributesData() {
        UpdateIdentityAttributesData updateIdentityAttributesData = new UpdateIdentityAttributesData();
        IdentityAttributeItem identityAttributeItem = new IdentityAttributeItem();
        identityAttributeItem.setAttributeName(IdentityAttributeItem.AttributeNameEnum.LEGAL_NAME);
        identityAttributeItem.attributeValue(NEW_LEGAL_NAME);
        updateIdentityAttributesData.setIdentityAttributes(Collections.singletonList(identityAttributeItem));
        updateIdentityAttributesData.setUserConsent(ACCEPT);
        return updateIdentityAttributesData;
    }
}
