package com.theftbuster.theftbuster.Libraries;

import android.util.Base64;
import android.util.Log;

import com.theftbuster.theftbuster.Helpers.Crypto;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    /** Librairie trouvée sur: https://github.com/scottyab/AESCrypt-Android
     *
     * Encrypt and decrypt messages using AES 256 bit encryption that are compatible with AESCrypt-ObjC and AESCrypt Ruby.
     * <p/>
     * Created by scottab on 04/10/2014.
     */

        private static final String TAG = "AESCrypt";

        //AESCrypt-ObjC uses CBC and PKCS7Padding
        private static final String AES_MODE = "AES/CBC/PKCS7Padding";
        private static final String CHARSET = "UTF-8";

        //AESCrypt-ObjC uses SHA-256 (and so a 256-bit key)
        private static final String HASH_ALGORITHM = "SHA-256";

        //AESCrypt-ObjC uses blank IV (not the best security, but the aim here is compatibility)
        static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        //togglable log option (please turn off in live!)
         static boolean DEBUG_LOG_ENABLED = false;


        /**
         * Generates SHA256 hash of the password which is used as key
         *
         * @return SHA256 of the password
         */
        private static SecretKeySpec generateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
            final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] bytes = Crypto.genSalt().getBytes("UTF-8"); // Modification apportée, au lieu de demander un mot de passe, on utilise une génération de salt
            digest.update(bytes, 0, bytes.length);
            byte[] key = digest.digest();
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            return secretKeySpec;
        }


        /**
         * Encrypt and encode message using 256-bit AES with key generated from password.
         *
         *
         * @param message the thing you want to encrypt assumed String UTF-8
         * @return Base64 encoded CipherText
         * @throws GeneralSecurityException if problems occur during encryption
         */
        public static String encrypt(final SecretKeySpec key, String message)
                throws GeneralSecurityException {

            try {
                byte[] cipherText = encrypt(key, ivBytes, message.getBytes(CHARSET));
                //NO_WRAP is important as was getting \n at the end
                String encoded = Base64.encodeToString(cipherText, Base64.NO_WRAP);
                return encoded;
            } catch (UnsupportedEncodingException e) {
                throw new GeneralSecurityException(e);
            }
        }


        /**
         * More flexible AES encrypt that doesn't encode
         * @param key AES key typically 128, 192 or 256 bit
         * @param iv Initiation Vector
         * @param message in bytes (assumed it's already been decoded)
         * @return Encrypted cipher text (not encoded)
         * @throws GeneralSecurityException if something goes wrong during encryption
         */
        public static byte[] encrypt(final SecretKeySpec key, final byte[] iv, final byte[] message)
                throws GeneralSecurityException {
            final Cipher cipher = Cipher.getInstance(AES_MODE);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] cipherText = cipher.doFinal(message);
            return cipherText;
        }


        /**
         * Decrypt and decode ciphertext using 256-bit AES with key generated from password
         *
         * @param base64EncodedCipherText the encrpyted message encoded with base64
         * @return message in Plain text (String UTF-8)
         * @throws GeneralSecurityException if there's an issue decrypting
         */
        public static String decrypt(final SecretKeySpec key, String base64EncodedCipherText)
                throws GeneralSecurityException {
            try {
                byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP);
                byte[] decryptedBytes = decrypt(key, ivBytes, decodedCipherText);
                String message = new String(decryptedBytes, CHARSET);

                return message;
            } catch (UnsupportedEncodingException e) {
                if (DEBUG_LOG_ENABLED)
                    Log.e(TAG, "UnsupportedEncodingException ", e);

                throw new GeneralSecurityException(e);
            }
        }


        /**
         * More flexible AES decrypt that doesn't encode
         *
         * @param key AES key typically 128, 192 or 256 bit
         * @param iv Initiation Vector
         * @param decodedCipherText in bytes (assumed it's already been decoded)
         * @return Decrypted message cipher text (not encoded)
         * @throws GeneralSecurityException if something goes wrong during encryption
         */
        public static byte[] decrypt(final SecretKeySpec key, final byte[] iv, final byte[] decodedCipherText)
                throws GeneralSecurityException {
            final Cipher cipher = Cipher.getInstance(AES_MODE);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(decodedCipherText);
            return decryptedBytes;
        }


        /**
         * Converts byte array to hexidecimal useful for logging and fault finding
         * @param bytes
         * @return
         */
        private static String bytesToHex(byte[] bytes) {
            final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            char[] hexChars = new char[bytes.length * 2];
            int v;
            for (int j = 0; j < bytes.length; j++) {
                v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }

        private AES() {
        }

    }

