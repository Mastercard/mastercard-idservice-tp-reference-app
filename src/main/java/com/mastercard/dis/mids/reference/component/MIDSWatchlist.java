package com.mastercard.dis.mids.reference.component;


import com.mastercard.dis.mids.reference.example.FraudDataExample;
import com.mastercard.dis.mids.reference.service.FraudSignalsService;
import org.openapitools.client.model.FraudData;
import org.openapitools.client.model.FraudSearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MIDSWatchlist {

    @Autowired
    FraudSignalsService fraudSignalsService;

    public void createPerformFraudDataCreation() {
        FraudData fraudData = FraudDataExample.getFraudDataExample();
        fraudSignalsService.createFraudSignal(fraudData);
    }


    public void performFraudDataSearchSignals() {
        FraudSearchData fraudSearchData = FraudDataExample.getFraudSearchExample();
        fraudSignalsService.performFraudDataSearchSignals(fraudSearchData);

    }
}
