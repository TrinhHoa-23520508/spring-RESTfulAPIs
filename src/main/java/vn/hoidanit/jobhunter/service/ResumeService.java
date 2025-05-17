package vn.hoidanit.jobhunter.service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResumeCreateDto;
import vn.hoidanit.jobhunter.domain.response.resume.ResumeResponseDto;
import vn.hoidanit.jobhunter.domain.response.resume.ResumeUpdateDto;
import vn.hoidanit.jobhunter.domain.response.user.UserResponseDto;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.service.mapper.ResumeMapper;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    @Autowired
    FilterBuilder filterBuilder;

    @Autowired
    FilterSpecificationConverter filterSpecificationConverter;

    @Autowired
    FilterParser filterParser;

    private final ResumeRepository resumeRepository;
    private final JobService jobService;
    private final UserService userService;
    private final ResumeMapper resumeMapper;

    public ResumeService(ResumeRepository resumeRepository, JobService jobService, UserService userService, ResumeMapper resumeMapper) {
        this.resumeRepository = resumeRepository;
        this.jobService = jobService;
        this.userService = userService;
        this.resumeMapper = resumeMapper;
    }

    public ResumeCreateDto handleCreateResume(Resume resume) throws IdInvalidException {
        if(!this.userService.checkExistUserById(resume.getUser().getId())) {
            throw new IdInvalidException("user không tồn tại!");
        }
        if(!this.jobService.checkExistJobById(resume.getJob().getId())) {
            throw new IdInvalidException("Job không tồn tại!");
        }

        Resume currentResume = this.resumeRepository.save(resume);
        ResumeCreateDto createDTO = new ResumeCreateDto();
        createDTO.setId(currentResume.getId());
        createDTO.setCreatedAt(currentResume.getCreatedAt());
        createDTO.setCreatedBy(currentResume.getCreatedBy());
        return createDTO;
    }

    public ResumeResponseDto handelGetResumeById(long id) throws IdInvalidException {
        Resume resume = this.resumeRepository.findById(id).orElse(null);
        if(resume == null) {
            throw new IdInvalidException("Resume không tồn tại");
        }
        ResumeResponseDto responseDto = this.resumeMapper.toResumeResponseDto(resume);

        return responseDto;

    }

    //check valid resume by id
    public boolean checkExistResumeById(long id) {
        return this.resumeRepository.existsById(id);
    }

    public ResumeUpdateDto handleUpdateResume(Resume resume) throws IdInvalidException {
        Resume resumeDB = this.resumeRepository.findById(resume.getId()).orElse(null);
        if(resumeDB == null) {
            throw new IdInvalidException("resume không tồn tại");
        }
        resumeDB.setStatus(resume.getStatus());
        Resume currentResume = this.resumeRepository.save(resumeDB);
        ResumeUpdateDto updateDTO = new ResumeUpdateDto();
        updateDTO.setUpdateAt(currentResume.getUpdatedAt());
        updateDTO.setUpdateBy(currentResume.getUpdatedBy());
        return updateDTO;

    }

    public void deleteResumeById(long id) throws IdInvalidException {
        if(!this.checkExistResumeById(id)) {
            throw new IdInvalidException("resume không tồn tại");
        }
        this.resumeRepository.deleteById(id);
    }

    public ResultPaginationDTO<List<ResumeResponseDto>> fetchResumeWithPagination(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumePage = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO<List<ResumeResponseDto>> paginationDTO = new ResultPaginationDTO<>();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setTotal((int) resumePage.getTotalElements());
        meta.setPages(resumePage.getTotalPages());
        meta.setPageSize(resumePage.getSize());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(
                resumePage.getContent()
                        .stream()
                        .map(resume -> this.resumeMapper.toResumeResponseDto(resume))
                        .collect(Collectors.toList())
                        );
        return paginationDTO;

    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResumeResponseDto> listResume = pageResume.getContent()
                .stream().map(item -> this.resumeMapper.toResumeResponseDto(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }
}
