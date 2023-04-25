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
import org.openapitools.client.model.EmailOtp;
import org.openapitools.client.model.OtpVerification;

import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.OTP_CODE;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailOtpExample {

    public static EmailOtp getEmailOtpGenerationObject() {
        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setLocale("en-US");
        emailOtp.setCountryCode(COUNTRY_CODE);
        emailOtp.setUserConsent(ACCEPT);

        //to receive an actual otp, change this to a real email address
        emailOtp.setEmailAddress("email@email.com");

        return emailOtp;
    }

    public static OtpVerification getEmailOtpVerificationObject() {
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setCountryCode(COUNTRY_CODE);
        otpVerification.setUserConsent(ACCEPT);

        return otpVerification;
    }
}
