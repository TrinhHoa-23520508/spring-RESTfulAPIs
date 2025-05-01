package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.DuplicateResourceException;
import vn.hoidanit.jobhunter.util.error.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    //create skill
    public Skill handleCreateSkill(Skill skill) {
        Skill skillDB = this.skillRepository.findByName(skill.getName());
        if(skillDB != null) {
            throw new DuplicateResourceException("Skill with name " + skill.getName() + " already exists");
        }
        return skillRepository.save(skill);
    }

    //update skill
    public Skill handleUpdateSkill(Skill newSkill) {
        Skill skillDB = this.skillRepository.findByName(newSkill.getName());
        if(skillDB != null&&skillDB.getId() != newSkill.getId()) {
            throw new DuplicateResourceException("Skill with name " + newSkill.getName() + " already exists");
        }
        Optional<Skill> skillOptional = this.skillRepository.findById(newSkill.getId());
        if(skillOptional.isPresent()) {
            Skill currentSkill = skillOptional.get();
            currentSkill.setName(newSkill.getName());
            return skillRepository.save(currentSkill);
        }
        else{
            throw new NotFoundException("Skill with id " + newSkill.getId() + " does not exist");
        }
    }

    //get skill by id
    public Skill handleGetSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if(skillOptional.isPresent()) {
            return skillOptional.get();
        }
        else{
            throw new NotFoundException("Skill with id " + id + " does not exist");
        }
    }

    //get all skill
    public ResultPaginationDTO<List<Skill>> fetchAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> skills = this.skillRepository.findAll(spec, pageable);

        ResultPaginationDTO<List<Skill>> paginationDTO = new ResultPaginationDTO<>();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setTotal((int) skills.getTotalElements());
        meta.setPages(skills.getTotalPages());
        meta.setPageSize(skills.getSize());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(
                skills.getContent()

        );

        return paginationDTO;
    }

    //delete skill
    public void handleDeleteSkill(long id){
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if(!skillOptional.isPresent()) {
            throw new NotFoundException("Skill with id " + id + " does not exist");
        }
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        this.skillRepository.delete(currentSkill);
    }
}
