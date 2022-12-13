package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.RPClaimsUserConsent;
import org.openapitools.client.model.RPClaimsUserDetails;

import java.util.UUID;

import static com.mastercard.dis.mids.reference.util.Constants.ARID_VALUE;
import static com.mastercard.dis.mids.reference.util.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.util.Constants.LOCALE;
import static com.mastercard.dis.mids.reference.util.Constants.PDS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClaimsApiExample {

    public static RPClaimsUserConsent getUserConsentStatusExample() {
        RPClaimsUserConsent rpClaimsUserConsent = new RPClaimsUserConsent();
        rpClaimsUserConsent.pds(PDS);
        rpClaimsUserConsent.arid(UUID.fromString(ARID_VALUE));
        rpClaimsUserConsent.userConsent(RPClaimsUserConsent.UserConsentEnum.ACCEPT);
        rpClaimsUserConsent.setCountryCode(COUNTRY_CODE);
        rpClaimsUserConsent.setLocale(LOCALE);
        return rpClaimsUserConsent;
    }

    public static RPClaimsUserDetails extractClaimsUserDataExample() {
        RPClaimsUserDetails rpClaimsUserDetails = new RPClaimsUserDetails();
        rpClaimsUserDetails.pds(PDS);
        rpClaimsUserDetails.arid(UUID.fromString(ARID_VALUE));
        rpClaimsUserDetails.setCountryCode(COUNTRY_CODE);
        rpClaimsUserDetails.setLocale(LOCALE);
        return rpClaimsUserDetails;
    }

}