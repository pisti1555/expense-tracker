package hu.projects.expense_tracker.services.auth_token_service;

import hu.projects.expense_tracker.common.exceptions.UnauthorizedException;
import hu.projects.expense_tracker.configuration.properties.JwtConfigurationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService implements AuthTokenService {
    private final JwtConfigurationProperties jwtProperties;

    @Autowired
    public JwtService(JwtConfigurationProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return buildToken(userDetails, jwtProperties.getExpirationTime());
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        var nowDate = new Date(System.currentTimeMillis());
        var expDate = new Date(nowDate.getTime() + jwtProperties.getExpirationTime());

        var claims = extractAllClaims(token);

        final var isNotExpired = extractClaim(claims, Claims::getExpiration).isPresent() && extractClaim(claims, Claims::getExpiration).get().before(expDate);
        final var isNotBeforeNow = extractClaim(claims, Claims::getNotBefore).isPresent() && extractClaim(claims, Claims::getNotBefore).get().before(nowDate);
        final var isIssuedAtBeforeNow = extractClaim(claims, Claims::getIssuedAt).isPresent() && extractClaim(claims, Claims::getIssuedAt).get().before(nowDate);
        final var isUsernameValid = extractClaim(claims, Claims::getSubject).isPresent() &&
                extractClaim(claims, Claims::getSubject).get().equals(userDetails.getUsername());
        final var isIssuerValid = extractClaim(claims, Claims::getIssuer).isPresent() &&
                extractClaim(claims, Claims::getIssuer).get().equals(jwtProperties.getIssuer());
        final var containsAuthority = claims.containsKey("authority");

        return isNotExpired && isNotBeforeNow && isIssuedAtBeforeNow && isUsernameValid && isIssuerValid && containsAuthority;
    }

    @Override
    public String getUsernameFromToken(String token) {
        var claims = extractAllClaims(token);
        var username = extractClaim(claims, Claims::getSubject);
        return username.orElseThrow(() -> new UnauthorizedException("Invalid token. Token does not contain username."));
    }


    // --- Helper methods ---
    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(UserDetails userDetails, long expiration) {
        var claims = new HashMap<String, String>();
        userDetails.getAuthorities().forEach(authority -> claims.put("authority", authority.getAuthority()));

        var nowDate = new Date(System.currentTimeMillis());
        var expirationDate = new Date(nowDate.getTime() + expiration);

        return Jwts
                .builder()
                .claims(claims)
                .id(UUID.randomUUID().toString())
                .subject(userDetails.getUsername())
                .issuedAt(nowDate)
                .expiration(expirationDate)
                .notBefore(nowDate)
                .issuer(jwtProperties.getIssuer())
                .signWith(getSecretKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <R> Optional<R> extractClaim(Claims claims, Function<Claims, R> claimsResolver) {
        return Optional.ofNullable(claimsResolver.apply(claims));
    }
}