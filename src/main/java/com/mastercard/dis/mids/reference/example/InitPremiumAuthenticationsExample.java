package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.InitPremiumAuthentications;

import static com.mastercard.dis.mids.reference.example.InitializeReAuthenticationExample.getSdkAuditEventsObject;
import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.LOCALE;
import static com.mastercard.dis.mids.reference.constants.Constants.SDK_VERSION;
import static org.openapitools.client.model.UserConsent.ACCEPT;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InitPremiumAuthenticationsExample {

    public static InitPremiumAuthentications getInitPremiumAuthentications() {
        InitPremiumAuthentications initPremiumAuthentications = new InitPremiumAuthentications();
        initPremiumAuthentications.setLocale(LOCALE);
        initPremiumAuthentications.setCountryCode(COUNTRY_CODE);
        initPremiumAuthentications.setSdkVersion(SDK_VERSION);
        initPremiumAuthentications.setUserConsent(ACCEPT);
        initPremiumAuthentications.setChannelType(InitPremiumAuthentications.ChannelTypeEnum.SDK);
        initPremiumAuthentications.setSdkAuditEvents(getSdkAuditEventsObject());
        return initPremiumAuthentications;
    }

}
