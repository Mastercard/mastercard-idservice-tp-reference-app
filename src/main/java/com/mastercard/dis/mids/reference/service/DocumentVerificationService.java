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

package com.mastercard.dis.mids.reference.service;

import com.mastercard.dis.mids.reference.example.dto.DocumentVerificationToken;
import org.openapitools.client.model.AccessToken;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.DocumentDataRetrieval;
import org.openapitools.client.model.DocumentIdConfirmation;
import org.openapitools.client.model.DocumentVerificationConfirmData;
import org.openapitools.client.model.DocumentVerificationExtractedData;


public interface DocumentVerificationService {

    AccessToken retrieveDocumentVerificationToken(DocumentVerificationToken documentVerificationToken);

    ConfirmedPDS confirmDocumentData(DocumentVerificationConfirmData documentVerificationConfirmData);

    DocumentVerificationExtractedData retrieveDocument(DocumentDataRetrieval documentDataRetrieval);

    void updateIdConfirmations(DocumentIdConfirmation documentIdConfirmation);
}
