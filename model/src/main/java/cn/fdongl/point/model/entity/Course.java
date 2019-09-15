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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"courseNumber", "courseSemester"})})
@EntityListeners(AuditingEntityListener.class)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 课程名称
     */
    @Column(length = 100)
    private String courseName;

    /**
     * 课程学分
     */
    @Column(columnDefinition = "double(10,2) default '0.00'")
    private Double courseCredit;

    /**
     * 课程代码/编码
     */
    @Column(length = 30,nullable = false)
    private String courseNumber;

    /**
     * 开课学年学期(示例：2018-2019-1)
     */
    @Column(nullable = false)
    private Long courseSemester;

    /**
     * 课程开课单位/院系/部门
     */
    @Column(length = 100)
    private String courseDepartment;

    /**
     * 课程所在路线
     */
    @Column(length = 100)
    private Long courseRoute;

    /**
     * 课程性质/属性
     */
    @Column(length = 100)
    private String courseCharacter;

    /**
     * 课程体系/类别
     */
    @Column(length = 50)
    private String courseType;

    /**
     * 课程种类(文学与艺术)
     */
    @Column(length = 50)
    private String courseKind;

    /**
     * 课程归属(文化素质通识课)
     */
    @Column(length = 50)
    private String courseAttribution;

    /**
     * 课程考核方式
     */
    @Column(length = 20)
    private String assessMethod;

    /**
     * 培养环节类别标识
     */
    @Column(length = 100)
    private String typeIdentification;

    /**
     * 模块与层次标识
     */
    @Column(length = 100)
    private String moduleIdentification;

    /**
     * 是否可用高层次课程代替代课程(-1不可，0可)
     */
    private Integer isSubstitute;

    /**
     * 课程总学时
     */
    private Double totalLen;

    /**
     * 课程学期学时(一般和课程总学时相同)
     */
    private Double semesterLen;

    /**
     * 课程上机/实验学时
     */
    private Double experimentLen;

    /**
     * 备注
     */
    private String remarks;

    @CreatedDate
    Date createDate;

    @CreatedBy
    Long createBy;

    @LastModifiedDate
    Date modifyDate;

    @LastModifiedBy
    Long modifiedBy;
}
