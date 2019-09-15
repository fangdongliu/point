package cn.fdongl.point.core.repository;

import cn.fdongl.point.model.entity.Course;
import cn.fdongl.point.model.entity.MapTeacherCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapTeacherCourseRepository extends JpaRepository<MapTeacherCourse,Long> {

    Page<MapTeacherCourse> findByTeacherWorkIdAndCourseSemester(String teacherWorkId,Long courseSemester, Pageable pageable);

    @Query(value = " select distinct(course_semester) " +
            "from map_teacher_course " +
            "where teacher_work_id=:workId " +
            "order by course_semester desc",nativeQuery = true)
    List<Long> getDistinctCourseSemesterByUserId(@Param("workId")String workId);


}
