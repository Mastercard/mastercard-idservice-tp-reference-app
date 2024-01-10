package com.mastercard.dis.mids.reference.interceptor;

import okhttp3.Request;
import okio.Buffer;
import org.openapitools.client.ApiException;

import java.io.IOException;
import java.util.Objects;

public class BaseInterceptor {

    String bodyToString(Request request) throws ApiException {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            Objects.requireNonNull(copy.body()).writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            throw new ApiException(e);
        }
    }
}
