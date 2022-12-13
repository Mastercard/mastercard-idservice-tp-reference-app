package com.mastercard.dis.mids.reference.component;

import com.mastercard.dis.mids.reference.service.AuditEventsService;
import com.mastercard.dis.mids.reference.service.ClaimsApiService;
import com.mastercard.dis.mids.reference.service.DocumentVerificationService;
import com.mastercard.dis.mids.reference.service.EmailOtpService;
import com.mastercard.dis.mids.reference.service.GpaAuthenticationService;
import com.mastercard.dis.mids.reference.service.MultiDocumentVerificationService;
import com.mastercard.dis.mids.reference.service.RPActivitySearchService;
import com.mastercard.dis.mids.reference.service.ReAuthenticationService;
import com.mastercard.dis.mids.reference.service.SMSOTPService;
import com.mastercard.dis.mids.reference.service.TPRPClaimsService;
import com.mastercard.dis.mids.reference.service.TPScopesService;
import com.mastercard.dis.mids.reference.service.UserAccountActivitySearchService;
import com.mastercard.dis.mids.reference.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.openapitools.client.model.ActivationCode;
import org.openapitools.client.model.ClaimIdentities;
import org.openapitools.client.model.ClientIdentities;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.CreatedEmailOtp;
import org.openapitools.client.model.CreatedSMSOtp;
import org.openapitools.client.model.DocumentDataStatus;
import org.openapitools.client.model.DocumentVerificationExtractedData;
//import org.openapitools.client.model.MobileIdRegistration;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MIDSReferenceTest {

    public static final String ISSUING_DATE = "2020-09-09";
    public static final String CREATED_DATE = "2021-04-09T18:55:35.944Z";
    public static final String ACTION_SAMPLE = "ID Created";
    public static final String CONTENT_VALUE = "5ed2cf35-a125-aaaaaaaaaa-bbbbbb-ccc";
    public static final String ARID = "524a46e4-9ecf-11eb-a8b3-0242ac130009";

    @InjectMocks
    MIDSReference midsReference;

    @Mock
    EmailOtpService emailOtpServiceMock;

    @Mock
    SMSOTPService smsOtpServiceMock;

    @Mock
    DocumentVerificationService documentVerificationServiceMock;

    @Mock
    ReAuthenticationService initializeReAuthenticationServiceMock;

    @Mock
    UserProfileService userProfileService;

    @Mock
    TPRPClaimsService tprpClaimsService;

    @Mock
    UserAccountActivitySearchService userAccountActivitySearchServiceMock;

    @Mock
    RPActivitySearchService rPActivitySearchService;

    @Mock
    AuditEventsService auditEventsService;

    @Mock
    MultiDocumentVerificationService multiDocumentVerificationServiceMock;

    @Mock
    GpaAuthenticationService gpaAuthenticationService;

    @Mock
    ClaimsApiService claimsApiService;

    @Mock
    TPScopesService tpScopesService;

    @BeforeEach
    void prepareTest() {
        ReflectionTestUtils.setField(midsReference, "sdkVersion", "2.3.0");
        midsReference.init();
    }

    @Test
    void generateToken_successfulResponse() {
        midsReference.generateToken();

        verify(documentVerificationServiceMock, times(1)).retrieveDocumentVerificationToken(any());
    }

    @Test
    void generateMultiToken_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        midsReference.generateMultiToken();

        verify(multiDocumentVerificationServiceMock, times(1)).documentVerificationMultiAccessSDKToken(any());
    }

    @Test
    void generateMultiToken_splitPds_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        ConfirmedPDS confirmedDataUpdatedPDS = new ConfirmedPDS();
        confirmedDataUpdatedPDS.setPds("eyJldmlkZW5jZVBEUyI6IiIsImZhY2VQRFMiOiIiLCJhdHRyaWJ1dGVQRFMiOiIifQ==");
        doReturn(confirmedDataUpdatedPDS).when(documentVerificationServiceMock).confirmDocumentData(any());
        midsReference.generateMultiToken();

        verify(multiDocumentVerificationServiceMock, times(1)).documentVerificationMultiAccessSDKToken(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void performEnrollment_successfulResponse(boolean claimSharingFlow) {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        doReturn(new CreatedSMSOtp()).when(smsOtpServiceMock).createSmsOtp(any());
        doReturn(new CreatedEmailOtp()).when(emailOtpServiceMock).createEmailOtp(any());

        midsReference.performEnrollment(claimSharingFlow);

        verify(documentVerificationServiceMock, times(1)).retrieveDocument(any());
        verify(documentVerificationServiceMock, times(1)).confirmDocumentData(any());
        verify(smsOtpServiceMock, times(1)).createSmsOtp(any());
        verify(emailOtpServiceMock, times(1)).createEmailOtp(any());
        verify(smsOtpServiceMock, times(1)).verifyOtp(any());
        verify(emailOtpServiceMock, times(1)).verifyEmailOtp(any());
    }

    @Test
    void performEnrollmentWithUpdateIdConfirmations_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        doReturn(new CreatedSMSOtp()).when(smsOtpServiceMock).createSmsOtp(any());
        doReturn(new CreatedEmailOtp()).when(emailOtpServiceMock).createEmailOtp(any());

        midsReference.performEnrollmentWithUpdateIdConfirmations();

        verify(documentVerificationServiceMock, times(1)).retrieveDocument(any());
        verify(documentVerificationServiceMock, times(1)).confirmDocumentData(any());
        verify(documentVerificationServiceMock, times(1)).updateIdConfirmations(any());
        verify(smsOtpServiceMock, times(1)).createSmsOtp(any());
        verify(emailOtpServiceMock, times(1)).createEmailOtp(any());
        verify(smsOtpServiceMock, times(1)).verifyOtp(any());
        verify(emailOtpServiceMock, times(1)).verifyEmailOtp(any());
    }

    @Test
    void performReAuthentication_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());

        midsReference.performReAuthentication();

        verify(initializeReAuthenticationServiceMock, times(1)).initiateAuthentication(any());
        verify(gpaAuthenticationService, times(1)).initPremiumAuthentications(any());
    }

    @Test
    void performReAuthentication_successfulResponse_nullDocumentIssueDate() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());

        midsReference.performReAuthentication();

        verify(initializeReAuthenticationServiceMock, times(1)).initiateAuthentication(any());
        verify(gpaAuthenticationService, times(1)).initPremiumAuthentications(any());
    }

    @Test
    void performReAuthenticationDecissions_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());

        midsReference.performAuthenticationDecisions();
        verify(initializeReAuthenticationServiceMock, times(1)).initiateAuthenticationDecisions(any(), any());
    }

    @Test
    void performReAuthenticationDecissions_successfulResponse_nullDocumentIssueDate() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());

        midsReference.performAuthenticationDecisions();
        verify(initializeReAuthenticationServiceMock, times(1)).initiateAuthenticationDecisions(any(), any());
    }

    @Test
    void performReAuthenticationWithUpdateIdConfirmations_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());

        midsReference.performReAuthenticationWithUpdateIdConfirmations();

        verify(initializeReAuthenticationServiceMock, times(1)).initiateAuthentication(any());
        verify(documentVerificationServiceMock, times(1)).updateIdConfirmations(any());
    }

    @Test
    void performTpRpClaimSharing_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());

        midsReference.performTpRpClaimSharing();

        verify(tprpClaimsService, times(1)).claimsSharing(any());
    }

    @Test
    void callRetrieveUserActivities_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        midsReference.retrieveUserActivities();
        verify(userAccountActivitySearchServiceMock, times(1)).retrieveUserActivities(any());
    }

    private Object createDocumentExtractedData(boolean b) {
        DocumentDataStatus documentData = new DocumentDataStatus();
        documentData.setDateOfBirth(ISSUING_DATE);
        documentData.setExpiryDate(ISSUING_DATE);

        documentData.setIssuingDate(ISSUING_DATE);
        documentData.setDocumentType("PASSPORT");

        DocumentVerificationExtractedData extractedData = new DocumentVerificationExtractedData();
        extractedData.setDocumentData(documentData);
        return extractedData;
    }

    @Test
    void callRetrieveIdentities_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        midsReference.callRetrieveIdentities();
        verify(userProfileService, times(1)).retrieveIdentities(any());
    }
    @Test
    void callDeleteIdentityAttribute_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        doReturn(new ClientIdentities()).when(userProfileService).retrieveIdentities(any());
        midsReference.deleteIdentityAttribute();
        verify(userProfileService, times(1)).deleteIdentityAttribute(any());
    }

    @Test
    void performUserProfileRegistration_successfulResponse() {
        midsReference.callUserProfileRegistration();
        verify(userProfileService, times(1)).userProfileRegistration(any());
    }

    @Test
    void performRPActivitySearh_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        doReturn(new ClaimIdentities()).when(tprpClaimsService).claimsSharing(any());
        midsReference.performRPActivitySearch();
        verify(rPActivitySearchService, times(1)).rpActivitySearch(any());
    }

    @Test
    void performDeleteUserProfile_successfulResponse() {
        midsReference.performDeletion();
        verify(userProfileService, times(1)).userProfileDelete(any(), any());
    }

    @Test
    void performAuditEvents_successfulResponse() {
        midsReference.performAuditEvents();
        verify(auditEventsService, times(1)).auditEvents(any());
    }

    private DocumentVerificationExtractedData createDocumentExtractedData(Boolean includeIssuingDate) {
        DocumentDataStatus documentData = new DocumentDataStatus();
        documentData.setDateOfBirth(ISSUING_DATE);
        documentData.setExpiryDate(ISSUING_DATE);
        if (includeIssuingDate) {
            documentData.setIssuingDate(ISSUING_DATE);
        }
        documentData.setDocumentType("PASSPORT");

        DocumentVerificationExtractedData extractedData = new DocumentVerificationExtractedData();
        extractedData.setDocumentData(documentData);
        return extractedData;
    }

    @Test
    void reAuthenticationForRPClaimsSharing_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        midsReference.enrollmentAndReAuthRPClaimsSharing();
        verify(claimsApiService, times(1)).getUserConsentStatus(any());
        verify(claimsApiService, times(1)).extractClaimsUserData(any());
    }

    @Test
    void getRpRequestedScopes_successfulResponse() {
        midsReference.getRpRequestedScopes();
        tpScopesService.getRpScopes(ARID);
        verify(tpScopesService, times(1)).getRpScopes(ARID);
    }

 /*   @Test
    void performMobileIDRegistration_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        doReturn(new MobileIdRegistration()).when(userProfileService).mobileIdRegistrations(any());

        midsReference.mobileIdRegistrations();
        verify(userProfileService, times(1)).mobileIdRegistrations(any());
    }

    @Test
    void getMobileIDActivationCode_successfulResponse() {
        doReturn(createDocumentExtractedData(false)).when(documentVerificationServiceMock).retrieveDocument(any());
        doReturn(new MobileIdRegistration()).when(userProfileService).mobileIdRegistrations(any());
        doReturn(new ConfirmedPDS()).when(documentVerificationServiceMock).confirmDocumentData(any());
        doReturn(new ActivationCode()).when(userProfileService).getMobileIdActivationCode(any(), any());

        midsReference.getMobileIdActivationCode();
        verify(userProfileService, times(1)).getMobileIdActivationCode(any(), any());
    }*/
}