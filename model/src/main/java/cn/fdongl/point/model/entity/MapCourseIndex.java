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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"courseNumber","indexId"}))
@EntityListeners(AuditingEntityListener.class)
public class MapCourseIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 课程编号
     */
    @Column(length = 30,nullable = false)
    private String courseNumber;

    /**
     * 指标要求id
     */
    private Long indexId;

    private String title;

    /**
     * 达成目标值，指标系数之和=1
     */
    private Double proportionValue;

    /**
     * 学生年级(e.g. 2016级)
     */
    @Column(nullable = false)
    private Long courseSemester;

    /**
     * 统计年份(示例：2014代表2014-2015学年)
     */
    private String statisticYear;

    @Column(columnDefinition = "double(10,2) default 0.00")
    private Double evaluation;

    private Integer evaluateSum;

    private Integer evaluateCount;

    @CreatedDate
    Date createDate;

    @CreatedBy
    Long createBy;

    @LastModifiedDate
    Date modifyDate;

    @LastModifiedBy
    Long modifiedBy;

}
