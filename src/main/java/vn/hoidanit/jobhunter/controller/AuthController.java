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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.response.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import vn.hoidanit.jobhunter.util.error.NotFoundException;

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
    @ApiMessage("Login account")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                reqLoginDTO.getUsername(),
                reqLoginDTO.getPassword()
        );
        //xác thực người dùng cần viết hàm loadUserByName()
        Authentication authentication = this.authenticationManagerBuidler.getObject().authenticate(authenticationToken);

        //nạp thông tin người đăng nhập vào security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        User currentUserLogin = this.userService.handleGetUserByUserName(reqLoginDTO.getUsername());
        if(currentUserLogin != null) {
            userLogin.setId(currentUserLogin.getId());
            userLogin.setUserName(currentUserLogin.getName());
            userLogin.setEmail(currentUserLogin.getEmail());
            resLoginDTO.setUser(userLogin);
        }

        //Create token
        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUser());
        resLoginDTO.setAccessToken(accessToken);

        //Create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(reqLoginDTO.getUsername(), resLoginDTO);

        //Update user refresh token
        this.userService.updateRefreshToken(refreshToken, reqLoginDTO.getUsername());

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
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        ResLoginDTO.UserGetAccount userLogin = this.userService.getUserLogin();
        return ResponseEntity.ok().body(userLogin);
    }

    //refresh access token by cookie
    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token", defaultValue = "abc") String refreshToken) {

        //check cookie has refresh token
        if(refreshToken.equals("abc")) {
            throw new NotFoundException("Bạn không có refresh token ở trong cookie");
        }
        //check valid
        Jwt decodeToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodeToken.getSubject();

        //check user by token + email
        User user = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if(user == null){
            throw new NotFoundException("Refresh token không hợp lệ ");
        }

        //create response DTO and create new access_token and refresh_token
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        User currentUserLogin = this.userService.handleGetUserByUserName(email);
        if(currentUserLogin != null) {
            userLogin.setId(currentUserLogin.getId());
            userLogin.setUserName(currentUserLogin.getName());
            userLogin.setEmail(currentUserLogin.getEmail());
            resLoginDTO.setUser(userLogin);
        }
        //Create token
        String accessToken = this.securityUtil.createAccessToken(email, resLoginDTO.getUser());
        resLoginDTO.setAccessToken(accessToken);

        //Create refresh token
        String newRefreshToken = this.securityUtil.createRefreshToken(email, resLoginDTO);

        //Update user refresh token
        this.userService.updateRefreshToken(newRefreshToken, email);

        //send cookie
        ResponseCookie springCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(resLoginDTO);

    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout account")
    public ResponseEntity<Void> logout() throws IdInvalidException{
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        if(email.equals(""))
        {
            throw new IdInvalidException("Token truyền lên không hợp lệ");
        }
        this.userService.handleLogout(email);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(null);
    }
}
