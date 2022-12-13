/*
 Copyright (c) 2021 Mastercard

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

package com.mastercard.dis.mids.reference.constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class Menu {

    private final LinkedHashMap<String, String> menuMap;

    public Menu() {
        menuMap = new LinkedHashMap<>();
        menuMap.put("1", "1)   Registering a User Profile");
        menuMap.put("2", "2)   Access Token API (SDKToken)");
        menuMap.put("3", "3)   Create User Identity");
        menuMap.put("4", "4)   Multi-Access Token API (MultiSDKToken)");
        menuMap.put("5", "5)   Access User Identity");
        menuMap.put("6", "6)   Update ID Confirmations (Enrollment)");
        menuMap.put("7", "7)   Update ID Confirmations (Re-authentication)");
        menuMap.put("8", "8)   User Account Activity Searches");
        menuMap.put("9", "9)   Email OTP");
        menuMap.put("10", "10)   SMS OTP");
        menuMap.put("11", "11)   User Profiles Identity Searches");
        menuMap.put("12", "12)   Share User Identity (TP-TP)");
        menuMap.put("13", "13)   RP Activity Searches");
        menuMap.put("14", "14)   Audit Events");
        menuMap.put("15", "15)   Delete ID");
        menuMap.put("16", "16)   Share User Identity (TP-RP) (Enrollment)");
        menuMap.put("17", "17)   Additional Document Support");
        menuMap.put("18", "18)   Share User Identity (TP-RP) (Re-authentication)");
        menuMap.put("19", "19)   TP Scopes");
        menuMap.put("20", "20)   TP Data Shares");
        menuMap.put("21", "21)   Update Identity");
        menuMap.put("22", "22)   Create Watchlist Fraud Signal");
        menuMap.put("23", "23)   Search for a Watchlist Fraud Signal");
        menuMap.put("24", "24)   Delete Identity Attribute");
        menuMap.put("25", "25)   Authentication Decisions");
        menuMap.put("26", "26)   Exit");
    }

    public Map<String, String> get() {
        return menuMap;
    }
}
