package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.RPActivitySearch;

import static org.openapitools.client.model.UserConsent.ACCEPT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RPActivitySearchTokenExample {

    public static RPActivitySearch getRPActivitySearchToken() {
        RPActivitySearch rPActivitySearch = new RPActivitySearch();
        rPActivitySearch.setStartIndex(1);
        rPActivitySearch.setEndIndex(2);
        rPActivitySearch.setUserConsent(ACCEPT);
        return rPActivitySearch;
    }
}
