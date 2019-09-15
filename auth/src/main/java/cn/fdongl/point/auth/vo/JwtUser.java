package cn.fdongl.point.auth.vo;

import cn.fdongl.point.model.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;



@AllArgsConstructor
@Data
public class JwtUser implements UserDetails {

    public static JwtUser fromUser(User user){
        return new JwtUser(
                user.getId(),
                user.getWorkId(),
                user.getUserPwd(),
                user.getRealName(),
                user.getWorkId(),
                user.getUserType(),
                user.getUserDepartment(),
                user.getClassName(),
                user.getStartYear(),
                user.getEducationSystem(),
                user.getTrainLevel(),
                user.getUserTitle(),
                new ArrayList<>());
    }

    private Long id;
    private String userName;
    private String userPwd;
    private String realName;
    private String workId;
    private String userType;
    private String userDepartment;
    private String className;
    private String startYear;
    private Integer educationSystem;
    private String trainLevel;
    private String userTitle;

    private final Collection<? extends GrantedAuthority> authorities;

    //返回分配给用户的角色列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return userPwd;
    }

    @Override
    public String getUsername() {
        return userName;
    }
    // 账户是否未过期
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 账户是否未锁定
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 密码是否未过期
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 账户是否激活
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}

