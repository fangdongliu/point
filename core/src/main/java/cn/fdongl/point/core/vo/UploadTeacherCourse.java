package cn.fdongl.point.core.vo;

import cn.fdongl.point.model.entity.MapTeacherCourse;
import lombok.Data;

@Data
public class UploadTeacherCourse {

    public String selectNumber;
    public String part;
    public String courseName;
    public Double score;
    public String teacherName;
    public String transient1;
    public String property;
    public String examtype;
    public Long peopleCount;
    public Long paiCount;
    public String transient2;
    public String transient3;
    public String transient4;
    public String transient5;
    public String transient6;
    public String transient7;
    public String transient8;
    public String transient9;
    public String transient10;
    public String courseNumber;

    public static MapTeacherCourse toCourse(UploadTeacherCourse input){
        MapTeacherCourse mapTeacherCourse = new MapTeacherCourse();
        mapTeacherCourse.setCourseNumber(input.courseNumber);
        mapTeacherCourse.setCourseProperty(input.property);
        mapTeacherCourse.setCourseName(input.courseName);
        mapTeacherCourse.setTeacherRealName(input.teacherName);
        mapTeacherCourse.setCourseSemester(Long.valueOf(input.selectNumber.substring(1,5)));
        mapTeacherCourse.setCourseCredit(input.score);
        return mapTeacherCourse;
    }

}
