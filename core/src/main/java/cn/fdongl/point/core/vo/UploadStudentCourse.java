package cn.fdongl.point.core.vo;

import cn.fdongl.point.model.entity.MapStudentCourse;
import cn.fdongl.point.model.entity.User;
import lombok.Data;

@Data
public class UploadStudentCourse {

    public String userWorkId;
    public String realName;
    public String transient1;
    public String semester;
    public String department;
    public String transient2;
    public String classNumber;
    public String courseNumber;
    public String courseName;
    public String totalGrade;
    public String transient3;
    public String courseNature;
    public String courseProperty;
    public String courseAscription;
    public String courseKind;
    public String courseHour;
    public Double courseCredit;

    public User toUser(){
        User user = new User();
        user.setWorkId(userWorkId);
        user.setUserType("student");
        user.setUserDepartment(department);
        user.setClassName(classNumber);
        user.setRealName(realName);
        return user;
    }

    public static MapStudentCourse toMapStudentCourse(UploadStudentCourse input){
        MapStudentCourse mapStudentCourse = new MapStudentCourse();
        mapStudentCourse.setUserWorkId(input.userWorkId);
       mapStudentCourse.setCourseSemester(Long.valueOf(input.semester.substring(0,4)));
       mapStudentCourse.setCourseDepartment(input.department);
       mapStudentCourse.setCourseNumber(input.courseNumber);
       mapStudentCourse.setCourseName(input.courseName);
       mapStudentCourse.setTotalGrade(input.totalGrade);
       mapStudentCourse.setCourseNature(input.courseNature);
       mapStudentCourse.setCourseProperty(input.courseProperty);
       mapStudentCourse.setCourseAscription(input.courseAscription);
       mapStudentCourse.setCourseKind(input.courseKind);
       mapStudentCourse.setCourseHour(Double.valueOf(input.courseHour.substring(0,input.courseHour.length() - 4)));
       mapStudentCourse.setCourseCredit(input.courseCredit);
       return mapStudentCourse;
    }

}
