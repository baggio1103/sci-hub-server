package javajedi.com.service;

import javajedi.com.data.PublicationData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PublicationService {

    List<PublicationData> findAllPublications();

    PublicationData findPublicationById(String publicationId);

    List<PublicationData> findAllUserPublications(String username);

    PublicationData postPublication(String username, String title, String description, Boolean isPublic, MultipartFile publication,
                                    String publicationType, List<String> topics) throws IOException;

    PublicationData editPublication(String publicationId, String title, String description, Boolean isPublic,
                                    List<String> topics);

    byte[] getPublicationMedia(String username, String fileName) throws IOException;

    void deletePublication(String publicationId);

}
