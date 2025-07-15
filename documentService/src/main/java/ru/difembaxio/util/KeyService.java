package ru.difembaxio.util;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;
import lombok.experimental.UtilityClass;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@UtilityClass
public class KeyService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    
    public KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA","BC");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public String encodeKeyToBase64(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
