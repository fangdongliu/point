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
@Table(name = "`index`")
@EntityListeners(AuditingEntityListener.class)
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 指标点要求序号 (4.1/4.2)
     */
    private Long indexNumber;

    /**
     * 指标点大项标题
     */
    private Long indexTitle;

    /**
     * 指标要求内容
     */
    private String indexContent;

    private String parent;

    private String department;

    @CreatedDate
    Date createDate;

    @CreatedBy
    Long createBy;

    @LastModifiedDate
    Date modifyDate;

    @LastModifiedBy
    Long modifiedBy;
}
