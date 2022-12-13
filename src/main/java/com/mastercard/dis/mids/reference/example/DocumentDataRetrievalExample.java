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

package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.DocumentDataRetrieval;
import org.openapitools.client.model.TpAuditMetadata;

import java.util.ArrayList;

import static com.mastercard.dis.mids.reference.util.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.util.Constants.LOCALE;
import static com.mastercard.dis.mids.reference.util.Constants.PRIVACY_POLICY_VERSION;
import static com.mastercard.dis.mids.reference.util.Constants.SESSION_ID;
import static com.mastercard.dis.mids.reference.util.Constants.USER_PROFILE_ID;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentDataRetrievalExample {

    public static DocumentDataRetrieval getDocumentDataRetrievalExample() {
        DocumentDataRetrieval documentDataRetrieval = new DocumentDataRetrieval();
        documentDataRetrieval.setCountryCode(COUNTRY_CODE);
        documentDataRetrieval.setUserConsent(ACCEPT);
        documentDataRetrieval.setUserProfileId(USER_PROFILE_ID);
        documentDataRetrieval.setWorkflowId(DocumentDataRetrieval.SERIALIZED_NAME_WORKFLOW_ID);
        documentDataRetrieval.setPrivacyPolicyVersion(PRIVACY_POLICY_VERSION);
        documentDataRetrieval.setSdkAuditEvents(new ArrayList<>());
        documentDataRetrieval.setLocale(LOCALE);
        documentDataRetrieval.setDocumentType(DocumentDataRetrieval.DocumentTypeEnum.DRIVING_LICENSE);
        TpAuditMetadata tpAuditMetadata = new TpAuditMetadata();
        tpAuditMetadata.setSessionId(SESSION_ID);
        tpAuditMetadata.setTransactionGroupId(TpAuditMetadata.SERIALIZED_NAME_TRANSACTION_GROUP_ID);
        documentDataRetrieval.setTpAuditMetadata(tpAuditMetadata);
        return documentDataRetrieval;
    }

}
