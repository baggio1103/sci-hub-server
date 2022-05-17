package javajedi.com.service;

import javajedi.com.data.TopicData;
import javajedi.com.exception.domain.TopicExistException;
import javajedi.com.exception.domain.TopicNotFoundException;
import javajedi.com.mapper.TopicMapper;
import javajedi.com.model.Topic;
import javajedi.com.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static javajedi.com.constant.TopicConstant.TOPIC_EXISTS_BY_NAME;
import static javajedi.com.constant.TopicConstant.TOPIC_NOT_FOUND_BY_NAME;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    @Override
    public List<TopicData> findAllTopics() {
        return topicMapper.mapTopicList(topicRepository.findAll());
    }

    @Override
    public TopicData findTopicByName(String topicName) {
        Topic topic = topicRepository.findTopicByName(topicName)
                .orElseThrow(() -> new TopicNotFoundException(TOPIC_NOT_FOUND_BY_NAME + topicName));
        return topicMapper.toTopicData(topic);
    }

    @Override
    public TopicData saveTopic(TopicData topicData) {
        Topic topic = topicMapper.toTopic(topicData);
        return topicMapper.toTopicData(topicRepository.save(topic));
    }

    @Override
    public TopicData updateTopic(String topicName, TopicData topicData) {
        Topic topic = topicRepository.findTopicByName(topicName)
                .orElseThrow(() -> new TopicNotFoundException(TOPIC_NOT_FOUND_BY_NAME + topicName));
        validateTopicName(topic.getName(), topicData.getName());
        topic.setName(topicData.getName());
        topic.setDescription(topicData.getDescription());
        return topicMapper.toTopicData(topicRepository.save(topic));
    }

    private void validateTopicName(String currentTopicName, String newTopicName) {
        if (!currentTopicName.equalsIgnoreCase(newTopicName)) {
            topicRepository.findTopicByName(newTopicName).ifPresent(topic -> {
                throw new TopicExistException(TOPIC_EXISTS_BY_NAME + newTopicName);
            });
        }
    }

    @Override
    public void deleteTopicByName(String name) {
        topicRepository.deleteTopicByName(name);
    }

}
