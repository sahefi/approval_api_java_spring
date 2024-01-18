package approval_api.approval_api.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.common.UserInfo;
import approval_api.approval_api.entity.User;
import approval_api.approval_api.model.LoginUserRequest;
import approval_api.approval_api.model.LoginUserResponse;
import approval_api.approval_api.repository.UserRepository;
import approval_api.approval_api.security.BCrypt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
            String token = generateJwtToken(user.getId(), user.getUsername(),user.getRole().getName());
            return new LoginUserResponse().builder()
                    .token(token)
                    .build();
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username / Password Wrong");
        }
        
    }


    private String generateJwtToken(UUID userId, String username,String role) {
        String key = "alviantoputrasahefiseptemberbarudakwel";
        long expirationTimeMillis = 30L * 24L * 60L * 60L * 1000L;  // 30 days in milliseconds
        long currentTimeMillis = System.currentTimeMillis();
        Date expirationDate = new Date(currentTimeMillis + expirationTimeMillis);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuer("IssuerAnda")
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(expirationDate)
                .claim("username", username)
                .claim("role",role)
                .signWith(SignatureAlgorithm.HS256,key.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

      public UserInfo extractUserIdFromToken(String token) {
            try { 
                String key = "alviantoputrasahefiseptemberbarudakwel";
                Claims claims = Jwts.parserBuilder().setSigningKey(key.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token).getBody();
                String userIdString = claims.getSubject();
                String role = claims.get("role",String.class);
        
                if (userIdString != null) {
                    UUID userId = UUID.fromString(userIdString);
                    System.out.println("Extracted userId: " + userId);
                    return new UserInfo(userId,role);
                } else {
                    System.out.println("UserId not found in token subject");
                    return null;
                }
            } catch (Exception e) {
                System.err.println("Error extracting userId from token: " + e.getMessage());
                return null;
            }

    }

}


