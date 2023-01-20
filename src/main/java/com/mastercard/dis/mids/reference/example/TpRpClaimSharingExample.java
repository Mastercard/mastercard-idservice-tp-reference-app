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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.ClaimScopes;

import java.util.Arrays;
import java.util.List;

import static com.mastercard.dis.mids.reference.constants.Constants.TP_RP_SCOPES;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TpRpClaimSharingExample {

    public static ClaimScopes getClaimsSharingRequestObject() {
        ClaimScopes claimScopes = new ClaimScopes();
        claimScopes.setScopedFields(getScopedFields());
        claimScopes.setUserConsent(ACCEPT);
        return claimScopes;
    }

    private static List<String> getScopedFields() {
        return Arrays.asList(TP_RP_SCOPES.split("\\s*,\\s*"));
    }
}
