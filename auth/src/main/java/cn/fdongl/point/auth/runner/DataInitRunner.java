package cn.fdongl.point.auth.runner;

import cn.fdongl.point.auth.repository.UserRepository;
import cn.fdongl.point.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitRunner implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setWorkId("admin");
        user.setRealName("admin");
        user.setUserType("admin");
        user.setUserPwd(passwordEncoder.encode("123456"));
        try {
            userRepository.save(user);
        }catch (Exception e){
            System.out.println("重复的用户");
        }
    }
}
