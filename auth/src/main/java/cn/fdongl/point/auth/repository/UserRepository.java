package cn.fdongl.point.auth.repository;

import cn.fdongl.point.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findFirstByWorkId(String workId);

    Page<User> findByWorkIdLikeOrRealNameLike(String workId, String realName, Pageable pageable);

}
