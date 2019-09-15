package cn.fdongl.point.core.service;

import cn.fdongl.point.core.repository.CourseRepository;
import cn.fdongl.point.core.repository.IndexRepository;
import cn.fdongl.point.core.repository.MapCourseIndexRepository;
import cn.fdongl.point.core.repository.ScoreRepository;
import cn.fdongl.point.model.entity.Course;
import cn.fdongl.point.model.entity.Index;
import cn.fdongl.point.model.entity.MapCourseIndex;
import cn.fdongl.point.model.entity.MapCourseIndexEvaluation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExportService {

    @Autowired
    MapCourseIndexRepository mapCourseIndexRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    IndexRepository indexRepository;

    public void exportByClasses(List<Course> courses, HttpServletResponse response,String name,long yearStart,long yearEnd) throws IOException {
        List<String>courseNumbers = courses.stream().map(Course::getCourseNumber).collect(Collectors.toList());
        List<MapCourseIndex>mapCourseIndices = mapCourseIndexRepository.findByCourseNumberIn(courseNumbers);
        List<Index>indices = indexRepository.findAllById(
                mapCourseIndices.stream().map(MapCourseIndex::getIndexId).collect(Collectors.toList()));
        Map<String,Course>mappedCourses = parseMap(courses,Course::getCourseNumber);
        Map<CourseIndex,MapCourseIndex>mappedCourseIndexs =
                parseMap(mapCourseIndices,(k)-> new CourseIndex(k.getCourseNumber(),k.getIndexId()));
        List<List<Index>> stringListMap = indices.stream().collect(Collectors.groupingBy(Index::getParent))
                .values().stream().sorted((a,b)-> (int)(a.get(0).getIndexTitle() - b.get(0).getIndexTitle()))
                .collect(Collectors.toList());
        HSSFWorkbook workbook = new HSSFWorkbook();
        Map<CourseIndex,MapCourseIndexEvaluation> scores =
                parseMap(scoreRepository.findByCourseSemesterBetween(yearStart,yearEnd),
                (k) -> new CourseIndex(k.getCourseNumber(),k.getIndexId()));
        for (List<Index> value : stringListMap) {
            value.sort((a,b)-> (int)(a.getIndexNumber() - b.getIndexNumber()));
            List<List<String>>tables = new ArrayList<>();
            Map<Index,Map<String,Double>>vvvv = new HashMap<>();
            for (Course cours : courses) {
                boolean flag = false;
                List<String> line = new ArrayList<>();
                line.add(cours.getCourseName());

                for (Index index : value) {
                    Map<String,Double>hhhh = vvvv.get(index);
                    if(hhhh == null){
                        hhhh = new HashMap<>();
                        vvvv.put(index,hhhh);
                    }
                    CourseIndex key = new CourseIndex(cours.getCourseNumber(),index.getId());
                    MapCourseIndex mapCourseIndex = mappedCourseIndexs.get(key);
                    if(mapCourseIndex!=null){
                        flag = true;
                        MapCourseIndexEvaluation vv = scores.get(key);
                        String suffix = "("+mapCourseIndex.getProportionValue()+")";
                        if(vv == null){
                            line.add("未评价"+suffix);
                        } else {
                            Double val = vv.getEvaluation();
                            if(val == null) {
                                val = ((double) vv.getEvaluateSum()) / vv.getEvaluateCount();
                            }
                            line.add(String.format("%.2f",val)+suffix);
                            Double v = hhhh.get(cours.getCourseName());
                            if(v==null || v>val){
                                hhhh.put(cours.getCourseName(),val);
                            }
                        }
                    } else {
                        line.add(null);
                    }
                }
                if(flag){
                    tables.add(line);
                }
            }
            Index head = value.get(0);
            HSSFSheet sheet = workbook.createSheet(head.getParent().substring(0,head.getParent().indexOf('\n')).replaceAll("/","、").replaceAll("\\[",""));
            fillSheet(sheet,value,tables,vvvv);
//            System.out.println(value.get(0));
        }
        response.setContentType("application/vnd.ms-excel");

        String fileName = new String( name.getBytes( "gb2312" ), "ISO8859-1" );
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
        response.setContentType("application/x-download; charset=UTF-8");

        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    void fillSheet(HSSFSheet sheet,List<Index>indices,List<List<String>>tables,Map<Index,Map<String,Double>>vvvv){
        HSSFCellStyle style1 = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setColor((short)0x0000);
        font.setBold(true);
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        style1.setFont(font);
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setVerticalAlignment(VerticalAlignment.CENTER);
        style1.setWrapText(true);
        HSSFCellStyle style12= sheet.getWorkbook().createCellStyle();
        Font font2 = sheet.getWorkbook().createFont();
        font2.setColor((short)0x0000);
        font2.setBold(true);
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 16);
        style12.setFont(font);
        style12.setAlignment(HorizontalAlignment.LEFT);
        style12.setVerticalAlignment(VerticalAlignment.CENTER);
        style12.setWrapText(true);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, indices.size()));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, indices.size()));
        String parent = indices.get(0).getParent();
        HSSFRow row0 = sheet.createRow((int) 0);
        row0.setHeight((short) 0x400);
        HSSFCell cell = row0.createCell(0);
        cell.setCellValue("课程毕业要求达成度评价表");
        cell.setCellStyle(style1);
        HSSFRow row1 = sheet.createRow((int) 1);
        cell = row1.createCell(0);
        cell.setCellValue(parent);
        cell.setCellStyle(style12);
        row1.setHeight((short) 0x400);
        HSSFRow row3 = sheet.createRow(2);
        row3.setHeight((short) 0x400);
        HSSFCellStyle cellstyle = sheet.getWorkbook().createCellStyle();
        Font font3 = sheet.getWorkbook().createFont();
        font3.setColor((short)0x0000);
        font3.setFontName("宋体");
        font3.setFontHeightInPoints((short) 11);
        cellstyle.setFont(font3);
        cellstyle.setAlignment(HorizontalAlignment.LEFT);
        cellstyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellstyle.setWrapText(true);
        HSSFCellStyle cellvaluestyle = sheet.getWorkbook().createCellStyle();
        Font font4 = sheet.getWorkbook().createFont();
        font4.setColor((short)0x0480);
        font4.setFontName("宋体");
        font4.setFontHeightInPoints((short) 11);
        cellvaluestyle.setFont(font4);
        cellvaluestyle.setAlignment(HorizontalAlignment.CENTER);
        cellvaluestyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellvaluestyle.setWrapText(true);
        for(int i=1;i<=indices.size();i++){
            cell = row3.createCell(i);
            cell.setCellValue(indices.get(i-1).getIndexContent());
            cell.setCellStyle(cellstyle);
        }
        for(int i=0;i<=indices.size();i++){
            sheet.setColumnWidth(i,256*30);
        }
        for(int i=0;i<tables.size();i++){
            HSSFRow row = sheet.createRow(i+3);
            row.setHeight((short) 0x400);
            List<String> d = tables.get(i);
            for(int j = 0;j < d.size();j++){
                cell = row.createCell(j);
                if(j==0){
                    cell.setCellStyle(cellstyle);
                } else{
                    cell.setCellStyle(cellvaluestyle);
                }
                cell.setCellValue(d.get(j));
            }
        }
        List<String>lastLine = new ArrayList<>();
        lastLine.add("评价合计");
        for (Map<String, Double> value : vvvv.entrySet().stream().
                sorted((a,b)->(int)(a.getKey().getIndexNumber() - b.getKey().getIndexNumber())).
                map(Map.Entry::getValue).collect(Collectors.toList())) {
            double sum = 0.0;
            for (Double aDouble : value.values()) {
                sum += aDouble;
            }
            lastLine.add(String.format("%.2f",sum));
        }
        HSSFRow rowlast = sheet.createRow(tables.size()+3);
        rowlast.setHeight((short) 0x400);
        for(int i=0;i<lastLine.size();i++){
            cell = rowlast.createCell(i);
            cell.setCellValue(lastLine.get(i));
            cell.setCellStyle(cellvaluestyle);
        }

    }

    public <K,E> Map<K,E> parseMap(List<E>list, Function<E,K>getKey){
        Map<K,E> map = new HashMap<>();
        for (E e : list) {
            map.put(getKey.apply(e),e);
        }
        return map;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CourseIndex{
        String courseNumber;
        Long indexId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CourseIndex)) return false;
            CourseIndex i = (CourseIndex) o;
            return indexId.equals(i.indexId) &&
                    courseNumber.equals(i.courseNumber);
        }

        @Override
        public int hashCode(){
            return Objects.hash(courseNumber,indexId);
        }

    }

}
