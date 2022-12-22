package com.mastercard.dis.mids.reference.util;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.model.FraudDetection;
import org.openapitools.client.model.TpAuditMetadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class DocumentVerificationUtilTest {

    @InjectMocks
    DocumentVerificationUtil documentVerificationUtil;

    @Mock
    private ApiClient apiClientMock;

    @Mock
    private ApiClientConfiguration apiClientConfigurationMock;

    @Test
    void test_construction(){
        this.documentVerificationUtil = new DocumentVerificationUtil(apiClientMock, apiClientConfigurationMock);
        assertNotNull(documentVerificationUtil);
    }


    @Test
    void getTpAuditMetadata_test() {
        TpAuditMetadata tpAuditMetadata = this.documentVerificationUtil.getTpAuditMetadata();

        assertNotNull(tpAuditMetadata);
    }

    @Test
    void createSessionContext_test() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("x-user-identity", Collections.singletonList("123456"));

        assertDoesNotThrow(() -> this.documentVerificationUtil.createSessionContext(headers));
    }

    @Test
    void getFraudDetection_test() {
        FraudDetection fraudDetection = this.documentVerificationUtil.getFraudDetection();

        assertNotNull(fraudDetection);
    }

}