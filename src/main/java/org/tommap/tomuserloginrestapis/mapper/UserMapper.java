package org.tommap.tomuserloginrestapis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;
import org.tommap.tomuserloginrestapis.model.entity.User;
import org.tommap.tomuserloginrestapis.model.request.UserDetailsRequest;
import org.tommap.tomuserloginrestapis.model.response.UserRest;

import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "encryptedPassword", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationStatus", ignore = true)
    UserDto requestToUserDto(UserDetailsRequest request);

    UserRest userDtoToResponse(UserDto userDto);

    User userDtoToUserEntity(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto userEntityToUserDto(User user);
}
