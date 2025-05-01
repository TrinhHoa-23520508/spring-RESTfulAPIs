package vn.hoidanit.jobhunter.controller;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.JobCreateDto;
import vn.hoidanit.jobhunter.domain.response.job.JobUpdateDto;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @ApiMessage("Create a job")
    public ResponseEntity<JobCreateDto> createJob(@Valid @RequestBody Job newJob) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.handleCreateJob(newJob));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update job")
    public ResponseEntity<JobUpdateDto> updateJob(@PathVariable long id, @Valid @RequestBody Job newJob) {
        newJob.setId(id);
        return ResponseEntity.ok(jobService.handleUpdateJob(newJob));
    }

    @GetMapping("/{id}")
    @ApiMessage("Get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable long id) {
        return ResponseEntity.ok(jobService.handleGetJobById(id));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete job")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) {
        jobService.handleDeleteJobById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiMessage("Fetch jobs")
    public ResponseEntity<ResultPaginationDTO<List<Job>>> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok(jobService.handleGetAllJobs(spec, pageable));
    }
}
