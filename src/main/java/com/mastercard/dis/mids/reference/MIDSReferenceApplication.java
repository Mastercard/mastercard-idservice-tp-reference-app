/*
 Copyright (c) 2023 Mastercard

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

package com.mastercard.dis.mids.reference;

import com.mastercard.dis.mids.reference.component.MIDSReference;
import com.mastercard.dis.mids.reference.constants.Cache;
import com.mastercard.dis.mids.reference.constants.Menu;
import com.mastercard.dis.mids.reference.constants.TpVariables;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.model.CreatedEmailOtp;
import org.openapitools.client.model.CreatedSMSOtp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

import static com.mastercard.dis.mids.reference.constants.Constants.ARID_SENTENCE;
import static com.mastercard.dis.mids.reference.constants.Constants.ATTRIBUTE_PDS;
import static com.mastercard.dis.mids.reference.constants.Constants.EVIDENCE_PDS;
import static com.mastercard.dis.mids.reference.constants.Constants.FACE_PDS;
import static com.mastercard.dis.mids.reference.constants.Constants.PROFILE_ID_SENTENCE;
import static com.mastercard.dis.mids.reference.constants.Constants.WORKFLOW_ID_MULTI_SENTENCE;
import static com.mastercard.dis.mids.reference.constants.Constants.WORKFLOW_ID_REAUTH_SENTENCE;
import static com.mastercard.dis.mids.reference.constants.Constants.WORKFLOW_ID_SENTENCE;


@Slf4j
@SpringBootApplication
public class MIDSReferenceApplication implements CommandLineRunner {

    private static final String ERROR = "Error : ";
    private final MIDSReference midsReference;
    private boolean exit = false;
    private Scanner scanner = new Scanner(System.in, "UTF-8");

    public MIDSReferenceApplication(MIDSReference midsReference) {
        this.midsReference = midsReference;
    }

    public static void main(String[] args) {
        SpringApplication.run(MIDSReferenceApplication.class);
        System.exit(0);
    }

    @Override
    public void run(String... args) {

        while (!exit) {
            showMenu();
            handleOption(scanner.nextLine());
            pressAnyKey();
        }
        System.exit(0);
    }

    void showMenu() {
        log.info(" <--- Welcome to ID Reference APP --->");
        for (Map.Entry<String, String> entry : new Menu().get().entrySet()) {
            log.info(entry.getValue());
        }
        log.info(" ---> Type your option and press ENTER: ");
    }

    void handleOption(String option) {
        log.info("Your option : " + option);
        switch (option) {
            case "1":
                userProfileRegistration();
                break;
            case "2":
                generateSDKToken();
                break;
            case "3":
                performEnrollment();
                break;
            case "4":
                generateMultiSDKToken();
                break;
            case "5":
                performReAuthentication();
                break;
            case "6":
                performEnrollmentWithUpdateIdConfirmations();
                break;
            case "7":
                performReAuthenticationWithUpdateIdConfirmations();
                break;
            case "8":
                retrieveUserActivities();
                break;
            case "9":
                performEmailOptions(false);
                break;
            case "10":
                performSMSOptions(false);
                break;
            case "11":
                retrieveIdentities();
                break;
            case "12":
                performTpRpClaims();
                break;
            case "13":
                performActivitySearch();
                break;
            case "14":
                performAuditEvents();
                break;
            case "15":
                performDeletion();
                break;
            case "16":
                performRPClaimsSharingEnrollment(true);
                break;
            case "17":
                performMultiDocEnrollment();
                break;
            case "18":
                performRPClaimsSharingReAuthentication();
                break;
            case "19":
                getRpScopes();
                break;
            case "20":
                performTpDataShares();
                break;
            case "21":
                updateIdentityAttributes();
                break;
            case "22":
                deleteIdentityAttribute();
                break;
            case "23":
                performAuthenticationDecisions();
                break;
            case "24":
                this.exit = true;
                break;
            default:
                log.info("Invalid option!");
        }
    }



    synchronized void performMultiDocEnrollment() {
        try {
            log.info("<<--- MultiDocEnrollment Started --->>");

            readUserProfileAndWorkflowID();

            if(Cache.isPdsMultiDocumentNull()){
                Cache.setPdsMultiDocument(midsReference.getPDS(false, Arrays.asList(FACE_PDS, ATTRIBUTE_PDS, EVIDENCE_PDS)));
            }
            log.info(WORKFLOW_ID_MULTI_SENTENCE);
            TpVariables.setMultiDocumentWorkflowId(scanner.nextLine());

            midsReference.addMultiDoc();
            log.info("<<--- MultiDocEnrollment Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- MultiDocEnrollment Failed Ended --->>");
        }
    }

    void userProfileRegistration() {
        try {
            log.info("<<--- UserProfileRegistration Started --->>");

            log.info(PROFILE_ID_SENTENCE);
            TpVariables.setUserProfileId(scanner.nextLine());

            midsReference.callUserProfileRegistration();
            log.info("<<--- UserProfileRegistration Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- UserProfileRegistration Failed Ended --->>");
        }
    }

    void retrieveIdentities() {
        try {
            log.info("<<--- RetrieveIdentities Started --->>");

            readUserProfileAndWorkflowID();

            midsReference.callRetrieveIdentities();
            log.info("<<--- RetrieveIdentities Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- RetrieveIdentities Failed Ended --->>");
        }
    }

    void generateSDKToken() {
        try {
            log.info("<<--- SDKToken Started --->>");

            log.info(PROFILE_ID_SENTENCE);
            TpVariables.setUserProfileId(scanner.nextLine());

            midsReference.generateToken();
            log.info("<<--- SDKToken Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- SDKToken Failed Ended --->>");
        }
    }

    void generateMultiSDKToken() {
        try {
            log.info("<<--- MultiSDKToken Started --->>");

            readUserProfileAndWorkflowID();

            midsReference.generateMultiToken();
            log.info("<<--- MultiSDKToken Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- MultiSDKToken Failed Ended --->>");
        }
    }

    synchronized void performEnrollment( ) {
        try {
            log.info( "<<--- Enrollment Started --->>");

            readUserProfileAndWorkflowID();

            if(Cache.isPdsEnrollmentNull()){
                Cache.setPdsEnrollment(midsReference.getPDS(false, Collections.singletonList(ATTRIBUTE_PDS)));
            }
            enrollmentVerification();

            log.info("<<--- Enrollment Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info( "<<--- Enrollment Failed Ended --->>");
        }
    }

    synchronized  void performReAuthentication() {
        try {
            log.info("<<--- ReAuthentication Started --->>");

            readUserProfileAndWorkflowID();

            if(Cache.isFacePdsNull()){
                Cache.setFacePds(midsReference.getPDS(false, Collections.singletonList(FACE_PDS)));
            }

            log.info(WORKFLOW_ID_REAUTH_SENTENCE);
            TpVariables.setWorkflowIdReAuth(scanner.nextLine());

            midsReference.performReAuthentication();
            log.info("<<--- ReAuthentication Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- ReAuthentication Failed Ended --->>");
        }
    }

    void performAuthenticationDecisions() {
        try {
            log.info("<<--- Authentication Decissions Started --->>");

            readUserProfileAndWorkflowID();

            midsReference.performAuthenticationDecisions();
            log.info("<<--- Authentication Decissions Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- Authentication Decissions Failed Ended --->>");
        }
    }

    synchronized void performEnrollmentWithUpdateIdConfirmations() {
        try {
            log.info("<<--- Enrollment With Update Id Confirmations Started --->>");

            readUserProfileAndWorkflowID();

            if(Cache.isPdsEnrollmentNull()){
                Cache.setPdsEnrollment(midsReference.getPDS(false, Collections.singletonList(ATTRIBUTE_PDS)));
            }
            midsReference.callUpdateIdConfirmationsApi();

            enrollmentVerification();

            log.info("<<--- Enrollment With Update Id Confirmations Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- Enrollment With Update Id Confirmations Failed Ended --->>");
        }
    }

    synchronized void performReAuthenticationWithUpdateIdConfirmations() {
        try {
            log.info("<<--- ReAuthentication With Update Id Confirmations Started --->>");

            readUserProfileAndWorkflowID();

            if(Cache.isFacePdsNull()){
                Cache.setFacePds(midsReference.getPDS(false, Collections.singletonList(FACE_PDS)));
            }

            log.info(WORKFLOW_ID_REAUTH_SENTENCE);
            TpVariables.setWorkflowIdReAuth(scanner.nextLine());

            midsReference.performReAuthenticationWithUpdateIdConfirmations();
            log.info("<<--- ReAuthentication With Update Id Confirmations Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- ReAuthentication With Update Id Confirmations Failed Ended --->>");
        }
    }

    void performTpRpClaims() {
        try {
            log.info("<<--- TP/RP Claims Started --->>");

            readUserProfileAndWorkflowID();

            midsReference.performTpRpClaimSharing();
            log.info("<<--- TP/RP Claims Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- TP/RP Claims Failed Ended --->>");
        }
    }

    void performActivitySearch() {
        try {
            log.info("<<--- Activity Search Started --->>");

            readUserProfileAndWorkflowID();

            midsReference.performRPActivitySearch();
            log.info("<<--- Activity Search Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- Activity Search Failed Ended --->>");
        }
    }

    void performAuditEvents() {
        try {
            log.info("<<--- Audit Events Started --->>");

            log.info(PROFILE_ID_SENTENCE);
            TpVariables.setUserProfileId(scanner.nextLine());

            midsReference.performAuditEvents();
            log.info("<<--- Audit Events Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- Audit Events Failed Ended --->>");
        }
    }

    void performDeletion() {
        try {
            log.info("<<--- Delete User Profile Api Started --->>");

            log.info(PROFILE_ID_SENTENCE);
            TpVariables.setUserProfileId(scanner.nextLine());

            midsReference.performDeletion();
            log.info("<<--- Delete User Profile Api Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- Delete User Profile Api Failed Ended --->>");
        }
    }

    void retrieveUserActivities() {
        try {
            log.info("<<--- Retrieve User Activities  Started --->>");

            readUserProfileAndWorkflowID();

            midsReference.retrieveUserActivities();
            log.info("<<--- Retrieve User Activities Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- Retrieve User Activities Failed Ended --->>");
        }
    }

    void pressAnyKey() {
        if (!exit) {
            log.info("Press ENTER to continue...");
            scanner.nextLine();
        }
    }

    synchronized void performRPClaimsSharingEnrollment(boolean claimSharingFlow) {
        try {
            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Started --->>" : "<<--- Enrollment Started --->>");

            readUserProfileAndWorkflowID();

            log.info(ARID_SENTENCE);
            TpVariables.setAridValue(scanner.nextLine());

            if(Cache.isFaceAndAttributePdsNull()){
             Cache.setFaceAndAttributePds(midsReference.getPDS(true, Arrays.asList(ATTRIBUTE_PDS,FACE_PDS)));
            }

            midsReference.enrollmentAndReAuthRPClaimsSharing();

            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Successfully Ended --->>" : "<<--- Enrollment Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Failed Ended --->>" : "<<--- Enrollment Failed Ended --->>");
        }
    }

    synchronized void performRPClaimsSharingReAuthentication() {
        try {
            log.info("<<--- RPClaimsSharing ReAuthentication Started --->>");

            readUserProfileAndWorkflowID();

            log.info(ARID_SENTENCE);
            TpVariables.setAridValue(scanner.nextLine());

            if(Cache.isFaceAndAttributePdsNull()){
                Cache.setFaceAndAttributePds(midsReference.getPDS(true, Arrays.asList(ATTRIBUTE_PDS,FACE_PDS)));
            }

            midsReference.enrollmentAndReAuthRPClaimsSharing();
            log.info("<<--- ReAuthentication for RPClaimsSharing Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- ReAuthentication for RPClaimsSharing Failed Ended --->>");
        }
    }

    synchronized void getRpScopes() {
        try {
            log.info("<<--- RetrieveRpScopes Started --->>");
            log.info(ARID_SENTENCE);
            TpVariables.setAridValue(scanner.nextLine());

            midsReference.getRpRequestedScopes();
            log.info("<<--- RetrieveRpScopes Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- RetrieveRpScopes Failed Ended --->>");
        }
    }

    void performTpDataShares() {
        try {
            log.info("<<--- TPDataShares Started --->>");

            readUserProfileAndWorkflowID();

            midsReference.updatePdsData();
            log.info("<<--- TPDataShares Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- TPDataShares Failed Ended --->>");
        }
    }

    synchronized void updateIdentityAttributes() {
        try {
            log.info("<<--- updateIdentityAttributes Started --->>");

            readUserProfileAndWorkflowID();

            if(Cache.isPdsMultiDocumentNull()){
                Cache.setPdsMultiDocument(midsReference.getPDS(false, Arrays.asList(FACE_PDS, ATTRIBUTE_PDS, EVIDENCE_PDS)));
            }
            log.info(WORKFLOW_ID_MULTI_SENTENCE);
            TpVariables.setMultiDocumentWorkflowId(scanner.nextLine());
            midsReference.updateIdentiyAttributes();
            log.info("<<--- updateIdentityAttributes Successfully Ended --->>");
        } catch (Exception e) {

            log.info(ERROR + e.getMessage());
            log.info("<<--- updateIdentityAttributes Failed Ended --->>");
        }
    }

    synchronized void performEmailOptions(boolean claimSharingFlow) {
        try {
            log.info("<<--- performEmailOptions Started --->>");

            readUserProfileAndWorkflowID();

            String pds = midsReference.getPDS(claimSharingFlow, Collections.singletonList(ATTRIBUTE_PDS));
            CreatedEmailOtp emailOtp = midsReference.callCreateEmailOtpsApi(pds);

            log.info("Enter the Email Code");
            TpVariables.setEmailCode(scanner.nextLine());

            midsReference.callEmailOtpVerificationsApi(emailOtp.getOtpId(), emailOtp.getPds());
            log.info("<<--- performEmailOptions Started --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- performEmailOptions Failed Ended --->>");
        }
    }

    synchronized  void performSMSOptions(boolean claimSharingFlow) {
        try {
            log.info("<<--- performSMSOptions Started --->>");

            readUserProfileAndWorkflowID();

            String pds = midsReference.getPDS(claimSharingFlow, Collections.singletonList(ATTRIBUTE_PDS));
            CreatedSMSOtp smsOtp = midsReference.callCreateSmsOtpsApi(pds);

            log.info("Enter the SMS Code");
            TpVariables.setOtpCode(scanner.nextLine());

            midsReference.callSmsOtpVerificationsApi(smsOtp.getOtpId(), smsOtp.getPds());
            log.info("<<--- performSMSOptions Started --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- performSMSOptions Failed Ended --->>");
        }
    }

    void deleteIdentityAttribute() {
        try {
            log.info("<<--- deleteIdentityAttributes Started --->>");

            readUserProfileAndWorkflowID();

            midsReference.deleteIdentityAttribute();
            log.info("<<--- deleteIdentityAttribute Successfully Ended --->>");
        } catch (Exception e) {

            log.info(ERROR + e.getMessage());
            log.info("<<--- deleteIdentityAttribute Failed Ended --->>");
        }
    }

    private synchronized void enrollmentVerification(){
        CreatedSMSOtp smsOtp = midsReference.callCreateSmsOtpsApi(Cache.getPdsEnrollment());
        //Adding email address to the original PDS
        CreatedEmailOtp emailOtp = midsReference.callCreateEmailOtpsApi(smsOtp.getPds());

        log.info("Enter the SMS Code");
        TpVariables.setOtpCode( scanner.nextLine());

        log.info("Enter the Email Code");
        TpVariables.setEmailCode(scanner.nextLine());

        //Calling verifications Api for both sms and email, using a fix otp number, expected invalid response.
        midsReference.callSmsOtpVerificationsApi(smsOtp.getOtpId(), smsOtp.getPds());
        midsReference.callEmailOtpVerificationsApi(emailOtp.getOtpId(), emailOtp.getPds());
    }

    private synchronized void readUserProfileAndWorkflowID(){
        log.info(PROFILE_ID_SENTENCE);
        TpVariables.setUserProfileId(scanner.nextLine());

        log.info(WORKFLOW_ID_SENTENCE);
        TpVariables.setWorkflowId(scanner.nextLine());
    }

}