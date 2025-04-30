package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final SecurityUtil securityUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuidler;
    private final UserService userService;


    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuidler
            , SecurityUtil securityUtil
            , UserService userService) {
        this.authenticationManagerBuidler = authenticationManagerBuidler;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );
        //xác thực người dùng cần viết hàm loadUserByName()
        Authentication authentication = this.authenticationManagerBuidler.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        User currentUserLogin = this.userService.handleGetUserByUserName(loginDTO.getUsername());
        if(currentUserLogin != null) {
            userLogin.setId(currentUserLogin.getId());
            userLogin.setUserName(currentUserLogin.getName());
            userLogin.setEmail(currentUserLogin.getEmail());
            resLoginDTO.setUserLogin(userLogin);
        }

        //Create token
        String accessToken = this.securityUtil.createAccessToken(authentication, resLoginDTO.getUserLogin());
        resLoginDTO.setAccessToken(accessToken);

        //Create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(), resLoginDTO);

        //Update user refresh token
        this.userService.updateRefreshToken(refreshToken, loginDTO.getUsername());

        //send cookie
        ResponseCookie springCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(resLoginDTO);
    }

    //get account
    @GetMapping("/auth/account")
    @ApiMessage("Fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        ResLoginDTO.UserLogin userLogin = this.userService.getUserLogin();
        return ResponseEntity.ok().body(userLogin);
    }
}
