package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.JobCreateDto;
import vn.hoidanit.jobhunter.domain.response.job.JobUpdateDto;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.mapper.JobMapper;
import vn.hoidanit.jobhunter.util.error.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final JobMapper jobMapper;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository,
                      SkillRepository skillRepository,
                      JobMapper jobMapper,
                      CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.jobMapper = jobMapper;
        this.companyRepository = companyRepository;
    }

    // handle create job
    public JobCreateDto handleCreateJob(Job newJob) {
        if (newJob.getSkills() != null) {
            List<Long> skillIds = newJob.getSkills()
                    .stream()
                    .map(Skill::getId)
                    .collect(Collectors.toList());
            List<Skill> skills = skillRepository.findAllById(skillIds);
            newJob.setSkills(skills);
        }

        Job savedJob = jobRepository.save(newJob);
        return jobMapper.toJobCreatedDto(savedJob);
    }

    // handle update job
    public JobUpdateDto handleUpdateJob(Job updatedJob) {
        Job currentJob = jobRepository.findById(updatedJob.getId())
                .orElseThrow(() -> new NotFoundException("Job không tồn tại"));

        // Cập nhật các trường cơ bản
        currentJob.setName(updatedJob.getName());
        currentJob.setLocation(updatedJob.getLocation());
        currentJob.setSalary(updatedJob.getSalary());
        currentJob.setQuantity(updatedJob.getQuantity());
        currentJob.setLevel(updatedJob.getLevel());
        currentJob.setDescription(updatedJob.getDescription());
        currentJob.setStartDate(updatedJob.getStartDate());
        currentJob.setEndDate(updatedJob.getEndDate());
        currentJob.setActive(updatedJob.getActive());

        // Cập nhật company nếu có
        if (updatedJob.getCompany() != null) {
            Long companyId = updatedJob.getCompany().getId();
            Optional<Company> companyOpt = companyRepository.findById(companyId);
            currentJob.setCompany(companyOpt.orElse(null));
        } else {
            currentJob.setCompany(null);
        }

        // Cập nhật skill nếu có
        if (updatedJob.getSkills() != null && !updatedJob.getSkills().isEmpty()) {
            List<Long> skillIds = updatedJob.getSkills()
                    .stream()
                    .map(Skill::getId)
                    .collect(Collectors.toList());
            List<Skill> existingSkills = skillRepository.findAllById(skillIds);
            currentJob.setSkills(existingSkills);
        } else {
            currentJob.setSkills(null);
        }

        Job savedJob = jobRepository.save(currentJob);
        return jobMapper.toJobUpdateDto(savedJob);
    }

    // handle get job by id
    public Job handleGetJobById(long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job không tồn tại"));
    }

    // handle delete job
    public void handleDeleteJobById(long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job không tồn tại"));
        jobRepository.delete(job);
    }

    // handle get all jobs with pagination
    public ResultPaginationDTO<List<Job>> handleGetAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> jobPage = jobRepository.findAll(spec, pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setTotal((int) jobPage.getTotalElements());
        meta.setPages(jobPage.getTotalPages());
        meta.setPageSize(jobPage.getSize());

        ResultPaginationDTO<List<Job>> result = new ResultPaginationDTO<>();
        result.setMeta(meta);
        result.setResult(jobPage.getContent());

        return result;
    }

    public boolean checkExistJobById(long id) {
        return jobRepository.existsById(id);
    }
}
