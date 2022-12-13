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

import org.openapitools.client.model.AuthenticationDecisions;
import org.openapitools.client.model.AuthenticationResults;
import org.openapitools.client.model.Authentications;
import org.openapitools.client.model.InitializeAuthentications;
import org.openapitools.client.model.VerifyAuthentication;
import org.openapitools.client.model.VerifyAuthenticationDecisions;

public interface ReAuthenticationService {

    Authentications initiateAuthentication(final InitializeAuthentications initializeAuthentications);

    AuthenticationResults authenticationResults(final VerifyAuthentication verifyAuthentication);

    AuthenticationDecisions initiateAuthenticationDecisions(String scanId, final VerifyAuthenticationDecisions verifyAuthenticationDecisions);

}