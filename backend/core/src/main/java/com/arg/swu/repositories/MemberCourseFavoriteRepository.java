package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.MemberCourseFavorite;

public interface MemberCourseFavoriteRepository extends JpaRepository<MemberCourseFavorite, Long> {

    public List<MemberCourseFavorite> findByMemberId(Long memberId);

}
