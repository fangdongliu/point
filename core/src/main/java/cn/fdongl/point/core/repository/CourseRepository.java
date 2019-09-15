package cn.fdongl.point.core.repository;

import cn.fdongl.point.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    List<Course>findByCourseSemesterBetween(Long start,Long end);

    List<Course>findByCourseRoute(Long route);

}
