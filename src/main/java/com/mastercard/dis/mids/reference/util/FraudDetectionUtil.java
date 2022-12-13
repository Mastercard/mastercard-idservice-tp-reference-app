package com.mastercard.dis.mids.reference.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.FraudDetection;
import org.openapitools.client.model.FraudDetectionNuDetectMeta;

import static com.mastercard.dis.mids.reference.util.Constants.BEHAVIOUR_DATA;
import static com.mastercard.dis.mids.reference.util.Constants.REMOTE_IP;
import static com.mastercard.dis.mids.reference.util.Constants.REQUEST_URL;
import static com.mastercard.dis.mids.reference.util.Constants.SESSION_ID;
import static com.mastercard.dis.mids.reference.util.Constants.USER_AGENT;
import static com.mastercard.dis.mids.reference.util.Constants.X_FORWARDED_FOR;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FraudDetectionUtil {

    public static FraudDetection getFraudDetection() {
        FraudDetection fraudDetection = new FraudDetection();
        FraudDetectionNuDetectMeta fraudDetectionNuDetectMeta = new FraudDetectionNuDetectMeta();
        fraudDetectionNuDetectMeta.setBehaviourData(BEHAVIOUR_DATA);
        fraudDetectionNuDetectMeta.sessionId(SESSION_ID);
        fraudDetectionNuDetectMeta.setRemoteIp(REMOTE_IP);
        fraudDetectionNuDetectMeta.setUserAgent(USER_AGENT);
        fraudDetectionNuDetectMeta.setxForwardedFor(X_FORWARDED_FOR);
        fraudDetectionNuDetectMeta.setRequestUrl(REQUEST_URL);
        fraudDetection.setNuDetectMeta(fraudDetectionNuDetectMeta);
        return fraudDetection;
    }
}
