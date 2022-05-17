package javajedi.com.service;

import javajedi.com.data.InstituteData;
import javajedi.com.data.TopicData;
import javajedi.com.data.UserData;
import javajedi.com.exception.domain.EmailExistException;
import javajedi.com.exception.domain.UserNotFoundException;
import javajedi.com.exception.domain.UsernameExistException;
import javajedi.com.mapper.TopicMapper;
import javajedi.com.mapper.UserMapper;
import javajedi.com.model.Institute;
import javajedi.com.model.Topic;
import javajedi.com.model.User;
import javajedi.com.repository.InstituteRepository;
import javajedi.com.repository.TopicRepository;
import javajedi.com.repository.UserRepository;
import javajedi.com.utility.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.nonNull;
import static javajedi.com.constant.FileConstant.*;
import static javajedi.com.constant.UserImplementationException.*;
import static javajedi.com.enumeration.Role.ROLE_USER;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PrivacyServiceImpl implements PrivacyService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final AuthenticationService authenticationService;

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    private final InstituteRepository instituteRepository;

    @Override
    public UserData register(UserData userData) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        validateNewUsernameAndEmail(EMPTY, userData.getUsername(), userData.getEmail());
        User user = new User();
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setUsername(userData.getUsername());
        user.setPassword(encodedPassword);
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setEmail(userData.getEmail());
        user.setJoinDate(LocalDate.now());
        user.setActive(true);
        user.setDob(userData.getDob());
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(userData.getUsername()));
        log.info("New User password: {}", password);
//        Long userCount = userRepository.count() + 1;
//        emailService.sendWelcomeMessage(isNotEmpty(user.getFirstName()) ? user.getFirstName() : user.getUsername(),
//                password, user.getEmail(), userCount);
        userRepository.save(user);
        return userMapper.toUserData(user);
    }

    @Override
    public UserData login(String username, String password) {
        authenticationService.authenticate(username, password);
        return userMapper.toUserData(userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + username)));
    }
    
    @Override
    public UserData updateProfileImage(String username, MultipartFile profileImage) throws IOException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow( () -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + username));
        saveProfileImage(user, profileImage);
        return userMapper.toUserData(user);
    }

    @Override
    public void delete(String username) {
        log.info("User with username : {} deleting his/her account", username);
        userRepository.deleteUserByUsername(username);
    }

    @Override
    public byte[] getTemporaryProfileImage(String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_BASE_URL + username);
        log.info("Fetching image from : {}, by username : {}", url.toString(), username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(InputStream inputStream = url.openStream()){
            int bytesRead;
            byte[] chunk = new byte[1012];
            while ((bytesRead = inputStream.read(chunk)) > 0){
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] getProfileImage(String username, String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + PROFILE + FORWARD_SLASH + fileName));
    }

    @Override
    public UserData editUserProfile(String username, UserData userData) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow( () -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + username));
        validateNewUsernameAndEmail(username, userData.getUsername(), userData.getEmail());
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setEmail(user.getEmail());
        user.setUsername(userData.getUsername());
        user.setDob(userData.getDob());
        List<Topic> topics = new ArrayList<>(user.getTopics());
        List<TopicData> userDataTopics = nonNull(userData.getTopics()) ? userData.getTopics() : List.of();
        List<String> newUserTopics = nonNull(userDataTopics) ? userDataTopics.stream()
                .map(TopicData::getName).collect(Collectors.toList()) : List.of();
        topics.forEach(topic -> {
            if (!newUserTopics.contains(topic.getName())) {
                user.removeTopic(topic);
            }
        });
        userDataTopics.stream().filter(topicData -> user.getTopics()
                .stream().noneMatch(topic -> topic.getName().equalsIgnoreCase(topicData.getName())))
                .forEach(topicData -> topicRepository.findTopicByName(topicData.getName())
                        .ifPresentOrElse(
                                user::addTopic,
                                () -> user.addTopic(topicMapper.toTopic(topicData))));
        initializeOrEditUserInstitute(user, userData);
        userRepository.save(user);
        return userMapper.toUserData(user);
    }

    private void initializeOrEditUserInstitute(User user, UserData userData) {
        InstituteData instituteData = userData.getInstitute();
        if (nonNull(instituteData)) {
            Optional<Institute> optional = instituteRepository.findInstituteByInstituteId(instituteData.getInstituteId());
            if (isEmpty(instituteData.getInstituteId()) || optional.isEmpty()) {
                Institute institute = new Institute();
                institute.setInstituteId(generateId());
                institute.setCountry(instituteData.getCountry());
                institute.setName(instituteData.getName());
                institute.addUser(user);
                instituteRepository.save(institute);
                return;
            }
            Institute institute = optional.get();
            institute.addUser(user);
            instituteRepository.save(institute);
        }
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String email) {
        User userByNewUsername = userRepository.findUserByUsername(newUsername).orElse(null);
        User userByNewEmail = userRepository.findUserByEmail(email).orElse(null);
        log.info("New user username: {}", newUsername);
        log.info("Has user been found by username : {} ? - {}", newUsername, nonNull(userByNewUsername));
        log.info("New User email: {}", email);
        log.info("Has user been found by email : {} ? - {}", email, nonNull(userByNewEmail));
        if (isNotBlank(currentUsername)) {
            User currentUser = userRepository.findUserByUsername(currentUsername)
                    .orElseThrow( () -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + currentUsername));
            if (nonNull(userByNewUsername) && !currentUser.getId().equals(userByNewUsername.getId())){
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (nonNull(userByNewEmail) && !currentUser.getId().equals(userByNewEmail.getId())){
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (nonNull(userByNewUsername)){
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (nonNull(userByNewEmail)){
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    private String getTemporaryProfileImageUrl(String username) {
        return fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return randomAlphanumeric(10);
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException {
        if (nonNull(profileImage)) {
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername() + FORWARD_SLASH + PROFILE).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                log.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(USER_FOLDER + user.getUsername() + FORWARD_SLASH + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImage(user.getUsername()));
            userRepository.save(user);
        }
    }

    private String setProfileImage(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(USER_IMAGE_PATH + FORWARD_SLASH + username + FORWARD_SLASH +
                        username + DOT + JPG_EXTENSION).toUriString();
    }

    private String generateId() {
        return randomAlphanumeric(10) + instituteRepository.count();
    }

}
