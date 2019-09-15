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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"teacherWorkId","courseNumber","courseSemester"}))
@EntityListeners(AuditingEntityListener.class)
public class MapTeacherCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 教师work_id - from sys_user 表
     */
    @Column(nullable = false,length = 100)
    private String teacherWorkId;

    /**
     * 教师姓名
     */
    private String teacherRealName;

    /**
     * 课程id - from sys_course 表
     */
    private String courseId;

    /**
     * 课程编号
     */
    @Column(length = 30,nullable = false)
    private String courseNumber;

    /**
     * 选课课号(学期-课程编号-教师工号-该师该学期第几门课)
     */
    private String courseSelectNumber;

    /**
     * 课程学期
     */
    @Column(nullable = false)
    private Long courseSemester;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程学分
     */
    private Double courseCredit;

    /**
     * 开课校区
     */
    private String courseCampus;

    /**
     * 功能区(上课地点)
     */
    private String courseArea;

    /**
     * 课程属性
     */
    private String courseProperty;

    /**
     * 课程考试方式
     */
    private String courseTestMethod;

    /**
     * 讲课班级名称
     */
    private String courseClass;

    /**
     * 选课人数
     */
    private Integer courseElectNumber;

    /**
     * 排课人数
     */
    private Integer courseArrangeNumber;

    /**
     * 讲课周次
     */
    private String courseTeacheWeek;

    /**
     * 课程周学时
     */
    private Integer weekLen;

    /**
     * 安排学时
     */
    private Integer planLen;

    /**
     * 课程讲课学时
     */
    private Integer teachLen;

    /**
     * 课程实践学时
     */
    private Integer practiceLen;

    @CreatedDate
    Date createDate;

    @CreatedBy
    Long createBy;

    @LastModifiedDate
    Date modifyDate;

    @LastModifiedBy
    Long modifiedBy;

}
