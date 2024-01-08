package com.mastercard.dis.mids.reference.example;

import com.mastercard.dis.mids.reference.constants.TpVariables;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.AuditEvents;
import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuditEventsTokenExample {

    public static AuditEvents getAuditEventsToken() {
        AuditEvents auditEvents = new AuditEvents();
        auditEvents.countryCode(COUNTRY_CODE);
        auditEvents.userProfileId(TpVariables.getUserProfileId());
        return auditEvents;
    }

}
