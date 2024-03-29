# Changelog
All notable changes to the Application should be documented in this file.
Versioning will follow the values set for the main API [Mastercard ID for Trust Providers](https://developer.mastercard.com/mastercard-id-for-tp/documentation/release-history/).

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)

## [9.0] - 2023-01-05
### Updates
- Claim Sharing Improvements - Global Users.

### Updates reference app
- Refactor TP reference app to use typed inputs like arid, re-auth and multi doc workflow id.
- Created logic to read the X-user-identity header from the application properties file.

## [8.0] - 2022-08-22
### Updates
- Authentication-decisions api.
- Access Token to remove the livenessType.

### Removed
- Delete Individual Attribute.

## [7.0] - 2022-05-10
### Added
- Support for the cardNumber field added for Australian customers during enrollment.

### Updated
- Adapt Claim Sharing SDK to support ID button displaying in modal-based views [iOS].
- A new high-performance API has been introduced for reauthentication named /authentication-decision.

## [6.0] - 2022-02-02
### Added
- Sanctions Checks during enrollment.
- Support for Channel Flag during enrollment.
- Face Match 1:N.
- Split PDS functionality is introduced with the MIDS SDK version 2.3.0.

## [5.0] - 2021-12-08
### Added
- Trust Provider data sharing.
- Additional Brazilian documents support.

## [4.0] - 2021-08-04
### Added
- Claim sharing.
- Device-based backup and restore.
- Delete ID.
- Revoke and suspend ID.
- Multi Document.

## [2.1.0] - 2021-06-11
### Added
- Changelog file.
- The first version of the application "Mastercard ID For Trust Providers Reference Implementation".

## [2.0] -  2021-06-04
### Added
- Support for International passports with Australian Visa added.
- Metrics reports are available to customers upon request.
- ID enrollment.
- ID authentication.


