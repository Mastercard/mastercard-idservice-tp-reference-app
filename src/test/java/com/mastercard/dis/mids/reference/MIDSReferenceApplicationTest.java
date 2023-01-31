package com.mastercard.dis.mids.reference;

import com.mastercard.dis.mids.reference.constants.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MIDSReferenceApplicationTest {

    private static final Map<String, String> menuMapTest = new HashMap<String, String>() {{
        put("1", "1)   Registering a User Profile");
        put("2", "2)   Access Token API (SDKToken)");
        put("3", "3)   Create User Identity");
        put("4", "4)   Multi-Access Token API (MultiSDKToken)");
        put("5", "5)   Access User Identity");
        put("6", "6)   Update ID Confirmations (Enrollment)");
        put("7", "7)   Update ID Confirmations (Re-authentication)");
        put("8", "8)   User Account Activity Searches");
        put("9", "9)   Email OTP");
        put("10", "10)   SMS OTP");
        put("11", "11)   User Profiles Identity Searches");
        put("12", "12)   Share User Identity (TP-TP)");
        put("13", "13)   RP Activity Searches");
        put("14", "14)   Audit Events");
        put("15", "15)   Delete ID");
        put("16", "16)   Share User Identity (TP-RP) (Enrollment)");
        put("17", "17)   Additional Document Support");
        put("18", "18)   Share User Identity (TP-RP) (Re-authentication)");
        put("19", "19)   TP Scopes");
        put("20", "20)   TP Data Shares");
        put("21", "21)   Update Identity");
        put("22", "22)   Delete Identity Attribute");
        put("23", "23)   Authentication Decisions");
        put("24", "24)   Exit");
    }};

    @Test
    void consoleMenu_runAndcheckingValues_works() {
        Map<String, String> menu = new Menu().get();
        for (Map.Entry<String, String> entry : menuMapTest.entrySet()) {
            String valueMenu = menu.get(entry.getKey());
            Assertions.assertEquals(valueMenu, entry.getValue());
        }
    }

    @Test
    void console_showMenu_works() {
        MIDSReferenceApplication spyMIDSReferenceApplication = spy(new MIDSReferenceApplication(null));
        spyMIDSReferenceApplication.showMenu();
        verify(spyMIDSReferenceApplication, times(1)).showMenu();
    }

    @Test
    void console_handleOption_works() {
        MIDSReferenceApplication spyMIDSReferenceApplication = spy(new MIDSReferenceApplication(null));
        menuMapTest.put("99","Invalid option!");
        for (Map.Entry<String, String> entry : menuMapTest.entrySet()) {
            spyMIDSReferenceApplication.handleOption(entry.getKey());
        }
        verify(spyMIDSReferenceApplication, times(menuMapTest.size())).handleOption(anyString());
    }
}


