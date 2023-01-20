/*
 Copyright (c) 2023 Mastercard

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.TPScopesService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TpScopesRequestApi;
import org.openapitools.client.model.RPScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TPScopesServiceImpl implements TPScopesService {

    private final TpScopesRequestApi tpScopesRequestApi;
    private final ExceptionUtil exceptionUtil;

    @Autowired
    public TPScopesServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil) {
        tpScopesRequestApi = new TpScopesRequestApi(apiClient);
        this.exceptionUtil = exceptionUtil;
    }

    @Override
    public RPScopes getRpScopes(String arid) {
        try {
            return tpScopesRequestApi.retrieveCSScopes(arid);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }
}