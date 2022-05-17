package javajedi.com.mapper;

import javajedi.com.data.InstituteData;
import javajedi.com.data.UserData;
import javajedi.com.model.Institute;
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
public interface InstituteMapper {

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    Institute toInstitute(InstituteData instituteData);

    @Mappings({@Mapping(target = "users", source = "users", ignore = true)})
    InstituteData toBasicInstituteData(Institute institute);

    @Mappings({@Mapping(target = "users", source = "users", qualifiedByName = "instituteUsers")})
    InstituteData toInstituteData(Institute institute);

    default List<InstituteData> mapInstitutes(List<Institute> institutes) {
        return !isEmpty(institutes) ?
                institutes.stream().map(this::toInstituteData).collect(Collectors.toList()) : List.of();
    }

    @Named("instituteUsers")
    default List<UserData> mapInstituteUsers(List<User> users) {
        return !isEmpty(users) ?
                userMapper.mapBasicUserList(users) : List.of();
    }

}
