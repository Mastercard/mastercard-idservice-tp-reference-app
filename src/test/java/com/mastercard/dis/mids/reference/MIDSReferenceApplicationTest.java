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
import org.openapitools.client.model.CreatedEmailOtp;
import org.openapitools.client.model.CreatedSMSOtp;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MIDSReferenceApplicationTest {

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
        put("20", "20)   TP Data Shares");
        put("22", "22)   Delete Identity Attribute");
        put("23", "23)   Authentication Decisions");
        put("24", "24)   Exit");
    }};

    private static final Map<String, String> menuMapErrorTest = new HashMap<String, String>() {{
        put("5", "5)   Access User Identity");
        put("7", "7)   Update ID Confirmations (Re-authentication)");
        put("16", "16)   Share User Identity (TP-RP) (Enrollment)");
        put("17", "17)   Additional Document Support");
        put("18", "18)   Share User Identity (TP-RP) (Re-authentication)");
        put("19", "19)   TP Scopes");
        put("21", "21)   Update Identity");
    }};

    @BeforeEach
    void setUp(){
        Cache.setFaceAndAttributePds(null);
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
        Scanner scanner;
        menuMapTest.put("99","Invalid option!");
        for (Map.Entry<String, String> entry : menuMapTest.entrySet()) {
            scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 123456 \n 123456 ");
            ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
            midsReferenceApplication.handleOption(entry.getKey());
        }
        Assertions.assertEquals(18,menuMapTest.size());
    }

    @Test
    void console_handleOption_works_prompt(){

        Scanner scanner;
        for (Map.Entry<String, String> entry : menuMapErrorTest.entrySet()) {
             scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 2d205aec-c7b5-4881-b4b1-000000000200");
            ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

            midsReferenceApplication.handleOption(entry.getKey());
        }
       Assertions.assertEquals(7,menuMapErrorTest.size());
    }

    @Test
    void  performReAuthentication_test(){


        doReturn(pds).when(midsReference).getPDS(false, Collections.singletonList("facePDS"));
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
        midsReferenceApplication.performReAuthentication();
        verify(midsReference, times(1)).getPDS(false, Collections.singletonList("facePDS"));
    }

    @Test
    void performReAuthenticationWithUpdateIdConfirmations_test(){

        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
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
         Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 12345 \n 54321");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.performEnrollmentWithUpdateIdConfirmations();
        verify(midsReference, times(1)).getPDS(false, Collections.singletonList("attributePDS"));
    }

    @Test
    void performEnrollment_test(){
        CreatedSMSOtp smsOtp = new CreatedSMSOtp();
        CreatedEmailOtp emailOtp = new CreatedEmailOtp();
        Cache.setPdsEnrollment(null);

        doReturn(smsOtp).when(midsReference).callCreateSmsOtpsApi(any());
        doReturn(emailOtp).when(midsReference).callCreateEmailOtpsApi(any());
        Scanner scanner = new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 12345 \n 54321");

        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.performEnrollment();
        verify(midsReference, times(1)).callCreateSmsOtpsApi(Cache.getPdsEnrollment());
    }


    @Test
     void performEmailOptions_test(){
         doReturn(pds).when(midsReference).getPDS(false, Collections.singletonList("attributePDS"));
         CreatedEmailOtp emailOtp = new CreatedEmailOtp();
         doReturn(emailOtp).when(midsReference).callCreateEmailOtpsApi(any());

         Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 54321");
         ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
         midsReferenceApplication.performEmailOptions(false);
         verify(midsReference, times(1)).getPDS(false, Collections.singletonList("attributePDS"));
     }

     @Test
     void performSMSOptions_test(){
         doReturn(pds).when(midsReference).getPDS(false, Collections.singletonList("attributePDS"));
         CreatedSMSOtp smsOtp = new CreatedSMSOtp();
         doReturn(smsOtp).when(midsReference).callCreateSmsOtpsApi(any());

         Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 54321");
         ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
         midsReferenceApplication.performSMSOptions(false);
         verify(midsReference, times(1)).getPDS(false, Collections.singletonList("attributePDS"));
     }

     @Test
     void  updateIdentityAttributes_test(){
         doReturn(pds).when(midsReference).getPDS(false, Arrays.asList("facePDS", "attributePDS", "evidencePDS"));
         doNothing().when(midsReference).updateIdentiyAttributes();

         Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65  \n 2d205aec-c7b5-4881-b4b1-000000000200 \n e1b33934-6ff3-45b8-bcbf-c3a2f31c80c8");
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
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65  \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 7ec89f22-8b4c-44ad-80a5-088c87bd61df");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
        doNothing().when(midsReference).enrollmentAndReAuthRPClaimsSharing();

        midsReferenceApplication.performRPClaimsSharingReAuthentication();
        verify(midsReference,times(1)).enrollmentAndReAuthRPClaimsSharing();
    }

    @Test
     void performRPClaimsSharingEnrollment_test(){
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65  \n 2d205aec-c7b5-4881-b4b1-000000000200 \n 7ec89f22-8b4c-44ad-80a5-088c87bd61df");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
        doReturn(pds).when(midsReference).getPDS(true, Arrays.asList("attributePDS","facePDS"));
        doNothing().when(midsReference).enrollmentAndReAuthRPClaimsSharing();

        midsReferenceApplication.performRPClaimsSharingEnrollment(true);
        verify(midsReference,times(1)).enrollmentAndReAuthRPClaimsSharing();
    }

    @Test
    void performMultiDocEnrollment(){
       // doReturn(pds).when(midsReference).getPDS(false, Arrays.asList("facePDS", "attributePDS", "evidencePDS"));
        Cache.setPdsMultiDocument(null);
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65  \n 2d205aec-c7b5-4881-b4b1-000000000200 \n  e1b33934-6ff3-45b8-bcbf-c3a2f31c80c8");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        doNothing().when(midsReference).addMultiDoc();

        midsReferenceApplication.performMultiDocEnrollment();
        verify(midsReference,times(1)).addMultiDoc();
    }

    @Test
    void  deleteIdentityAttribute_test(){

        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        doNothing().when(midsReference).deleteIdentityAttribute();
        midsReferenceApplication.deleteIdentityAttribute();
        verify(midsReference,times(1)).deleteIdentityAttribute();
    }

    @Test
    void performTpDataShares_test(){
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        doNothing().when(midsReference).updatePdsData();
        midsReferenceApplication.performTpDataShares();
        verify(midsReference,times(1)).updatePdsData();
    }

    @Test
    void retrieveUserActivities_test(){
        doNothing().when(midsReference).retrieveUserActivities();
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.retrieveUserActivities();
        verify(midsReference,times(1)).retrieveUserActivities();
    }

    @Test
    void performDeletion_test(){
        doNothing().when(midsReference).performDeletion();
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.performDeletion();
        verify(midsReference,times(1)).performDeletion();
    }

    @Test
    void performAuditEvents_test(){
        doNothing().when(midsReference).performAuditEvents();
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.performAuditEvents();
        verify(midsReference,times(1)).performAuditEvents();
    }

    @Test
    void  performActivitySearch_test(){
        doNothing().when(midsReference).performRPActivitySearch();
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.performActivitySearch();
        verify(midsReference,times(1)).performRPActivitySearch();
    }

    @Test
    void performTpRpClaims_test(){
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
         midsReferenceApplication.performTpRpClaims();
        verify(midsReference,times(1)).performTpRpClaimSharing();
    }

    @Test
    void performAuthenticationDecisions_test(){
        doNothing().when(midsReference).performAuthenticationDecisions();
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.performAuthenticationDecisions();
        verify(midsReference,times(1)).performAuthenticationDecisions();
    }

    @Test
    void generateMultiSDKToken_test(){
        doNothing().when(midsReference).generateMultiToken();
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.generateMultiSDKToken();
        verify(midsReference,times(1)).generateMultiToken();
    }

    @Test
    void generateSDKToken_test(){
        doNothing().when(midsReference).generateToken();
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.generateSDKToken();
        verify(midsReference,times(1)).generateToken();
    }

    @Test
   void retrieveIdentities_test(){
        doNothing().when(midsReference).callRetrieveIdentities();
        Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65 \n 2d205aec-c7b5-4881-b4b1-000000000200");
        ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );

        midsReferenceApplication.retrieveIdentities();
        verify(midsReference,times(1)).callRetrieveIdentities();
   }

   @Test
    void userProfileRegistration_test(){
       doNothing().when(midsReference).callUserProfileRegistration();
       Scanner scanner =  new Scanner("a9e45d02-89c7-4f0a-9d3e-5333a0f64a65");
       ReflectionTestUtils.setField(midsReferenceApplication, "scanner",scanner );
       midsReferenceApplication.userProfileRegistration();
       verify(midsReference,times(1)).callUserProfileRegistration();
    }

}


