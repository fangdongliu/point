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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"courseNumber","indexId","courseSemester"}))
@EntityListeners(AuditingEntityListener.class)
public class MapCourseIndexEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 课程编号
     */
    @Column(length = 30,nullable = false)
    private String courseNumber;

    @Column(nullable = false)
    private Long indexId;

    @Column(nullable = false)
    private Long courseSemester;

    private Double maxValue;

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
