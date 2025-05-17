package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("create new permission")
    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission permission) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createPermission(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission){
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.updatePermission(permission));
    }

    @GetMapping("/permissions")
    @ApiMessage("Get permission with pagination")
    public ResponseEntity<ResultPaginationDTO<List<Permission>>> getPermission(
            @Filter Specification<Permission> spec,
            Pageable pageable
            ){
        return ResponseEntity.ok().body(this.permissionService.getPermissions(spec,pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable long id){
        this.permissionService.deletePermission(id);
        return ResponseEntity.ok().body(null);
    }
}
