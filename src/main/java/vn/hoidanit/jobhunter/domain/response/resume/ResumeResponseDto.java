package vn.hoidanit.jobhunter.domain.response.resume;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.ResumeStatusEnum;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponseDto {
    private long id;
    private String email;
    private String url;
    private ResumeStatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private UserDto user;
    private JobDto job;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserDto{
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class JobDto{
        private long id;
        private String name;
    }


}
