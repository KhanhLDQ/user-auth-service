package org.tommap.tomuserloginrestapis.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;
import org.tommap.tomuserloginrestapis.model.entity.User;
import org.tommap.tomuserloginrestapis.model.request.CreateUserRequest;
import org.tommap.tomuserloginrestapis.model.request.UpdateUserRequest;
import org.tommap.tomuserloginrestapis.model.response.UserRest;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "encryptedPassword", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationStatus", ignore = true)
    UserDto createUserRequestToUserDto(CreateUserRequest request);

    UserRest userDtoToResponse(UserDto userDto);

    User userDtoToUserEntity(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto userEntityToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "encryptedPassword", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationStatus", ignore = true)
    UserDto updateUserRequestToUserDto(UpdateUserRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "emailVerificationStatus", ignore = true)
    void updateUserEntityFromUserDto(UserDto userDto, @MappingTarget User user);
}
