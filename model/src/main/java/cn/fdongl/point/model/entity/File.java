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
@EntityListeners(AuditingEntityListener.class)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    String filename;

    @Column(length = 50)
    String type;

    @CreatedDate
    Date createDate;

    @CreatedBy
    Long createBy;

    @LastModifiedDate
    Date modifyDate;

    @LastModifiedBy
    Long modifiedBy;
}
