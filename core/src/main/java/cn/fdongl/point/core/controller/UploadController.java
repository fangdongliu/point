package cn.fdongl.point.core.controller;

import cn.fdongl.point.auth.util.AjaxMessage;
import cn.fdongl.point.auth.util.MsgType;
import cn.fdongl.point.auth.vo.JwtUser;
import cn.fdongl.point.core.service.ClassPointService;
import cn.fdongl.point.core.service.StatusService;
import cn.fdongl.point.core.service.UploadService;
import cn.fdongl.point.core.vo.StudentEvaluation;
import cn.fdongl.point.file.service.FileService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@RequestMapping("/upload")
@RestController
public class UploadController {

    @Autowired
    ClassPointService classPointService;

    @Autowired
    UploadService uploadService;

    @Autowired
    StatusService statusService;
    
    @Autowired
    FileService fileService;

    @PostMapping(value = "/uploadTeacherCom")
    public Object uploadTeacherCom(
            @RequestParam("classId")Long classId,
            @RequestParam("file")MultipartFile file,
            JwtUser user){
        String msg=null;
        try{
            Workbook workbook = getWorkbook(file);
            classPointService.uploadTeacherEvaluation(workbook,classId,user);
            fileService.save("老师课程评价表",file.getOriginalFilename(),file);
            return AjaxMessage.Set(MsgType.SUCCESS);
        } catch (Exception e) {
            return AjaxMessage.Set(MsgType.ERROR,"",e.getCause().toString());
        }
    }

    @PostMapping(value = "cultivatePlan")
    public Object uploadCultivatePlan(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("上传培养方案");
            fileService.save("培养方案",file.getOriginalFilename(),file);
            return AjaxMessage.Set(MsgType.SUCCESS);
        } catch (Exception e) {
            return AjaxMessage.Set(MsgType.ERROR);
        }
    }

    @PostMapping(value = "cultivateMatrix")
    public Object uploadCultivateMatrix(@RequestParam("file") MultipartFile file,JwtUser user) {
        try {
            Workbook workbook = getWorkbook(file);
            return AjaxMessage.Set(MsgType.SUCCESS,uploadService.uploadCultivateMatrix(workbook,file.getOriginalFilename(),user));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxMessage.Set(MsgType.ERROR);
        }
    }

    @PostMapping(value = "teacherInfo")
    public Object uploadTeacherInfo(
            @RequestParam("file") MultipartFile teacherFile,
            JwtUser user
    ) {
        try {
            Workbook workbook = getWorkbook(teacherFile);
            fileService.save("老师信息表",teacherFile.getOriginalFilename(),teacherFile);
            return AjaxMessage.Set(MsgType.SUCCESS,uploadService.uploadTeacher(workbook,user),"上传教师信息成功");
        } catch (Exception e) {
            return AjaxMessage.Set(MsgType.ERROR,null,"上传教师信息失败");
        }
    }

    @GetMapping("queryStatus")
    public Object queryStatus(String id){
        return AjaxMessage.Set(MsgType.SUCCESS,statusService.queryStatus(id));
    }

    @PostMapping(value = "studentCourse")
    public Object uploadStudentCourse(
            @RequestParam("file") MultipartFile studentCourse,
            @RequestParam String id,
            JwtUser user
    ) {
        try {
            Integer status = statusService.queryStatus(id);
            if(!(status == null || status == -3)){
                return AjaxMessage.Set(MsgType.ERROR,null,"正在处理");
            }
            Workbook workbook = getWorkbook(studentCourse);
            uploadService.uploadStudentCourse(workbook, id, user);
            fileService.save("学生选课表",studentCourse.getOriginalFilename(),studentCourse);
            return AjaxMessage.Set(MsgType.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            statusService.updateStatus(id,-3);
            return AjaxMessage.Set(MsgType.ERROR);
        }
    }

    @PostMapping(value = "courseUpload")
    public Object uploadCourse(@RequestParam("file") MultipartFile file,JwtUser user) {
        try {
            Workbook workbook = getWorkbook(file);
            fileService.save("课程表",file.getOriginalFilename(),file);
            return AjaxMessage.Set(MsgType.SUCCESS,uploadService.uploadCourse(workbook,user));
        } catch (Exception e) {
            return AjaxMessage.Set(MsgType.ERROR,null,e.getCause().toString());
        }
    }

    Workbook getWorkbook(MultipartFile file) throws IOException, FileUploadException {
        InputStream inputStream = file.getInputStream();
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        // init工作簿
        Workbook workbook = null;
        // 获取文件后缀
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        // 根据不同后缀init不同的类，是xls还是xlsx
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            throw new FileUploadException("请上传正确的表格文件");
        }
        return workbook;
    }


    @PostMapping(value = "teacherCourseUpload")
    public Object teacherCourseUpload(@RequestParam("file") MultipartFile file,JwtUser user) {
        try {
            Workbook workbook = getWorkbook(file);
            fileService.save("老师开课表",file.getOriginalFilename(),file);
            return AjaxMessage.Set(MsgType.SUCCESS,uploadService.uploadTeacherCourse(workbook,user));
        } catch (Exception e) {
            return AjaxMessage.Set(MsgType.ERROR,null,e.getCause().toString());
        }
    }

    @PostMapping(value = "studentEvaluation")
    public Object uploadStudentEvaluation(@RequestBody StudentEvaluation studentEvaluation) {
        try {
            classPointService.uploadStudentEvaluation(studentEvaluation);
            return AjaxMessage.Set(MsgType.SUCCESS, null, "上传学生评价成功");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxMessage.Set(MsgType.ERROR, null, "上传学生评价失败");
        }
    }

}