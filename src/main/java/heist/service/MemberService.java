package heist.service;

import heist.domain.Member;
import heist.dto.MemberSkillDto;
import heist.dto.view.SkillsDto;

import java.util.List;

public interface MemberService {

    Member addMember(Member member);

    void updateMember(Long memberId, MemberSkillDto memberSkillDto);

    void deleteSkill(Long memberId, String skillName);

    List<Member> listAll();

    Member getMember(Long memberId);

    SkillsDto getSkills(Long memberId);
}
