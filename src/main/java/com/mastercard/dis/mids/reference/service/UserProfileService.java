package com.mastercard.dis.mids.reference.service;

import org.openapitools.client.model.ClientIdentities;
import org.openapitools.client.model.IdentityAttributeDeleted;
import org.openapitools.client.model.IdentityAttributeDeletions;
import org.openapitools.client.model.IdentitySearch;
import org.openapitools.client.model.UserProfile;

public interface UserProfileService {

    ClientIdentities retrieveIdentities(IdentitySearch identitySearch);

    void userProfileRegistration(UserProfile userProfile);

    void userProfileDelete(String userProfileId, String userConsent);

    IdentityAttributeDeleted deleteIdentityAttribute(IdentityAttributeDeletions identityAttributeDeletions);


}
