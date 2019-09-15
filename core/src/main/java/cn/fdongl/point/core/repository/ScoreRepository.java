package cn.fdongl.point.core.repository;

import cn.fdongl.point.model.entity.MapCourseIndexEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<MapCourseIndexEvaluation,Long> {
    List<MapCourseIndexEvaluation>findByCourseSemesterBetween(Long start,Long end);
}
