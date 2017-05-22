/*
 * Copyright (c) 2017 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.aexp.api.client.core.utils;

import com.nimbusds.jose.JWSObject;
import io.aexp.api.client.core.exceptions.CryptoException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class EncryptionUtilityTest {

    static private EncryptionUtility encryptionUtility = EncryptionUtility.getInstance();

    @Test
    public void validEncryptDecrypt() {

        String aesKey = "LvXdoSVmSc0VxTRd13DfNYS7qKEVdsHURcBI/FSnv2w=";

        String data = "This is a test";
        String encrypted = encryptionUtility.encrypt(data, "", aesKey);
        String decrypted = encryptionUtility.decrypt(aesKey, encrypted);

        assertEquals(data, decrypted);
    }

    @Test(expected = CryptoException.class)
    public void invalidDecrypt() {
        encryptionUtility.decrypt("invalid", "wwww");
    }

    @Test(expected = CryptoException.class)
    public void invalidEncrypt() {
        encryptionUtility.encrypt("data", "invalid", "wwww");
    }

    @Test
    public void validSignRS256() {
        String privKey = "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";

        String signature = encryptionUtility.sign("RS256", "", privKey, "Sign this");
        assertTrue(signature.contains("\"protected\":"));
        assertTrue(signature.contains("\"payload\":"));
        assertTrue(signature.contains("\"signature\":"));
    }

    @Test
    public void validSignHS256() {
        String signature = encryptionUtility.sign("HS256", "", "LvXdoSVmSc0VxTRd13DfNYS7qKEVdsHURcBI/FSnv2w=", "Sign this");
        assertTrue(signature.contains("\"protected\":"));
        assertTrue(signature.contains("\"payload\":"));
        assertTrue(signature.contains("\"signature\":"));
    }
    @Test (expected = CryptoException.class)
    public void invalidSign() {
        encryptionUtility.sign("HS256", "", null, "Sign this");
    }

    @Test
    public void checkObjectFailure() throws Exception {
        Method method = encryptionUtility.getClass().getDeclaredMethod("checkObject", JWSObject.class);

        method.setAccessible(true);
        JWSObject object = mock(JWSObject.class);

        Throwable exception = null;
        try {
            method.invoke(encryptionUtility, object);
        } catch(InvocationTargetException e) {
            exception = e.getCause();
        }

        assertNotNull(exception);
        assertTrue(exception instanceof CryptoException);
    }
}
