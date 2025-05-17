package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResumeCreateDto;
import vn.hoidanit.jobhunter.domain.response.resume.ResumeResponseDto;
import vn.hoidanit.jobhunter.domain.response.resume.ResumeUpdateDto;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;


    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResumeCreateDto> createResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreateResume(resume));
    }

    @GetMapping("resumes/{id}")
    @ApiMessage("Get a resume by id")
    public ResponseEntity<ResumeResponseDto> getResumeById(@PathVariable long id) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.handelGetResumeById(id));
    }

    @PutMapping("/resumes")
    @ApiMessage("Cập nhật Resume")
    public ResponseEntity<ResumeUpdateDto> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.handleUpdateResume(resume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable long id) throws IdInvalidException {
        this.resumeService.deleteResumeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch resume with pagination")
    public ResponseEntity<ResultPaginationDTO<List<ResumeResponseDto>>> getResumes(
            @Filter Specification<Resume> spec,
            Pageable pageable
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.fetchResumeWithPagination(spec,pageable));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
}
