package vn.hoidanit.jobhunter.domain.response.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateDto {

    private long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;

    private Instant startDate;
    private Instant endDate;
    private Boolean active;
    private Instant createdAt;
    private String createdBy;
    private List<SkillDto> skills;

    @Getter
    @Setter
    public static class SkillDto{
        private String name;
    }
}
