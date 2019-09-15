package cn.fdongl.point.core.service;

import cn.fdongl.point.auth.vo.JwtUser;
import cn.fdongl.point.core.repository.*;
import cn.fdongl.point.core.vo.StudentEvaluation;
import cn.fdongl.point.excel.util.SheetHelper;
import cn.fdongl.point.model.entity.Course;
import cn.fdongl.point.model.entity.MapCourseIndex;
import cn.fdongl.point.model.entity.MapCourseIndexEvaluation;
import cn.fdongl.point.model.entity.MapTeacherCourse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.annotation.ExceptionProxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClassPointService {

    @Autowired
    MapTeacherCourseRepository mapTeacherCourseRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    MapCourseIndexRepository mapCourseIndexRepository;

    @Autowired
    MapCourseIndexEvaluationRepository mapCourseIndexEvaluationRepository;

    @Transactional(rollbackFor = Exception.class)
    public void uploadStudentEvaluation(StudentEvaluation studentEvaluation){
        List<MapCourseIndexEvaluation> mapCourseIndexEvaluation =
                mapCourseIndexEvaluationRepository.
                        findByCourseNumberAndCourseSemester(studentEvaluation.getCourseNumber(),studentEvaluation.getCourseSemester());
        if(mapCourseIndexEvaluation.size()!=studentEvaluation.getEvaluations().size()){
            return;
        }
        for(int i=0;i<mapCourseIndexEvaluation.size();i++){
            mapCourseIndexEvaluation.get(i).setEvaluateSum(studentEvaluation.getEvaluations().get(i).getEvaluationValue());
            mapCourseIndexEvaluation.get(i).setEvaluateCount(mapCourseIndexEvaluation.get(i).getEvaluateCount());
        }
        mapCourseIndexEvaluationRepository.saveAll(mapCourseIndexEvaluation);
    }

    @Transactional(rollbackFor = Exception.class)
    public void uploadTeacherEvaluation(Workbook workbook, Long classId, JwtUser user) throws IOException, FileUploadException {
        int numOfSheet = workbook.getNumberOfSheets();
        MapTeacherCourse mapTeacherCourse = mapTeacherCourseRepository.findById(classId).get();


//        Course course = courseRepository.getOne(classId);
//        Long courseSemester = course.getCourseSemester();
//        String courseNumber = course.getCourseNumber();
        Sheet destSheet = null;
        for(int i=0;i<numOfSheet;i++){
            destSheet = workbook.getSheetAt(i);
            if(destSheet.getSheetName().contains("级评价表")){
                break;
            }
            destSheet = null;
        }
        if (destSheet ==null){
            throw new FileUploadException("sheet不存在");
        }
        SheetHelper sheetHelper = new SheetHelper(destSheet);
        List<String> indices = sheetHelper.filterNull(sheetHelper.collectColumnValues(0,String::valueOf));
        List<Double> maxes =  sheetHelper.filterNull(sheetHelper.collectColumnValues(1,1,i -> {
            return Double.valueOf(i.toString());
        }));
        List<Double>vals =  sheetHelper.filterNull(sheetHelper.collectColumnValues(-3,1,i->{
            return Double.valueOf(i.toString());
        }));
        Map<String,MapCourseIndex> mapCourseIndices = parseMap(mapCourseIndexRepository.findByCourseNumber(mapTeacherCourse.getCourseNumber()),MapCourseIndex::getTitle);
        mapCourseIndexEvaluationRepository.deleteByCourseNumberAndCourseSemester(mapTeacherCourse.getCourseNumber(),mapTeacherCourse.getCourseSemester());
        List<MapCourseIndexEvaluation>data = new ArrayList<>();
        for(int i=vals.size()-1;i>=0;i--){
            String t = indices.get(i);
            String h = t.substring(0,t.indexOf(' '));
            MapCourseIndex mapCourseIndex = mapCourseIndices.get(h);
            MapCourseIndexEvaluation mapCourseIndexEvaluation = new MapCourseIndexEvaluation();
            mapCourseIndexEvaluation.setCourseNumber(mapTeacherCourse.getCourseNumber());
            mapCourseIndexEvaluation.setCourseSemester(mapTeacherCourse.getCourseSemester());
            mapCourseIndexEvaluation.setIndexId(mapCourseIndex.getIndexId());
            mapCourseIndexEvaluation.setMaxValue(maxes.get(i));
            mapCourseIndexEvaluation.setEvaluation(vals.get(i));
            mapCourseIndexEvaluation.setEvaluateCount(0);
            mapCourseIndexEvaluation.setEvaluateSum(0);
            data.add(mapCourseIndexEvaluation);
        }
        mapCourseIndexEvaluationRepository.saveAll(data);
        System.out.println(indices);
    }

    public <K,E> Map<K,E> parseMap(List<E>list, Function<E,K> getKey){
        Map<K,E> map = new HashMap<>();
        for (E e : list) {
            map.put(getKey.apply(e),e);
        }
        return map;
    }

}
