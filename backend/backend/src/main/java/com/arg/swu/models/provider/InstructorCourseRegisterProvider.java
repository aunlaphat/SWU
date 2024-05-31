package com.arg.swu.models.provider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.NotifLecturerCourseRegister;
import com.arg.swu.dto.NotifLecturerCourseRegisterRowmap;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.models.MemberInfo;
import com.arg.swu.models.handler.EmailProviderInterface;
import com.arg.swu.repositories.CourseMainRepository;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.MasPersonalRepository;
import com.arg.swu.repositories.MemberCourseRepository;
import com.arg.swu.repositories.MemberInfoRepository;
import java.util.List;
import io.jsonwebtoken.lang.Assert;
import lombok.Builder;
import lombok.Data;

/*
 * 
 * input param
-coursepublic_id
-name = mas_personal.firstname_th lastname_th from mas_personal join coursepublic_instructor on coursepublic_instructor.instructor_id=mas_personal.personal_id join coursepublic_main on coursepublic_main.coursepublic_id = coursepublic_instructor.coursepublic_id where coursepublic_instructor.instructor_main=true
- student_name = member_info.member_firstname_th+' '+member_info.member_lastname_th
- learner_id = member_info.member_id
-course_code = course_main.course_code
-course_name = coursepublic_main.public_name_th
-leacturer = mas_personal.firstname_th lastname_th from mas_personal join coursepublic_instructor on coursepublic_instructor.instructor_id=mas_personal.personal_id join coursepublic_main on coursepublic_main.coursepublic_id = coursepublic_instructor.coursepublic_id
*/
@Builder
@Data
public class InstructorCourseRegisterProvider extends EmailProviderInterface implements ApiConstant
{


    private JdbcTemplate jdbcTemplate;

    private Long memberCourseId;
    private String subject;

    public static final String NAME = "name";
    public static final String LECTURE = "lecture";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_CODE = "courseCode";
    public static final String LEARNEER_ID = "learneerId";
    public static final String EMAIL = "email";
    public static final String STUDENT_NAME = "studentName";

    @Override
    public Map<String, Object> generateParam() {
        if(null == memberCourseId)
        {
            Assert.notNull(memberCourseId, "memberCourseId not null");
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("select concat(mp.firstname_th , ' ', mp.lastname_th) as instructor_name_th, mp.email , concat(mi.member_firstname_th , ' ', mi.member_firstname_en) as student_name_th,");
        sql.append(" concat(mp.firstname_en, ' ', mp.lastname_en) as instructor_name_en, concat(mi.member_firstname_en, ' ', mi.member_firstname_en) as student_name_en,");
        sql.append(" mi.member_id ,cm.course_code, cpm.public_name_th, cpm.public_name_en");
        sql.append(" from member_course mc");
        sql.append(" join member_info mi on mc.member_id = mi.member_id ");
        sql.append(" join coursepublic_main cpm on cpm.coursepublic_id = mc.coursepublic_id");
        sql.append(" join course_main cm on cm.course_id = cpm.course_id ");
        sql.append(" join coursepublic_instructor ci on ci.coursepublic_id =cpm.coursepublic_id ");
        sql.append(" join mas_personal mp on ci.instructor_id = mp.personal_id ");
        sql.append(" where ci.instructor_main=true and mc.member_course_id = ?");
 
        Object[] args = {memberCourseId};
        NotifLecturerCourseRegisterRowmap rowmap = new NotifLecturerCourseRegisterRowmap();
        NotifLecturerCourseRegister nlcr = this.jdbcTemplate.queryForObject(sql.toString(),  rowmap, args); 

        Map<String, Object> param = new HashMap<>();
        param.put(NAME, nlcr.getInstructorNameTh());
        param.put(LECTURE, nlcr.getInstructorNameTh());
        param.put(STUDENT_NAME, nlcr.getStudentNameTh());
        param.put(COURSE_CODE, nlcr.getCourseCode());
        param.put(COURSE_NAME, nlcr.getPublicNameTh() + "/" + nlcr.getPublicNameEn());
        param.put(LEARNEER_ID, nlcr.getMemberId() );
        param.put(EMAIL, nlcr.getEmail());

        return param;
    }
    
}
