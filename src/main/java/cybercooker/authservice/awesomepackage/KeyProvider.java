package cybercooker.authservice.awesomepackage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
public class KeyProvider {
    @Value("${rsaPublicKey}")
    private String publicKeyString;

    @Value("${rsaPrivateKey}")
    private String privateKeyString;

    public RSAPublicKey getPublicKey() throws Exception {
        return KeyUtil.getPublicKey(publicKeyString);
    }

    public RSAPrivateKey getPrivateKey() throws Exception {
        return KeyUtil.getPrivateKey(privateKeyString);
    }
}