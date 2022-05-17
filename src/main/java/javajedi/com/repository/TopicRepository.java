package javajedi.com.repository;

import javajedi.com.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findTopicByName(String name);

    void deleteTopicByName(String name);

    @Query("SELECT DISTINCT topic from Topic topic WHERE topic.name IN :topicNames")
    List<Topic> findAllTopicsInNames(@Param("topicNames") List<String> names);

}
