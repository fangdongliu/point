package cn.fdongl.point.core.service;

import cn.fdongl.point.auth.repository.UserRepository;
import cn.fdongl.point.auth.vo.JwtUser;
import cn.fdongl.point.core.exception.SheetNotFoundException;
import cn.fdongl.point.core.mapper.BatchUploadMapper;
import cn.fdongl.point.core.repository.CourseRepository;
import cn.fdongl.point.core.repository.IndexRepository;
import cn.fdongl.point.core.repository.MapCourseIndexRepository;
import cn.fdongl.point.core.vo.UploadCourse;
import cn.fdongl.point.core.vo.UploadStudentCourse;
import cn.fdongl.point.core.vo.UploadTeacher;
import cn.fdongl.point.core.vo.UploadTeacherCourse;
import cn.fdongl.point.excel.util.SheetHelper;
import cn.fdongl.point.model.entity.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiDesktopPaneUI;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UploadService {

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StatusService statusService;

    @Autowired
    BatchUploadMapper uploadMapper;

    @Autowired
    IndexRepository indexRepository;
    @Autowired
    MapCourseIndexRepository mapCourseIndexRepository;

    @Value("${batch-size}")
    Integer batchSize;

    private final Pattern pattern = Pattern.compile("\\d{4}");

    public int uploadCultivateMatrix(Workbook workbook,String filename,JwtUser jwtUser) throws SheetNotFoundException {
        Sheet sheet = workbook.getSheetAt(2);
        Matcher matcher = pattern.matcher(filename);
        Long grade = null;
        if(matcher.find()) {
            grade = Long.valueOf(matcher.group(0));
        }
        if(sheet==null){
            throw new SheetNotFoundException();
        }
        SheetHelper sheetHelper = new SheetHelper(sheet,3);
        List<List<Object>>headers = sheetHelper.collectLines(1,3,2,sheetHelper.getColCount());
        List<String>h1 = sheetHelper.filterNull(headers.get(0)).stream().map(Object::toString).collect(Collectors.toList());
        List<String>h2 = sheetHelper.filterNull(headers.get(1)).stream().map(Object::toString).collect(Collectors.toList());
        String[]h = new String[h1.size()];
        for(int i =h1.size()-1;i>=0;i--){
            h[i] = h1.get(i) +'\n' + h2.get(i);
        }
        List<List<Object>>lines = sheetHelper.collectLines(3,-2,0,-2);
        Date now = new Date();
        List<Object>obj = sheetHelper.filterNull(lines.get(0));
        obj = obj.subList(0,obj.size());
        List<Index>firstLine = obj.stream().map(i->{
            Index index = new Index();
            String s = i.toString();
            int comma = s.indexOf('.');
            int space = s.indexOf(' ');
            String sub1 = s.substring(0,comma);
            Long title = Long.valueOf(sub1);
            String sub2 = s.substring(comma+1,space);
            index.setIndexContent(s.substring(space+1));
            index.setIndexTitle(title);
            index.setIndexNumber(Long.valueOf(sub2));
            index.setParent(h[title.intValue() - 1]);
            index.setCreateDate(now);
            index.setModifyDate(now);
            index.setCreateBy(jwtUser.getId());
            index.setModifiedBy(jwtUser.getId());
            return index;
        }).collect(Collectors.toList());
        indexRepository.saveAll(firstLine);
        List<MapCourseIndex>indices = new ArrayList<>();
        for(int i=lines.size()-1;i>0;i--){
            List<Object>line = lines.get(i);
            for(int j=line.size()-2;j>1;j--){
                Object o = line.get(j);
                if(o!=null){
                    MapCourseIndex mapCourseIndex = new MapCourseIndex();
                    Index index = firstLine.get(j - 2);
                    if(line.get(0) instanceof String){
                        mapCourseIndex.setCourseNumber((String)line.get(0));
                    } else {
                        mapCourseIndex.setCourseNumber(String.valueOf(((Double) line.get(0)).longValue()));
                    }
                    mapCourseIndex.setIndexId(index.getId());
                    mapCourseIndex.setTitle(index.getIndexTitle()+"."+index.getIndexNumber());
                    mapCourseIndex.setProportionValue((Double)o);
                    mapCourseIndex.setCourseSemester(grade);
                    indices.add(mapCourseIndex);
                }
            }
        }
        mapCourseIndexRepository.deleteAllInBatch();
        mapCourseIndexRepository.saveAll(indices);
        return indices.size();
    }

    public int uploadTeacherCourse(Workbook workbook,JwtUser jwtUser) throws SheetNotFoundException, IllegalAccessException, ParseException, InstantiationException {
        Sheet sheet = workbook.getSheetAt(0);
        if(sheet==null){
            throw new SheetNotFoundException();
        }
        SheetHelper sheetHelper = new SheetHelper(sheet,2);
        List<UploadTeacherCourse> sourceCourses = sheetHelper.collectLinesForClass(UploadTeacherCourse.class,3);
        List<MapTeacherCourse> courses = sourceCourses.stream().map(UploadTeacherCourse::toCourse).collect(Collectors.toList());
        Date now = new Date();
        List<User> users = userRepository.findAll();
        Map<String,String>userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getRealName(),user.getWorkId());
        }
        List<MapTeacherCourse>newCourses = new ArrayList<>();

        for (MapTeacherCourse cours : courses) {
            cours.setTeacherWorkId(userMap.get(cours.getTeacherRealName()));
            if(cours.getTeacherWorkId() == null){
                continue;
            }
            cours.setCreateDate(now);
            cours.setModifyDate(now);
            cours.setCreateBy(jwtUser.getId());
            cours.setModifiedBy(jwtUser.getId());
           newCourses.add(cours);
        }
        for(int i = newCourses.size();i>=0;i-=batchSize){
            int start = i-batchSize;
            if(start<0){
                start = 0;
            }
            uploadMapper.insertTeacherCourseIgnore(newCourses.subList(start,i));
        }
        return newCourses.size();
    }

//    @Async
    public void uploadStudentCourse(Workbook workbook,String statusId,JwtUser jwtUser) throws SheetNotFoundException, IllegalAccessException, ParseException, InstantiationException {
        try {
            statusService.updateStatus(statusId, -1);
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new SheetNotFoundException();
            }
            SheetHelper sheetHelper = new SheetHelper(sheet, 0);
            String password = passwordEncoder.encode("123456");
            List<UploadStudentCourse> lines = sheetHelper.collectLinesForClass(UploadStudentCourse.class, 1);
            Map<String, User> students = new HashMap<>();
            for (UploadStudentCourse line : lines) {
                if (!students.containsKey(line.getUserWorkId())) {
                    students.put(line.getUserWorkId(), line.toUser());
                }
            }
            Collection<User> cl = students.values();
            List<User> clist = new ArrayList<>(cl);
            Date now = new Date();
            for (User user : clist) {
                user.setUserPwd(password);
                user.setCreateDate(now);
                user.setModifyDate(now);
                user.setCreateBy(jwtUser.getId());
                user.setModifiedBy(jwtUser.getId());
            }
            for (int i = clist.size(); i >= 0; i -= batchSize) {
                int start = i - batchSize;
                if (start < 0) {
                    start = 0;
                }
                uploadMapper.insertUserIgnore(clist.subList(start, i));
            }
            List<MapStudentCourse> courses = lines.stream().map(UploadStudentCourse::toMapStudentCourse).collect(Collectors.toList());
            for (MapStudentCourse cours : courses) {
                cours.setCreateDate(now);
                cours.setModifyDate(now);
                cours.setCreateBy(jwtUser.getId());
                cours.setModifiedBy(jwtUser.getId());
            }
            int count = 0;
            for (int i = courses.size(); i >= 0; i -= batchSize) {
                int start = i - batchSize;
                if (start < 0) {
                    start = 0;
                }
                uploadMapper.insertStudentCourseIgnore(courses.subList(start, i));
                count += (i - start);
                statusService.updateStatus(statusId, count);
            }
            System.out.println("status 2");
            statusService.updateStatus(statusId, -2);
        }catch (Exception e){
            System.out.println("status -3");
            statusService.updateStatus(statusId,-3);
        }
    }

    public int uploadCourse(Workbook file, JwtUser jwtUser) throws Exception {
        Sheet sheet = file.getSheetAt(0);
        if(sheet==null){
            throw new SheetNotFoundException();
        }
        SheetHelper sheetHelper = new SheetHelper(sheet,2);
        String head = String.valueOf(sheetHelper.filterNull(sheetHelper.collectLines(0,1).get(0)).get(0));

        Matcher match = pattern.matcher(head);
        Long semester = null;
        if(match.find()){
            semester = Long.valueOf(match.group(0));
        } else {
            throw new Exception("未指定学期");
        }
        List<UploadCourse> sourceCourses = sheetHelper.collectLinesForClass(UploadCourse.class,3);
        List<Course> courses = sourceCourses.stream().map(UploadCourse::toCourse).collect(Collectors.toList());
        Date now = new Date();
        for (Course cours : courses) {
            cours.setCourseSemester(semester);
            cours.setCreateDate(now);
            cours.setModifyDate(now);
            cours.setCreateBy(jwtUser.getId());
            cours.setModifiedBy(jwtUser.getId());
        }
        for(int i = courses.size();i>=0;i-=batchSize){
            int start = i-batchSize;
            if(start<0){
                start = 0;
            }
            uploadMapper.insertCourseIgnore(courses.subList(start,i));
        }
        return courses.size();
    }

    public int uploadTeacher(Workbook workbook, JwtUser jwtUser) throws SheetNotFoundException, IllegalAccessException, ParseException, InstantiationException {
        Sheet sheet = workbook.getSheetAt(0);
        if(sheet==null){
            throw new SheetNotFoundException();
        }
        SheetHelper sheetHelper = new SheetHelper(sheet,2);
        String password = passwordEncoder.encode("123456");
        List<UploadTeacher> sourseUsers = sheetHelper.collectLinesForClass(UploadTeacher.class,3);
        List<User> users = sourseUsers.stream().map(UploadTeacher::toUser).collect(Collectors.toList());
        Date now = new Date();
        for (User user : users) {
            user.setUserPwd(password);
            user.setCreateDate(now);
            user.setModifyDate(now);
            user.setCreateBy(jwtUser.getId());
            user.setModifiedBy(jwtUser.getId());
        }
        for(int i = users.size();i>=0;i-=batchSize){
            int start = i-batchSize;
            if(start<0){
                start = 0;
            }
            uploadMapper.insertUserIgnore(users.subList(start,i));
        }
//        userMapper.insertUserIgnore(users);
//        userRepository.insertUserIgnore(users.get(0));
//        userRepository.saveAll(users);
        return users.size();
    }


}
