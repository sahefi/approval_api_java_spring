package approval_api.approval_api.service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.JwkSetUriJwtDecoderBuilderCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.entity.User;
import approval_api.approval_api.model.LoginUserRequest;
import approval_api.approval_api.model.LoginUserResponse;
import approval_api.approval_api.repository.UserRepository;
import approval_api.approval_api.security.BCrypt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public LoginUserResponse login(LoginUserRequest request){
        validationService.validate(request);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username / Password Wrong"));
        
        if(BCrypt.checkpw(request.getPassword(), user.getPassword())){
            String token = generateJwtToken(user.getId(), user.getUsername());
            return new LoginUserResponse().builder()
                    .token(token)
                    .build();
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username / Password Wrong");
        }
        
    }

    private String generateJwtToken(UUID userId, String username) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

        // Menggunakan metode secretKeyFor untuk membuat kunci yang aman
        Key key = Keys.secretKeyFor(signatureAlgorithm);
        long expirationTimeMillis = 30L * 24L * 60L * 60L * 1000L;  // 30 days in milliseconds
        long currentTimeMillis = System.currentTimeMillis();
        Date expirationDate = new Date(currentTimeMillis + expirationTimeMillis);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuer("IssuerAnda")
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(expirationDate)
                .claim("username", username)
                .signWith(key,signatureAlgorithm)
                .compact();
    }

}
