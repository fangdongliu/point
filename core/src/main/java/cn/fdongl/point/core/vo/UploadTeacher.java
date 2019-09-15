package cn.fdongl.point.core.vo;

import cn.fdongl.point.model.entity.User;
import lombok.Data;

import javax.persistence.Column;

@Data
public class UploadTeacher {

    public String workId;
    public String account;
    public String realName;
    public String userDepartment;
    public String userType;
    public String status;
    public String startYear;
    public String endYear;
    public Long holds;
    public Long score;
    public Long totalScore;

    public static User toUser(UploadTeacher uploadTeacher){
        User teacher = new User();
        teacher.setWorkId(uploadTeacher.getWorkId());
        teacher.setRealName(uploadTeacher.getRealName());
        teacher.setUserDepartment(uploadTeacher.getUserDepartment());
        teacher.setUserType("teacher");
        teacher.setStartYear(teacher.getStartYear());
        return teacher;
    }

}
