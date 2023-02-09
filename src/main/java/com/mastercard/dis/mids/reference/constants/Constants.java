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

package com.mastercard.dis.mids.reference.constants;

public class Constants {

    public static final String X_MIDS_USERAUTH_SESSIONID = "x-mids-userauth-sessionid";
    public static final String X_USER_IDENTITY = "x-user-identity";
    public static final String ACCEPT = "Accept";
    //attributes
    //Input Constants
    public static final String USER_PROFILE_ID = "user profile id";
    public static final String USER_PROFILE_ID_VALUE = "17005442-51cd-4e46-ab74-7a3a25c503ec";
    public static final String AUDIT_USER_PROFILE_ID = "17005442-51cd-4e46-ab74-7a3a25c503ec";
    public static final String COUNTRY_CODE = "US";
    public static final String ENROLLMENT_ORIGIN_VALUE = "RETAIL";
    public static final String SDK_VERSION = "1.0.0";
    public static final String PDS = "pds";
    public static final String LOCALE = "en-US";
    public static final String PRIVACY_POLICY_VERSION = "1.0.0";
    public static final String OTP_CODE = "123456";
    public static final String DATE_TIME = "2020-01-28T13:16:01.714-05:00";
    public static final String DEVICE_MAKE = "Samsung S10";
    public static final String LOG_EVENT = "ID Enrollment";
    public static final String LOG_EVENT_TYPE = "User Profile Creation";
    public static final String LOG_CORE_SDK_TP = "CoreSDK-TP";
    public static final String OS_VERSION = "Android 5.0";
    public static final String SOFTWARE_VERSION = "1.0.0";
    public static final String TRANSACTION_GROUP_ID = "28eae1aa-6744-433e-879d-7da48d63e89a";
    public static final String TRANSACTION_GROUP_ID_META_DATA = "be3ad617-04ad-43e1-a438-79425b6511b6";
    public static final String AUDIT = "audit";
    public static final String AUDIT_EVENT = "Document Scan";
    public static final String AUDIT_EVENT_TYPE = "Enrollment";
    public static final String AUDIT_EVENT_GENERATED_SOURCE = "CoreSDK";
    public static final String TP = "TP";
    public static final String TP_RP_SCOPES_FIRST_NAME = "passport.firstName";
    public static final String TP_RP_SCOPES_DATE_OF_BIRTH = "passport.dateOfBirth";
    public static final String REDIRECT_URL = "http://redirecturl.com";
    public static final String ARID_VALUE = "7ec89f22-8b4c-44ad-80a5-088c87bd61df";
    public static final String RELYING_PARTY_NAME = "RELYING_PARTY";
    //fraudDetection
    public static final String BEHAVIOUR_DATA = "ewogICJuZHMtcG1kIiA6ICJ7XCJmdnFcIjpcIjBONjROOVAzLVBPM1ItNDcyNi04MDBPLTNQMzM2Mzg2Tk5RT1wiLFwianZxdHJnUW5nblwiOntcInp2cXNpXCI6XCJRUTYzOTc5Ny1OUFAyLTQ0OVItOVA0MS04UTM0NDdOODU5UzdcIixcInpjdlwiOlwidmJmXCIsXCJ6b3pzXCI6XCJOY2N5clwiLFwiemZ6XCI6MTcxNzk4NjkxODQsXCJqeGVcIjo0OTcwLFwiem9vXCI6XCJOY2N5clwiLFwienVmXCI6W1wienZ6Z1wiXSxcInF2cWdtXCI6NDIwLFwidmNlXCI6XCJhcHZjLDAsNXM5bzg0MXAsMywxO2ZnLDAsYXF2Y2V2YWNoZzAsMCxhcXZjZXZhY2hnMSwwO3NzLDAsYXF2Y2V2YWNoZzA7eHEsc3A7eHEsNG47eHEsNW87eHEsNjY7eHEsMjUzO3hxLDg1O3hxLHBxO3hxLDM4O3hxLDVuO3hxLDcyO3hxLDU2O3hxLDUyO3hxLDJwO3NvLG45LGFxdmNldmFjaGcwO3NzLDEsYXF2Y2V2YWNoZzE7eHEsMTI1O3hxLDQwO3hxLG82O3hxLDRyO3hxLG87Z3IscXMsNzcsMjM0LGFxdmNlcGJhZ2VieTBcIixcInpjemlcIjoxMixcInpvelwiOlwidkN1YmFyXCIsXCJ6Y3p2aVwiOjIsXCJ6ZmZcIjo0OTk5NjMxNzQ5MTIsXCJ6dW9wZlwiOi0xLFwiem9jXCI6XCJ2Q3ViYXIgS8qAXCIsXCJ6dmh2XCI6XCJjdWJhclwiLFwiZmVcIjpcIjE3OTJrODI4XCIsXCJoblwiOlwiMi4yLjEzODA2NVwiLFwiemh5XCI6XCJIRlwifSxcImpnXCI6XCIxLmotNDUxNjgwLjEuMi56U0dVQk56cHZNeUFoQjBOR2h2ZlRqLCwuM3hCRWhxSGhKZFREb1ZYY0hjYXJOSDUzVWFFS2t2amxNVF9RQTl5dUlYcWUwbV9iX0k0WWtBWGExUExTVXJzS1R4c0NFWHB2bkJjNkpEajhNTzhGZFVXMEcyaE9BWXZDRWZ5NXRLOHdJdVN4MzBnZmszZDF0MWhnc2NJODdFTG9MVlozNi1BcjhhV0pFSjJ1Ungza3RJYW5teDktOExZZmZsMWlFbVhSd1pqQ3FaQTVVMUZ1dzYzUERKZ2lIYzFUendFVjN3ak8yZDBrbUZIWUJaZTAxd0hnajVKQThTM0R6Z3FydE9pN1d3Q0VqZldZMHFITXFGT1g1d2RBYkl5LVNmOGVkYXBaeHNxaTItc0N6ejhRN2osLFwifSIsCiAgInNpZCIgOiAiMEE2NEE5QzMtQ0IzRS00NzI2LTgwMEItM0MzMzYzODZBQURCIgp9";
    public static final String REMOTE_IP = "127.0.0.1";
    public static final String SESSION_ID = "FA326050-85A2-4197-9186-BAC914F401D0";
    public static final String USER_AGENT = "Mozilla: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.3 Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/43.4.";
    public static final String X_FORWARDED_FOR = "203.0.113.195, 70.41.3.18, 150.172.238.178";
    public static final String REQUEST_URL = "http://bla.com/hello/world/yellow";
    public static final String DEVICE_ID = "123456dfgcfr";
    public static final String MAKE = "Samsung";
    public static final String MODEL = "S8";
    public static final boolean VISA_VERIFY_REQUIRED = false;
    public static final String VISA_SUPPORTED_COUNTRY_CODE = "AU";

    //Multi-Doc
    public static final String NEW_LEGAL_NAME = "New Legal Name";

    public static final String X_ENCRYPTED_HEADER ="X-Encrypted-Payload";

    //Tp Data Share
    public static final String EMAIL_ID ="example@email.com";

    //Fraud signals
    public static final String ORIGINATED_SYSTEM = "RiskManager";
    public static final String ORIGINATED_SOURCE = "RISKCENTER";
    public static final String ORIGINATED_SYSTEM_TRANSACTION_ID = "cc3f61fa-22bc-4c7e-879c-ee74507d0b14";
    public static final String SCORE_BAND = "YELLOW";
    public static final Integer SCORE_VALUE= 35;
    public static final String IP_ADDRESS = "127.0.0.1";
    public static final String DOCUMENT_ID = "D-id-123";
    public static final String NAME = "John Doe";
    public static final String DOB = "2000-01-01";
    public static final String NATIONAL_ID= "N-id-123";
    public static final String PHONE_NUMBER = "123456789";
    public static final String ADDRESS = "123 Second Street";
    public static final String N_DEVICE_ID = "0630fd7c-3e89-4a1c-9271-d1e264386318";

    //Input test Constants
    public static final String ATTRIBUTE_ID = "868e9531-71e5-4bc9-a19d-d09fb4797dea";
    public static final String USER_SELECTED_COUNTRY = "USA";


    private Constants() {
    }
}