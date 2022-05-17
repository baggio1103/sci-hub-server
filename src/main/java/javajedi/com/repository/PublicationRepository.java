package javajedi.com.repository;

import javajedi.com.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    Optional<Publication> findPublicationByPublicationId(String publicationId);

    void deletePublicationByPublicationId(String publicationId);

}
