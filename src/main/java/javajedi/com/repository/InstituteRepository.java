package javajedi.com.repository;

import javajedi.com.model.Institute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstituteRepository extends JpaRepository<Institute, Long> {

    Optional<Institute> findInstituteByInstituteId(String instituteId);

    void deleteInstituteByInstituteId(String instituteId);

}
