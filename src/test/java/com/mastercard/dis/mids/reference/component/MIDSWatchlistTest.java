package com.mastercard.dis.mids.reference.component;

import com.mastercard.dis.mids.reference.service.FraudSignalsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.FraudData;
import org.openapitools.client.model.FraudSearchData;

@ExtendWith(MockitoExtension.class)
class MIDSWatchlistTest {

    @InjectMocks
    MIDSWatchlist midsWatchlist;

    @Mock
    FraudSignalsService fraudSignalsService;

    @Test
    void createPerformFraudData_successfulResponse(){
        midsWatchlist.createPerformFraudDataCreation();

        Mockito.verify(fraudSignalsService, Mockito.times(1)).createFraudSignal(Mockito.any(FraudData.class));
    }

    @Test
    void performFraudDataSearchSignals_successfulResponse(){
        midsWatchlist.performFraudDataSearchSignals();

        Mockito.verify(fraudSignalsService, Mockito.times(1)).performFraudDataSearchSignals(Mockito.any(FraudSearchData.class));
    }
}