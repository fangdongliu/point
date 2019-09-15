package cn.fdongl.point.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Statuz {
    @Column(length = 50)
    @Id
    String id;

    Integer val;
}
