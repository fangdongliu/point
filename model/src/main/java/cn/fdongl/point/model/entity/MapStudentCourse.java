package cn.fdongl.point.model.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userWorkId", "courseNumber", "courseSemester"}))
@EntityListeners(AuditingEntityListener.class)
public class MapStudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生work_id from sys_user 表
     */
    @Column(length = 100, nullable = false)
    private String userWorkId;

    /**
     * 总成绩
     */
    private String totalGrade;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程编号
     */
    @Column(nullable = false,length = 30)
    private String courseNumber;

    /**
     * 课程选课课号 - from map_teacher_course 表
     * (学期-课程编号-教师工号-该师该学期第几门课)
     */
    private String courseSelectNumber;

    /**
     * 开课学期(2017-2018-1)
     */
    @Column(nullable = false)
    private Long courseSemester;

    /**
     * 课程性质
     */
    private String courseNature;

    /**
     * 课程属性
     */
    private String courseProperty;

    /**
     * 课程归属
     */
    private String courseAscription;

    /**
     * 课程种类
     */
    private String courseKind;

    /**
     * 课程学时
     */
    private Double courseHour;

    /**
     * 课程学分
     */
    private Double courseCredit;

    /**
     * 开课单位
     */
    private String courseDepartment;

    /**
     * 成绩录入人名称
     */
    private String inputUserName;

    /**
     * 成绩标志
     */
    private String gradeSign;

    /**
     * 考试性质
     */
    private String examNature;

    /**
     * 补重学期
     */
    private String supplementRepeatSemester;

    @CreatedDate
    Date createDate;

    @CreatedBy
    Long createBy;

    @LastModifiedDate
    Date modifyDate;

    @LastModifiedBy
    Long modifiedBy;
}
