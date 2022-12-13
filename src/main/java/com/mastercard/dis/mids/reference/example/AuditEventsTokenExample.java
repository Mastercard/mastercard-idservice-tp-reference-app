package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.AuditEvents;

import static com.mastercard.dis.mids.reference.util.Constants.AUDIT_USER_PROFILE_ID;
import static com.mastercard.dis.mids.reference.util.Constants.COUNTRY_CODE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuditEventsTokenExample {

    public static AuditEvents getAuditEventsToken() {
        AuditEvents auditEvents = new AuditEvents();
        auditEvents.countryCode(COUNTRY_CODE);
        auditEvents.userProfileId(AUDIT_USER_PROFILE_ID);
        return auditEvents;
    }

}
