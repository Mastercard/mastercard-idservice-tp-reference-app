package com.mastercard.dis.mids.reference.service;

import org.openapitools.client.model.Authentications;
import org.openapitools.client.model.InitPremiumAuthentications;

public interface GpaAuthenticationService {

    Authentications initPremiumAuthentications(final InitPremiumAuthentications initPremiumAuthentications);
}
