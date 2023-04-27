package com.mastercard.dis.mids.reference.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class TpVariables {

    private static  String OTP_CODE ;
    private static  String EMAIL_CODE ;
    private static  String MULTI_DOCUMENT_WORKFLOW_ID;
    private static  String WORKFLOW_ID_RE_AUTH;
    private static  String ARID_VALUE ;

    public static String getOtpCode() {
        return OTP_CODE;
    }

    public static void setOtpCode(String otpCode) {
        OTP_CODE = otpCode;
    }

    public static String getEmailCode() {
        return EMAIL_CODE;
    }

    public static void setEmailCode(String emailCode) {
        EMAIL_CODE = emailCode;
    }

    public static String getMultiDocumentWorkflowId() {
        return MULTI_DOCUMENT_WORKFLOW_ID;
    }

    public static void setMultiDocumentWorkflowId(String multiDocumentWorkflowId) {
        MULTI_DOCUMENT_WORKFLOW_ID = multiDocumentWorkflowId;
    }

    public static String getWorkflowIdReAuth() {
        return WORKFLOW_ID_RE_AUTH;
    }

    public static void setWorkflowIdReAuth(String workflowIdReAuth) {
        WORKFLOW_ID_RE_AUTH = workflowIdReAuth;
    }

    public static String getAridValue() {
        return ARID_VALUE;
    }

    public static void setAridValue(String aridValue) {
        ARID_VALUE = aridValue;
    }

}
