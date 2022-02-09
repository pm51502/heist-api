package heist.service.impl;

import heist.dao.MemberRepository;
import heist.dao.SkillRepository;
import heist.domain.Member;
import heist.domain.Skill;
import heist.dto.MemberSkillDto;
import heist.dto.view.SkillsDto;
import heist.mapper.SkillsMapper;
import heist.service.EmailService;
import heist.service.MemberService;
import heist.service.exceptions.MemberNotFoundException;
import heist.service.exceptions.RequestDeniedException;
import heist.service.exceptions.SkillNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberServiceJpa implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillsMapper skillsMapper;

    @Autowired
    private EmailService emailService;

    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final String LEVEL_FORMAT = "\\*{1,10}";

    @Override
    public Member addMember(Member member) {
        Assert.notNull(member, "Member object must be given");
        Assert.isNull(member.getMemberId(), "Member ID must be null, not " + member.getMemberId());
        Assert.notNull(member.getName(), "Member name must be given");
        Assert.notNull(member.getSex(), "Member sex must be given");

        Assert.notNull(member.getEmail(), "Member email must be given");
        Assert.isTrue(member.getEmail().matches(EMAIL_FORMAT), "Member email must be in valid format");

        if(memberRepository.countByEmail(member.getEmail()) > 0)
            throw new RequestDeniedException("Member with email '" + member.getEmail() + "' already exists");

        List<String> skillNames = member.getSkills().stream().map(Skill::getName).collect(Collectors.toList());
        Assert.isTrue(skillNames.stream().allMatch(new HashSet<>()::add), "Multiple skills having the same name were provided");

        if(member.getMainSkill() != null)
            Assert.isTrue(skillNames.contains(member.getMainSkill()), "Member main skill must reference one of the skills form skills array");

        Assert.notNull(member.getStatus(), "Member status must be given");

        for(Skill skill : member.getSkills()){
            checkSkill(skill);

            skill.setMember(member);
        }

        emailService.SendSimpleMessage(member.getEmail(),
                "Member addition",
                "Congrats, you have been added as member\nHeist API team");

        return memberRepository.save(member);
    }

    private void checkSkill(Skill skill){
        Assert.notNull(skill.getName(), "Skill name must be given");
        skill.setName(skill.getName().toLowerCase());

        if(skill.getLevel() != null)
            Assert.isTrue(skill.getLevel().matches(LEVEL_FORMAT), "Skill level must be made of 1 to 10 asterisk characters");
    }

    @Override
    public void updateMember(Long memberId, MemberSkillDto memberSkillDto) {
        Member member = memberRepository.findByMemberId(memberId);

        if(member == null)
            throw new MemberNotFoundException("Member with id " + memberId + " does not exist");

        List<Skill> oldSkills = member.getSkills();
        Set<Skill> newSkills = new HashSet<>();

        if(memberSkillDto.getSkills() != null){
            List<String> newSkillNames = memberSkillDto.getSkills().stream().map(Skill::getName).collect(Collectors.toList());
            Assert.isTrue(newSkillNames.stream().allMatch(new HashSet<>()::add), "Multiple skills having the same name were provided");

            for(Skill skill : memberSkillDto.getSkills()){
                checkSkill(skill);

                skill.setMember(member);
                newSkills.add(skill);
            }
            newSkills.addAll(oldSkills);

            List<Skill> newSkillsList = new ArrayList<>(newSkills);
            member.setSkills(newSkillsList);
        }

        if(memberSkillDto.getMainSkill() != null) {
            List<String> skillNames = newSkills.stream().map(Skill::getName).collect(Collectors.toList());
            Assert.isTrue(skillNames.contains(memberSkillDto.getMainSkill()), "Updated main skill is not part of the member’s previous or updated skill array");

            member.setMainSkill(memberSkillDto.getMainSkill());
        }

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteSkill(Long memberId, String skillName) {
        Member member = memberRepository.findByMemberId(memberId);

        if(member == null)
            throw new MemberNotFoundException("Member with id " + memberId + " does not exist");

        if(!member.getSkills().stream().map(Skill::getName).collect(Collectors.toList()).contains(skillName))
            throw new SkillNotFoundException("Member's skill does not exist");

        System.out.println(skillRepository.deleteCustom(skillName, memberId));
    }

    @Override
    public List<Member> listAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member getMember(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        if(member == null)
            throw new MemberNotFoundException("Member with id " + memberId + " does not exist");

        return member;
    }

    @Override
    public SkillsDto getSkills(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        if(member == null)
            throw new MemberNotFoundException("Member with id " + memberId + " does not exist");

        return skillsMapper.toDto(member.getSkills(), member.getMainSkill());
    }
}
