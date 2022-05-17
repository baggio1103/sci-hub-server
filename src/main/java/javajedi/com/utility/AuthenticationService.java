package javajedi.com.utility;

import javajedi.com.exception.domain.UserNotFoundException;
import javajedi.com.model.User;
import javajedi.com.model.UserPrincipal;
import javajedi.com.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import static javajedi.com.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static javajedi.com.constant.UserImplementationException.NO_USER_FOUND_BY_USERNAME;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JWTTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    public void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    public String getPrincipal() {
        return getContext().getAuthentication().getPrincipal().toString();
    }

    @SneakyThrows
    public HttpHeaders getJwtHeader(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow( () -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + username));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

}
