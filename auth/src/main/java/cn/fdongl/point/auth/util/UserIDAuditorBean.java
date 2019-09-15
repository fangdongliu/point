package cn.fdongl.point.auth.util;

import cn.fdongl.point.auth.vo.JwtUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class UserIDAuditorBean implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx == null) {
            return Optional.empty();
        }
        if (ctx.getAuthentication() == null) {
            return Optional.empty();
        }
        if (ctx.getAuthentication().getPrincipal() == null) {
            return Optional.empty();
        }
        Object principal = ctx.getAuthentication().getPrincipal();
        if (principal.getClass().isAssignableFrom(JwtUser.class)) {
            return Optional.of(((JwtUser)principal).getId());
        } else {
            return Optional.empty();
        }
    }
}