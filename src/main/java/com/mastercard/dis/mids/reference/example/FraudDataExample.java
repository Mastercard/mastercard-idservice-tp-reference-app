package com.mastercard.dis.mids.reference.example;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.FraudData;
import org.openapitools.client.model.FraudIdentifierData;
import org.openapitools.client.model.FraudSearchData;
import org.openapitools.client.model.FraudSignals;

import java.util.Arrays;

import static com.mastercard.dis.mids.reference.util.Constants.ADDRESS;
import static com.mastercard.dis.mids.reference.util.Constants.DOB;
import static com.mastercard.dis.mids.reference.util.Constants.DOCUMENT_ID;
import static com.mastercard.dis.mids.reference.util.Constants.IP_ADDRESS;
import static com.mastercard.dis.mids.reference.util.Constants.NAME;
import static com.mastercard.dis.mids.reference.util.Constants.NATIONAL_ID;
import static com.mastercard.dis.mids.reference.util.Constants.N_DEVICE_ID;
import static com.mastercard.dis.mids.reference.util.Constants.ORIGINATED_SOURCE;
import static com.mastercard.dis.mids.reference.util.Constants.ORIGINATED_SYSTEM;
import static com.mastercard.dis.mids.reference.util.Constants.ORIGINATED_SYSTEM_TRANSACTION_ID;
import static com.mastercard.dis.mids.reference.util.Constants.PHONE_NUMBER;
import static com.mastercard.dis.mids.reference.util.Constants.SCORE_BAND;
import static com.mastercard.dis.mids.reference.util.Constants.SCORE_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FraudDataExample {

    public static FraudData getFraudDataExample() {

        FraudData fraudData = new FraudData();
        fraudData.setOriginatedSystem(ORIGINATED_SYSTEM);
        fraudData.setOriginatedSource(ORIGINATED_SOURCE);
        fraudData.setOriginatedSystemTransactionId(ORIGINATED_SYSTEM_TRANSACTION_ID);

        FraudIdentifierData fraudIdentifierData = new FraudIdentifierData();
        fraudIdentifierData.setScoreBand(SCORE_BAND);
        fraudIdentifierData.setScoreValue(SCORE_VALUE);
        fraudIdentifierData.setIpAddress(IP_ADDRESS);
        fraudIdentifierData.setDocumentId(DOCUMENT_ID);
        fraudIdentifierData.setName(NAME);
        fraudIdentifierData.setDob(DOB);
        fraudIdentifierData.setNationalId(NATIONAL_ID);
        fraudIdentifierData.setPhoneNumber(PHONE_NUMBER);
        fraudIdentifierData.setNdDeviceId(N_DEVICE_ID);

        FraudSignals fraudSignals = new FraudSignals();
        fraudSignals.setFraudSignalIdentifierData(fraudIdentifierData);

        fraudData.setFraudSignals(Arrays.asList(fraudSignals));

        return fraudData;
    }

    public static FraudSearchData getFraudSearchExample() {

        FraudSearchData fraudSearchData = new FraudSearchData();
        fraudSearchData.setIpAddress(IP_ADDRESS);
        fraudSearchData.setAddress(ADDRESS);
        fraudSearchData.setDocumentId(DOCUMENT_ID);
        fraudSearchData.setName(NAME);
        fraudSearchData.setDob(DOB);
        fraudSearchData.setNationalId(NATIONAL_ID);
        fraudSearchData.setPhoneNumber(PHONE_NUMBER);
        fraudSearchData.setNdDeviceId(N_DEVICE_ID);

        return fraudSearchData;


    }

}
