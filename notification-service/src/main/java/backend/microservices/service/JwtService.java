package backend.microservices.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    public Claims extractAllClaims(String token) {
        String newToken = removeBearerPrefix(token);
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(newToken)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public Long extractUserId(String token) {
        String newToken = removeBearerPrefix(token);
        Claims claims = extractAllClaims(newToken);
        return Long.parseLong(claims.get("id").toString());
    }

    public String extractEmail(String token) {
        String newToken = removeBearerPrefix(token);
        Claims claims = extractAllClaims(newToken);
        return claims.get("email").toString();
    }
    private String removeBearerPrefix(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
