package com.mastercard.dis.mids.reference.constants;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class TpVariables {

    @Setter
    @Getter
    private static  String otpCode;
    @Setter
    @Getter
    private static  String emailCode;
    @Setter
    @Getter
    private static  String multiDocumentWorkflowId;
    @Setter
    @Getter
    private static  String workflowIdReAuth;
    @Setter
    @Getter
    private static  String aridValue;
    @Setter
    @Getter
    private static  String userProfileId;
    @Setter
    @Getter
    private static  String workflowId;
}
