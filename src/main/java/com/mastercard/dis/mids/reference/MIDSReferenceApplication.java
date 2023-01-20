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
import com.mastercard.dis.mids.reference.component.MIDSWatchlist;
import com.mastercard.dis.mids.reference.constants.Menu;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.model.CreatedEmailOtp;
import org.openapitools.client.model.CreatedSMSOtp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class MIDSReferenceApplication implements CommandLineRunner {

    private static final String ERROR = "Error : ";
    private final MIDSReference midsReference;
    private final MIDSWatchlist midsWatchlist;
    private boolean exit = false;
    private Scanner scanner;

    public MIDSReferenceApplication(MIDSReference midsReference, MIDSWatchlist midsWatchlist) {
        this.midsReference = midsReference;
        this.midsWatchlist = midsWatchlist;
    }

    public static void main(String[] args) {
        SpringApplication.run(MIDSReferenceApplication.class);
        System.exit(0);
    }

    @Override
    public void run(String... args) {
        scanner = new Scanner(System.in, "UTF-8");

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
                performEnrollment(false);
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
                performFraudDataCreation();
                break;
            case "23":
                performFraudDataSearch();
                break;
            case "24":
                deleteIdentityAttribute();
                break;
            case "25":
                performAuthenticationDecisions();
                break;
            case "26":
                this.exit = true;
                break;
            default:
                log.info("Invalid option!");
        }
    }



    private void performMultiDocEnrollment() {
        try {
            log.info("<<--- MultiDocEnrollment Started --->>");
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
            midsReference.generateMultiToken();
            log.info("<<--- MultiSDKToken Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- MultiSDKToken Failed Ended --->>");
        }
    }

    void performEnrollment(boolean claimSharingFlow) {
        try {
            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Started --->>" : "<<--- Enrollment Started --->>");
            midsReference.performEnrollment(claimSharingFlow);
            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Successfully Ended --->>" : "<<--- Enrollment Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Failed Ended --->>" : "<<--- Enrollment Failed Ended --->>");
        }
    }

    void performReAuthentication() {
        try {
            log.info("<<--- ReAuthentication Started --->>");
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
            midsReference.performAuthenticationDecisions();
            log.info("<<--- Authentication Decissions Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- Authentication Decissions Failed Ended --->>");
        }
    }

    void performEnrollmentWithUpdateIdConfirmations() {
        try {
            log.info("<<--- Enrollment With Update Id Confirmations Started --->>");
            midsReference.performEnrollmentWithUpdateIdConfirmations();
            log.info("<<--- Enrollment With Update Id Confirmations Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- Enrollment With Update Id Confirmations Failed Ended --->>");
        }
    }

    void performReAuthenticationWithUpdateIdConfirmations() {
        try {
            log.info("<<--- ReAuthentication With Update Id Confirmations Started --->>");
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

    void performRPClaimsSharingEnrollment(boolean claimSharingFlow) {
        try {
            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Started --->>" : "<<--- Enrollment Started --->>");
            midsReference.enrollmentAndReAuthRPClaimsSharing();
            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Successfully Ended --->>" : "<<--- Enrollment Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info(claimSharingFlow ? "<<--- Claim Sharing Enrollment Failed Ended --->>" : "<<--- Enrollment Failed Ended --->>");
        }
    }

    void performRPClaimsSharingReAuthentication() {
        try {
            log.info("<<--- RPClaimsSharing ReAuthentication Started --->>");
            midsReference.enrollmentAndReAuthRPClaimsSharing();
            log.info("<<--- ReAuthentication for RPClaimsSharing Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- ReAuthentication for RPClaimsSharing Failed Ended --->>");
        }
    }

    void getRpScopes() {
        try {
            log.info("<<--- RetrieveRpScopes Started --->>");
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
            midsReference.updatePdsData();
            log.info("<<--- TPDataShares Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- TPDataShares Failed Ended --->>");
        }
    }

    void updateIdentityAttributes() {
        try {
            log.info("<<--- updateIdentityAttributes Started --->>");
            midsReference.updateIdentiyAttributes();
            log.info("<<--- updateIdentityAttributes Successfully Ended --->>");
        } catch (Exception e) {

            log.info(ERROR + e.getMessage());
            log.info("<<--- updateIdentityAttributes Failed Ended --->>");
        }
    }

    void performEmailOptions(boolean claimSharingFlow) {
        try {
            log.info("<<--- performEmailOptions Started --->>");
            String pds = midsReference.getPDS(claimSharingFlow, Collections.singletonList("attributePDS"));
            CreatedEmailOtp emailOtp = midsReference.callCreateEmailOtpsApi(pds);
            midsReference.callEmailOtpVerificationsApi(emailOtp.getOtpId(), emailOtp.getPds());
            log.info("<<--- performEmailOptions Started --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- performEmailOptions Failed Ended --->>");
        }
    }

    void performSMSOptions(boolean claimSharingFlow) {
        try {
            log.info("<<--- performSMSOptions Started --->>");
            String pds = midsReference.getPDS(claimSharingFlow, Collections.singletonList("attributePDS"));
            CreatedSMSOtp smsOtp = midsReference.callCreateSmsOtpsApi(pds);
            midsReference.callSmsOtpVerificationsApi(smsOtp.getOtpId(), smsOtp.getPds());
            log.info("<<--- performSMSOptions Started --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- performSMSOptions Failed Ended --->>");
        }
    }

    void performFraudDataCreation() {
        try {
            log.info("<<--- performFraudDataCreation Started --->>");
            midsWatchlist.createPerformFraudDataCreation();
            log.info("<<--- performFraudDataCreation Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- performFraudDataCreation Failed Ended --->>");
        }
    }

    void performFraudDataSearch() {
        try {
            log.info("<<--- performFraudDataSearch Started --->>");
            midsWatchlist.performFraudDataSearchSignals();
            log.info("<<--- performFraudDataSearch Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- performFraudDataSearch Failed Ended --->>");
        }
    }

    void deleteIdentityAttribute() {
        try {
            log.info("<<--- deleteIdentityAttributes Started --->>");
            midsReference.deleteIdentityAttribute();
            log.info("<<--- deleteIdentityAttribute Successfully Ended --->>");
        } catch (Exception e) {

            log.info(ERROR + e.getMessage());
            log.info("<<--- deleteIdentityAttribute Failed Ended --->>");
        }
    }

}