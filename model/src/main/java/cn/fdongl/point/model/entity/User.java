package cn.fdongl.point.model.entity;

import lombok.Data;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(unique = true,nullable = false,length = 100)
    private String workId;

    /**
     * 用户密码
     */
    @Column(nullable = false,length = 100)
    private String userPwd;

    /**
     * 真实姓名
     */
    @Column(length = 50)
    private String realName;

    /**
     * 用户类型/账号类型
     */
    @Column(length = 50)
    private String userType;

    /**
     * 所属部门/学院
     */
    @Column(length = 100)
    private String userDepartment;

    /**
     * 班级名称
     */
    @Column(length = 20)
    private String className;

    /**
     * 入学年份
     */
    private String startYear;

    /**
     * 学生学制
     */
    @Column(columnDefinition = "int default 4")
    private Integer educationSystem;

    /**
     * 培养层次
     */
    @Column(length = 100)
    private String trainLevel;

    /**
     * 教师职称
     */
    @Column(length = 100)
    private String userTitle;

    @CreatedDate
    Date createDate;

    @CreatedBy
    Long createBy;

    @LastModifiedDate
    Date modifyDate;

    @LastModifiedBy
    Long modifiedBy;
}
