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

package com.mastercard.dis.mids.reference.service;

import com.mastercard.dis.mids.reference.example.dto.MultiDocumentVerificationToken;
import org.openapitools.client.model.AccessToken;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.DocumentVerificationExtractedData;
import org.openapitools.client.model.MultiDocConfirmData;
import org.openapitools.client.model.MultiDocumentConfirmedPDS;
import org.openapitools.client.model.MultiDocumentDataRetrieval;
import org.openapitools.client.model.UpdateIdentityAttributesData;


public interface MultiDocumentVerificationService {

    AccessToken documentVerificationMultiAccessSDKToken(MultiDocumentVerificationToken multiDocumentVerificationToken);

    DocumentVerificationExtractedData multiDocumentVerificationStatus(MultiDocumentDataRetrieval multiDocumentDataRetrieval);

    MultiDocumentConfirmedPDS multiDocumentVerificationConfirmation(MultiDocConfirmData multiDocConfirmData);

    ConfirmedPDS updateIdentityAttributes(UpdateIdentityAttributesData updateIdentityAttributesData);
}
