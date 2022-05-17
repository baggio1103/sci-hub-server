package javajedi.com.mapper;

import javajedi.com.data.InstituteData;
import javajedi.com.data.PublicationData;
import javajedi.com.data.TopicData;
import javajedi.com.data.UserData;
import javajedi.com.model.Institute;
import javajedi.com.model.Publication;
import javajedi.com.model.Topic;
import javajedi.com.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Mapper(componentModel = "spring")
public interface UserMapper {

    PublicationMapper publicationMapper = Mappers.getMapper(PublicationMapper.class);

    InstituteMapper instituteMapper = Mappers.getMapper(InstituteMapper.class);

    TopicMapper topicMapper = Mappers.getMapper(TopicMapper.class);

    @Mappings(
            {@Mapping(source = "publications", target = "publications", ignore = true),
            @Mapping(source = "institute", target = "institute", ignore = true),
            @Mapping(source = "topics", target = "topics", qualifiedByName = "userTopics"),})
    UserData toBasicUserData(User user);

    @Mappings({
            @Mapping(source = "publications", target = "publications", qualifiedByName = "userPublications"),
            @Mapping(source = "institute", target = "institute", qualifiedByName = "userInstitute")
    })
    UserData toUserData(User user);

    default List<UserData> mapUserList(List<User> users) {
        return !isEmpty(users) ?
                users.stream().map(this::toUserData).collect(Collectors.toList()) : List.of();
    }

    default List<UserData> mapBasicUserList(List<User> users) {
        return !isEmpty(users) ?
                users.stream().map(this::toBasicUserData).collect(Collectors.toList()) : List.of();
    }

    @Named("userPublications")
    default List<PublicationData> mapPublications(List<Publication> publications) {
        return publicationMapper.mapPublicationList(publications);
    }

    @Named("userInstitute")
    default InstituteData mapInstitute(Institute institute) {
        return instituteMapper.toBasicInstituteData(institute);
    }

    @Named("userTopics")
    default List<TopicData> mapInstitute(List<Topic> topics) {
        return !isEmpty(topics) ? topicMapper.mapTopicList(topics) : List.of();
    }

}
