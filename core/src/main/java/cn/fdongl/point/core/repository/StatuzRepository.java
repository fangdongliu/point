package cn.fdongl.point.core.repository;

import cn.fdongl.point.model.entity.Statuz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatuzRepository extends JpaRepository<Statuz,String>{

}
