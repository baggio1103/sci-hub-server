package javajedi.com.service;

import javajedi.com.data.TopicData;

import java.util.List;

public interface TopicService {

    List<TopicData> findAllTopics();

    TopicData findTopicByName(String topicName);

    TopicData saveTopic(TopicData topicData);

    TopicData updateTopic(String name, TopicData topicData);

    void deleteTopicByName(String name);

}
