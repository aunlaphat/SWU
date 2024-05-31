package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.MemberCoupon;

import jakarta.transaction.Transactional;

/**
 * 
 * 
 * @author sitthichaim
 *
 */
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    public long countByMemberIdAndCampaignId(Long memberId, Long campaignId);

    public MemberCoupon findByMemberIdAndCampaignId(Long memberId, Long campaignId);

    @Query(value = "select mc.* from member_coupon mc join mas_campaign mcp on mc.campaign_id = mcp.campaign_id and mc.member_id = :memberId and mcp.campaign_code = :campaignCode ", nativeQuery = true)
    public MemberCoupon findByMemberIdAndCampaignCode(Long memberId, String campaignCode);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = """
			update member_coupon 
			set member_course_id = :
			where member_coupon.member_id = :
			and member_coupon.campaign_id = :
			""")
	public void updateMemberCourseCoupon(Long memberCourseId, Long memberId, Long campaignId);
	
}
