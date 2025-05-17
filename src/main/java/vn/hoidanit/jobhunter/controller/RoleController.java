package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.boot.actuate.endpoint.annotation.FilteredEndpoint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create new role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.updateRole(role));
    }

    @GetMapping("/roles")
    @ApiMessage("Get roles with pagination")
    public ResponseEntity<ResultPaginationDTO<List<Role>>> getAllRole(
            @Filter Specification<Role> spec,
            Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getRoles(
                spec,
                pageable
        ));

    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable long id){
        this.roleService.deleteRole(id);
        return ResponseEntity.ok().body(null);
    }
}
