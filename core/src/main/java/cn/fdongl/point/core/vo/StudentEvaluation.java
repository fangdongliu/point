package cn.fdongl.point.core.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SysEvaluation
 * @Description 评价类
 * @Author zm
 * @Date 2019/9/10 20:41
 * @Version 1.0
 **/
@Data
public class StudentEvaluation {

    private String courseNumber;

    private Long courseSemester;

    /**
     * 学生评价：
     * indexId 指标点主键id
     * evaluationValue 评价值(0-4)
     **/
    private List<Evaluation> evaluations;

    @Data
    public static class Evaluation{
        private String indexId;
        private Integer evaluationValue;
    }

}
