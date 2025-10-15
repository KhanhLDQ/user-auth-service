package org.tommap.tomuserloginrestapis.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.tommap.tomuserloginrestapis.model.dto.AddressDto;
import org.tommap.tomuserloginrestapis.model.dto.UserDto;
import org.tommap.tomuserloginrestapis.model.entity.Address;
import org.tommap.tomuserloginrestapis.model.entity.AddressType;
import org.tommap.tomuserloginrestapis.model.entity.User;
import org.tommap.tomuserloginrestapis.model.request.CreateUserRequest;
import org.tommap.tomuserloginrestapis.model.request.UpdateUserRequest;
import org.tommap.tomuserloginrestapis.model.response.UserRest;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR, imports = {AddressType.class})
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "encryptedPassword", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationStatus", ignore = true)
    @Mapping(target = "emailTokenExpiry", ignore = true)
    UserDto createUserRequestToUserDto(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    AddressDto createAddressRequestToAddressDto(CreateUserRequest.CreateAddressRequest request);

    UserRest userDtoToResponse(UserDto userDto);

    UserRest.AddressRest addressDtoToResponse(AddressDto addressDto);

    @Mapping(target = "roles", ignore = true)
    User userDtoToUserEntity(UserDto userDto);

    @Mapping(target = "type", expression = "java(AddressType.fromValue(addressDto.getType()))")
    Address addressDtoToAddressEntity(AddressDto addressDto);

    @Mapping(target = "password", ignore = true)
    UserDto userEntityToUserDto(User user);

    @Mapping(target = "type", expression = "java(address.getType().getValue())")
    AddressDto addressEntityToAddressDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "encryptedPassword", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationStatus", ignore = true)
    @Mapping(target = "emailTokenExpiry", ignore = true)
    //TODO: fix temporarily
    @Mapping(target = "addresses", ignore = true)
    UserDto updateUserRequestToUserDto(UpdateUserRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "emailVerificationStatus", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUserEntityFromUserDto(UserDto userDto, @MappingTarget User user);
}
