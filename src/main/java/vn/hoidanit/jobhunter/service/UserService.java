package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User handleCreateUser(User user) {
        // encode
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Pageable pageable) {
        Page<User> users = this.userRepository.findAll(pageable);
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(users.getNumber()+1);
        meta.setTotal(users.getTotalElements());
        meta.setPages(users.getTotalPages());
        meta.setPageSize(users.getSize());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(users.getContent());

        return paginationDTO;

    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setEmail(reqUser.getEmail());
            currentUser.setName(reqUser.getName());
            currentUser.setPassword(this.passwordEncoder.encode(reqUser.getPassword()));
            // update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }
    public User handleGetUserByUserName(String username){
        return this.userRepository.findByEmail(username);
    }

}
