package ru.tinkoff.lab.tripAPI.security.utils;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

@Component
public class PasswordEncoder {
    private static final Random RANDOM = new SecureRandom();

    private static final Charset charset = StandardCharsets.UTF_8;

    private static final int ITERATIONS = 1000;

    private static final int KEY_LENGTH = 512;

    public String generatePassword(String password, String salt){
        try {
            byte[] byteSalt = encodeString(salt);
            char[] charPassword = password.toCharArray();

            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec(charPassword, byteSalt, ITERATIONS, KEY_LENGTH);

            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();

            return decodeString(res);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException( e );
        }
    }

    public String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return decodeString(salt);
    }

    public String decodeString(byte[] str) {
        return new String(str, charset);
    }
    public byte[] encodeString(String str) {
        return str.getBytes(charset);
    }
}