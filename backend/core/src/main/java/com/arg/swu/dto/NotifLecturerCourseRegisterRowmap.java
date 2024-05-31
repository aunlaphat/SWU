package com.arg.swu.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

public class NotifLecturerCourseRegisterRowmap implements RowMapper<NotifLecturerCourseRegister> {

    @Override
    @Nullable
    public NotifLecturerCourseRegister mapRow(ResultSet rs, int rowNum) throws SQLException {
        return NotifLecturerCourseRegister.builder()
        .instructorNameTh(rs.getString("instructor_name_th"))
        .instructorNameEn(rs.getString("instructor_name_en"))
        .studentNameTh(rs.getString("student_name_th"))
        .studentNameEn(rs.getString("student_name_en"))
        .memberId(rs.getLong("member_id"))
        .courseCode(rs.getString("course_code"))
        .publicNameTh(rs.getString("public_name_th"))
        .publicNameEn(rs.getString("public_name_en"))
        .email(rs.getString("email")).build();
    }
 

}
