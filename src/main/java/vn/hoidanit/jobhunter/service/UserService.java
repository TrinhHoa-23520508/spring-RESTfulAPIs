package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.*;
import vn.hoidanit.jobhunter.domain.response.user.UserCreateDto;
import vn.hoidanit.jobhunter.domain.response.user.UserResponseDto;
import vn.hoidanit.jobhunter.domain.response.user.UserUpdateDto;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.service.mapper.UserMapper;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.DuplicateResourceException;
import vn.hoidanit.jobhunter.util.error.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CompanyRepository companyRepository;
    private final RoleService roleService;


    public UserService(UserRepository userRepository
            , PasswordEncoder passwordEncoder
            , UserMapper userMapper
            , CompanyRepository companyRepository
    , RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.companyRepository = companyRepository;
        this.roleService = roleService;
    }

    public UserCreateDto handleCreateUser(User user) {

        UserCreateDto userCreateDto = new UserCreateDto();
        if(user.getCompany() != null){
            long id =user.getCompany().getId();
            Optional<Company> companyDB = this.companyRepository.findById(id);

            if(companyDB.isPresent()){
                user.setCompany(companyDB.get());
            }
            else{
                throw new NotFoundException("Company not found");
            }

        }
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email "+user.getEmail()+" đã tồn tại!");
        }

       Optional<Role> roleOptional = this.roleService.getRoleById(user.getRole().getId());
        user.setRole(roleOptional.isPresent()?roleOptional.get():null);

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        User savedUser = this.userRepository.save(user);

        return this.userMapper.toUserCreateDto(savedUser);
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

    public ResultPaginationDTO<List<UserResponseDto>> fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO<List<UserResponseDto>> paginationDTO = new ResultPaginationDTO<>();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setTotal((int) users.getTotalElements());
        meta.setPages(users.getTotalPages());
        meta.setPageSize(users.getSize());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(
                users.getContent()
                        .stream()
                        .map(u -> this.userMapper.toUserResponseDto(u))
                        .collect(Collectors.toList())
        );

        return paginationDTO;
    }


    public UserUpdateDto handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setName(reqUser.getName());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAge(reqUser.getAge());
            currentUser.setAddress(reqUser.getAddress());
            if(reqUser.getCompany() != null){
                Optional<Company> companyOptional = this.companyRepository.findById(reqUser.getCompany().getId());
                if(companyOptional.isPresent()){
                    currentUser.setCompany(companyOptional.get());
                }
                else{
                    currentUser.setCompany(null);
                }
            }
            if(reqUser.getRole() != null){
                Optional<Role> roleOptional = this.roleService.getRoleById(reqUser.getRole().getId());
                if(roleOptional.isPresent()){
                    currentUser.setRole(roleOptional.get());
                }
                else{
                    currentUser.setRole(null);
                }
            }
            // update
            currentUser = this.userRepository.save(currentUser);
        }
        else {
            throw new NotFoundException("User not found!");
        }
        return this.userMapper.toUserUpdateDto(currentUser);
    }
    public User handleGetUserByUserName(String username){
        return this.userRepository.findByEmail(username);
    }

    public UserResponseDto handleGetUserById(Long id) {
        User user = this.fetchUserById(id);
        if(user == null){
            throw new NotFoundException("User has id = "+id+" not found!");
        }
        return this.userMapper.toUserResponseDto(user);
    }

    public void updateRefreshToken(String refreshToken, String email) {
        User currentUser = this.handleGetUserByUserName(email);
        if(currentUser != null){
            currentUser.setRefreshToken(refreshToken);
            this.userRepository.save(currentUser);
        }
    }

    public ResLoginDTO.UserGetAccount getUserLogin() {
        String email = SecurityUtil.getCurrentUserLogin()
                                    .isPresent()?
                        SecurityUtil.getCurrentUserLogin().get()
                                    :"";

        User user = this.handleGetUserByUserName(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        userLogin.setEmail(email);
        userLogin.setUserName(user.getName());
        userLogin.setId(user.getId());
        userLogin.setRole(user.getRole());

        return new ResLoginDTO.UserGetAccount(userLogin);
    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        User user = this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
        return user;
    }

    public void handleLogout(String email){
        User user = this.userRepository.findByEmail(email);
        user.setRefreshToken(null);
        return;

    }

    public boolean checkExistUserById(long id) {
        return this.userRepository.existsById(id);
    }

}
