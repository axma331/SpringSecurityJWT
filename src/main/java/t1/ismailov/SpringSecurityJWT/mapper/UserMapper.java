package t1.ismailov.SpringSecurityJWT.mapper;

import t1.ismailov.SpringSecurityJWT.dto.UserDto;
import t1.ismailov.SpringSecurityJWT.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserDto userToUserDto(User user);

    List<UserDto> usersToUserDtos(List<User> users);

}
