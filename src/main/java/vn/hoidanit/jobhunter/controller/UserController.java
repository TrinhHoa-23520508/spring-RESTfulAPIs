package vn.hoidanit.jobhunter.controller;

import java.util.List;


import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.user.UserCreateDto;
import vn.hoidanit.jobhunter.domain.response.user.UserResponseDto;
import vn.hoidanit.jobhunter.domain.response.user.UserUpdateDto;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<UserCreateDto> createNewUser(@RequestBody User postManUser) {
        UserCreateDto ericUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ericUser);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id)
            throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("Id khong lon hon 1500");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") long id) {
        UserResponseDto fetchUser = this.userService.handleGetUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    // fetch all users
    @GetMapping("/users")
    @ApiMessage("fetch users")
    public ResponseEntity<ResultPaginationDTO<List<UserResponseDto>>> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable
            ) {

        ResultPaginationDTO<List<UserResponseDto>> users = this.userService.fetchAllUser(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<UserUpdateDto> updateUser(@RequestBody User user) {
        UserUpdateDto updateUser = this.userService.handleUpdateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

}
