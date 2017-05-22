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

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.AESDecrypter;
import com.nimbusds.jose.crypto.AESEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import io.aexp.api.client.core.exceptions.CryptoException;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class EncryptionUtility {
    private static final EncryptionUtility ENCRYPTION_UTILITY = new EncryptionUtility();

    private EncryptionUtility() {
    }

    public static EncryptionUtility getInstance() {
        return ENCRYPTION_UTILITY;
    }

    public String encrypt(String data, String keyId, String aesKey) {

        try {
            byte[] keyBytes = Base64.decode(aesKey);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");

            JWEAlgorithm jweAlgorithm = JWEAlgorithm.A256KW;
            EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;
            JWEHeader.Builder headerBuilder = new JWEHeader.Builder(jweAlgorithm, encryptionMethod);

            headerBuilder.keyID(keyId);

            JWEHeader header = headerBuilder.build();
            JWEEncrypter encrypter = new AESEncrypter(secretKey);
            encrypter.getJCAContext().setProvider(BouncyCastleProviderSingleton.getInstance());
            JWEObject jweObject = new JWEObject(header, new Payload(data));
            jweObject.encrypt(encrypter);
            return jweObject.serialize();
        } catch (Exception e) {
            throw new CryptoException("Exception encrypting data: " + e.getMessage(), e);
        }
    }

    public String sign(String algorithm, String kid, String keyStr, String dataToSign) {
        try {

            Key key = getKey(algorithm, keyStr);

            JWSHeader.Builder jwsBuilder = new JWSHeader.Builder("HS256".equals(algorithm) ? JWSAlgorithm.HS256 : JWSAlgorithm.RS256);
            jwsBuilder.keyID(kid);

            JWSHeader signingHeader = jwsBuilder.build();
            JWSSigner signer = "HS256".equals(algorithm) ? new MACSigner(key.getEncoded()) : new RSASSASigner((RSAPrivateKey) key);
            JWSObject jwsObject = new JWSObject(signingHeader, new Payload(dataToSign));
            jwsObject.sign(signer);
            checkObject(jwsObject);

            String parts[] = jwsObject.serialize().split("\\.");

            return "{\"protected\":\"" + parts[0] + "\", \"payload\":\"" + parts[1] + "\", \"signature\":\"" + parts[2] + "\"}";

        } catch (Exception e) {
            throw new CryptoException("Exception signing data: " + e.getMessage(), e);
        }
    }

    private void checkObject(JWSObject jwsObject) {
        if (JWSObject.State.SIGNED != jwsObject.getState()) {
            throw new CryptoException("Failed to generate signed payload");
        }
    }

    private Key getKey(String algorithm, String keyStr) {
        try {
            if ("HS256".equals(algorithm)) {

                //return Secret Key
                byte[] decodedKey = Base64.decode(keyStr);
                return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            } else {
                //return private key
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(keyStr)));
            }
        } catch(Exception e) {
            throw new CryptoException("Exception creating key: " + e.getMessage(), e);
        }
    }

    public String decrypt(String keyStr, String encryptedData) {
        try {
            byte[] decodedKey = Base64.decode(keyStr);
            SecretKeySpec key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            JWEDecrypter decrypter = new AESDecrypter(key);
            decrypter.getJCAContext().setProvider(BouncyCastleProviderSingleton.getInstance());

            JWEObject jweObject = JWEObject.parse(encryptedData);
            jweObject.decrypt(decrypter);
            return jweObject.getPayload().toBase64URL().decodeToString();

        } catch (Exception e) {
            throw new CryptoException("Exception decrypting field: " + e.getMessage(), e);
        }
    }
}
