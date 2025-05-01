package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create new skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleUpdateSkill(skill));
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("Get skill by id")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleGetSkillById(id));
    }

    @GetMapping("/skills")
    @ApiMessage("Get skills")
    public ResponseEntity<ResultPaginationDTO<List<Skill>>> getSkills(
            @Filter Specification<Skill> spec,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkill(spec, pageable));

    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) {
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
