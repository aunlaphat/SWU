package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.MemberSimulateTranscript;

public interface MemberSimulateTranscriptRepository extends JpaRepository<MemberSimulateTranscript, Long> {

    public List<MemberSimulateTranscript> findByMemberId(Long memberId);

    public MemberSimulateTranscript findOneByMemberIdAndCourseId(Long memberId, Long CourseId);

}
