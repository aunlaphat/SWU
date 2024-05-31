/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.services;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.AutUserData;
import com.arg.swu.dto.CoursepublicGradeData;
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.CoursepublicMediaData;
import com.arg.swu.dto.MasDepartmentData;
import com.arg.swu.dto.MasGradeConfigData;
import com.arg.swu.dto.MasPersonalData;
import com.arg.swu.dto.MemberCourseData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.CoursepublicGrade;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.MasDepartment;
import com.arg.swu.models.MasPersonal;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.repositories.AutUserRepo;
import com.arg.swu.repositories.CoursepublicGradeRepository;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.MasDepartmentRepository;
import com.arg.swu.repositories.MasPersonalRepository;
import com.arg.swu.repositories.MemberCourseRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author kumpeep
 */
@Service
@RequiredArgsConstructor

public class TeacherPortalService implements ApiConstant {

    private static final Logger LOG = LogManager.getLogger(TeacherPortalService.class);

    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    private CoursepublicMainRepository coursepublicMainRepository;
    @Autowired
    private CoursepublicGradeRepository coursepublicGradeRepository;
    private final MemberCourseRepository memberCourseRepository;
    private final MasPersonalRepository masPersonalRepository;
    private final MasDepartmentRepository masDepartmentRepository;
    private final AutUserRepo autUserRepo;
    private final EntityMapperService mapperService;
    private final CommonService commonService;
    private final JmsSender jmsSender;
    public Map<String, Object> findPersonalData() {
        LOG.info(">>>>>findPersonalData");
        Map<String, Object> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        List<Object> params = new ArrayList<>();

        sb.append(" select aut_user.ref_user_id as personal_id from aut_user where aut_user.ref_user_type = 30032001 and aut_user.active_flag = true");

        List<AutUserData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(AutUserData.class));
        if (!entries.isEmpty()) {
            Long personalId = entries.get(0).getPersonalId();

            
            sb = new StringBuilder();

            sb.append("select * from mas_personal ms where ms.personal_id = ?");
            params.add(personalId);
            List<MasPersonalData> masData = jdbcTemplate.query(sb.toString(),
                    BeanPropertyRowMapper.newInstance(MasPersonalData.class), params.toArray());
//            MasPersonal masData = masPersonalRepository.findById(personalId).orElse(null);

            result.put(ENTRIES, masData);
        } else {
            result.put(ENTRIES, null);
        }

        return result;

    }

    public Map<String, Object> findDepartmentData(Long depIdLevel1) {

        Map<String, Object> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        List<Object> params = new ArrayList<>();

        sb.append(" select * from mas_department md where md.dep_id = ?");
        params.add(depIdLevel1);

        List<MasDepartmentData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(MasDepartmentData.class), params.toArray());
        if (!entries.isEmpty()) {
            result.put(ENTRIES, entries);
        } else {
            result.put(ENTRIES, null);
        }
//        MasDepartment masDepData = masDepartmentRepository.findById(depIdLevel1).orElse(null);
//        result.put(ENTRIES, masDepData);
        return result;

    }

    public Map<String, Object> findActiveCourse(Long personalId) {

        Map<String, Object> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        List<Object> params = new ArrayList<>();

        sb.append("select count(coursepublic_id) count_actived\n"
                + "from coursepublic_main cpm\n"
                + "where exists (select 1 from coursepublic_instructor cpi where instructor_id = ? and cpm.coursepublic_id = cpi.coursepublic_id )\n"
                + "and cpm.coursepublic_status in (30014003) \n"
                + "and cpm.active_flag = true");
        params.add(personalId);

        List<CoursepublicMainData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(CoursepublicMainData.class), params.toArray());
        LOG.info("Entries::" + entries);
        if (!entries.isEmpty()) {
            result.put(ENTRIES, entries);
        } else {
            result.put(ENTRIES, null);
        }

        return result;

    }

    public Map<String, Object> findCourseInfo(MasPersonalData courseInput) {

        Map<String, Object> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        StringBuilder conditions = new StringBuilder();

        List<Object> params = new ArrayList<>();
        LOG.info("courseInput.getPersonalId()::"+courseInput.getPersonalId());
        sb.append("select cpm.coursepublic_id,cpm.course_id,cm.grade_format,cm.course_code ,cpm.public_name_th ,cpm.public_name_en ,cpm.coursepublic_status, cm.course_desc_th , cm.course_desc_en , cpm.course_class_start \n"
                + ",cpm.course_class_end ,cpm.course_format,lc.name_lo, lc.name_en, cpmd.filename, cpmd.module,cpmd.prefix \n"
                + " from coursepublic_main cpm\n"
                + " join course_main cm on cpm.course_id = cm.course_id\n"
                + " join coursepublic_media cpmd on cpmd.coursepublic_id = cpm.coursepublic_id"
                + " join lookup_catalog lc on lc.catalog_id  = cpm.course_format\n"
                + " where exists (select 1 from coursepublic_instructor cpi where instructor_id = ? and cpm.coursepublic_id = cpi.coursepublic_id )\n"
                + " and cpm.active_flag = true and cpmd.media_type = 30012001");
        params.add(courseInput.getPersonalId());
        if (courseInput.getCoursepublicStatus() != null) {
            conditions.append(" and cpm.coursepublic_status = ?");
            params.add(courseInput.getCoursepublicStatus());
        } else {
            conditions.append(" and cpm.coursepublic_status in (30014003, 30014006)");
        }
        if (courseInput.getPublicNameTh() != null) {
            conditions.append(" and cpm.public_name_th like ?");
            params.add(CommonUtils.concatLikeParam(courseInput.getPublicNameTh().trim(), true, true));
        }
        if (courseInput.getPublicNameEn() != null) {
            conditions.append(" and cpm.public_name_en like ?");
            params.add(CommonUtils.concatLikeParam(courseInput.getPublicNameEn().trim(), true, true));
        }
        sb.append(conditions.toString());
        sb.append(LIMIT);
        List<CoursepublicMainData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(CoursepublicMainData.class), CommonUtils.joinParam(params.toArray(), courseInput.getPageable()));
        LOG.info("Entries:138:" + entries);
        if (!entries.isEmpty()) {
            result.put(ENTRIES, entries);
        } else {
            result.put(ENTRIES, null);
        }
        StringBuilder count = new StringBuilder();
        conditions = new StringBuilder();
        params = new ArrayList<>();
        count.append("select count(*) as count");
        count.append(" from coursepublic_main cpm ");
        count.append(" join course_main cm on cpm.course_id = cm.course_id ");
        count.append(" join coursepublic_media cpmd on cpmd.coursepublic_id = cpm.coursepublic_id" );
        count.append(" join lookup_catalog lc on lc.catalog_id  = cpm.course_format ");
        count.append(" where exists (select 1 from coursepublic_instructor cpi where instructor_id = ? and cpm.coursepublic_id = cpi.coursepublic_id ) ");
        count.append(" and cpm.active_flag = true and cpmd.media_type = 30012001");
        params.add(courseInput.getPersonalId());
         if (courseInput.getCoursepublicStatus() != null) {
            conditions.append(" and cpm.coursepublic_status = ?");
            params.add(courseInput.getCoursepublicStatus());
        } else {
            conditions.append(" and cpm.coursepublic_status in (30014003, 30014006)");
        }
        if (courseInput.getPublicNameTh() != null) {
            conditions.append(" and cpm.public_name_th like ?");
            params.add(CommonUtils.concatLikeParam(courseInput.getPublicNameTh().trim(), true, true));
        }
        if (courseInput.getPublicNameEn() != null) {
            conditions.append(" and cpm.public_name_en like ?");
            params.add(CommonUtils.concatLikeParam(courseInput.getPublicNameEn().trim(), true, true));
        }
        count.append(conditions.toString());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
        result.put(TOTAL_RECORDS, totalRecords);
        return result;

    }

    public Map<String, Object> findPassGrade(CoursepublicMainData coursepublicGradeData) {

        Map<String, Object> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        List<Object> params = new ArrayList<>();

//        sb.append("select  row_number() over (order by score_max desc) row_num,cpg.create_date,cpg.create_by_id,cpg.coursepublic_grade_id,cpg.grade_symbol,cpg.pass_status,cpg.score_max,cpg.score_min,cpg.coursepublic_id "
//                + "from coursepublic_grade cpg join coursepublic_main cpm on cpg.coursepublic_id=cpm.coursepublic_id  "
//                + "join mas_grade_config mgc on mgc.grade_symbol = cpg.grade_symbol "
//                + "where cpg.coursepublic_id = ?");
        sb.append("select row_number() over (order by conf.grade_no asc) row_num,coursepublic_g.coursepublic_grade_id\n" +
                    "	, conf.grade_symbol \n" +
                    "	, conf.grade_format \n" +
                    "	, coursepublic_g.pass_status \n" +
                    "	, coursepublic_g.score_min\n" +
                    "	, coursepublic_g.score_max\n" +
                    "   ,coursepublic_g.create_date\n"+
                    "   ,coursepublic_g.create_by_id\n"+                                           
                    "from mas_grade_config conf \n" +
                    "	join course_main course_m on conf.grade_format = course_m.grade_format \n" +
                    "	join coursepublic_main coursepublic_m on  coursepublic_m.course_id = course_m.course_id\n" +
                    "	left join coursepublic_grade coursepublic_g on conf.grade_symbol = coursepublic_g.grade_symbol and coursepublic_g.coursepublic_id = coursepublic_m.coursepublic_id\n" +
                    "	and coursepublic_g.active_flag = true\n" +
                    "where coursepublic_m.coursepublic_id = ? and conf.active_flag = true");
        
        params.add(coursepublicGradeData.getCoursepublicId());
        sb.append(" order by conf.grade_no asc");
//        sb.append(LIMIT);
        
        List<CoursepublicGradeData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class), params.toArray());
        LOG.info("Entries:138:" + entries);
        if (!entries.isEmpty()) {
            result.put(ENTRIES, entries);
        } else {
            result.put(ENTRIES, null);
        }
        StringBuilder count = new StringBuilder();
        params = new ArrayList<>();
        count.append("select count(*) as count from mas_grade_config conf \n" +
                    "	join course_main course_m on conf.grade_format = course_m.grade_format \n" +
                    "	join coursepublic_main coursepublic_m on  coursepublic_m.course_id = course_m.course_id\n" +
                    "	left join coursepublic_grade coursepublic_g on conf.grade_symbol = coursepublic_g.grade_symbol and coursepublic_g.coursepublic_id = coursepublic_m.coursepublic_id\n" +
                    "	and coursepublic_g.active_flag = true\n" +
                    "where coursepublic_m.coursepublic_id = ? and conf.active_flag = true");
        params.add(coursepublicGradeData.getCoursepublicId());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
        result.put(TOTAL_RECORDS, totalRecords);
        return result;
    }

    public Map<String, Object> findMasGradeConfig(CoursepublicMainData coursepublicGradeData) {

        Map<String, Object> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        List<Object> params = new ArrayList<>();

        sb.append("select row_number() over () row_num,* from mas_grade_config where grade_format = ?");
        params.add(coursepublicGradeData.getGradeFormat());

        List<MasGradeConfigData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(MasGradeConfigData.class), params.toArray());
        LOG.info("Entries:204:" + entries);
        if (!entries.isEmpty()) {
            result.put(ENTRIES, entries);
        } else {
            result.put(ENTRIES, null);
        }
        StringBuilder count = new StringBuilder();
        params = new ArrayList<>();
        count.append(" select count(*) as count from mas_grade_config where grade_format = ?");
        params.add(coursepublicGradeData.getGradeFormat());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
        result.put(TOTAL_RECORDS, totalRecords);
        return result;
    }

    public Map<String, Object> updatePassGrade(AutUser userAction, CoursepublicGradeData coursepublicGradeData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info("userId::" + userAction.getUserId());
        LOG.info("coursepublicGradeData.getCoursepublicId():265:" + coursepublicGradeData.getCoursepublicId());
        LOG.info("coursepublicGradeData.getGradeSymbol():266:" + coursepublicGradeData.getGradeSymbol());
        sb.append("UPDATE coursepublic_grade SET update_date= ?, pass_status=?,update_by_id=? where coursepublic_id = ?\n");
        params.add(new Date());
        params.add(Boolean.FALSE);
        params.add(userAction.getUserId());
        params.add(coursepublicGradeData.getCoursepublicId());
        jdbcTemplate.update(sb.toString(), params.toArray());
//        CoursepublicGrade obj = mapperService.convertToEntity(coursepublicGradeData, CoursepublicGrade.class);
//        obj.setPassStatus(Boolean.FALSE);
//        obj.setUpdateDate(new Date());
//        obj.setUpdateBy(userAction);
//        coursepublicGradeRepository.save(obj);
//        sb = new StringBuilder();
//        params = new ArrayList<>();
//        sb.append("select * from coursepublic_grade cpg where cpg.coursepublic_grade_id = ?");
//        params.add(coursepublicGradeData.getCoursepublicGradeId());
//        List<CoursepublicGradeData> entries = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class), params.toArray());
        CoursepublicGrade obj = coursepublicGradeRepository.findById(coursepublicGradeData.getCoursepublicGradeId()).orElse(null);
        LOG.info("getScoreMin::" + obj.getScoreMin());
        LOG.info("getCoursepublicId()::" + coursepublicGradeData.getCoursepublicId());
        LOG.info("coursepublicGradeData.getCoursepublicGradeId()::" + coursepublicGradeData.getCoursepublicGradeId());
        sb = new StringBuilder();
        params = new ArrayList<>();
        sb.append("UPDATE coursepublic_grade SET update_date= ?, pass_status=?,update_by_id=? where score_min >= ? and coursepublic_id = ?\n");
        params.add(new Date());
        params.add(Boolean.TRUE);
        params.add(userAction.getUserId());
        params.add(obj.getScoreMin());
        params.add(coursepublicGradeData.getCoursepublicId());
        jdbcTemplate.update(sb.toString(), params.toArray());
        sb = new StringBuilder();
        params = new ArrayList<>();
        sb.append("select cpg.coursepublic_grade_id,cpg.grade_symbol,cpg.pass_status,cpg.score_max,cpg.score_min,cpg.coursepublic_id from coursepublic_grade cpg where cpg.coursepublic_id = ?");
        params.add(coursepublicGradeData.getCoursepublicId());
        List<CoursepublicGradeData> entries = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class), params.toArray());
        if (!entries.isEmpty()) {
            result.put(ENTRIES, entries);
        } else {
            result.put(ENTRIES, null);
        }
        StringBuilder count = new StringBuilder();
        params = new ArrayList<>();
        count.append(" select count(*) from coursepublic_grade cpg where cpg.coursepublic_id = ?");
        params.add(coursepublicGradeData.getCoursepublicId());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
        result.put(TOTAL_RECORDS, totalRecords);
        return result;
    }

    public Map<String, Object> updateEditGrade(AutUser userAction, CoursepublicGradeData coursepublicGradeData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
            LOG.info("userId::" + userAction.getUserId());
            LOG.info("coursepublicGradeData.getCoursepublicGradeId()::" + coursepublicGradeData.getCoursepublicGradeId());
            LOG.info("coursepublicGradeData.getCoursepublicId()::" + coursepublicGradeData.getCoursepublicId());
            LOG.info("coursepublicGradeData.getGradeSymbol()::" + coursepublicGradeData.getGradeSymbol());
            LOG.info("coursepublicGradeData.getScoreMax()::" + coursepublicGradeData.getScoreMax());
            LOG.info("coursepublicGradeData.getScoreMin()::" + coursepublicGradeData.getScoreMin());
            LOG.info("coursepublicGradeData.getPassStatus()::" + coursepublicGradeData.getPassStatus());
            if(coursepublicGradeData.getScoreMax()==null&&coursepublicGradeData.getScoreMin()==null){
                coursepublicGradeData.setScoreMax(BigDecimal.ZERO);
                coursepublicGradeData.setScoreMin(BigDecimal.ZERO);
            }
              CoursepublicGrade coursepublicGradeNew = mapperService.convertToEntity(coursepublicGradeData, CoursepublicGrade.class);
            if(coursepublicGradeData.getCoursepublicGradeId()!=null){
                coursepublicGradeNew.setCoursepublicGradeId(coursepublicGradeData.getCoursepublicGradeId());
                coursepublicGradeNew.setCreateDate(coursepublicGradeData.getCreateDate());
                AutUser createBy = autUserRepo.findByUserId(coursepublicGradeData.getCreateById());
                coursepublicGradeNew.setCreateBy(createBy);
                coursepublicGradeNew.setPassStatus(coursepublicGradeData.getPassStatus());
                coursepublicGradeNew.setUpdateBy(userAction);
                coursepublicGradeNew.setUpdateDate(new Date());
                
            }else{
                coursepublicGradeNew.setPassStatus(Boolean.FALSE);
                coursepublicGradeNew.setCreateDate(new Date());
                coursepublicGradeNew.setCreateBy(userAction);
            }
//            sb.append("select * from coursepublic_grade cg join mas_grade_config mgc on mgc.grade_symbol = cg.grade_symbol where cg.coursepublic_id = ? and cg.pass_status = true order by cg.score_max desc,mgc.grade_no asc limit 1 ");
//            params.add(coursepublicGradeData.getCoursepublicId());
//            List<CoursepublicGradeData> obj = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class), params.toArray());
//            if(!obj.isEmpty()){
//                if(coursepublicGradeData.getScoreMax().compareTo(obj.get(0).getScoreMin()) > 0){
//                   coursepublicGradeNew.setPassStatus(Boolean.TRUE); 
//                }else{
//                   coursepublicGradeNew.setPassStatus(Boolean.FALSE);  
//                }
//
//            }else{
//                coursepublicGradeNew.setPassStatus(Boolean.FALSE); 
//            }
   //        sb.append("UPDATE coursepublic_grade SET update_date= ?,update_by_id=?,score_max=?,score_min=? where coursepublic_id = ? and grade_symbol = ? \n");
   //        params.add(new Date());
   //        params.add(userId);
   //        params.add(coursepublicGradeData.getScoreMax());
   //        params.add(coursepublicGradeData.getScoreMin());
   //        params.add(coursepublicGradeData.getCoursepublicId());
   //        params.add(coursepublicGradeData.getGradeSymbol());        

        coursepublicGradeRepository.save(coursepublicGradeNew);
   //        jdbcTemplate.update(sb.toString(), params.toArray());

           sb = new StringBuilder();
           params = new ArrayList<>();
           sb.append("select row_number() over (order by conf.grade_no asc) row_num,coursepublic_g.coursepublic_grade_id\n" +
                    "	, conf.grade_symbol \n" +
                    "	, conf.grade_format \n" +
                    "	, coursepublic_g.pass_status \n" +
                    "	, coursepublic_g.score_min\n" +
                    "	, coursepublic_g.score_max\n" +
                    "   ,coursepublic_g.create_date\n"+
                    "   ,coursepublic_g.create_by_id\n"+                                           
                    "from mas_grade_config conf \n" +
                    "	join course_main course_m on conf.grade_format = course_m.grade_format \n" +
                    "	join coursepublic_main coursepublic_m on  coursepublic_m.course_id = course_m.course_id\n" +
                    "	left join coursepublic_grade coursepublic_g on conf.grade_symbol = coursepublic_g.grade_symbol and coursepublic_g.coursepublic_id = coursepublic_m.coursepublic_id\n" +
                    "	and coursepublic_g.active_flag = true\n" +
                    "where coursepublic_m.coursepublic_id = ? and conf.active_flag = true");;
           params.add(coursepublicGradeData.getCoursepublicId());
           List<CoursepublicGradeData> entries = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class), params.toArray());
           if (!entries.isEmpty()) {
               result.put(ENTRIES, entries);
           } else {
               result.put(ENTRIES, null);
           }
           StringBuilder count = new StringBuilder();
           params = new ArrayList<>();
           count.append(" select count(*) from coursepublic_grade cpg where cpg.coursepublic_id = ?");
           params.add(coursepublicGradeData.getCoursepublicId());
           Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
           result.put(TOTAL_RECORDS, totalRecords);
        return result;
    }

    public Map<String, Object> findStudentList(MemberCourseData memberCourseData) {
        LOG.info(">>>findStudentList<<<");
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder conditions = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info(">>>memberCourseData.getCoursepublicId()::"+memberCourseData.getCoursepublicId());
        
        sb.append("select row_number() over () row_num,mc.*,mi.member_no,mi.member_id ,mi.member_firstname_th,mi.member_lastname_th "
                + ",mi.member_firstname_en ,mi.member_lastname_en,lc.name_lo as paymentNameLo ,lc.name_en as paymentNameEn,lc2.name_lo as studyStatusNameLo,"
                + "sp.prefixname_code,sp.prefixname_name_th,sp.prefixname_name_en,lc2.name_en as studyStatusNameEn  "
                + "from member_course mc join member_info mi on mc.member_id =mi.member_id "
                + "join lookup_catalog lc on lc.catalog_id =mc.payment_status "
                + "join sys_prefixname sp on sp.prefixname_id =mi.prefixname_id "
                + "join lookup_catalog lc2 on lc2.catalog_id = mc.study_status where mc.coursepublic_id =? ");
        params.add(memberCourseData.getCoursepublicId());
        if (memberCourseData.getMemberNo() != null) {
            conditions.append(" and mi.member_no= ?");
            params.add(memberCourseData.getMemberNo());
        }
        if (memberCourseData.getPaymentStatus() != null) {
            conditions.append(" and mc.payment_status = ?");
            params.add(memberCourseData.getPaymentStatus());
        }else{
            conditions.append(" and mc.payment_status in (30017002,30017003)");
        }
        if (memberCourseData.getStudyStatus() != null) {
            conditions.append(" and mc.study_status = ?");
            params.add(memberCourseData.getStudyStatus());
        }else{
            conditions.append(" and mc.study_status in (30016001,30016002)");
        }
        if (memberCourseData.getNameOrSurnameTh() != null && !memberCourseData.getNameOrSurnameTh().equals("")) {
            conditions.append(" and mi.member_firstname_th like ? or mi.member_lastname_th like ?");
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameTh(), true, true));
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameTh(), true, true));
        }
        if (memberCourseData.getNameOrSurnameEn() != null && !memberCourseData.getNameOrSurnameEn().equals("")) {
            conditions.append(" and mi.member_firstname_en like ? or mi.member_lastname_en like ?");
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameEn(), true, true));
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameEn(), true, true));
        }
        sb.append(conditions.toString());
        sb.append(LIMIT);
        List<MemberCourseData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(MemberCourseData.class),
                CommonUtils.joinParam(params.toArray(), memberCourseData.getPageable()));
        LOG.info("entries:420:"+entries);
        StringBuilder count = new StringBuilder();
        count.append("select count(*) from member_course mc join member_info mi on mc.member_id =mi.member_id "
                + "join lookup_catalog lc on lc.catalog_id =mc.payment_status "
                + "join lookup_catalog lc2 on lc2.catalog_id = mc.study_status where mc.coursepublic_id =? ");
        count.append(conditions.toString());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

        result.put(ENTRIES, entries);
        result.put(TOTAL_RECORDS, totalRecords);

        return result;
    }

    public Map<String, Object> findStudyResultList(MemberCourseData memberCourseData) {
        LOG.info(">>>>>>>>>>>>findStudyResultList<<<<<<<<<<<<<<");
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder conditions = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info(">>>>memberCourseData.getMemberNo::"+memberCourseData.getMemberNo());
        sb.append("select row_number() over (order by mc.create_date desc) row_num,mc.*,mi.member_no,mi.member_id,sp.prefixname_code,sp.prefixname_name_th,sp.prefixname_name_en,  \n" +
                    "mi.member_firstname_th,mi.member_lastname_th ,mi.member_firstname_en ,mi.member_lastname_en,mi.moodle_user_id, \n" +
                    "lc2.name_lo as studyStatusNameLo,lc2.name_en as studyStatusNameEn\n" +
                    "from member_course mc \n" +
                    "join member_info mi on mc.member_id =mi.member_id \n" +
                    "join sys_prefixname sp on sp.prefixname_id =mi.prefixname_id \n" +
                    "join lookup_catalog lc2 on lc2.catalog_id = mc.study_status \n" +
                    "where mc.study_status in (30016001, 30016002) and mc.payment_status in (30017002,30017003)  and mc.active_flag = true");
        if (memberCourseData.getMemberNo() != null && !memberCourseData.getMemberNo().equals("")) {
            conditions.append(" and mi.member_no = ?");
            params.add(memberCourseData.getMemberNo().trim());
        }
        if (memberCourseData.getNameOrSurnameTh() != null && !memberCourseData.getNameOrSurnameTh().equals("")) {
            conditions.append(" and ( sp.prefixname_name_th like ? or mi.member_firstname_th like ? or mi.member_lastname_th like ? )");
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameTh().trim(), true, true));
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameTh().trim(), true, true));
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameTh().trim(), true, true));
        }
        if (memberCourseData.getNameOrSurnameEn() != null && !memberCourseData.getNameOrSurnameEn().equals("")) {
            conditions.append(" and ( sp.prefixname_name_en like ? or mi.member_firstname_en like ? or mi.member_lastname_en like ? )");
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameEn().trim(), true, true));
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameEn().trim(), true, true));
            params.add(CommonUtils.concatLikeParam(memberCourseData.getNameOrSurnameEn().trim(), true, true));
        }
        if (memberCourseData.getStudyStatus() != null) {
            conditions.append(" and mc.study_status = ?");
            params.add(memberCourseData.getStudyStatus());
        }
        if (memberCourseData.getCoursepublicId() != null) {
            LOG.info("coursepublic_id::" + memberCourseData.getCoursepublicId());
            conditions.append(" and mc.coursepublic_id = ?");
            params.add(memberCourseData.getCoursepublicId());
        }
        sb.append(conditions.toString());
        sb.append(" order by mc.create_date desc ");
//        sb.append(LIMIT);
        List<MemberCourseData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(MemberCourseData.class),
                params.toArray());

        StringBuilder count = new StringBuilder();
        count.append("select count(*) from member_course mc \n" +
                    "join member_info mi on mc.member_id =mi.member_id \n" +
                    "join sys_prefixname sp on sp.prefixname_id =mi.prefixname_id \n" +
                    "join lookup_catalog lc2 on lc2.catalog_id = mc.study_status \n" +
                    "where mc.study_status in (30016001, 30016002) and mc.payment_status in (30017002,30017003)  and mc.active_flag = true");
        count.append(conditions.toString());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

        result.put(ENTRIES, entries);
        result.put(TOTAL_RECORDS, totalRecords);

        return result;
    }

    public Map<String, Object> findPointInRange(CoursepublicGradeData memberCourseData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sb.append("select cpg.coursepublic_id,cpg.grade_symbol , cpg.pass_status from coursepublic_grade cpg where cpg.score_min<=? and ? <=cpg.score_max and cpg.coursepublic_id=?");
        params.add(memberCourseData.getScore());
        params.add(memberCourseData.getScore());
        params.add(memberCourseData.getCoursepublicId());
        List<CoursepublicGradeData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class),
                params.toArray());
        result.put(ENTRIES, entries);
        return result;
    }

    public Map<String, Object> putMemberData(AutUser user,Long memberCourseId, MemberCourseData memberCourseData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info(">>>memberCourseId::"+memberCourseId);
        LOG.info(">>>memberCourseData.getResultScore:" + memberCourseData.getResultScore());
        LOG.info(">>>memberCourseData.getResultGrade():" + memberCourseData.getResultGrade());
        LOG.info(">>>memberCourseData.getMemberId():" + memberCourseData.getMemberId());
        LOG.info(">>>memberCourseData.getCoursepublicId():" + memberCourseData.getCoursepublicId());
        LOG.info(">>>>496<<<<");
        MemberCourse obj = mapperService.convertToEntity(memberCourseData, MemberCourse.class);
        obj.setUpdateDate(new Date());
        obj.setUpdateBy(user);
        LOG.info(">>>>memberCourseData.getCreateById():"+memberCourseData.getCreateById());
        AutUser createBy = autUserRepo.findByUserId(memberCourseData.getCreateById());
        obj.setCreateBy(createBy);
        obj.setCreateDate(obj.getCreateDate());
        memberCourseRepository.save(obj);
        LOG.info(">>>>504<<<<");
        memberCourseData = mapperService.convertToEntity(obj, MemberCourseData.class);
        LOG.info(">>>>>>MemberCourseData::"+memberCourseData);
        if (memberCourseData.getResultGrade() != null) {
            LOG.info("Result grade:" + memberCourseData.getResultGrade());
            LOG.info("member_course_id::"+memberCourseId);
            LOG.info("memberCourseData.getCoursepublicId()::"+memberCourseData.getCoursepublicId());
            sb = new StringBuilder();
            params = new ArrayList<>();
            sb.append("select cpg.pass_status from coursepublic_grade cpg join member_course mc on mc.result_grade = cpg.grade_symbol where mc.result_grade = ? and mc.member_course_id = ? and mc.coursepublic_id =? and cpg.pass_status is not null");
            params.add(memberCourseData.getResultGrade());
            params.add(memberCourseId);
            params.add(memberCourseData.getCoursepublicId());
            List<CoursepublicGradeData> entries2 = jdbcTemplate.query(sb.toString(),
                    BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class),
                    params.toArray());
            LOG.info("entries2.get(0)::" + entries2.get(0));
            if (entries2.get(0).getPassStatus() != null) {
                LOG.info("entries2.get(0).getPassStatus()::"+entries2.get(0).getPassStatus());
                  obj = mapperService.convertToEntity(memberCourseData, MemberCourse.class);
                  obj.setPassStatus(entries2.get(0).getPassStatus());
                  obj.setPassDate(new Date());
                  obj.setUpdateDate(new Date());
                  obj.setUpdateBy(user);
                  createBy = autUserRepo.findByUserId(memberCourseData.getCreateById());
                  obj.setCreateBy(createBy);
                  obj.setCreateDate(obj.getCreateDate());
                  memberCourseRepository.save(obj);
                  memberCourseData = mapperService.convertToEntity(obj, MemberCourseData.class);
//                if (memberCourseData.getPaymentStatus() != 30017001L) {
//                    obj = mapperService.convertToEntity(memberCourseData, MemberCourse.class);
//                    obj.setStudyStatus(30016002L);
//                    obj.setUpdateDate(new Date());
//                    obj.setUpdateBy(user);
//                    createBy = autUserRepo.findByUserId(memberCourseData.getCreateById());
//                    obj.setCreateBy(createBy);
//                    obj.setCreateDate(obj.getCreateDate());
//                    memberCourseRepository.save(obj);
//                    memberCourseData = mapperService.convertToEntity(obj, MemberCourseData.class);
//                }
            }
        }
        sb = new StringBuilder();
        params = new ArrayList<>();
        sb.append("select * from member_course where member_id=? and coursepublic_id = ?");
        params.add(memberCourseData.getMemberId());
        params.add(memberCourseData.getCoursepublicId());
        List<MemberCourseData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(MemberCourseData.class),
                params.toArray());
        result.put(ENTRIES, entries);
        return result;
    }
    
    public Map<String, Object> putConfirmMemberData(AutUser user,Long memberCourseId, MemberCourseData memberCourseData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info(">>>memberCourseId::"+memberCourseId);
        LOG.info(">>>memberCourseData.getCourseCode()::"+memberCourseData.getCourseCode());
        LOG.info(">>>memberCourseData.getResultScore:" + memberCourseData.getResultScore());
        LOG.info(">>>memberCourseData.getResultGrade():" + memberCourseData.getResultGrade());
        LOG.info(">>>memberCourseData.getMemberId():" + memberCourseData.getMemberId());
        LOG.info(">>>memberCourseData.getCoursepublicId():" + memberCourseData.getCoursepublicId());
        LOG.info(">>>>496<<<<");
        MemberCourse obj = mapperService.convertToEntity(memberCourseData, MemberCourse.class);
        obj.setUpdateDate(new Date());
        obj.setUpdateBy(user);
        LOG.info(">>>>memberCourseData.getCreateById():"+memberCourseData.getCreateById());
        AutUser createBy = autUserRepo.findByUserId(memberCourseData.getCreateById());
        obj.setCreateBy(createBy);
        obj.setCreateDate(obj.getCreateDate());
        if(memberCourseData.getPassStatus()){
            obj.setCertificateNo(commonService.generateMemberCourseCertificateRunningNo(memberCourseData.getCourseCode()));
        }
        memberCourseRepository.save(obj);
        
        LOG.info(">>>>608<<<<");
        memberCourseData = mapperService.convertToEntity(obj, MemberCourseData.class);
        LOG.info(">>>>>>MemberCourseData::"+memberCourseData);
        if (memberCourseData.getResultGrade() != null) {
            LOG.info("Result grade:" + memberCourseData.getResultGrade());
            LOG.info("member_course_id::"+memberCourseId);
            LOG.info("memberCourseData.getCoursepublicId()::"+memberCourseData.getCoursepublicId());
            sb = new StringBuilder();
            params = new ArrayList<>();
            sb.append("select cpg.pass_status from coursepublic_grade cpg join member_course mc on mc.result_grade = cpg.grade_symbol where mc.result_grade = ? and mc.member_course_id = ? and mc.coursepublic_id =? and cpg.pass_status is not null");
            params.add(memberCourseData.getResultGrade());
            params.add(memberCourseId);
            params.add(memberCourseData.getCoursepublicId());
            List<CoursepublicGradeData> entries2 = jdbcTemplate.query(sb.toString(),
                    BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class),
                    params.toArray());
            LOG.info("entries2.get(0)::" + entries2.get(0));
            if (entries2.get(0).getPassStatus() != null) {
                  obj = mapperService.convertToEntity(memberCourseData, MemberCourse.class);
                  LOG.info("entries2.get(0).getPassStatus()::"+entries2.get(0).getPassStatus());
                  obj.setPassStatus(entries2.get(0).getPassStatus());
                  obj.setPassDate(new Date());
                  obj.setUpdateDate(new Date());
                  obj.setUpdateBy(user);
                  createBy = autUserRepo.findByUserId(memberCourseData.getCreateById());
                  obj.setCreateBy(createBy);
                  obj.setCreateDate(obj.getCreateDate());
                  memberCourseRepository.save(obj);
                  memberCourseData = mapperService.convertToEntity(obj, MemberCourseData.class);
                if (memberCourseData.getPaymentStatus() != 30017001L) {
                    obj = mapperService.convertToEntity(memberCourseData, MemberCourse.class);
                    obj.setStudyStatus(30016002L);
                    obj.setUpdateDate(new Date());
                    obj.setUpdateBy(user);
                    createBy = autUserRepo.findByUserId(memberCourseData.getCreateById());
                    obj.setCreateBy(createBy);
                    obj.setCreateDate(obj.getCreateDate());
                    memberCourseRepository.save(obj);
                    memberCourseData = mapperService.convertToEntity(obj, MemberCourseData.class);
                }  
            }
            sb = new StringBuilder();
            params = new ArrayList<>();
            sb.append("update member_info mi set finish_course_taken=( ");
            sb.append("select count(mc.member_course_id) from member_course mc ");
            sb.append("where mc.member_id=mi.member_id and mc.payment_status in (30017002,30017003) and mc.study_status='30016002' "); 
            sb.append("), ");
            sb.append("credit_bank =( ");
            sb.append("select sum(cm.credit_amount) from member_course mc ");
            sb.append("join course_main cm on mc.course_id = cm.course_id ");
            sb.append("join mas_course_type mct on cm.course_type_id = mct.course_type_id ");
            sb.append("where mc.member_id=? and mc.study_status='30016002' and mct.course_mapping_status=true) ");
            sb.append("where mi.member_id =?");
            params.add(memberCourseData.getMemberId());
            params.add(memberCourseData.getMemberId());
            jdbcTemplate.update(sb.toString(), params.toArray());
        }
        sb = new StringBuilder();
        params = new ArrayList<>();
        sb.append("select * from member_course where member_id=? and coursepublic_id = ?");
        params.add(memberCourseData.getMemberId());
        params.add(memberCourseData.getCoursepublicId());
        List<MemberCourseData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(MemberCourseData.class),
                params.toArray());
        result.put(ENTRIES, entries);
        if(memberCourseData.getPassStatus()){
            jmsSender.sendMessage(Q_PRINTING, String.valueOf(memberCourseData.getMemberCourseId()));
        }
        return result;
    }
    
    public Map<String, Object> findMemberCount(MemberCourseData memberCourseData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info(">>>>>coursepublicId:::"+memberCourseData.getCoursepublicId());
        sb.append("select count(member_course_id) as member_count \n"
                + "from member_course mc join coursepublic_main cpm on mc.coursepublic_id = cpm.coursepublic_id \n"
                + "where mc.active_flag = true \n"
                + "and mc.coursepublic_id = ?");
        params.add(memberCourseData.getCoursepublicId());
        List<MemberCourseData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(MemberCourseData.class),
                params.toArray());
        result.put(ENTRIES, entries);
        return result;
    }

    public Map<String, Object> deleteGrade(CoursepublicGradeData coursepublicGradeData, AutUser userAction) {
        Map<String, Object> result = new HashMap<>();
//        StringBuilder sb = new StringBuilder();
//        List<Object> params = new ArrayList<>();
//        LOG.info(">>>coursepublicGradeData.getCoursepublicGradeId:" + coursepublicGradeData.getCoursepublicGradeId());
//        sb.append("delete from coursepublic_grade where coursepublic_grade_id= ? \n");
//        params.add(coursepublicGradeData.getCoursepublicGradeId());
//        Integer entries = jdbcTemplate.update(sb.toString(), params.toArray());
//        LOG.info(">>>>entries::" + entries);
//        result.put(ENTRIES, entries);
        CoursepublicGrade obj = mapperService.convertToEntity(coursepublicGradeData, CoursepublicGrade.class);
        if(obj.getCoursepublicGradeId()!=null){
                coursepublicGradeRepository.deleteById(obj.getCoursepublicGradeId());
        }
        return result;
    }

    public Map<String, Object> findPassingCriteria(CoursepublicGradeData coursepublicGradeData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info(">>>>>>>CoursePublicId::" + coursepublicGradeData.getCoursepublicId());
        sb.append("select count (coursepublic_grade.pass_status) from coursepublic_grade where coursepublic_grade.coursepublic_id = ?");
        params.add(coursepublicGradeData.getCoursepublicId());
        Long count = jdbcTemplate.queryForObject(sb.toString(), Long.class, params.toArray());
        result.put(TOTAL_RECORDS, count);
        return result;
    }

    public Map<String, Object> findPointRangeCriteria(CoursepublicGradeData coursepublicGradeData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        Boolean rangeUnsequenced = false;
        LOG.info(">>>>>>>CoursePublicId::" + coursepublicGradeData.getCoursepublicId());
        sb.append("select coursepublic_grade.score_max,coursepublic_grade.score_min from coursepublic_grade where coursepublic_grade.coursepublic_id = ?");
        params.add(coursepublicGradeData.getCoursepublicId());
        sb.append(" order by coursepublic_grade.score_min ");
        List<CoursepublicGradeData> entries = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class), params.toArray());
        if (!entries.isEmpty()) {
            for (int i = 0; i < entries.size(); i++) {
                BigDecimal scoreMax = entries.get(i).getScoreMax();
                if (i + 1 != entries.size()) {
                    BigDecimal nextScoreMin = entries.get(i + 1).getScoreMin();
                    if (nextScoreMin.subtract(scoreMax).compareTo(BigDecimal.ONE) == 1) {
                        rangeUnsequenced = true;
                        break;
                    }
                }
            }
        }
        LOG.info(">>>rangeUnsequenced::" + rangeUnsequenced);
        result.put(ENTRIES, rangeUnsequenced);
        return result;
    }
    
    public Map<String, Object> updateStatusCourse(AutUser userAction,CoursepublicMainData coursepublicMainData) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info(">>>>>>>CoursePublicId::" + coursepublicMainData.getCoursepublicId());
        sb.append("UPDATE coursepublic_main SET coursepublic_status  = 30014006 where coursepublic_id = ? \n");
        params.add(coursepublicMainData.getCoursepublicId());
        Integer entries = jdbcTemplate.update(sb.toString(), params.toArray());
        LOG.info(">>>>entries::" + entries);
        result.put(ENTRIES, entries);
//        CoursepublicMain obj = mapperService.convertToEntity(coursepublicMainData, CoursepublicMain.class);
//        obj.setCoursepublicStatus(30014006L);
//        obj.setUpdateBy(userAction);
//        obj.setUpdateDate(new Date());
//        coursepublicMainRepository.save(obj);
//        coursepublicMainData = mapperService.convertToEntity(obj, CoursepublicMainData.class);
//        result.put(ENTRIES, coursepublicMainData);
        return result;
    }
    
    public Map<String, Object> findCourseThumbnail(CoursepublicMainData criteria) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        LOG.info(">>>>>>>CoursePublicId::" + criteria.getCoursepublicId());
        sb.append("select * from coursepublic_media cm  where coursepublic_id = ? and media_type = 30012001");
        params.add(criteria.getCoursepublicId());
        List<CoursepublicMediaData> entries = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(CoursepublicMediaData.class), params.toArray());
        result.put(ENTRIES, entries);
        return result;
    }
    
    public Map<String,Object> findPersonalDataByName(String teacherName){
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sb.append("select * from mas_personal mp where mp.prefix_short_th=? or mp.prefix_short_en=? or mp.firstname_th like ? or mp.lastname_th like ? or mp.firstname_en like ? or mp.lastname_en like ?");
        params.add(CommonUtils.concatLikeParam(teacherName, true, true));
        params.add(CommonUtils.concatLikeParam(teacherName, true, true));
        params.add(CommonUtils.concatLikeParam(teacherName, true, true));
        params.add(CommonUtils.concatLikeParam(teacherName, true, true));
        params.add(CommonUtils.concatLikeParam(teacherName, true, true));
        params.add(CommonUtils.concatLikeParam(teacherName, true, true));
        List<MasPersonalData> mpd = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(MasPersonalData.class), params.toArray());
        result.put(ENTRIES, mpd);
        return result;
    }
    
    public Map<String,Object> findSelectedGrade(Long coursepublicId){
        
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sb.append("select * from coursepublic_grade where coursepublic_id = ? and pass_status = true order by score_min asc limit 1");
        params.add(coursepublicId);
        List<CoursepublicGradeData> cpgd = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(CoursepublicGradeData.class), params.toArray());
        result.put(ENTRIES, cpgd);
        return result;
    }
}
