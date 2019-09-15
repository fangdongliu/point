package cn.fdongl.point.core.controller;

import cn.fdongl.point.auth.vo.JwtUser;
import cn.fdongl.point.core.repository.CourseRepository;
import cn.fdongl.point.core.repository.MapCourseIndexRepository;
import cn.fdongl.point.core.service.ExportService;
import cn.fdongl.point.file.service.FileService;
import cn.fdongl.point.model.entity.Course;
import cn.fdongl.point.model.entity.MapCourseIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    FileService fileService;

    @Autowired
    CourseRepository courseRepository;

//    @Autowired
//    MapCourseIndexRepository mapCourseIndexRepository;

    @Autowired
    ExportService exportService;

    /**
     * 下载与该用户所有有关的表
     */
    @GetMapping(value = "downloadUserTable", produces = MediaType.ALL_VALUE)
    public void userTable(@RequestParam("id") Long id,
                          HttpServletResponse response) throws UnsupportedEncodingException {
        fileService.download(id,response);
    }

//    @GetMapping("exportTest")
//    public void exportTest(
//            JwtUser jwtUser,
//            HttpServletRequest httpServletRequest,
//            HttpServletResponse httpServletResponse
//    ) {
//        Map<String, Result> stringResult = new HashMap<>();
//        Result tmpResult = new Result();
//
//        List<String> indexList = new ArrayList<>();
//        indexList.add("7.1能够了解软件工程及相关行业的政策和法律法规势");
//        indexList.add("7.2能够理解复杂软件工程问题的专业实践和对环境以及社会可持续的影响");
//        List<String> courseList = new ArrayList<>();
//        courseList.add("思想道德与法律基础2014-2015学年");
//        courseList.add("思想道德与法律基础2015-2016学年");
//        courseList.add("知识产权法基础2014-2015学年");
//        courseList.add("知识产权法基础2015-2016学年");
//        courseList.add("大类专业导论2014-2015学年");
//        courseList.add("大类专业导论2015-2016学年");
//        courseList.add("互联网应用开发基础训练2014-2015学年");
//        courseList.add("互联网用用开发基础训练2015-2016学年");
//        courseList.add("毕业设计(论文)2014-2015学年");
//        courseList.add("毕业设计(论文)2015-2016学年");
//        Map<TwoString, String> relation = new HashMap<>();
//        relation.put(new TwoString("思想道德与法律基础2014-2015学年", "7.1能够了解软件工程及相关行业的政策和法律法规势"), "0.3");
//        relation.put(new TwoString("思想道德与法律基础2015-2016学年", "7.1能够了解软件工程及相关行业的政策和法律法规势"), "0.3");
//        relation.put(new TwoString("知识产权法基础2014-2015学年", "7.2能够理解复杂软件工程问题的专业实践和对环境以及社会可持续的影响"), "0.4");
//        relation.put(new TwoString("知识产权法基础2015-2016学年", "7.2能够理解复杂软件工程问题的专业实践和对环境以及社会可持续的影响"), "0.4");
//
//        relation.put(new TwoString("大类专业导论2014-2015学年", "7.1能够了解软件工程及相关行业的政策和法律法规势"), "0.3");
//        relation.put(new TwoString("大类专业导论2015-2016学年", "7.1能够了解软件工程及相关行业的政策和法律法规势"), "0.3");
//        relation.put(new TwoString("互联网应用开发基础训练2014-2015学年", "7.2能够理解复杂软件工程问题的专业实践和对环境以及社会可持续的影响"), "0.6");
//        relation.put(new TwoString("互联网用用开发基础训练2015-2016学年", "7.2能够理解复杂软件工程问题的专业实践和对环境以及社会可持续的影响"), "0.6");
//        relation.put(new TwoString("毕业设计(论文)2014-2015学年", "7.1能够了解软件工程及相关行业的政策和法律法规势"), "0.4");
//        relation.put(new TwoString("毕业设计(论文)2015-2016学年", "7.1能够了解软件工程及相关行业的政策和法律法规势"), "0.4");
//
//        tmpResult.setIndexList(indexList);
//        tmpResult.setCourseList(courseList);
//        tmpResult.setRelation(relation);
//
//        Map<String, Result> sr = new HashMap<>();
//        sr.put("7", tmpResult);
//        try {
//            exportService.exportExcelResult(jwtUser, sr, httpServletRequest, httpServletResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 按年导出指标点excel结果
     *
     * @param schoolYear
     * @return void
     * @author zm
     * @date 2019/9/12 0:21
     **/
    @GetMapping("bySchoolYear")
    public void exportByTwoYear(
            @RequestParam("schoolYear") String schoolYear,
            HttpServletResponse httpServletResponse) throws IOException {
        if(!schoolYear.matches("\\d{4}-\\d{4}")){
            throw new IllegalArgumentException("字符串类型不匹配yyyy-yyyy");
        }
        Long start = Long.valueOf(schoolYear.substring(0,4));
        Long end = Long.valueOf(schoolYear.substring(5,9));
        List<Course> courses = courseRepository.findByCourseSemesterBetween(start, end);
        exportService.exportByClasses(courses,httpServletResponse,schoolYear+"计算结果.xlsx",start,end);
    }
//
//    /**
//     * 按学级导出指标点excel结果
//     *
//     * @param grade 年级
//     * @return void
//     * @author zm
//     * @date 2019/9/12 0:21
//     **/
    @GetMapping("byGrade")
    public void exportByGrade(
            @RequestParam("grade") Long grade,
            HttpServletResponse httpServletResponse) throws IOException {
        Long start = grade;
        Long end = grade+4;
        List<Course> courses = courseRepository.findByCourseRoute(grade);
        exportService.exportByClasses(courses,httpServletResponse,"第"+grade+"级计算结果.xlsx",start,end);
    }
}
