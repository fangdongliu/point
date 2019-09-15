package cn.fdongl.point.excel.util;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class SheetHelper {

    private final Sheet sheet;

    private final FormulaEvaluator evaluator;

    private final int rowCount;

    private final int colCount;

    public SheetHelper(Sheet sheet){
        this(sheet,0);
    }

    public SheetHelper(Sheet sheet,int headerLine){
        this.sheet = sheet;
        rowCount = sheet.getLastRowNum()+1;
        colCount = sheet.getRow(headerLine).getLastCellNum()+1;
        evaluator  = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
    }

    public <T> List<T> filterNull(List<T>source){
        return source.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public <T> List<T>collectLinesForClass(Class<? extends T>clazz,int rowStart,int rowEnd,int colStart,int colEnd) throws IllegalAccessException, InstantiationException, ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Field[]fields = clazz.getDeclaredFields();
        List<List<Object>>res = collectLines(rowStart,rowEnd,colStart,colEnd);
        List<T> result = new ArrayList<>();
        for (List<Object> re : res) {
            T t = clazz.newInstance();
            result.add(t);
            Iterator<Object> iterable = re.iterator();
            for (Field field : fields) {
                Object val = iterable.next();
                if(val == null){
                    field.set(t,null);
                    continue;
                }
                Class<?> fclass = field.getType();
                if(fclass == Date.class){
                    if(val.getClass()==Double.class){
                        field.set(t, DateUtil.getJavaDate((Double) val));
                    }
                    else{
                        field.set(t,dateFormat.parse(val.toString()));
                    }
                }
                String value = String.valueOf(val);
                if(fclass == String.class){
                    field.set(t,value);
                } else if(fclass == Double.class){
                    field.set(t,Double.valueOf(value));
                }else if(fclass == Float.class){
                    field.set(t,Float.valueOf(value));
                }else if(fclass == Long.class){
                    field.set(t,Long.valueOf(value));
                }else if(fclass == Integer.class){
                    field.set(t,Integer.valueOf(value));
                }
            }
        }
        return result;
    }

    public <T> List<T>collectLinesForClass(Class<? extends T>clazz,int start,int end) throws IllegalAccessException, InstantiationException, ParseException{
        return collectLinesForClass(clazz,start,end,0,colCount);
    }

    public <T> List<T>collectLinesForClass(Class<? extends T>clazz,int start) throws IllegalAccessException, InstantiationException, ParseException {
        return collectLinesForClass(clazz,start,rowCount);
    }

    public <T> List<T>collectLinesForClass(Class<? extends T>clazz) throws IllegalAccessException, InstantiationException, ParseException {
        return collectLinesForClass(clazz,0,rowCount);
    }

    public List<List<Object>>collectLines(int start){
        return collectLines(start,rowCount,0,colCount);
    }

    public List<List<Object>>collectLines(int start,int end){
        return collectLines(start,end,0,colCount);
    }

    public List<List<Object>>collectLines(int rowStart,int rowEnd,int colStart,int colEnd){
        List<List<Object>>list = new ArrayList<>();
        if(colEnd < 0){
            colEnd += colCount;
        }
        if(colStart < 0){
            colStart += colCount;
        }
        if(rowStart < 0){
            rowStart += rowCount;
        }
        if(rowEnd < 0){
            rowEnd += rowCount;
        }
        for(int i=rowStart;i<rowEnd;i++){
            List<Object>l = new ArrayList<>();
            Row row = sheet.getRow(i);
            for(int j = colStart;j<colEnd;j++){
                l.add(getCellValue(row.getCell(j)));
            }
            list.add(l);
        }
        return list;
    }

    public List<Object> collectColumn(int column){
        return collectColumn(column,0);
    }
    public List<Object> collectColumn(int column,int start){
        return collectColumn(column,start,rowCount);
    }

    public List<Object> collectColumn(int column,int start,int end){
        List<Object> result = new ArrayList<>();
        if(column < 0){
            column += colCount;
        }
        if(start < 0){
            start += rowCount;
        }
        if(end < 0){
            end += rowCount;
        }
        for(int i=start;i<=end;i++){
            Row row = sheet.getRow(i);
            if(row == null){
                result.add(null);
            } else {
                result.add(getCellValue(row.getCell(column)));
            }
        }
        return result;
    }

    public <T> List<T> collectColumnValues(int column, int start,int end, Function<Object,T>parser){
        List<Object>res = collectColumn(column,start,end);
        List<T> result = new ArrayList<>();
        for(Object i:res){
            if(i==null){
                result.add(null);
            }else {
                result.add(parser.apply(i));
            }
        }
        return result;
    }

    public <T> List<T> collectColumnValues(int column, int start,Function<Object,T>parser){
        return collectColumnValues(column,start,rowCount,parser);
    }

    public <T> List<T> collectColumnValues(int column, Function<Object,T>parser){
        return collectColumnValues(column,0,parser);
    }


    private Object getCellValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING: {
                    String val = cell.getStringCellValue();
                    if(val.length() == 0){
                        return null;
                    }
                    return val;
                }
                case FORMULA:
                    return evaluator.evaluateInCell(cell);
                case _NONE:
                case BLANK:
                    return null;
                case BOOLEAN:
                    return cell.getBooleanCellValue();
                case ERROR:
                    return cell.getErrorCellValue();
                default:
                    return null;
            }
        } else {
            return null;
        }
    }
}
