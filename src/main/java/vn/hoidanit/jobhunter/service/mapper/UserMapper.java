package vn.hoidanit.jobhunter.service.mapper;

import org.springframework.stereotype.Component;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.user.UserCreateDto;
import vn.hoidanit.jobhunter.domain.response.user.UserResponseDto;
import vn.hoidanit.jobhunter.domain.response.user.UserUpdateDto;

@Component
public class UserMapper {
    public UserCreateDto toUserCreateDto(User user) {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setId(user.getId());
        userCreateDto.setName(user.getName());
        userCreateDto.setEmail(user.getEmail());
        userCreateDto.setAge(user.getAge());
        userCreateDto.setGender(user.getGender());
        userCreateDto.setAddress(user.getAddress());
        userCreateDto.setCreatedAt(user.getCreatedAt());
        if(user.getCompany() != null) {
            userCreateDto.setCompany(new UserCreateDto.Company(user.getCompany().getId(), user.getCompany().getName()));
        }
        else{
            userCreateDto.setCompany(null);
        }

        return userCreateDto;
    }
    public UserUpdateDto toUserUpdateDto(User user) {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(user.getId());
        userUpdateDto.setName(user.getName());
        userUpdateDto.setAge(user.getAge());
        userUpdateDto.setGender(user.getGender());
        userUpdateDto.setAddress(user.getAddress());
        userUpdateDto.setUpdateAt(user.getUpdatedAt());
        if(user.getCompany() != null) {
            userUpdateDto.setCompany(new UserCreateDto.Company(user.getCompany().getId(), user.getCompany().getName()));
        }
        else{
            userUpdateDto.setCompany(null);
        }
        return userUpdateDto;
    }
    public UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setAge(user.getAge());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setGender(user.getGender());
        userResponseDto.setAddress(user.getAddress());
        userResponseDto.setUpdatedAt(user.getUpdatedAt());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        if(user.getCompany() != null) {
            userResponseDto.setCompany(new UserCreateDto.Company(user.getCompany().getId(), user.getCompany().getName()));
        }
        else{
            userResponseDto.setCompany(null);
        }
        return userResponseDto;
    }
}
