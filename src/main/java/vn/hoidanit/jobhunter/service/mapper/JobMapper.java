package vn.hoidanit.jobhunter.service.mapper;

import org.springframework.stereotype.Component;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.job.JobCreateDto;
import vn.hoidanit.jobhunter.domain.response.job.JobUpdateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobMapper {

    public JobCreateDto toJobCreatedDto(Job job) {
        JobCreateDto dto = new JobCreateDto();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setQuantity(job.getQuantity());
        dto.setLevel(job.getLevel());
        dto.setDescription(job.getDescription());
        dto.setStartDate(job.getStartDate());
        dto.setEndDate(job.getEndDate());
        dto.setCreatedBy(job.getCreatedBy());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setActive(job.getActive());
        List<JobCreateDto.SkillDto> skills = new ArrayList<>();
        skills = job.getSkills().stream().map(skill -> {
            JobCreateDto.SkillDto skillDto = new JobCreateDto.SkillDto();
            skillDto.setName(skill.getName());
            return skillDto;
        }).collect(Collectors.toList());
        dto.setSkills(skills);
        return dto;
    }

    public JobUpdateDto toJobUpdateDto(Job job) {
        JobUpdateDto dto = new JobUpdateDto();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setQuantity(job.getQuantity());
        dto.setLevel(job.getLevel());
        dto.setDescription(job.getDescription());
        dto.setStartDate(job.getStartDate());
        dto.setEndDate(job.getEndDate());
        dto.setUpdatedBy(job.getUpdatedBy());
        dto.setUpdatedAt(job.getUpdatedAt());
        dto.setActive(job.getActive());
        List<JobCreateDto.SkillDto> skills = new ArrayList<>();
        skills = job.getSkills().stream().map(skill -> {
            JobCreateDto.SkillDto skillDto = new JobCreateDto.SkillDto();
            skillDto.setName(skill.getName());
            return skillDto;
        }).collect(Collectors.toList());
        dto.setSkills(skills);
        return dto;
    }
}
