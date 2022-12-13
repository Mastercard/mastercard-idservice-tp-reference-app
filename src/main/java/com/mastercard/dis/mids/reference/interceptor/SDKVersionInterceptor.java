/*
 Copyright (c) 2021 Mastercard

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

package com.mastercard.dis.mids.reference.interceptor;

import com.mastercard.dis.mids.reference.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class SDKVersionInterceptor extends BaseInterceptor implements Interceptor {

    @Value("${mastercard.api.sdk.version}")
    private String sdkVersion;

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = addSDKVersionToRequest(chain.request());
        return chain.proceed(request);
    }

    private Request addSDKVersionToRequest(Request request) {
        if (Arrays.asList(HttpMethod.POST, HttpMethod.PUT).contains(HttpMethod.valueOf(request.method()))) {
            try {
                String body = bodyToString(request);
                JSONObject requestJson = (JSONObject) JSONValue.parse(body);
                requestJson.put("sdkVersion", sdkVersion);
                Request.Builder builder = request
                        .newBuilder()
                        .headers(request.headers());
                if (HttpMethod.POST.equals(HttpMethod.valueOf(request.method()))) {
                    return builder.post(RequestBody.create(requestJson.toJSONString(), MediaType.parse("application/json"))).build();
                } else {
                    return builder.put(RequestBody.create(requestJson.toJSONString(), MediaType.parse("application/json"))).build();
                }
            } catch (Exception e) {
                log.error("Unable to parse request data", e);
                throw new ServiceException("Unable to parse request data", e);
            }
        }
        return request;
    }
}