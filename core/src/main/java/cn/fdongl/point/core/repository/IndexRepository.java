package cn.fdongl.point.core.repository;

import cn.fdongl.point.model.entity.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<Index,Long> {
}
