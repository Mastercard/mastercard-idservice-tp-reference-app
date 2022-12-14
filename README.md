# Mastercard ID For Trust Providers Reference Implementation

[![](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](./LICENSE)
[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_mastercard-idservice-tp-reference-app&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Mastercard_mastercard-idservice-tp-reference-app)
[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_mastercard-idservice-tp-reference-app&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Mastercard_mastercard-idservice-tp-reference-app)
[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_mastercard-idservice-tp-reference-app&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Mastercard_mastercard-idservice-tp-reference-app)

## Table of Contents
- [Overview](#overview)
  * [Compatibility](#compatibility)
  * [References](#references)
- [Usage](#usage)
  * [Prerequisites](#prerequisites)
  * [Configuration](#configuration)
  * [Integrating with OpenAPI Generator](#integrating-with-openapi-generator)
  * [OpenAPI Generator Plugin Configuration](#openAPI_generator_plugin_configuration)
  * [Generating The API Client Sources](#generating_the_API_client_sources)
  * [Running the Project](#running-the-project)
  * [Use Cases](#use-cases)
- [API Reference](#api-reference)
- [Support](#support)

## Overview <a name="overview"></a>
ID For Trust Providers is a digital identity service from Mastercard that helps you apply for, enroll in, log in to, and access services more simply, securely and privately. Rather than manually providing your information when you are trying to complete tasks online or in apps, ID enables you to share your verified information automatically, more securely, and with your consent and control. ID also enables you to do away with passwords and protects your personal information. Please see here for more details on the API: [Mastercard ID for Trust Providers](https://developer.mastercard.com/mastercard-id-for-tp/documentation/).

For more information regarding the program refer to [Digital Identity Services](https://idservice.com/)


### Compatibility <a name="compatibility"></a>
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

### References <a name="references"></a>
* [Mastercard’s OAuth Signer library](https://github.com/Mastercard/oauth1-signer-java)
* [Using OAuth 1.0a to Access Mastercard APIs](https://developer.mastercard.com/platform/documentation/using-oauth-1a-to-access-mastercard-apis/)

## Usage <a name="usage"></a>
### Prerequisites <a name="prerequisites"></a>
* [Mastercard Developers Account](https://developer.mastercard.com/dashboard) with access to Mastercard ID for Trust Providers API
* A text editor or IDE
* [Spring Boot 2.2+](https://spring.io/projects/spring-boot)
* [Apache Maven 3.3+](https://maven.apache.org/download.cgi)
* Set up the `JAVA_HOME` environment variable to match the location of your Java installation.

### Configuration <a name="configuration"></a> 

1. Create an account at [Mastercard Developers](https://developer.mastercard.com/account/sign-up).  
2. Create a new project and add `Mastercard ID for Trust Providers` API to your project.
3. Download all the keys. It will download multiple files.
4. Select all `.p12` files, `.pem` file and copy it to `src/main/resources` in the project folder.
5. Open `${project.basedir}/src/main/resources/application.properties` and configure the parameters accordingly.

### Integrating with OpenAPI Generator <a name="integrating-with-openapi-generator"></a>
[OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) generates API client libraries from [OpenAPI Specs](https://github.com/OAI/OpenAPI-Specification). 
It provides generators and library templates for supporting multiple languages and frameworks.

See also:
* [OpenAPI Generator (maven Plugin)](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-maven-plugin)
* [OpenAPI Generator (executable)](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-cli)
* [CONFIG OPTIONS for java](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/java.md)

#### OpenAPI Generator Plugin Configuration <a name="openAPI_generator_plugin_configuration"></a>
```xml
<!-- https://mvnrepository.com/artifact/org.openapitools/openapi-generator-maven-plugin -->
<plugin>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>${openapi-generator.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputSpec>${project.basedir}/src/main/resources/mastercard-idservice-reference_api_spec.yaml</inputSpec>
                <generatorName>java</generatorName>
                <library>okhttp-gson</library>
                <generateApiTests>false</generateApiTests>
                <generateModelTests>false</generateModelTests>
                <configOptions>
                    <sourceFolder>src/gen/main/java</sourceFolder>
                    <dateLibrary>java8</dateLibrary>
                </configOptions>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### Generating The API Client Sources <a name="generating_the_API_client_sources"></a>
Now that you have all the dependencies you need, you can generate the sources. To do this, use one of the following methods:

* **Using IDE**<br/>
  In IntelliJ IDEA (or any other IDE of your choice), open the Maven menu. Click the icons `Reimport All Maven Projects` and `Generate Sources and Update Folders for All Projects`.

* **Using Terminal or CMD**<br/>
  Navigate to the root directory of the project within a terminal window and execute the `mvn clean compile` command.

### Running the Project <a name="running-the-project"></a>

* **VM Parameter for country selection**<br/>
    Pass VM Options for `userSelectedCountry`. `USA` set as default.<br/>
    Example: `-DuserSelectedCountry=BRA`

* **Using IDE**<br/>
  Navigate to the `com.mastercard.dis.mids.reference` package and right-click to run `MIDSReferenceApplication`

* **Using Terminal or CMD**<br/>
  Navigate to the root directory of the project within a terminal window and execute the `mvn spring-boot:run` command.

* After that you can see all the menu options, such as:

1. Registering a User Profile
2. Access Token API (SDKToken)
3. Create User Identity
4. Multi-Access Token API (MultiSDKToken)
5. Access User Identity
6. Update ID Confirmations (Enrollment)
7. Update ID Confirmations (Re-authentication)  
8. User Account Activity Searches 
9. Email OTP  
10. SMS OTP  
11. User Profiles Identity Searches  
12. Share User Identity (TP-TP)  
13. RP Activity Searches
14. Audit Events
15. Delete ID
16. Share User Identity (TP-RP) (Enrollment)
17. Additional Document Support
18. Share User Identity (TP-RP) (Re-authentication)
19. TP Scopes
20. TP Data Shares
21. Update Identity
22. Create Watchlist Fraud Signal
23. Search for a Watchlist Fraud Signal
24. Delete Identity Attribute
25. Exit

### Use cases <a name="use-cases"></a>
Main use cases in ID For Trust Providers Reference APIs are Personal Data Storage, SMS One Time Password, Email One Time Password, Document Verification, Initiate Authentications, Re-Authentication, Claims Sharing, Audit Events, User profiles.

Below are the different APIs available in ID For Trust Providers Reference application:

| Use case | Source Code | URL | Sample request | Sample response |
|----------|-------------|-----|----------------|-----------------|
| RP Activity Searches ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/tutorial/client-code/step7/)) | `performRPActivitySearch` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /rp-activity-searches | [RpActivitySearchesRequest](./docs/RpActivitySearchesRequest.md) | [RpActivitySearchesResponse](./docs/RpActivitySearchesResponse.md) |
| User Profiles Identity Searches ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/tutorial/client-code/step7/)) | `callRetrieveIdentities` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /user-profiles/identity-searches | [UserProfilesIdentitySearchesRequest](./docs/UserProfilesIdentitySearchesRequest.md) | [UserProfilesIdentitySearchesRequest](./docs/UserProfilesIdentitySearchesRequest.md) |
| User Account Activity Searches ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/tutorial/client-code/step7/)) | `retrieveUserActivities` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /user-account-activity-searches | [UserAccountActivitySearchesRequest](./docs/UserAccountActivitySearchesRequest.md) | [UserAccountActivitySearchesResponse](./docs/UserAccountActivitySearchesResponse.md) |
| SMS OTP ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/email-sms/#add-email-or-phone-number)) | `callCreateSmsOtp` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /sms-otps | [SmsOtpRequest](./docs/SmsOtpRequest.md) | [SmsOtpResponse](./docs/SmsOtpResponse.md) |
| SMS OTP Verifications ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/email-sms/#add-email-or-phone-number)) | `callVerifySmsOtp` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /sms-otp-verifications | [SmsOtpVerificationsRequest](./docs/SmsOtpVerificationsRequest.md) | [SmsOtpVerificationsResponse](./docs/SmsOtpVerificationsResponse.md) |
| Email OTP ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/email-sms/#add-email-or-phone-number)) | `callCreateEmailOtp` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /email-otps | [EmailOtpRequest](./docs/EmailOtpRequest.md) | [EmailOtpResponse](./docs/EmailOtpResponse.md) |
| Email OTP Verifications ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/email-sms/#add-email-or-phone-number)) | `callVerifyEmailOtp` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /email-otp-verifications | [EmailOtpVerificationsRequest](./docs/EmailOtpVerificationsRequest.md) | [EmailOtpVerificationsResponse](./docs/EmailOtpVerificationsResponse.md) |
| Document Retrieval ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/data-enrollment/)) | `callDocumentDataRetrieval` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /document-verifications/document-data-retrievals | [DocumentRetrievalRequest](./docs/DocumentRetrievalRequest.md) | [DocumentRetrievalResponse](./docs/DocumentRetrievalResponse.md) |
| Document Verification Confirmation ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/data-enrollment/)) | `callDocumentVerificationDataConfirmations` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /document-verifications/document-data-confirmations | [DocumentVerificationConfirmationRequest](./docs/DocumentVerificationConfirmationRequest.md) | [DocumentVerificationConfirmationResponse](./docs/DocumentVerificationConfirmationResponse.md) |
| Document Verification Update Confirmation ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/data-enrollment/)) | `callUpdateIdConfirmationsApi` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /document-verifications/update-id-confirmations | [DocumentVerificationUpdateConfirmationRequest](./docs/DocumentVerificationUpdateConfirmationRequest.md) | [DocumentVerificationUpdateConfirmationResponse](./docs/DocumentVerificationUpdateConfirmationResponse.md) |
| Access Tokens ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/data-enrollment/)) | `generateToken` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /access-tokens | [AccessTokensRequest](./docs/AccessTokensRequest.md) | [AccessTokensResponse](./docs/AccessTokensResponse.md) |
| Initiate Authentications ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/re-authentication/)) | `callInitiateAuthentication` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /initiate-authentications | [InitiateAuthenticationsRequest](./docs/InitiateAuthenticationsRequest.md) | [InitiateAuthenticationsResponse](./docs/InitiateAuthenticationsResponse.md) |
| Authentication Results ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/re-authentication/)) | `callAuthenticationResults` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /authentication-results | [AuthenticationResultsRequest](./docs/AuthenticationResultsRequest.md) | [AuthenticationResultsResponse](./docs/AuthenticationResultsResponse.md) |
| TP/RP Claims ([Documentation](https://developer.mastercard.com/mastercard-id-for-tp/documentation/use-cases/claim-sharing/#user-identity-share-between-tp-tp)) | `performTpRpClaimSharing` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /tprp-claims | [TPRPClaimsRequest](./docs/TPRPClaimsRequest.md) | [TPRPClaimsResponse](./docs/TPRPClaimsResponse.md) |
| Audit Events ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/tutorial/client-code/step9/))) | `performAuditEvents` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /audit-events | [AuditEventsRequest](./docs/AuditEventsRequest.md) | [AuditEventsResponse](./docs/AuditEventsResponse.md) |
| User Profiles ([API Documentation](https://developer.mastercard.com/mastercard-id-for-tp/tutorial/client-code/step2/)) | `callUserProfileRegistration` in [MIDSReference.java](./src\main\java\com\mastercard\dis\mids\reference\component\MIDSReference.java) | /user-profiles | [UserProfilesRequest](./docs/UserProfilesRequest.md) | [UserProfilesResponse](./docs/UserProfilesResponse.md) |

## API Reference <a name="api-reference"></a>

- To develop a client application that consumes the ID For Trust Providers API with Spring Boot, refer to the  [Mastercard ID For Trust Providers Reference](https://developer.mastercard.com/mastercard-id-for-tp/documentation/api-reference/).

- The OpenAPI specification with `idservice` and `idwatchlist` endpoints can be found [here](https://developer.mastercard.com/mastercard-id-for-tp/documentation/reference-app/).

## Support <a name="support"></a>

- For further information, send an email to `ID.Network.Support@mastercard.com`.
- For information regarding licensing, refer to the [License file](LICENSE.md).
- For copyright information, refer to the [COPYRIGHT.md](COPYRIGHT.md).
- For instructions on how to contribute to this project, refer to the [Contributing file](CONTRIBUTING.md).
