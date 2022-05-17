package javajedi.com.filter;

import javajedi.com.constant.SecurityConstant;
import javajedi.com.utility.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javajedi.com.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) {
            response.setStatus(OK.value());
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)){
                filterChain.doFilter(request, response);
                return;
            }
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            String userName = jwtTokenProvider.getSubjectFromToken(token);
            if (jwtTokenProvider.isTokenValid(userName, token) && getContext().getAuthentication() == null){
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(userName, authorities, request);
                getContext().setAuthentication(authentication);
            }else {
                clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

}
