package vn.hoidanit.jobhunter.service.mapper;

import org.springframework.stereotype.Component;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.resume.ResumeResponseDto;

@Component
public class ResumeMapper {
    public ResumeResponseDto toResumeResponseDto(Resume resume) {
        ResumeResponseDto resumeResponseDto = new ResumeResponseDto();
        resumeResponseDto.setId(resume.getId());
        resumeResponseDto.setEmail(resume.getEmail());
        resumeResponseDto.setUrl(resume.getUrl());
        resumeResponseDto.setStatus(resume.getStatus());
        resumeResponseDto.setCreatedAt(resume.getCreatedAt());
        resumeResponseDto.setUpdatedAt(resume.getUpdatedAt());
        resumeResponseDto.setCreatedBy(resume.getCreatedBy());
        resumeResponseDto.setUpdatedBy(resume.getUpdatedBy());
        // Map User
        if (resume.getUser() != null) {
            ResumeResponseDto.UserDto userDto = resumeResponseDto.new UserDto(
                    resume.getUser().getId(),
                    resume.getUser().getName()
            );
            resumeResponseDto.setUser(userDto);
        }

        // Map Job
        if (resume.getJob() != null) {
            ResumeResponseDto.JobDto jobDto = resumeResponseDto.new JobDto(
                    resume.getJob().getId(),
                    resume.getJob().getName()
            );
            resumeResponseDto.setJob(jobDto);
        }

        return resumeResponseDto;
    }
}
