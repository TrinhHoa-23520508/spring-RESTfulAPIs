package vn.hoidanit.jobhunter.domain.response.resume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeCreateDto {
    private long id;
    private Instant createdAt;
    private String createdBy;

}
