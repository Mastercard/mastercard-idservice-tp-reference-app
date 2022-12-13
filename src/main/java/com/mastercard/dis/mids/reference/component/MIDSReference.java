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
package com.mastercard.dis.mids.reference.component;

import com.mastercard.dis.mids.reference.example.AuditEventsTokenExample;
import com.mastercard.dis.mids.reference.example.BackUpAndRestoreExample;
import com.mastercard.dis.mids.reference.example.ClaimsApiExample;
import com.mastercard.dis.mids.reference.example.DocumentDataRetrievalExample;
import com.mastercard.dis.mids.reference.example.DocumentIdConfirmationExample;
import com.mastercard.dis.mids.reference.example.DocumentVerificationDataConfirmationsExample;
import com.mastercard.dis.mids.reference.example.DocumentVerificationTokenExample;
import com.mastercard.dis.mids.reference.example.EmailOtpExample;
import com.mastercard.dis.mids.reference.example.InitPremiumAuthenticationsExample;
import com.mastercard.dis.mids.reference.example.InitializeReAuthenticationExample;
import com.mastercard.dis.mids.reference.example.MultiDocumentVerificationExample;
import com.mastercard.dis.mids.reference.example.MultiDocumentVerificationTokenExample;
import com.mastercard.dis.mids.reference.example.RPActivitySearchTokenExample;
import com.mastercard.dis.mids.reference.example.SMSOTPExample;
import com.mastercard.dis.mids.reference.example.TpRpClaimSharingExample;
import com.mastercard.dis.mids.reference.example.UserAccountActivitySearchExample;
import com.mastercard.dis.mids.reference.example.UserProfileExample;
import com.mastercard.dis.mids.reference.example.dto.MultiDocumentVerificationToken;
import com.mastercard.dis.mids.reference.service.AuditEventsService;
import com.mastercard.dis.mids.reference.service.ClaimsApiService;
import com.mastercard.dis.mids.reference.service.DocumentVerificationService;
import com.mastercard.dis.mids.reference.service.EmailOtpService;
import com.mastercard.dis.mids.reference.service.GpaAuthenticationService;
import com.mastercard.dis.mids.reference.service.MultiDocumentVerificationService;
import com.mastercard.dis.mids.reference.service.RPActivitySearchService;
import com.mastercard.dis.mids.reference.service.ReAuthenticationService;
import com.mastercard.dis.mids.reference.service.SMSOTPService;
import com.mastercard.dis.mids.reference.service.TPDataSharesService;
import com.mastercard.dis.mids.reference.service.TPRPClaimsService;
import com.mastercard.dis.mids.reference.service.TPScopesService;
import com.mastercard.dis.mids.reference.service.UserAccountActivitySearchService;
import com.mastercard.dis.mids.reference.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.openapitools.client.model.AuditEvents;
import org.openapitools.client.model.AuditEventsItem;
import org.openapitools.client.model.ClaimIdentities;
import org.openapitools.client.model.ClaimScopes;
import org.openapitools.client.model.ClientIdentities;
import org.openapitools.client.model.ConfirmDocumentData;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.CreatedEmailOtp;
import org.openapitools.client.model.CreatedSMSOtp;
import org.openapitools.client.model.DocumentDataRetrieval;
import org.openapitools.client.model.DocumentVerificationConfirmData;
import org.openapitools.client.model.DocumentVerificationExtractedData;
import org.openapitools.client.model.Email;
import org.openapitools.client.model.EmailOtp;
import org.openapitools.client.model.IdentityAttribute;
import org.openapitools.client.model.IdentityAttributeDeletions;
import org.openapitools.client.model.IdentityAttributeItem;
import org.openapitools.client.model.IdentitySearch;
import org.openapitools.client.model.InitPremiumAuthentications;
import org.openapitools.client.model.InitializeAuthentications;
import org.openapitools.client.model.MultiDocConfirmData;
import org.openapitools.client.model.MultiDocumentConfirmedPDS;
import org.openapitools.client.model.MultiDocumentDataRetrieval;
import org.openapitools.client.model.OtpVerification;
import org.openapitools.client.model.RPActivitySearch;
import org.openapitools.client.model.RPClaimsUserConsent;
import org.openapitools.client.model.RPClaimsUserDetails;
import org.openapitools.client.model.SMSOtp;
import org.openapitools.client.model.TpDataShare;
import org.openapitools.client.model.UpdateIdentityAttributesData;
import org.openapitools.client.model.UserAccountActivitySearch;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.VerifyAuthentication;
import org.openapitools.client.model.VerifyAuthenticationDecisions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.mastercard.dis.mids.reference.util.Constants.ARID;
import static com.mastercard.dis.mids.reference.util.Constants.ARID_VALUE;
import static com.mastercard.dis.mids.reference.util.Constants.BEHAVIOUR_DATA;
import static com.mastercard.dis.mids.reference.util.Constants.EMAIL_ID;
import static com.mastercard.dis.mids.reference.util.Constants.USER_PROFILE_ID_VALUE;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@Component
@RequiredArgsConstructor
@Slf4j
public class MIDSReference {

    private static final String ATTRIBUTE_PDS = "attributePDS";
    private static final String FACE_PDS = "facePDS";
    private static final String EVIDENCE_PDS = "evidencePDS";
    private static final String MINIMUM_SPLIT_SDK_VERSION = "2.3.0";
    public static final String ANDROID = "ANDROID";

    private final EmailOtpService emailOtpService;
    private final SMSOTPService smsOtpService;
    private final DocumentVerificationService documentVerificationService;
    private final ReAuthenticationService initializeReAuthenticationService;
    private final TPRPClaimsService tprpClaimsService;
    private final UserProfileService userProfileService;
    private final UserAccountActivitySearchService userAccountActivitySearchService;
    private final RPActivitySearchService rPActivitySearchService;
    private final AuditEventsService auditEventsService;
    private final MultiDocumentVerificationService multiDocumentVerificationService;
    private final GpaAuthenticationService gpaAuthenticationService;
    private final ClaimsApiService claimsApiService;
    private final TPScopesService scopesService;
    private final TPDataSharesService tpDataSharesService;

    @Value("${mastercard.api.sdk.version}")
    private String sdkVersion;

    @Value("${mastercard.api.pds.update.conflict.attribute: FATHERS_NAME}")
    private String conflictAttribute;

    @Value("${mastercard.api.pds.update.conflict.attribute.value}")
    private String conflictAttributeValue;

    @Value("${mastercard.client.userProfileId}")
    private String userProfileId;

    @Value("${mastercard.api.scanID}")
    private String scanID;

    private ComparableVersion minSDKSplitPDSComparableVersion;

    @PostConstruct
    public void init() {
        minSDKSplitPDSComparableVersion = new ComparableVersion(MINIMUM_SPLIT_SDK_VERSION);
    }

    public void generateToken() {
        documentVerificationService.retrieveDocumentVerificationToken(DocumentVerificationTokenExample.getDocumentVerificationTokenObject());
    }

    public void generateMultiToken() {
        String pds = getPDS(false, Collections.singletonList(FACE_PDS));
        MultiDocumentVerificationToken multiDocumentVerificationToken = MultiDocumentVerificationTokenExample.getMultiDocumentVerificationToken();
        multiDocumentVerificationToken.setPds(pds);

        multiDocumentVerificationService.documentVerificationMultiAccessSDKToken(multiDocumentVerificationToken);
    }

    public void performEnrollment(boolean claimSharingFlow) {
        String pds = getPDS(claimSharingFlow, Collections.singletonList(ATTRIBUTE_PDS));
        callOtpFlows(pds);
    }

    public void performEnrollmentWithUpdateIdConfirmations() {
        String pds = getPDS(false, Collections.singletonList(ATTRIBUTE_PDS));
        callUpdateIdConfirmationsApi();
        callOtpFlows(pds);
    }

    public void performReAuthentication() {
        String pds = getPDS(false, Collections.singletonList(FACE_PDS));
        callInitiateAuthenticationsApi(pds);
        callAuthenticationResultsApi(pds);
        callStrongerAuthenticationApi(pds);
    }

    public void performAuthenticationDecisions() {
        String pds = getPDS(false, Collections.singletonList(ATTRIBUTE_PDS));
        callverifyAuthenticationDecissionsApi(scanID, pds);
    }

    public void performReAuthenticationWithUpdateIdConfirmations() {
        String pds = getPDS(false, Collections.singletonList(FACE_PDS));
        callInitiateAuthenticationsApi(pds);
        callAuthenticationResultsApi(pds);
        callUpdateIdConfirmationsApi();
    }

    public void performRPActivitySearch() {
        ClaimIdentities claimIdentities = performTpRpClaimSharing();
        String pds = claimIdentities.getPds();
        callCreateRPActivitySearch(pds);
    }

    public void callRetrieveIdentities() {
        String pds = getPDS(false, Arrays.asList(ATTRIBUTE_PDS, EVIDENCE_PDS, FACE_PDS));
        callRetriveIdentitiesApi(pds);
    }

    public void callUserProfileRegistration() {
        UserProfile userProfile = UserProfileExample.getUserProfile();
        userProfileService.userProfileRegistration(userProfile);
    }

    public ClaimIdentities performTpRpClaimSharing() {
        String pds = getPDS(false, Collections.singletonList(ATTRIBUTE_PDS));
        ClaimScopes claimScopes = TpRpClaimSharingExample.getClaimsSharingRequestObject();
        claimScopes.setPds(pds);
        return tprpClaimsService.claimsSharing(claimScopes);
    }

    public void performAuditEvents() {
        AuditEvents auditEvents = AuditEventsTokenExample.getAuditEventsToken();
        List<AuditEventsItem> sdkAuditEvents = InitializeReAuthenticationExample.getSdkAuditEventsObject();
        auditEvents.setSdkAuditEvents(sdkAuditEvents);
        auditEventsService.auditEvents(auditEvents);
    }

    public void performDeletion() {
        String userConsent = "ACCEPT";
        userProfileService.userProfileDelete(USER_PROFILE_ID_VALUE, userConsent);
    }

    private void callCreateRPActivitySearch(String pds) {
        RPActivitySearch rPActivitySearch = RPActivitySearchTokenExample.getRPActivitySearchToken();
        rPActivitySearch.setPds(pds);
        rPActivitySearchService.rpActivitySearch(rPActivitySearch);
    }

    private void callOtpFlows(String pds) {
        //Adding phone number to the PDS
        CreatedSMSOtp smsOtp = callCreateSmsOtpsApi(pds);
        //Adding email address to the original PDS
        CreatedEmailOtp emailOtp = callCreateEmailOtpsApi(smsOtp.getPds());
        //Calling verifications Api for both sms and email, using a fix otp number, expected invalid response.
        callSmsOtpVerificationsApi(smsOtp.getOtpId(), emailOtp.getPds());
        callEmailOtpVerificationsApi(emailOtp.getOtpId(), emailOtp.getPds());
    }

    public CreatedSMSOtp callCreateSmsOtpsApi(String pds) {
        SMSOtp smsOtpGeneration = SMSOTPExample.getSmsOtpGenerationObject();
        smsOtpGeneration.setPds(pds);
        return smsOtpService.createSmsOtp(smsOtpGeneration);
    }

    public CreatedEmailOtp callCreateEmailOtpsApi(String pds) {
        EmailOtp emailOtpGeneration = EmailOtpExample.getEmailOtpGenerationObject();
        emailOtpGeneration.setPds(pds);
        return emailOtpService.createEmailOtp(emailOtpGeneration);
    }

    public void callSmsOtpVerificationsApi(String otpId, String pds) {
        OtpVerification smsOtpVerification = SMSOTPExample.getSmsOtpVerificationObject();
        smsOtpVerification.setOtpId(otpId);
        smsOtpVerification.setPds(pds);
        smsOtpService.verifyOtp(smsOtpVerification);
    }

    public void callEmailOtpVerificationsApi(String otpId, String pds) {
        OtpVerification emailOtpVerification = EmailOtpExample.getEmailOtpVerificationObject();
        emailOtpVerification.setOtpId(otpId);
        emailOtpVerification.setPds(pds);
        emailOtpService.verifyEmailOtp(emailOtpVerification);
    }

    private DocumentVerificationExtractedData callDocumentDataRetrievalsApi() {
        return documentVerificationService.retrieveDocument(DocumentDataRetrievalExample.getDocumentDataRetrievalExample());
    }

    public void retrieveUserActivities() {
        String pds = getPDS(false, Collections.singletonList(ATTRIBUTE_PDS));
        callRetrieveUserActivitiesApi(pds);
    }

    private String callDocumentVerificationDataConfirmationsApi(DocumentVerificationExtractedData documentVerificationExtractedData, boolean claimSharingFlow) {
        DocumentVerificationConfirmData documentData = createDocumentVerificationConfirmData(documentVerificationExtractedData);
        DocumentVerificationConfirmData documentVerificationDataConfirmations = DocumentVerificationDataConfirmationsExample.getDocumentVerificationDataConfirmations(claimSharingFlow);
        documentVerificationDataConfirmations.setDocumentData(documentData.getDocumentData());
        documentVerificationDataConfirmations.setSdkVersion(sdkVersion);
        documentVerificationDataConfirmations.setDeviceInfo(BackUpAndRestoreExample.getDeviceInfo());
        ConfirmedPDS confirmedDataUpdatedPDS = documentVerificationService.confirmDocumentData(documentVerificationDataConfirmations);
        return confirmedDataUpdatedPDS.getPds();
    }

    private DocumentVerificationConfirmData createDocumentVerificationConfirmData(DocumentVerificationExtractedData extractedData) {

        Assert.notNull(extractedData.getDocumentData(), "DocumentData shouldn't be null");
        DocumentVerificationConfirmData documentData = new DocumentVerificationConfirmData();
        documentData.setDocumentData(new ConfirmDocumentData());
        documentData.getDocumentData().setAddressCity(extractedData.getDocumentData().getAddressCity());
        documentData.getDocumentData().setAddressCountry(extractedData.getDocumentData().getAddressCountry());
        documentData.getDocumentData().setAddressLine1(extractedData.getDocumentData().getAddressLine1());
        documentData.getDocumentData().setAddressLine2(extractedData.getDocumentData().getAddressLine2());
        documentData.getDocumentData().setAddressZipCode(extractedData.getDocumentData().getAddressZipCode());
        documentData.getDocumentData().setDateOfBirth(extractedData.getDocumentData().getDateOfBirth());
        documentData.getDocumentData().setDocumentNumber(extractedData.getDocumentData().getDocumentNumber());
        documentData.getDocumentData().setDocumentType(DocumentDataRetrieval.DocumentTypeEnum.fromValue(extractedData.getDocumentData().getDocumentType()).toString());
        documentData.getDocumentData().setExpiryDate(extractedData.getDocumentData().getExpiryDate());
        documentData.getDocumentData().setFirstName(extractedData.getDocumentData().getFirstName());
        documentData.getDocumentData().setLastName(extractedData.getDocumentData().getLastName());
        documentData.getDocumentData().setGender(extractedData.getDocumentData().getGender());
        documentData.getDocumentData().setIssuingAuthority(extractedData.getDocumentData().getIssuingAuthority());
        documentData.getDocumentData().setIssuingCountry(extractedData.getDocumentData().getIssuingCountry());
        documentData.getDocumentData().setIssuingDate(extractedData.getDocumentData().getIssuingDate());
        documentData.getDocumentData().setIssuingPlace(extractedData.getDocumentData().getIssuingPlace());
        documentData.getDocumentData().setPlaceOfBirth(extractedData.getDocumentData().getPlaceOfBirth());
        documentData.getDocumentData().setRgNumber(extractedData.getDocumentData().getRgNumber());
        documentData.getDocumentData().setCpf(extractedData.getDocumentData().getCpf());
        documentData.getDocumentData().setFathersName(extractedData.getDocumentData().getFathersName());
        documentData.getDocumentData().setMothersName(extractedData.getDocumentData().getMothersName());
        documentData.getDocumentData().setCardNumber(extractedData.getDocumentData().getCardNumber());
        return documentData;
    }

    private void callInitiateAuthenticationsApi(String pds) {
        InitializeAuthentications initializeAuthenticationPayload = InitializeReAuthenticationExample.getInitializeAuthenticationsObject();
        initializeAuthenticationPayload.setPds(pds);
        initializeAuthenticationPayload.setDeviceInfo(BackUpAndRestoreExample.getDeviceInfo());
        initializeReAuthenticationService.initiateAuthentication(initializeAuthenticationPayload);
    }

    private void callverifyAuthenticationDecissionsApi(String scanId, String pds) {
        VerifyAuthenticationDecisions verifyAuthenticationDecisions = InitializeReAuthenticationExample.initializeAuthenticationDecisionssObject();
        verifyAuthenticationDecisions.setPds(pds);
        initializeReAuthenticationService.initiateAuthenticationDecisions(scanId, verifyAuthenticationDecisions);
    }

    private ClientIdentities callRetriveIdentitiesApi(String pds) {
        IdentitySearch identitySearchPayload = UserProfileExample.getIdentitySearchObject();
        identitySearchPayload.setPds(pds);
        return userProfileService.retrieveIdentities(identitySearchPayload);
    }

    private void callAuthenticationResultsApi(String pds) {
        VerifyAuthentication verifyAuthenticationPayload = InitializeReAuthenticationExample.getVerifyAuthenticationsObject();
        verifyAuthenticationPayload.setPds(pds);
        verifyAuthenticationPayload.setDeviceInfo(BackUpAndRestoreExample.getDeviceInfo());
        initializeReAuthenticationService.authenticationResults(verifyAuthenticationPayload);
    }

    private void callStrongerAuthenticationApi(String pds) {
        InitPremiumAuthentications initPremiumAuthenticationsPayload = InitPremiumAuthenticationsExample.getInitPremiumAuthentications();
        initPremiumAuthenticationsPayload.setPds(pds);
        initPremiumAuthenticationsPayload.setDeviceInfo(BackUpAndRestoreExample.getDeviceInfo());
        gpaAuthenticationService.initPremiumAuthentications(initPremiumAuthenticationsPayload);
    }

    private void callUpdateIdConfirmationsApi() {
        documentVerificationService.updateIdConfirmations(DocumentIdConfirmationExample.getDocumentIdConfirmationExampleObject());
    }

    public String getPDS(boolean claimSharingFlow, List<String> pdsTypes) {
        DocumentVerificationExtractedData documentVerificationExtractedData = callDocumentDataRetrievalsApi();
        return extractPds(callDocumentVerificationDataConfirmationsApi(documentVerificationExtractedData, claimSharingFlow), pdsTypes);
    }

    private String extractPds(String pdsString, List<String> pdsTypes) {
        if (StringUtils.isEmpty(pdsString)) {
            return pdsString;
        }
        if (isPdsSplit()) {
            JSONObject pds = new JSONObject();
            for (String pdsType : pdsTypes) {
                pds.appendField(pdsType, ((JSONObject) JSONValue.parse(Base64.getDecoder().decode(pdsString))).getAsString(pdsType));
            }
            return new String(Base64.getEncoder().encode(pds.toJSONString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        }
        return pdsString;
    }

    private boolean isPdsSplit() {
        return new ComparableVersion(sdkVersion).compareTo(minSDKSplitPDSComparableVersion) >= 0;
    }

    public void addMultiDoc() {
        multiDocTasks();
        callUpdateIdConfirmationsApi();
    }

    public MultiDocumentConfirmedPDS multiDocTasks() {
        String pds = getPDS(false, Arrays.asList(FACE_PDS, ATTRIBUTE_PDS, EVIDENCE_PDS));
        DocumentVerificationExtractedData multiDocumentVerificationExtractedData = callMultiDocumentDataRetrievalsApi(extractPds(pds, Arrays.asList(FACE_PDS)));
        DocumentVerificationConfirmData documentData = createDocumentVerificationConfirmData(multiDocumentVerificationExtractedData);
        return callMultiDocumentDataConfirmationApi(documentData, extractPds(pds, Arrays.asList(ATTRIBUTE_PDS, EVIDENCE_PDS)));
    }

    private MultiDocumentConfirmedPDS callMultiDocumentDataConfirmationApi(DocumentVerificationConfirmData documentVerificationConfirmDataDocumentData, String pds) {
        MultiDocConfirmData multiDocConfirmData = MultiDocumentVerificationExample.getMultiDocConfirmData();
        multiDocConfirmData.setPds(pds);
        multiDocConfirmData.setDocumentData(documentVerificationConfirmDataDocumentData.getDocumentData());
        return multiDocumentVerificationService.multiDocumentVerificationConfirmation(multiDocConfirmData);
    }

    private DocumentVerificationExtractedData callMultiDocumentDataRetrievalsApi(String pds) {
        MultiDocumentDataRetrieval multiDocumentDataRetrieval = MultiDocumentVerificationExample.getMultiDocumentDataRetrieval();
        multiDocumentDataRetrieval.setPds(pds);
        multiDocumentDataRetrieval.setDocumentType(MultiDocumentDataRetrieval.DocumentTypeEnum.PASSPORT);

        return multiDocumentVerificationService.multiDocumentVerificationStatus(multiDocumentDataRetrieval);
    }

    private void callRetrieveUserActivitiesApi(String pds) {
        UserAccountActivitySearch userAccountActivitySearchPayload = UserAccountActivitySearchExample.getUserAccountActivitiesExampleObject();
        userAccountActivitySearchPayload.setPds(pds);
        userAccountActivitySearchService.retrieveUserActivities(userAccountActivitySearchPayload);
    }

    public void enrollmentAndReAuthRPClaimsSharing() {
        String pds = getPDS(true, Collections.singletonList(ATTRIBUTE_PDS));
        getRpRequestedScopes();
        extractClaimsUserData(pds);
        getUserConsentStatus(pds);
    }

    private void getUserConsentStatus(String pds) {
        RPClaimsUserConsent rpClaimsUserConsent = ClaimsApiExample.getUserConsentStatusExample();
        rpClaimsUserConsent.setPds(pds);
        claimsApiService.getUserConsentStatus(rpClaimsUserConsent);
    }

    private void extractClaimsUserData(String pds) {
        RPClaimsUserDetails rpClaimsUserDetails = ClaimsApiExample.extractClaimsUserDataExample();
        rpClaimsUserDetails.setPds(pds);
        claimsApiService.extractClaimsUserData(rpClaimsUserDetails);
    }

    public void getRpRequestedScopes() {
        scopesService.getRpScopes(ARID_VALUE);
    }

    public void updatePdsData() {
        TpDataShare tpDataShare = new TpDataShare();
        tpDataShare.setPds(getPDS(true, Collections.singletonList(ATTRIBUTE_PDS)));
        Email email = new Email();
        email.setValue(EMAIL_ID);
        tpDataShare.setEmail(email);
        tpDataShare.setUserProfileId(userProfileId);
        tpDataShare.setUserConsent(ACCEPT);
        tpDataSharesService.updatePdsData(tpDataShare);
    }

    public void updateIdentiyAttributes() {
        MultiDocumentConfirmedPDS multiDocumentConfirmedPDS = multiDocTasks();
        UpdateIdentityAttributesData updateIdentityAttributesData = new UpdateIdentityAttributesData();
        updateIdentityAttributesData.setPds(multiDocumentConfirmedPDS.getPds());
        updateIdentityAttributesData.setUserConsent(ACCEPT);
        updateIdentityAttributesData.setIdentityAttributes(createIdentityAttributes());
        multiDocumentVerificationService.updateIdentityAttributes(updateIdentityAttributesData);
    }

    public List<IdentityAttributeItem> createIdentityAttributes() {
        List<IdentityAttributeItem> identityAttributes = new ArrayList<>();
        IdentityAttributeItem identityAttributeItem = new IdentityAttributeItem();
        identityAttributeItem.setAttributeName(IdentityAttributeItem.AttributeNameEnum.valueOf(conflictAttribute));
        identityAttributeItem.setAttributeValue(conflictAttributeValue);
        identityAttributes.add(identityAttributeItem);
        return identityAttributes;
    }

    public void deleteIdentityAttribute() {
        IdentityAttributeDeletions identityAttributeDeletions = new IdentityAttributeDeletions();
        String pds = getPDS(false, Arrays.asList(ATTRIBUTE_PDS, EVIDENCE_PDS, FACE_PDS));

        String attributeId = null;
        Map<String, Map<String, IdentityAttribute>> attributes = callRetriveIdentitiesApi(pds).getAttributes();
        if (attributes.containsKey("driverLicense")) {
            attributeId = attributes.get("driverLicense").entrySet().iterator().next().getKey();
        }

        identityAttributeDeletions.setPds(pds);
        identityAttributeDeletions.setUserConsent(ACCEPT);
        identityAttributeDeletions.setAttributeName(IdentityAttributeDeletions.AttributeNameEnum.DRIVER_LICENSE);
        identityAttributeDeletions.setAttributeId(attributeId);

        userProfileService.deleteIdentityAttribute(identityAttributeDeletions);
    }

}
