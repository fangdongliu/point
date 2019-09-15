package cn.fdongl.point.core.vo;

import cn.fdongl.point.model.entity.Course;
import lombok.Data;

@Data
public class UploadCourse {
    public String courseNumber;
    public String courseName;
    public Double totalLen;
    public Double semesterLen;
    public Double courseCredit;
    public String courseType;
    public String courseCharacter;
    public String assessMethod;
    public String courseDepartment;
    public String courseRoute;

    public static Course toCourse(UploadCourse uploadCourse){
        Course course = new Course();
        course.setCourseName(uploadCourse.getCourseName());
        course.setCourseCharacter(uploadCourse.getCourseCharacter());
        course.setCourseNumber(uploadCourse.getCourseNumber());
        course.setTotalLen(uploadCourse.getTotalLen());
        course.setSemesterLen(uploadCourse.getSemesterLen());
        course.setCourseCredit(uploadCourse.getCourseCredit());
        course.setCourseType(uploadCourse.getCourseType());
        course.setAssessMethod(uploadCourse.getAssessMethod());
        course.setCourseDepartment(uploadCourse.getCourseDepartment());
        course.setCourseRoute(Long.valueOf(uploadCourse.getCourseRoute().substring(0,4)));
        return course;
    }

}
