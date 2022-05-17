package javajedi.com.utility;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import javajedi.com.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static javajedi.com.constant.SecurityConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UserPrincipal user){
        String[] claims = getClaimsFromUser(user);
        return JWT.create()
                .withIssuer(STELLAR_NET_LLC)
                .withAudience(STELLAR_NET_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(user.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(secret));
    }

    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String userName, List<GrantedAuthority> authorities,
                                            HttpServletRequest request){
        String credentials = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
        UsernamePasswordAuthenticationToken usernamePasswordAuthToken =
                new UsernamePasswordAuthenticationToken(userName,credentials, authorities);
        usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthToken;
    }

    public boolean isTokenValid(String userName, String token){
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(userName) && !isTokenExpired(verifier, token);
    }

    public String getSubjectFromToken(String token){
        JWTVerifier verifier = getJWTVerifier();
        verifier.verify(token);
        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expirationDate = verifier.verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(STELLAR_NET_LLC).build();
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

    private String[] getClaimsFromUser(UserPrincipal user) {
        List<String> authorities = new ArrayList<>();
        user.getAuthorities().forEach(grantedAuthority -> authorities.add(grantedAuthority.getAuthority()));
        return authorities.toArray(new String[0]);
    }

}
