package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.AuditEventsService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AuditEventsApi;
import org.openapitools.client.model.AuditEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditEventsServiceImpl implements AuditEventsService {

    private final AuditEventsApi auditEventsApi;
    private final ExceptionUtil exceptionUtil;

    @Autowired
    public AuditEventsServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil) {
        auditEventsApi = new AuditEventsApi(apiClient);
        this.exceptionUtil = exceptionUtil;

    }

    @Override
    public void auditEvents(AuditEvents auditEvents) {
        try {
            auditEventsApi.createAuditEvents(auditEvents);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }
}

