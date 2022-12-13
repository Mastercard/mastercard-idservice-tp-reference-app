package com.mastercard.dis.mids.reference.service;

import org.openapitools.client.model.RPActivities;
import org.openapitools.client.model.RPActivitySearch;

public interface RPActivitySearchService {

    RPActivities rpActivitySearch(RPActivitySearch rpActivitySearch);
}