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

package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.AuditEventsItem;
import org.openapitools.client.model.AuditEventsItemAudit;
import org.openapitools.client.model.InitializeAuthentications;
import org.openapitools.client.model.VerifyAuthentication;
import org.openapitools.client.model.VerifyAuthenticationDecisions;

import java.util.ArrayList;
import java.util.List;

import static com.mastercard.dis.mids.reference.constants.Constants.AUDIT;
import static com.mastercard.dis.mids.reference.constants.Constants.AUDIT_EVENT;
import static com.mastercard.dis.mids.reference.constants.Constants.AUDIT_EVENT_GENERATED_SOURCE;
import static com.mastercard.dis.mids.reference.constants.Constants.AUDIT_EVENT_TYPE;
import static com.mastercard.dis.mids.reference.constants.Constants.AUDIT_USER_PROFILE_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.DATE_TIME;
import static com.mastercard.dis.mids.reference.constants.Constants.DEVICE_MAKE;
import static com.mastercard.dis.mids.reference.constants.Constants.LOCALE;
import static com.mastercard.dis.mids.reference.constants.Constants.LOG_CORE_SDK_TP;
import static com.mastercard.dis.mids.reference.constants.Constants.LOG_EVENT;
import static com.mastercard.dis.mids.reference.constants.Constants.LOG_EVENT_TYPE;
import static com.mastercard.dis.mids.reference.constants.Constants.OS_VERSION;
import static com.mastercard.dis.mids.reference.constants.Constants.PRIVACY_POLICY_VERSION;
import static com.mastercard.dis.mids.reference.constants.Constants.SDK_VERSION;
import static com.mastercard.dis.mids.reference.constants.Constants.SESSION_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.SOFTWARE_VERSION;
import static com.mastercard.dis.mids.reference.constants.Constants.TP;
import static com.mastercard.dis.mids.reference.constants.Constants.TRANSACTION_GROUP_ID;
import static org.openapitools.client.model.UserConsent.ACCEPT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InitializeReAuthenticationExample {

    public static InitializeAuthentications getInitializeAuthenticationsObject() {
        InitializeAuthentications initializeAuthentications = new InitializeAuthentications();
        initializeAuthentications.setCountryCode(COUNTRY_CODE);
        initializeAuthentications.setSdkVersion(SDK_VERSION);
        initializeAuthentications.setChannelType(InitializeAuthentications.ChannelTypeEnum.SDK);
        initializeAuthentications.setLocale(LOCALE);
        initializeAuthentications.setUserConsent(ACCEPT);
        initializeAuthentications.sdkAuditEvents(getSdkAuditEventsObject());
        return initializeAuthentications;
    }

    public static VerifyAuthenticationDecisions initializeAuthenticationDecisionssObject() {
        VerifyAuthenticationDecisions verifyAuthenticationDecisions = new VerifyAuthenticationDecisions();
        verifyAuthenticationDecisions.setCountryCode(COUNTRY_CODE);
        verifyAuthenticationDecisions.setSdkVersion(SDK_VERSION);
        verifyAuthenticationDecisions.setPrivacyPolicyVersion(PRIVACY_POLICY_VERSION);
        verifyAuthenticationDecisions.setUserConsent(ACCEPT);
        return verifyAuthenticationDecisions;
    }

    public static VerifyAuthentication getVerifyAuthenticationsObject() {
        VerifyAuthentication verifyAuthentication = new VerifyAuthentication();
        verifyAuthentication.setCountryCode(COUNTRY_CODE);
        verifyAuthentication.setPrivacyPolicyVersion(PRIVACY_POLICY_VERSION);
        verifyAuthentication.setSdkAuditEvents(getSdkAuditEventsObject());
        return verifyAuthentication;
    }

    public static List<AuditEventsItem> getSdkAuditEventsObject() {
        List<AuditEventsItem> auditEventsItemList = new ArrayList<>();
        AuditEventsItem auditEventsItem = new AuditEventsItem();
        auditEventsItem.setAudit(getAuditEventsItemAuditObject());
        auditEventsItem.setSessionId(SESSION_ID);
        auditEventsItem.setDateTime(DATE_TIME);
        auditEventsItem.setDeviceMake(DEVICE_MAKE);
        auditEventsItem.setLogEvent(LOG_EVENT);
        auditEventsItem.setLogEventType(LOG_EVENT_TYPE);
        auditEventsItem.setLogRequestFlow(LOG_CORE_SDK_TP);
        auditEventsItem.setOsVersion(OS_VERSION);
        auditEventsItem.setSoftwareVersion(SOFTWARE_VERSION);
        auditEventsItem.setTransactionGroupId(TRANSACTION_GROUP_ID);
        auditEventsItem.setType(AUDIT);
        auditEventsItem.setUserProfileId(AUDIT_USER_PROFILE_ID);
        auditEventsItemList.add(auditEventsItem);
        return auditEventsItemList;
    }

    private static AuditEventsItemAudit getAuditEventsItemAuditObject() {
        AuditEventsItemAudit auditEventsItemAudit = new AuditEventsItemAudit();
        auditEventsItemAudit.setEvent(AUDIT_EVENT);
        auditEventsItemAudit.setEventType(AUDIT_EVENT_TYPE);
        auditEventsItemAudit.setEventGeneratedSource(AUDIT_EVENT_GENERATED_SOURCE);
        auditEventsItemAudit.setOwner(TP);
        auditEventsItemAudit.setPrivacyPolicy(PRIVACY_POLICY_VERSION);
        auditEventsItemAudit.setResult(AuditEventsItemAudit.ResultEnum.TRUE);
        return auditEventsItemAudit;
    }
}