package com.mastercard.dis.mids.reference.service;

import org.openapitools.client.model.FraudData;
import org.openapitools.client.model.FraudSearchData;

public interface FraudSignalsService {

    void createFraudSignal(FraudData fraudData);
    void performFraudDataSearchSignals(FraudSearchData fraudSearchData);
}
