package javajedi.com.controller;

import javajedi.com.data.PublicationData;
import javajedi.com.data.UserData;
import javajedi.com.exception.ExceptionHandling;
import javajedi.com.service.PublicationService;
import javajedi.com.service.UserService;
import javajedi.com.utility.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController extends ExceptionHandling {

    private final UserService userService;

    private final PublicationService publicationService;

    private final AuthenticationService authenticationService;

    @GetMapping("")
    public ResponseEntity<List<UserData>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserData> findUserByUsername(@PathVariable("username") String username) {
        UserData user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/publications")
    public ResponseEntity<List<PublicationData>> findAllUserPublications() {
        String username = authenticationService.getPrincipal();
        List<PublicationData> userPublications = publicationService.findAllUserPublications(username);
        return new ResponseEntity<>(userPublications, HttpStatus.OK);
    }

    @GetMapping("/{username}/publications")
    public ResponseEntity<List<PublicationData>> findAllUserPublications(@PathVariable("username") String username) {
        List<PublicationData> userPublications = publicationService.findAllUserPublications(username);
        return new ResponseEntity<>(userPublications, HttpStatus.OK);
    }

}
