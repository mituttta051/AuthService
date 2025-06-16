package cybercooker.authservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import cybercooker.authservice.awesomepackage.KeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@RestController("/token")
public class JWTController {
    private RSAPrivateKey rsaPrivateKey;
    private RSAPublicKey rsaPublicKey;

    @Autowired
    public JWTController(KeyProvider keyProvider) throws Exception {
        this.rsaPrivateKey = keyProvider.getPrivateKey();
        this.rsaPublicKey = keyProvider.getPublicKey();
    }
    
    @GetMapping("/getToken")
    public String getToken(@RequestParam String username) {
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withSubject(username)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid signing configuration");
        }
    }
    
    @GetMapping("/verifyToken")
    public String verifyToken(@RequestParam String token, @RequestParam String username) {
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (!username.equals(decodedJWT.getSubject())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username does not match token subject");
            }
            return "Token is valid for user: " + username;
        } catch (JWTVerificationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid signature");
        }
    }
}
