package heist.mapper;

import heist.domain.Member;
import heist.domain.Skill;
import heist.dto.view.MemberDto;
import heist.dto.view.SkillDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class MemberMapper {

    @Autowired
    private SkillMapper skillMapper;

    public MemberDto toDto(Member member){
        MemberDto memberDto = new MemberDto();

        memberDto.setName(member.getName());

        List<SkillDto> skillDtos = new LinkedList<>();

        for(Skill skill : member.getSkills())
            skillDtos.add(skillMapper.toDto(skill));

        memberDto.setSkills(skillDtos);
        return memberDto;
    }

}
