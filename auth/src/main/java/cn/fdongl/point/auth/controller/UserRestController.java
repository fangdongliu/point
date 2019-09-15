package cn.fdongl.point.auth.controller;

import cn.fdongl.point.auth.repository.UserRepository;
import cn.fdongl.point.auth.util.JwtTokenUtil;
import cn.fdongl.point.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取用户认证信息
     *
     * @author zm
     * @param1 request
     * @return cn.fdongl.authority.vo.JwtUser        
     * @date 2019/9/7 13:30
     **/
    @GetMapping(value = "user")
    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findFirstByWorkId(username);
    }
}