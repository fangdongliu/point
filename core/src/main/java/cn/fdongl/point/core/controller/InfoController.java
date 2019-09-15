package cn.fdongl.point.core.controller;

import cn.fdongl.point.auth.util.AjaxMessage;
import cn.fdongl.point.auth.util.MsgType;
import cn.fdongl.point.auth.vo.JwtUser;
import cn.fdongl.point.core.repository.IndexRepository;
import cn.fdongl.point.core.repository.MapCourseIndexRepository;
import cn.fdongl.point.core.repository.MapStudentCourseRepository;
import cn.fdongl.point.core.repository.MapTeacherCourseRepository;
import cn.fdongl.point.core.vo.PageResult;
import cn.fdongl.point.file.service.FileService;
import cn.fdongl.point.model.entity.File;
import cn.fdongl.point.model.entity.MapCourseIndex;
import cn.fdongl.point.model.entity.MapStudentCourse;
import cn.fdongl.point.model.entity.MapTeacherCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/getInfo")
public class InfoController{

    @Autowired
    MapTeacherCourseRepository mapTeacherCourseRepository;

    @Autowired
    MapStudentCourseRepository mapStudentCourseRepository;

    @Autowired
    IndexRepository indexRepository;

    @Autowired
    MapCourseIndexRepository mapCourseIndexRepository;

    @Autowired
    FileService fileService;

    /**
     * 获取用户所有的文件下载
     */
    @PostMapping(value = "getAllUserTables")
    public Object getAllUserTable(@RequestParam("pageIndex") int pageIndex,
                                  @RequestParam("pageSize") int pageSize,
                                  @RequestParam(value = "keyWord",required = false) String keyWord,
                                  JwtUser user) {
        PageRequest pageRequest = PageRequest.of(pageIndex,pageSize);
        Page<File> filePage = null;
        if(keyWord == null){
            filePage = fileService.getFileList(user.getId(),pageRequest);
        } else {
            filePage = fileService.getFileList(user.getId(),keyWord,pageRequest);
        }
        return AjaxMessage.Set(
                MsgType.SUCCESS,
                new PageResult<>(filePage.getTotalElements(),filePage.getContent()),
                "获取教师课程分页成功");
    }


    /**
     * 获取所有学院的列表
     *
     * @param
     * @return java.lang.Object
     * @author zm
     * @date 2019/9/9 12:10
     **/
    @PostMapping(value = "departmentInfo")
    public Object getDepartmentInfo() {
        try {
            return AjaxMessage.Set(MsgType.SUCCESS, Arrays.asList("计算机学院"), "获取学院机构成功");
        } catch (Exception e) {
            return AjaxMessage.Set(MsgType.ERROR, null, "获取学校机构失败");
        }
    }

    /**
     * 根据用户类型获取用户的所有学期List
     *
     * @return java.lang.Object
     * @author zm
     * @date 2019/9/9 18:50
     **/
    @GetMapping(value = "semester")
    public Object getUserSemesters(
            JwtUser jwtUser
    ) {
        List<Long> semesterList = new ArrayList<>();
        try {
            if ("student".equals(jwtUser.getUserType())) {
                // 学生就去 学生-课程关联表去找所有的学期
                semesterList = mapStudentCourseRepository.getDistinctCourseSemesterByUserId(jwtUser.getWorkId());
            } else if ("teacher".equals(jwtUser.getUserType())) {
                // 老师就去 教师-课程关联表去找所有的学期
                semesterList = mapTeacherCourseRepository.getDistinctCourseSemesterByUserId(jwtUser.getWorkId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxMessage.Set(MsgType.ERROR, null, "获取学期失败");
        }
        return AjaxMessage.Set(MsgType.SUCCESS, semesterList, "获取学期成功");
    }

    /**
     * 获取学生某学期所有的课程分页
     *
     * @param courseSemester 课程学期
     * @return java.lang.Object
     * @author zm
     * @date 2019/9/10 15:45
     **/
    @PostMapping(value = "coursePage")
    public Object getCoursePage(
            JwtUser jwtUser,
            @RequestParam(value = "courseSemester",required = false) Long courseSemester,
            @RequestParam("pageIndex") int pageIndex,
            @RequestParam("pageSize") int pageSize) {
        String workId = jwtUser.getWorkId();
        PageRequest pageRequest = PageRequest.of(pageIndex-1,pageSize);
        if(courseSemester == null){
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH);
            year = 2018;
            if(month >= 8){
                courseSemester = Long.valueOf(year);
            } else {
                courseSemester = Long.valueOf(year - 1);
            }
        }
        try {
            if(jwtUser.getUserType().equals("teacher")) {
                Page<MapTeacherCourse>result  = mapTeacherCourseRepository.findByTeacherWorkIdAndCourseSemester(workId,courseSemester,pageRequest);
                return AjaxMessage.Set(MsgType.SUCCESS,
                       new PageResult<>(result.getTotalElements(),result.getContent()),
                        "获取学生课程成功");
            }else{
                Page<MapStudentCourse> result = mapStudentCourseRepository.findByUserWorkIdAndCourseSemester(workId,courseSemester,pageRequest);
                return AjaxMessage.Set(MsgType.SUCCESS,
                        new PageResult<>(result.getTotalElements(),result.getContent()),
                        "获取学生课程成功");
            }
        } catch (Exception e) {
            return AjaxMessage.Set(MsgType.ERROR,
                    null,
                    "获取课程失败");
        }
    }


    /**
     * 获取当前时间(学期)课程对应的指标点并返回
     *
     * @param courseNumber  课程编号
     * @return java.lang.Object
     * @author zm
     * @date 2019/9/11 16:14
     **/
    @PostMapping(value = "nowCourseIndex")
    public Object getNowCourseIndex(
        @RequestParam("courseNumber") String courseNumber) {
        try {
            return AjaxMessage.Set(MsgType.SUCCESS,indexRepository.findAllById(
                    mapCourseIndexRepository.
                            findByCourseNumber(courseNumber).
                            stream().map(MapCourseIndex::getIndexId).collect(Collectors.toList())
            ),"获取当前课程指标点成功");
        } catch (Exception e) {
            return AjaxMessage.Set(MsgType.ERROR, "获取当前课程指标点失败");
        }
    }

}
