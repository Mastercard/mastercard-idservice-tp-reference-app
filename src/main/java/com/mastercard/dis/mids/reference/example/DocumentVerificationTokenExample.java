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

import com.mastercard.dis.mids.reference.example.dto.DocumentVerificationToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.RetrieveAccessToken;

import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.ENROLLMENT_ORIGIN_VALUE;
import static com.mastercard.dis.mids.reference.constants.Constants.SDK_VERSION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentVerificationTokenExample {

    public static DocumentVerificationToken getDocumentVerificationTokenObject() {
        DocumentVerificationToken documentVerificationToken = new DocumentVerificationToken();
        documentVerificationToken.setCountryCode(COUNTRY_CODE);
        documentVerificationToken.setSdkVersion(SDK_VERSION);
        documentVerificationToken.setChannelTypeEnum(RetrieveAccessToken.ChannelTypeEnum.SDK);
        documentVerificationToken.setEnrollmentOrigin(ENROLLMENT_ORIGIN_VALUE);
        return documentVerificationToken;
    }
}
