package cybercooker.authservice.mapper;

import cybercooker.authservice.entity.User;
import cybercooker.authservice.request.UserCreateRequest;
import cybercooker.authservice.request.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserCreateRequest user);

    User toUser(UserUpdateRequest user);

}
