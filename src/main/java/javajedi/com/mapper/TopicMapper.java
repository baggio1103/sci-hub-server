package javajedi.com.mapper;

import javajedi.com.data.TopicData;
import javajedi.com.model.Topic;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    Topic toTopic(TopicData topicData);

    TopicData toTopicData(Topic topic);

    default List<TopicData> mapTopicList(List<Topic> topics) {
        return !isEmpty(topics) ?
                topics.stream().map(this::toTopicData).collect(Collectors.toList())
                : List.of();
    }

}
