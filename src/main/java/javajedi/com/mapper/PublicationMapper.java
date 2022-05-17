package javajedi.com.mapper;

import javajedi.com.data.PublicationData;
import javajedi.com.data.TopicData;
import javajedi.com.data.UserData;
import javajedi.com.model.Publication;
import javajedi.com.model.Topic;
import javajedi.com.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TopicMapper.class, InstituteMapper.class})
public interface PublicationMapper {

    TopicMapper topicMapper = Mappers.getMapper(TopicMapper.class);

    @Mappings(
            {@Mapping(source = "author", target = "author", ignore = true),
            @Mapping(source = "topics", target = "topics", qualifiedByName = "topicData")})
    PublicationData toPublicationData(Publication publication);

    @Mappings({@Mapping(source = "author", target = "author", qualifiedByName = "basicAuthorData")})
    PublicationData toUserPublicationData(Publication publication);

    @Named("basicAuthorData")
    default UserData mapBasicUserData(User user) {
        UserData userData = new UserData();
        userData.setUsername(user.getUsername());
        userData.setProfileImageUrl(user.getProfileImageUrl());
        return userData;
    }

    @Named("topicData")
    default List<TopicData> mapBasicUserData(List<Topic> topics) {
        return !CollectionUtils.isEmpty(topics) ?
                topicMapper.mapTopicList(topics) : List.of();
    }

    default List<PublicationData> mapPublicationList(List<Publication> publications) {
        return !CollectionUtils.isEmpty(publications) ?
                publications.stream().map(this::toPublicationData).collect(Collectors.toList()) :
                List.of();
    }

    default List<PublicationData> mapUserPublicationList(List<Publication> publications) {
        return !CollectionUtils.isEmpty(publications) ?
                publications.stream().map(this::toUserPublicationData).collect(Collectors.toList()) :
                List.of();
    }

}
