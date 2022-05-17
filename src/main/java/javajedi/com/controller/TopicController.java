package javajedi.com.controller;

import javajedi.com.data.HttpResponse;
import javajedi.com.data.TopicData;
import javajedi.com.exception.ExceptionHandling;
import javajedi.com.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javajedi.com.constant.TopicConstant.TOPIC_DELETED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/topics")
public class TopicController extends ExceptionHandling {

    private final TopicService topicService;

    @GetMapping("")
    public ResponseEntity<List<TopicData>> findAllTopics() {
        List<TopicData> topics = topicService.findAllTopics();
        return new ResponseEntity<>(topics, OK);
    }

    @GetMapping("/{topicName}")
    public ResponseEntity<TopicData> findTopicByName(@PathVariable("topicName") String topicName) {
        TopicData topic = topicService.findTopicByName(topicName);
        return new ResponseEntity<>(topic, OK);
    }

    @PostMapping("")
    public ResponseEntity<TopicData> saveTopic(@RequestBody TopicData topicData) {
        TopicData topic = topicService.saveTopic(topicData);
        return new ResponseEntity<>(topic, OK);
    }

    @PutMapping("/{topicName}")
    public ResponseEntity<TopicData> editTopic(@PathVariable("topicName") String topicName,@RequestBody TopicData topicData) {
        TopicData topic = topicService.updateTopic(topicName, topicData);
        return new ResponseEntity<>(topic, OK);
    }

    @DeleteMapping("/{topicName}")
    public ResponseEntity<HttpResponse> deleteTopic(@PathVariable("topicName") String topicName) {
        topicService.deleteTopicByName(topicName);
        return createHttpResponse(OK, TOPIC_DELETED_SUCCESSFULLY + topicName);
    }

}
