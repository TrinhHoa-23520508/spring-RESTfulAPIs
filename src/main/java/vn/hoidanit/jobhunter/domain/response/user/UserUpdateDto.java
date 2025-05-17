package vn.hoidanit.jobhunter.domain.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private Long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updateAt;
    private UserCreateDto.Company company;
    private UserCreateDto.Role role;


}
