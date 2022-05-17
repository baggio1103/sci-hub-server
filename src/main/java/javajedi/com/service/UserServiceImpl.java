package javajedi.com.service;

import javajedi.com.data.UserData;
import javajedi.com.exception.domain.UserNotFoundException;
import javajedi.com.mapper.UserMapper;
import javajedi.com.model.User;
import javajedi.com.model.UserPrincipal;
import javajedi.com.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;
import static javajedi.com.constant.UserImplementationException.NO_USER_FOUND_BY_USERNAME;
import static javajedi.com.constant.UserImplementationException.RETURNING_USER_FOUND_BY_USERNAME;

@Slf4j
@Service("userDetailsService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElse(null);
        if (isNull(user)){
            log.error(NO_USER_FOUND_BY_USERNAME +" {} ", username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        }
//        validateLoginAttempt(user);
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
        log.info(RETURNING_USER_FOUND_BY_USERNAME, username);
        return new UserPrincipal(user);
    }

    @Override
    public List<UserData> findAllUsers() {
        return userMapper.mapUserList(userRepository.findAll());
    }

    @Override
    public UserData findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username));
        return userMapper.toUserData(user);
    }

}
