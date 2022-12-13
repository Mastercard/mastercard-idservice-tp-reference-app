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

import com.mastercard.dis.mids.reference.example.dto.MultiDocumentVerificationToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.MultiRetrieveAccessToken;

import static com.mastercard.dis.mids.reference.util.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.util.Constants.PDS;
import static com.mastercard.dis.mids.reference.util.Constants.SDK_VERSION;
import static com.mastercard.dis.mids.reference.util.Constants.USER_PROFILE_ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiDocumentVerificationTokenExample {

    public static MultiDocumentVerificationToken getMultiDocumentVerificationToken() {
        MultiDocumentVerificationToken multiDocumentVerificationToken = new MultiDocumentVerificationToken();
        multiDocumentVerificationToken.setCountryCode(COUNTRY_CODE);
        multiDocumentVerificationToken.setSdkVersion(SDK_VERSION);
        multiDocumentVerificationToken.setChannelTypeEnum(MultiRetrieveAccessToken.ChannelTypeEnum.WEB);
        multiDocumentVerificationToken.setUserProfileId(USER_PROFILE_ID);
        multiDocumentVerificationToken.setPds(PDS);
        return multiDocumentVerificationToken;
    }
}
