package javajedi.com.controller;

import javajedi.com.data.HttpResponse;
import javajedi.com.data.LoginData;
import javajedi.com.data.UserData;
import javajedi.com.exception.ExceptionHandling;
import javajedi.com.service.PrivacyService;
import javajedi.com.utility.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static javajedi.com.constant.UserImplementationException.USER_DELETED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class PrivacyController extends ExceptionHandling {

    private final PrivacyService privacyService;

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserData> register(@RequestBody UserData userData) {
        UserData user = privacyService.register(userData);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserData> login(@RequestBody LoginData loginData) {
        UserData userData = privacyService.login(loginData.getUsername(), loginData.getPassword());
        HttpHeaders httpHeaders = authenticationService.getJwtHeader(userData.getUsername());
        return new ResponseEntity<>(userData, httpHeaders, HttpStatus.OK);
    }

    @PutMapping("/profile-image")
    public ResponseEntity<UserData> updateProfileImage(@RequestParam("file") MultipartFile profileImage) throws IOException {
        String username = authenticationService.getPrincipal();
        log.info("Username: {}", username);
        UserData userData = privacyService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(userData, OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserData> editProfile(@RequestBody UserData userData) {
        String username = authenticationService.getPrincipal();
        log.info("Username: {}", username);
        UserData user = privacyService.editUserProfile(username, userData);
        return new ResponseEntity<>(user, OK);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<HttpResponse> deleteAccount() {
        String username = authenticationService.getPrincipal();
        privacyService.delete(username);
        return createHttpResponse(OK, USER_DELETED);
    }
    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username")String username,
                                  @PathVariable("fileName") String fileName) throws IOException {
        return privacyService.getProfileImage(username, fileName);
    }

    @GetMapping(value = "/image/profile/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getTemporaryProfileImage(@PathVariable("username") String username) throws IOException {
        return privacyService.getTemporaryProfileImage(username);
    }

}
