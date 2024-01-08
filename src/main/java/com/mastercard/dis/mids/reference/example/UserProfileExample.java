package com.mastercard.dis.mids.reference.example;


import com.mastercard.dis.mids.reference.constants.TpVariables;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.IdentitySearch;
import org.openapitools.client.model.TpAuditMetadata;
import org.openapitools.client.model.UserProfile;

import java.util.ArrayList;
import java.util.Collections;

import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.SESSION_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.TRANSACTION_GROUP_ID;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileExample {

    public static IdentitySearch getIdentitySearchObject() {
        IdentitySearch identitySearch = new IdentitySearch();
        identitySearch.setUserConsent(ACCEPT);
        identitySearch.setScopedFields(Collections.singletonList(IdentitySearch.ScopedFieldsEnum.PASSPORT));
        return identitySearch;
    }

    public static UserProfile getUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserProfileId(TpVariables.getUserProfileId());
        userProfile.setCountryCode(COUNTRY_CODE);
        userProfile.setSdkAuditEvents(new ArrayList<>());
        TpAuditMetadata tpAuditMetadata = new TpAuditMetadata();
        tpAuditMetadata.setSessionId(SESSION_ID);
        tpAuditMetadata.setTransactionGroupId(TRANSACTION_GROUP_ID);
        userProfile.setTpAuditMetadata(tpAuditMetadata);
        return userProfile;
    }

}
