package vn.hoidanit.jobhunter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import vn.hoidanit.jobhunter.util.error.NotFoundException;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (StringUtils.hasText(email)) {
            User user = this.userService.handleGetUserByUserName(email);
            if (user == null) {
                throw new IdInvalidException("User not found!");
            }

            Role role = user.getRole();
            if (role == null) {
                throw new IdInvalidException("You don't have permission to access this resource!");
            }

            List<Permission> permissions = role.getPermissions();
            boolean isAllow = permissions.stream()
                    .anyMatch(permission ->
                            permission.getApiPath().equals(path) &&
                                    permission.getMethod().equalsIgnoreCase(httpMethod)
                    );

            if (!isAllow) {
                throw new IdInvalidException("You don't have permission to access this resource!");
            }
        }




        return true;
    }
}