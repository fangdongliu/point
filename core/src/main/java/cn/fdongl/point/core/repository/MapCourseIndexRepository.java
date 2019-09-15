package cn.fdongl.point.core.repository;

import cn.fdongl.point.model.entity.MapCourseIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapCourseIndexRepository extends JpaRepository<MapCourseIndex,Long> {

    List<MapCourseIndex>findByCourseNumber(String courseNumber);

    List<MapCourseIndex>findByCourseNumberIn(Iterable<String>courses);

}
