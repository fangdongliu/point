package cn.fdongl.point.auth.service;

import cn.fdongl.point.auth.repository.UserRepository;
import cn.fdongl.point.auth.vo.JwtUser;
import cn.fdongl.point.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class JwtUserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByWorkId(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            System.out.println("类：JwtUserDetailServiceImpl，方法：loadUserByUsername，查询到的用户："+user);
            return JwtUser.fromUser(user);
        }
    }
}
