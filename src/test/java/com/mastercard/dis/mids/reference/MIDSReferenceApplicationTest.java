package com.mastercard.dis.mids.reference;

import com.mastercard.dis.mids.reference.component.MIDSReference;
import com.mastercard.dis.mids.reference.constants.Cache;
import com.mastercard.dis.mids.reference.constants.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.ClientIdentities;
import org.openapitools.client.model.ConfirmedPDS;
import org.openapitools.client.model.CreatedEmailOtp;
import org.openapitools.client.model.CreatedSMSOtp;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MIDSReferenceApplicationTest {

   // @Mock
   // Scanner scanner;

    @InjectMocks
    private MIDSReferenceApplication midsReferenceApplication;

    @Mock
    MIDSReference midsReference;

    String pds ;

    private static final Map<String, String> menuMapTest = new HashMap<String, String>() {{
        put("1", "1)   Registering a User Profile");
        put("2", "2)   Access Token API (SDKToken)");
        put("3", "3)   Create User Identity");
        put("4", "4)   Multi-Access Token API (MultiSDKToken)");
        put("6", "6)   Update ID Confirmations (Enrollment)");
        put("8", "8)   User Account Activity Searches");
        put("9", "9)   Email OTP");
        put("10", "10)   SMS OTP");
        put("11", "11)   User Profiles Identity Searches");
        put("12", "12)   Share User Identity (TP-TP)");
        put("13", "13)   RP Activity Searches");
        put("14", "14)   Audit Events");
        put("15", "15)   Delete ID");
        put("17", "17)   Additional Document Support");
        put("20", "20)   TP Data Shares");
        put("21", "21)   Update Identity");
        put("22", "22)   Delete Identity Attribute");
        put("23", "23)   Authentication Decisions");
        put("24", "24)   Exit");
    }};

    private static final Map<String, String> menuMapErrorTest = new HashMap<String, String>() {{
         put("5", "5)   Access User Identity");
          put("7", "7)   Update ID Confirmations (Re-authentication)");
          put("16", "16)   Share User Identity (TP-RP) (Enrollment)");
         put("18", "18)   Share User Identity (TP-RP) (Re-authentication)");
          put("19", "19)   TP Scopes");
    }};

    @BeforeEach
    void setUp(){
        Cache.faceAndAttributePds = null;
         pds = "eyJldmlkZW5jZVBEUyI6IiIsImZhY2VQRFMiOiIiLCJhdHRyaWJ1dGVQRFMiOiIifQ==";
    }

    @Test
    void consoleMenu_runAndcheckingValues_works() {
        Map<String, String> menu = new Menu().get();
        for (Map.Entry<String, String> entry : menuMapTest.entrySet()) {
            String valueMenu = menu.get(entry.getKey());
            Assertions.assertEquals(valueMenu, entry.getValue());
        }
    }

    @Test
    void console_showMenu_works() {
        MIDSReferenceApplication spyMIDSReferenceApplication = spy(new MIDSReferenceApplication(null));
        spyMIDSReferenceApplication.showMenu();
        verify(spyMIDSReferenceApplication, times(1)).showMenu();
    }

    @Test
    void console_handleOption_works() {
        MIDSReferenceApplication spyMIDSReferenceApplication = spy(new MIDSReferenceApplication(null));
        menuMapTest.put("99","Invalid option!");
        for (Map.Entry<String, String> entry : menuMapTest.entrySet()) {
            spyMIDSReferenceApplication.handleOption(entry.getKey());
        }
        verify(spyMIDSReferenceApplication, times(menuMapTest.size())).handleOption(anyString());
    }

    @Test
    void console_handleOption_works_Error(){
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",new Scanner("12345 \n 54321 "));
        for (Map.Entry<String, String> entry : menuMapErrorTest.entrySet()) {
            midsReferenceApplication.handleOption(entry.getKey());
        }

       Assertions.assertEquals(5,menuMapErrorTest.size());
    }

    @Test
    void  performReAuthentication_test(){


        doReturn(pds).when(midsReference).getPDS(false, Collections.singletonList("facePDS"));

        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",new Scanner("63d495cd-2575-453d-923d-598dc4fddcef"));
        midsReferenceApplication.performReAuthentication();
        verify(midsReference, times(1)).getPDS(false, Collections.singletonList("facePDS"));
    }

    @Test
    void performReAuthenticationWithUpdateIdConfirmations_test(){

        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",new Scanner("63d495cd-2575-453d-923d-598dc4fddcef"));
        midsReferenceApplication.performReAuthenticationWithUpdateIdConfirmations();

        verify(midsReference, times(1)).performReAuthenticationWithUpdateIdConfirmations();
    }


    @Test
    void performEnrollmentWithUpdateIdConfirmations_test(){

        CreatedSMSOtp smsOtp = new CreatedSMSOtp();
        CreatedEmailOtp emailOtp = new CreatedEmailOtp();

        doReturn(pds).when(midsReference).getPDS(false, Collections.singletonList("attributePDS"));
        doNothing().when(midsReference).callUpdateIdConfirmationsApi();

        doReturn(smsOtp).when(midsReference).callCreateSmsOtpsApi(any());
        doReturn(emailOtp).when(midsReference).callCreateEmailOtpsApi(any());
         Scanner scanner =  new Scanner("12345 \n 54321");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.performEnrollmentWithUpdateIdConfirmations();
        verify(midsReference, times(1)).getPDS(false, Collections.singletonList("attributePDS"));
    }

    @Test
    void performEnrollment_test(){
        CreatedSMSOtp smsOtp = new CreatedSMSOtp();
        CreatedEmailOtp emailOtp = new CreatedEmailOtp();

        doReturn(pds).when(midsReference).getPDS(false, Collections.singletonList("attributePDS"));

        doReturn(smsOtp).when(midsReference).callCreateSmsOtpsApi(any());
        doReturn(emailOtp).when(midsReference).callCreateEmailOtpsApi(any());
        Scanner scanner =  new Scanner("12345 \n 54321");

        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.performEnrollment(false);
        verify(midsReference, times(1)).getPDS(false, Collections.singletonList("attributePDS"));
    }


    @Test
     void performEmailOptions_test(){
         doReturn(pds).when(midsReference).getPDS(false, Collections.singletonList("attributePDS"));
         CreatedEmailOtp emailOtp = new CreatedEmailOtp();
         doReturn(emailOtp).when(midsReference).callCreateEmailOtpsApi(any());

         Scanner scanner =  new Scanner("12345");
         ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
         midsReferenceApplication.performEmailOptions(false);
         verify(midsReference, times(1)).getPDS(false, Collections.singletonList("attributePDS"));
     }

     @Test
     void performSMSOptions_test(){
         doReturn(pds).when(midsReference).getPDS(false, Collections.singletonList("attributePDS"));
         CreatedSMSOtp smsOtp = new CreatedSMSOtp();
         doReturn(smsOtp).when(midsReference).callCreateSmsOtpsApi(any());

         Scanner scanner =  new Scanner("54321");
         ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
         midsReferenceApplication.performSMSOptions(false);
         verify(midsReference, times(1)).getPDS(false, Collections.singletonList("attributePDS"));
     }

     @Test
     void  updateIdentityAttributes_test(){
         doReturn(pds).when(midsReference).getPDS(false, Arrays.asList("facePDS", "attributePDS", "evidencePDS"));
         doNothing().when(midsReference).updateIdentiyAttributes();

         Scanner scanner =  new Scanner("e1b33934-6ff3-45b8-bcbf-c3a2f31c80c8");
         ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
         midsReferenceApplication.updateIdentityAttributes();
         verify(midsReference, times(1)).getPDS(false, Arrays.asList("facePDS", "attributePDS", "evidencePDS"));
     }


     @Test
    void getRpScopes_test(){
         Scanner scanner =  new Scanner("7ec89f22-8b4c-44ad-80a5-088c87bd61df");
         ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
         doNothing().when(midsReference).getRpRequestedScopes();

         midsReferenceApplication.getRpScopes();
         verify(midsReference,times(1)).getRpRequestedScopes();
    }


    @Test
    void performRPClaimsSharingReAuthentication_test(){
        Scanner scanner =  new Scanner("7ec89f22-8b4c-44ad-80a5-088c87bd61df");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
        doNothing().when(midsReference).enrollmentAndReAuthRPClaimsSharing();

        midsReferenceApplication.performRPClaimsSharingReAuthentication();
        verify(midsReference,times(1)).enrollmentAndReAuthRPClaimsSharing();
    }

    @Test
     void performRPClaimsSharingEnrollment_test(){
        Scanner scanner =  new Scanner("7ec89f22-8b4c-44ad-80a5-088c87bd61df");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
        doReturn(pds).when(midsReference).getPDS(true, Arrays.asList("attributePDS","facePDS"));
        doNothing().when(midsReference).enrollmentAndReAuthRPClaimsSharing();

        midsReferenceApplication.performRPClaimsSharingEnrollment(true);
        verify(midsReference,times(1)).enrollmentAndReAuthRPClaimsSharing();
    }

    @Test
    void performMultiDocEnrollment(){
        doReturn(pds).when(midsReference).getPDS(false, Arrays.asList("facePDS", "attributePDS", "evidencePDS"));
        Scanner scanner =  new Scanner("e1b33934-6ff3-45b8-bcbf-c3a2f31c80c8");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        doNothing().when(midsReference).addMultiDoc();

        midsReferenceApplication.performMultiDocEnrollment();
        verify(midsReference,times(1)).addMultiDoc();
    }

    @Test
    void  deleteIdentityAttribute_test(){

        doNothing().when(midsReference).deleteIdentityAttribute();
        midsReferenceApplication.deleteIdentityAttribute();
        verify(midsReference,times(1)).deleteIdentityAttribute();
    }

    @Test
    void performTpDataShares_test(){
        doNothing().when(midsReference).updatePdsData();
        midsReferenceApplication.performTpDataShares();
        verify(midsReference,times(1)).updatePdsData();
    }

    @Test
    void retrieveUserActivities_test(){
        doNothing().when(midsReference).retrieveUserActivities();
        midsReferenceApplication.retrieveUserActivities();
        verify(midsReference,times(1)).retrieveUserActivities();
    }

    @Test
    void performDeletion_test(){
        doNothing().when(midsReference).performDeletion();
        midsReferenceApplication.performDeletion();
        verify(midsReference,times(1)).performDeletion();
    }

    @Test
    void performAuditEvents_test(){
        doNothing().when(midsReference).performAuditEvents();
        midsReferenceApplication.performAuditEvents();
        verify(midsReference,times(1)).performAuditEvents();
    }

    @Test
    void  performActivitySearch_test(){
        doNothing().when(midsReference).performRPActivitySearch();
        midsReferenceApplication.performActivitySearch();
        verify(midsReference,times(1)).performRPActivitySearch();
    }

    @Test
    void performTpRpClaims_test(){
         midsReferenceApplication.performTpRpClaims();
        verify(midsReference,times(1)).performTpRpClaimSharing();
    }

    @Test
    void performAuthenticationDecisions_test(){
        doNothing().when(midsReference).performAuthenticationDecisions();
        midsReferenceApplication.performAuthenticationDecisions();
        verify(midsReference,times(1)).performAuthenticationDecisions();
    }

    @Test
    void generateMultiSDKToken_test(){
        doNothing().when(midsReference).generateMultiToken();
        midsReferenceApplication.generateMultiSDKToken();
        verify(midsReference,times(1)).generateMultiToken();
    }

    @Test
    void generateSDKToken_test(){
        doNothing().when(midsReference).generateToken();
        midsReferenceApplication.generateSDKToken();
        verify(midsReference,times(1)).generateToken();
    }

    @Test
   void retrieveIdentities_test(){
        doNothing().when(midsReference).callRetrieveIdentities();
        midsReferenceApplication.retrieveIdentities();
        verify(midsReference,times(1)).callRetrieveIdentities();
   }

   @Test
    void userProfileRegistration_test(){
       doNothing().when(midsReference).callUserProfileRegistration();
       midsReferenceApplication.userProfileRegistration();
       verify(midsReference,times(1)).callUserProfileRegistration();
    }

}


