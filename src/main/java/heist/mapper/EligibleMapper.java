package heist.mapper;

import heist.domain.HeistSkill;
import heist.domain.Member;
import heist.dto.view.EligibleDto;
import heist.dto.view.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class EligibleMapper {

    @Autowired
    private MemberMapper memberMapper;

    public EligibleDto toDto(List<HeistSkill> skills, List<Member> members){
        EligibleDto eligibleDto = new EligibleDto();

        eligibleDto.setHeistSkills(skills);

        List<MemberDto> memberDtos = new LinkedList<>();
        for(Member member : members)
            memberDtos.add(memberMapper.toDto(member));

        eligibleDto.setMembers(memberDtos);
        return eligibleDto;
    }

}