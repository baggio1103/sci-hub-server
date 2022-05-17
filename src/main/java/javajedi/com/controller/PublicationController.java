package javajedi.com.controller;

import javajedi.com.data.HttpResponse;
import javajedi.com.data.PublicationData;
import javajedi.com.exception.ExceptionHandling;
import javajedi.com.service.PublicationService;
import javajedi.com.utility.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static javajedi.com.constant.PublicationConstant.PUBLICATION_DELETED_SUCCESSFULLY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/publications")
public class PublicationController extends ExceptionHandling {

    private final PublicationService publicationService;

    private final AuthenticationService authenticationService;

    @GetMapping("")
    public ResponseEntity<List<PublicationData>> findAllPublications() {
        List<PublicationData> publications = publicationService.findAllPublications();
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    @GetMapping("/{publicationId}")
    public ResponseEntity<PublicationData> findPublicationById(@PathVariable("publicationId") String publicationId) {
        PublicationData publication = publicationService.findPublicationById(publicationId);
        return new ResponseEntity<>(publication, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PublicationData> postPublication(@RequestParam("title") String title,
                                                           @RequestParam("description") String description,
                                                           @RequestParam("isPublic") String isPublic,
                                                           @RequestParam("file") MultipartFile file,
                                                           @RequestParam("publicationType") String publicationType,
                                                           @RequestParam("topics") List<String> topics) throws IOException {
        String username = authenticationService.getPrincipal();
        PublicationData publicationData = publicationService.postPublication(username, title, description,
                Boolean.parseBoolean(isPublic), file, publicationType, topics);
        return new ResponseEntity<>(publicationData, HttpStatus.OK);
    }

    @PutMapping("/{publicationId}")
    public ResponseEntity<PublicationData> editPublication(@PathVariable("publicationId") String publicationId,
                                                           @RequestParam("title") String title,
                                                           @RequestParam("description") String description,
                                                           @RequestParam("isPublic") String isPublic,
                                                           @RequestParam("topics") List<String> topics) {
        PublicationData publicationData = publicationService.editPublication(publicationId, title, description,
                Boolean.parseBoolean(isPublic), topics);
        return new ResponseEntity<>(publicationData, HttpStatus.OK);
    }

    @DeleteMapping("/{publicationId}")
    public ResponseEntity<HttpResponse> deletePublication(@PathVariable("publicationId") String publicationId) {
        publicationService.deletePublication(publicationId);
        return createHttpResponse(HttpStatus.OK, PUBLICATION_DELETED_SUCCESSFULLY);
    }

    @GetMapping(value = "/media/{username}/{fileName}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPublicationMedia(@PathVariable("username") String username,
                                      @PathVariable("fileName") String fileName) throws IOException {
        return publicationService.getPublicationMedia(username, fileName);
    }

}
