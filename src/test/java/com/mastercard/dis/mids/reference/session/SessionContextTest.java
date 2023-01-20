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
package com.mastercard.dis.mids.reference.session;

import com.mastercard.dis.mids.reference.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionContextTest {

    public static final String USER_IDENTITY_TOKEN = "sessionToken";

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(SessionContext.class, "instance", null);
    }

    @Test
    void testCreate_shouldCreateAnInstanceWithGivenJwtAndUserIdentityTokens() {
        SessionContext sessionContext = SessionContext.create(USER_IDENTITY_TOKEN);
        assertNotNull(sessionContext);
        assertEquals(USER_IDENTITY_TOKEN, sessionContext.getUserIdentityToken());
    }

    @Test
    void testGet_shouldReturnActiveInstance() {
        SessionContext.create(USER_IDENTITY_TOKEN);

        assertNotNull(SessionContext.get());
    }

    @Test
    void testGet_shouldThrowExceptionWhenInstanceIsNotSet() {
        assertThrows(ServiceException.class, SessionContext::get);
    }

    @Test
    void testGet_shouldThrowExceptionWhenJwtIsEmpty() {
        assertThrows(ServiceException.class, () -> SessionContext.create(""));
    }

    @Test
    void testGet_shouldThrowExceptionWhenJwtTokenIsNull() {
        assertThrows(ServiceException.class, () -> SessionContext.create(""));
    }

    @Test
    void testGet_shouldThrowExceptionWhenUserIdentityTokenIsNull() {
        assertThrows(ServiceException.class, () -> SessionContext.create(null));
    }
}