package cn.fdongl.point.core.repository;

import cn.fdongl.point.model.entity.MapCourseIndex;
import cn.fdongl.point.model.entity.MapCourseIndexEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapCourseIndexEvaluationRepository extends JpaRepository<MapCourseIndexEvaluation,Long> {

    int deleteByCourseNumberAndCourseSemester(String courseNumber,Long courseSemester);

    List<MapCourseIndexEvaluation>findByCourseNumberAndCourseSemester(String courseNumber,Long courseSemester);

}
