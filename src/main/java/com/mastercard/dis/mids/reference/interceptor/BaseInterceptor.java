package com.mastercard.dis.mids.reference.interceptor;

import okhttp3.Request;
import okio.Buffer;

import java.io.IOException;
import java.util.Objects;

public class BaseInterceptor {

    String bodyToString(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            Objects.requireNonNull(copy.body()).writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
