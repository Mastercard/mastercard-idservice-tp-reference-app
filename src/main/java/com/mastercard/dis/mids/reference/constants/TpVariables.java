package com.mastercard.dis.mids.reference.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class TpVariables {

    private static  String otpCode;
    private static  String emailCode;
    private static  String multiDocumentWorkflowId;
    private static  String workflowIdReAuth;
    private static  String aridValue;

    public static String getOtpCode() {
        return otpCode;
    }

    public static void setOtpCode(String otpCode) {
        TpVariables.otpCode = otpCode;
    }

    public static String getEmailCode() {
        return emailCode;
    }

    public static void setEmailCode(String emailCode) {
        TpVariables.emailCode = emailCode;
    }

    public static String getMultiDocumentWorkflowId() {
        return multiDocumentWorkflowId;
    }

    public static void setMultiDocumentWorkflowId(String multiDocumentWorkflowId) {
        TpVariables.multiDocumentWorkflowId = multiDocumentWorkflowId;
    }

    public static String getWorkflowIdReAuth() {
        return workflowIdReAuth;
    }

    public static void setWorkflowIdReAuth(String workflowIdReAuth) {
        TpVariables.workflowIdReAuth = workflowIdReAuth;
    }

    public static String getAridValue() {
        return aridValue;
    }

    public static void setAridValue(String aridValue) {
        TpVariables.aridValue = aridValue;
    }

}
