package com.arg.swu.models.provider;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.models.MemberInfo;
import com.arg.swu.models.handler.EmailProviderInterface;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.MemberCourseRepository;
import com.arg.swu.repositories.MemberInfoRepository;

import io.jsonwebtoken.lang.Assert;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseRegisterProvider extends EmailProviderInterface implements ApiConstant
{


    private MemberCourseRepository courseRepo;
    private MemberInfoRepository memberInfoRepo;
    private CoursepublicMainRepository coursePulicRepo;

    private Long memberCourseId;
    private String subject;

    public static final String NAME = "name";
    public static final String COURSE_NAME = "courseName";
    public static final String DATE_RESISTER = "dateRegister";
    public static final String EMAIL = "email";

    @Override
    public Map<String, Object> generateParam() {
        if(null == memberCourseId)
        {
            Assert.notNull(memberCourseId, "memberCourseId not null");
        }
        MemberCourse memberCourse = courseRepo.findById(memberCourseId).orElse(null);
        MemberInfo memberInfo = memberInfoRepo.findById(memberCourse.getMemberId()).orElse(null);
        CoursepublicMain cpMain = coursePulicRepo.findById(memberCourse.getCoursepublicId()).orElse(null);


        if(null == memberCourse)
        {
            Assert.notNull(memberCourse, "memberCourse not found");
        }
        if(null == memberInfo)
        {
            Assert.notNull(memberInfo, "memberInfo not found");
        }
        if(null == cpMain)
        {
            Assert.notNull(cpMain, "CoursepublicMain not found");
        }
        /*
            -name = member_info.member_firstname_th+' '+member_info.member_lastname_th
            -leaner_id = member_info.member_id
            -email = member_info.member_email
            -password = User กรอกเข้ามา
         */
        
        Map<String, Object> param = new HashMap<>();
        param.put(NAME, memberInfo.getMemberFirstnameTh()+ " "+ memberInfo.getMemberLastnameTh());
        param.put(COURSE_NAME, cpMain.getPublicNameTh() + "/" + cpMain.getPublicNameEn());
        param.put(DATE_RESISTER, CommonUtils.formatDate(memberCourse.getRegisterDate(), "yyyy-MM-dd HH:mm" , LOCALE_THAI) );
        param.put(EMAIL, memberInfo.getMemberEmail());

        return param;
    }
    
}
