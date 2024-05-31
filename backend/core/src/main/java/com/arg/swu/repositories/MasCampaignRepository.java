package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.MasCampaign;

public interface MasCampaignRepository extends JpaRepository<MasCampaign, Long> {

    public List<MasCampaign> findByActiveFlagTrue();

    @Query(value = "select * from mas_campaign mc where (now() between start_date and end_date) and campaign_full > (select count(member_coupon_id) from member_coupon mc2 where mc.campaign_id = mc2.campaign_id) and mc.active_flag = true", nativeQuery = true)
    public List<MasCampaign> findWithMemberCoupon();

    @Query(value = "select * from mas_campaign mc where (now() between start_date and end_date) and campaign_full > (select count(member_coupon_id) from member_coupon mc2 where mc.campaign_id = mc2.campaign_id) and mc.active_flag = true and mc.campaign_code = :campaignCode", nativeQuery = true)
    public List<MasCampaign> findWithMemberCoupon(String campaignCode);

    @Query(value = "select mc.* from mas_campaign mc join member_coupon mcp on mc.campaign_id = mcp.campaign_id where mc.active_flag = true and mcp.active_flag = true and mc.campaign_code = :campaignCode AND mcp.member_id = :memberId limit 1", nativeQuery = true)
    public MasCampaign findFirstWithMemberCoupon(String campaignCode, Long memberId);

    @Query(value = "select mc.* from mas_campaign mc join mas_campaign_coursepublic mcc on mc.campaign_id = mcc.campaign_id where mc.active_flag = true and mcc.active_flag = true and mc.campaign_code = :campaignCode AND mcc.coursepublic_id = :coursepublicId limit 1", nativeQuery = true)
    public MasCampaign findFirstWithMasCampaignCoursepublic(String campaignCode, Long coursepublicId);

}
