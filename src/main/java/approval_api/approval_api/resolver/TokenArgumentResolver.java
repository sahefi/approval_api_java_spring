package approval_api.approval_api.resolver;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {


        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            boolean supports = parameter.getParameterType().equals(String.class) && parameter.hasParameterAnnotation(Token.class);

        return supports;
        }
    

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
            String token = servletRequest.getHeader("Authorization");
            
            if(token == null){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized");
            }

            System.out.println(token);
            UUID userId = extractUserIdFromToken(token);
            System.out.println(userId);
            
            try {
                return token;
            } catch (ExpiredJwtException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized",e);
            }
        }


        private UUID extractUserIdFromToken(String token) {
            try { 
                String key = "alviantoputrasahefiseptemberbarudakwel";
                Claims claims = Jwts.parserBuilder().setSigningKey(key.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token).getBody();
                String userIdString = claims.getSubject();
        
                if (userIdString != null) {
                    UUID userId = UUID.fromString(userIdString);
                    System.out.println("Extracted userId: " + userId);
                    return userId;
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
