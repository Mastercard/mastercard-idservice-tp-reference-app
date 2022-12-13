package com.mastercard.dis.mids.reference.service;

import org.openapitools.client.model.TPDataShareSuccessData;
import org.openapitools.client.model.TpDataShare;

public interface TPDataSharesService {
    TPDataShareSuccessData updatePdsData(TpDataShare tpDataShare);
}
