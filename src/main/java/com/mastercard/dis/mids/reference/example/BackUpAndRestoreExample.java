package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.DeviceInfo;

import static com.mastercard.dis.mids.reference.constants.Constants.DEVICE_ID;
import static com.mastercard.dis.mids.reference.constants.Constants.MAKE;
import static com.mastercard.dis.mids.reference.constants.Constants.MODEL;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BackUpAndRestoreExample {

    public static DeviceInfo getDeviceInfo() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceAppId(DEVICE_ID);
        deviceInfo.setMake(MAKE);
        deviceInfo.setModel(MODEL);
        return deviceInfo;
    }
}
