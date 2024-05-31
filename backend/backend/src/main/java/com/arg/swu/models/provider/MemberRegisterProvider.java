package com.arg.swu.models.provider;

import java.util.HashMap;
import java.util.Map;

import com.arg.swu.models.MemberInfo;
import com.arg.swu.models.handler.EmailProviderInterface;
import com.arg.swu.repositories.MemberInfoRepository;

import io.jsonwebtoken.lang.Assert;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberRegisterProvider extends EmailProviderInterface
{


    private MemberInfoRepository memberRepo;
    private Long memberId;
    private String subject;
    private String envUrl;

    @Override
    public Map<String, Object> generateParam() {
        if(null == memberId)
        {
            Assert.notNull(memberId, "memberId not null");
        }
        MemberInfo member = memberRepo.findById(memberId).orElse(null);

        if(null == member)
        {
            Assert.notNull(member, "member not found");
        }
        /*
            -name = member_info.member_firstname_th+' '+member_info.member_lastname_th
            -leaner_id = member_info.member_id
            -email = member_info.member_email
            -password = User กรอกเข้ามา
         */
        
        Map<String, Object> param = new HashMap<>();
        param.put("name", member.getMemberFirstnameTh()+ " "+ member.getMemberLastnameTh());
        param.put("leanerId", member.getMemberId());
        param.put("email", member.getMemberEmail());
        param.put("forgetPasswordToken", envUrl+"/reset-password?token="+member.getForgetPasswordToken());
        param.put("password", "");


        return param;
    }
    
}
