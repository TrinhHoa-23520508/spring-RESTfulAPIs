package vn.hoidanit.jobhunter.service.mapper;

import org.springframework.stereotype.Component;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.UserCreateDto;
import vn.hoidanit.jobhunter.domain.dto.UserResponseDto;
import vn.hoidanit.jobhunter.domain.dto.UserUpdateDto;

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
        return userUpdateDto;
    }
    public UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setAge(user.getAge());
        userResponseDto.setGender(user.getGender());
        userResponseDto.setAddress(user.getAddress());
        userResponseDto.setUpdatedAt(user.getUpdatedAt());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        return userResponseDto;
    }
}
