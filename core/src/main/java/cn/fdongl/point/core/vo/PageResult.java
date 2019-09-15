package cn.fdongl.point.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult <T>{
    Long total;
    private List<T> resultList;
}
