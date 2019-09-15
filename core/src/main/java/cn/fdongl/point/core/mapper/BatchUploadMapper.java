package cn.fdongl.point.core.mapper;

import cn.fdongl.point.model.entity.Course;
import cn.fdongl.point.model.entity.MapStudentCourse;
import cn.fdongl.point.model.entity.MapTeacherCourse;
import cn.fdongl.point.model.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchUploadMapper {

    @Insert("<script>" +
            "insert ignore into map_teacher_course " +
            "(" +
            " course_credit, course_name," +
            " course_number, course_property," +
            " course_semester, create_by, create_date," +
            " modified_by, modify_date, teacher_work_id,teacher_real_name)" +
            "        values\n" +
            "        <foreach collection=\"list\" item=\"item\" separator=\",\">\n"+
            "            (#{item.courseCredit,jdbcType=DOUBLE}, #{item.courseName,jdbcType=VARCHAR},\n" +
            "            #{item.courseNumber,jdbcType=VARCHAR}, #{item.courseProperty,jdbcType=VARCHAR},\n" +
            "#{item.courseSemester,jdbcType=INTEGER},"+
            "#{item.createBy,jdbcType=INTEGER},"+
            "            #{item.createDate,jdbcType=DATE},"+
            "            #{item.modifiedBy,jdbcType=INTEGER}, #{item.modifyDate,jdbcType=DATE},\n" +
            "            #{item.teacherWorkId,jdbcType=VARCHAR}, #{item.teacherRealName,jdbcType=VARCHAR})\n" +
            "        </foreach></script>")
    int insertTeacherCourseIgnore(Iterable<MapTeacherCourse> list);

    @Insert("<script>" +
            "insert ignore into map_student_course( " +
            "`course_ascription`,`course_credit`,"+
            "`course_department`,"+
            "`course_hour`,"+
            "`course_kind`,"+
            "`course_name`,"+
            "`course_nature`,"+
            "`course_number`,"+
            "`course_property`,"+
            "`course_select_number`,"+
            "`course_semester`,"+
            "`create_by`,"+
            "`create_date`,"+
            "`exam_nature`,"+
            "`grade_sign`,"+
            "`input_user_name`,"+
            "`modified_by`,"+
            "`modify_date`,"+
            "`supplement_repeat_semester`,"+
            "`total_grade`,"+
            "`user_work_id`)"+
            "        values\n" +
            "        <foreach collection=\"list\" item=\"item\" separator=\",\">\n" +
            "            (#{item.courseAscription,jdbcType=VARCHAR}," +
            "#{item.courseCredit,jdbcType=DOUBLE}," +
            "#{item.courseDepartment,jdbcType=VARCHAR}," +
            "#{item.courseHour,jdbcType=DOUBLE}," +
            "#{item.courseKind,jdbcType=VARCHAR}," +
            "#{item.courseName,jdbcType=VARCHAR}," +
            "#{item.courseNature,jdbcType=VARCHAR}," +
            "#{item.courseNumber,jdbcType=VARCHAR}," +
            "#{item.courseProperty,jdbcType=VARCHAR}," +
            "#{item.courseSelectNumber,jdbcType=VARCHAR}," +
            "#{item.courseSemester,jdbcType=INTEGER}," +
            "#{item.createBy,jdbcType=INTEGER}," +
            "#{item.createDate,jdbcType=DATE}," +
            "#{item.examNature,jdbcType=VARCHAR}," +
            "#{item.gradeSign,jdbcType=VARCHAR}," +
            "#{item.inputUserName,jdbcType=VARCHAR}," +
            "#{item.modifiedBy,jdbcType=INTEGER}," +
            "#{item.modifyDate,jdbcType=DATE}," +
            "#{item.supplementRepeatSemester,jdbcType=VARCHAR}," +
            "#{item.totalGrade,jdbcType=VARCHAR}," +
            "#{item.userWorkId,jdbcType=VARCHAR})" +
            "        </foreach></script>")
    int insertStudentCourseIgnore(Iterable<MapStudentCourse> list);

    @Insert("<script>" +
            "insert ignore into course " +
            "(assess_method, course_attribution, course_character, course_credit," +
            " course_department, course_kind, course_name, course_number, course_route," +
            " course_semester, course_type, create_by, create_date, experiment_len," +
            " is_substitute, modified_by, modify_date, module_identification," +
            " remarks, semester_len, total_len, type_identification)" +
            "        values\n" +
            "        <foreach collection=\"list\" item=\"item\" separator=\",\">\n" +
            "            (#{item.assessMethod,jdbcType=VARCHAR}, #{item.courseAttribution,jdbcType=VARCHAR}, #{item.courseCharacter,jdbcType=VARCHAR},\n" +
            "            #{item.courseCredit,jdbcType=DOUBLE}, #{item.courseDepartment,jdbcType=VARCHAR}, #{item.courseKind,jdbcType=VARCHAR},\n" +
            "            #{item.courseName,jdbcType=VARCHAR}, #{item.courseNumber,jdbcType=VARCHAR}, #{item.courseRoute,jdbcType=INTEGER},\n" +
            "            #{item.courseSemester,jdbcType=INTEGER},\n" +
            "            #{item.courseType,jdbcType=VARCHAR}, #{item.createBy,jdbcType=INTEGER},\n" +
            "            #{item.createDate,jdbcType=DATE}, #{item.experimentLen,jdbcType=DOUBLE},\n" +
            "            #{item.isSubstitute,jdbcType=INTEGER}, #{item.modifiedBy,jdbcType=INTEGER}, #{item.modifyDate,jdbcType=DATE},\n" +
            "            #{item.moduleIdentification,jdbcType=VARCHAR}, #{item.remarks,jdbcType=VARCHAR}, #{item.semesterLen,jdbcType=DOUBLE},\n" +
            "            #{item.totalLen,jdbcType=DOUBLE}, #{item.typeIdentification,jdbcType=VARCHAR})\n" +
            "        </foreach></script>")
    int insertCourseIgnore(Iterable<Course> list);

    @Insert("<script>" +
            "insert ignore into user (work_id, user_pwd, real_name,\n" +
            "        user_type,\n" +
            "        user_department, class_name, start_year,\n" +
            "        education_system, train_level, user_title,\n" +
            "        create_date, create_by,\n" +
            "        modify_date,modified_by)\n" +
            "        values\n" +
            "        <foreach collection=\"list\" item=\"item\" separator=\",\">\n" +
            "            (#{item.workId,jdbcType=VARCHAR}, #{item.userPwd,jdbcType=VARCHAR}, #{item.realName,jdbcType=VARCHAR},\n" +
            "            #{item.userType,jdbcType=VARCHAR}, #{item.userDepartment,jdbcType=VARCHAR}, #{item.className,jdbcType=VARCHAR},\n" +
            "            #{item.startYear,jdbcType=VARCHAR}, #{item.educationSystem,jdbcType=INTEGER}, #{item.trainLevel,jdbcType=VARCHAR},\n" +
            "            #{item.userTitle,jdbcType=INTEGER},\n" +
            "            #{item.createDate,jdbcType=DATE}, #{item.createBy,jdbcType=INTEGER},\n" +
            "            #{item.modifyDate,jdbcType=DATE}, #{item.modifiedBy,jdbcType=INTEGER})\n" +
            "        </foreach></script>")
    int insertUserIgnore(Iterable<User> list);

}
