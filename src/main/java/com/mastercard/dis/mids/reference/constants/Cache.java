package com.mastercard.dis.mids.reference.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Cache {
    private static String facePds;

    private static String faceAndAttributePds;

    private static String pdsEnrollment;

    private static String pdsMultiDocument;

    public static String getFacePds() {
        return facePds;
    }

    public static void setFacePds(String facePds) {
        Cache.facePds = facePds;
    }

    public static String getFaceAndAttributePds() {
        return faceAndAttributePds;
    }

    public static void setFaceAndAttributePds(String faceAndAttributePds) {
        Cache.faceAndAttributePds = faceAndAttributePds;
    }

    public static String getPdsEnrollment() {
        return pdsEnrollment;
    }

    public static void setPdsEnrollment(String pdsEnrollment) {
        Cache.pdsEnrollment = pdsEnrollment;
    }

    public static String getPdsMultiDocument() {
        return pdsMultiDocument;
    }

    public static void setPdsMultiDocument(String pdsMultiDocument) {
        Cache.pdsMultiDocument = pdsMultiDocument;
    }
}
