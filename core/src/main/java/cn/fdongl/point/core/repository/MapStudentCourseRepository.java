package cn.fdongl.point.core.repository;

import cn.fdongl.point.model.entity.MapStudentCourse;
import cn.fdongl.point.model.entity.MapTeacherCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface MapStudentCourseRepository extends JpaRepository<MapStudentCourse,Long> {

    Page<MapStudentCourse>findByUserWorkIdAndCourseSemester(String user, Long courseSemester, Pageable pageable);

    @Query(value = "select distinct(course_semester)\n" +
            "from map_student_course\n" +
            "where user_work_id=:workId\n" +
            "order by course_semester desc",nativeQuery = true)
    List<Long> getDistinctCourseSemesterByUserId(@Param("workId")String workId);


}
