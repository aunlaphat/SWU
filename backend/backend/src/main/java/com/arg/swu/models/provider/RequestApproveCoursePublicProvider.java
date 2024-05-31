package com.arg.swu.models.provider;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.models.CourseMain;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.RequestApproveCourseData;
import com.arg.swu.models.handler.EmailProviderInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Assert;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestApproveCoursePublicProvider extends EmailProviderInterface implements ApiConstant
{


    private JdbcTemplate jdbcTemplate;

    private Long coursepublicId;
    private Long statusId;
    private String subject;
    private String envUrl;

    public static final String NAME_REQUIRE = "name";
    public static final String DATE_REQUIRE = "dateRequire";
    public static final String DATE_REQUIRE_EN = "dateRequireEn";
    public static final String DATE_REGISTER = "dateRegister";
    public static final String PROFESSOR = "professor";
    public static final String PROFESSOR_EN = "professorEn";
    public static final String COURSE_CODE = "courseCode";
    public static final String COURSE_NAME = "courseName";
    public static final String TEACH_TYPE = "teachType";
    public static final String COURSE_TYPE = "courseType";
    public static final String COURSE_COMPARE = "courseCompare";
    public static final String Curriculum_Administrator = "curriculumAdministrator";
    public static final String LINK_TOKEN = "linkToken";
    public static final String STAFF_NAME = "staffName";
    public static final String STAFF_NAME_EN = "staffNameEn";
    public static final String STAFF_EMAIL = "staffEmail";

    public static final String EMAIL = "email";

    @Override
    public Map<String, Object> generateParam() {
        if(null == coursepublicId)
        {
            Assert.notNull(coursepublicId, "coursepublicId not null");
        }
         
        List<RequestApproveCourseData> datas = getData(statusId, coursepublicId);

        Assert.isTrue(!datas.isEmpty(), "datas not found");
        
        /*
            -name = member_info.member_firstname_th+' '+member_info.member_lastname_th
            -leaner_id = member_info.member_id
            -email = member_info.member_email
            -password = User กรอกเข้ามา
         */
        RequestApproveCourseData data = datas.get(0);

        Map<String, Object> param = new HashMap<>();
        param.put(NAME_REQUIRE, data.getProfessor());
        param.put(PROFESSOR, data.getProfessor());
        param.put(PROFESSOR_EN, data.getProfessorEn());
        param.put(EMAIL,data.getProfessorEmail());
        param.put(DATE_REGISTER, data.getCourseRegisStart());
        if(!data.getProfessor().equals(data.getStaffName()))
        {
            param.put(STAFF_NAME, data.getStaffName());
            param.put(STAFF_NAME_EN, data.getStaffNameEn());
            param.put(STAFF_EMAIL, data.getStaffEmail());
        }
        param.put(DATE_REQUIRE, CommonUtils.formatDate(data.getDateRequire(), CommonUtils.formatDateDefault, CommonUtils.LOCALE_THAI));
        param.put(DATE_REQUIRE_EN, CommonUtils.formatDate(data.getDateRequire(), CommonUtils.formatDateDefault, CommonUtils.LOCALE_ENG));
        param.put(COURSE_CODE, data.getCourseCode());
        param.put(COURSE_NAME, data.getCourseCompareTh());
        param.put(TEACH_TYPE, data.getTeachType());
        param.put(COURSE_TYPE, data.getCourseTypeNameTh());
        param.put(COURSE_COMPARE, data.getCourseCompareTh());
        param.put(Curriculum_Administrator, data.getProfessor());
        String json = asJsonString(CoursepublicMain.builder().coursepublicId(coursepublicId).build());
        param.put(LINK_TOKEN, envUrl+"/course-management/course-preview?data="+Base64.getEncoder().encodeToString(json.getBytes()));

        return param;
    }

    public static String asJsonString(final Object obj) {
		try {
				return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
				throw new RuntimeException(e);
		}
	}

    private List<RequestApproveCourseData> getData(Long statusId, Long coursepublicId){
        String sql = """
            select cm.course_id, concat(mp.firstname_th , ' ', mp.lastname_th) as professor, 
            concat(mp.firstname_en, ' ', mp.lastname_en) as professore_en, mp.email as professorEmail, 
            cm.course_code, cpm.public_name_th  , cpm.public_name_en, cl.create_date as date_require, cpm.course_regis_start , 
            mct.course_type_code, mct.course_type_name_th, mct.course_type_name_en, lc.name as teach_type, cm.create_by_id, 
            au.ref_user_type, au.user_id
            ,au.email as staff_email
                , concat(au.firstname_th , ' ', au.lastname_th) as staffName
                , concat(au.firstname_en , ' ', au.lastname_en) as staffNameEn
            , au.ref_user_type
            from coursepublic_main cpm 
            join course_main cm on cpm.course_id = cm.course_id 
            join coursepublic_instructor ci on ci.coursepublic_id =cpm.coursepublic_id and ci.instructor_main=true
            join mas_personal mp on ci.instructor_id = mp.personal_id 
            join coursepublic_log cl on cl.coursepublic_id = cpm.coursepublic_id and cl.coursepublic_status = ?
            join mas_course_type mct on mct.course_type_id = cm.course_type_id 
            join lookup_catalog lc on cm.course_format=lc.catalog_id
            left join aut_user au on au.user_id=cm.create_by_id 
            where au.ref_user_type ='30032001' and cpm.coursepublic_id =  ?
              limit 1  """;
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(RequestApproveCourseData.class), statusId, coursepublicId);
    }
}
