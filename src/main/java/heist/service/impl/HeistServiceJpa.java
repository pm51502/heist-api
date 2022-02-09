package heist.service.impl;

import heist.config.EndTask;
import heist.config.StartTask;
import heist.dao.HeistRepository;
import heist.dao.MemberRepository;
import heist.dao.SkillRepository;
import heist.domain.Heist;
import heist.domain.HeistSkill;
import heist.domain.Member;
import heist.domain.enums.HeistStatus;
import heist.domain.enums.Status;
import heist.dto.HeistMemberDto;
import heist.dto.view.ConfirmedDto;
import heist.dto.view.EligibleDto;
import heist.dto.HeistSkillDto;
import heist.dto.view.HeistStatusDto;
import heist.dto.view.OutcomeDto;
import heist.mapper.ConfirmedMapper;
import heist.mapper.EligibleMapper;
import heist.service.EmailService;
import heist.service.HeistService;
import heist.service.enums.HeistOutcome;
import heist.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeistServiceJpa implements HeistService {

    @Autowired
    private HeistRepository heistRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EligibleMapper eligibleMapper;

    @Autowired
    private ConfirmedMapper confirmedMapper;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private EmailService emailService;

    private static final String TIME_FORMAT = "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:Z|[+-][01]\\d:[0-5]\\d)$";

    private static final String LEVEL_FORMAT = "\\*{1,10}";

    @Override
    public Heist addHeist(Heist heist) {
        Assert.notNull(heist, "Heist object must be given");
        Assert.isNull(heist.getHeistId(), "Heist ID must be null, not " + heist.getHeistId());

        Assert.notNull(heist.getName(), "Heist name must be given");
        if(heistRepository.countByName(heist.getName()) > 0)
            throw new RequestDeniedException("Heist with name '" + heist.getName() + "' already exists");

        Assert.notNull(heist.getLocation(), "Heist location must be given");

        Assert.notNull(heist.getStartTime(), "Heist start time must be given");
        Assert.isTrue(heist.getStartTime().toString().matches(TIME_FORMAT), "Start date and time must be given in ISO8601 format");

        Assert.notNull(heist.getEndTime(), "Heist end time must be given");
        Assert.isTrue(heist.getEndTime().toString().matches(TIME_FORMAT), "End date and time must be given in ISO8601 format");

        Assert.isTrue(heist.getStartTime().compareTo(heist.getEndTime()) < 0, "Start time is after the end time");
        Assert.isTrue(heist.getEndTime().compareTo(Instant.now().minus(2, ChronoUnit.HOURS)) > 0, "The end time is in the past");

        Assert.isTrue(heist.getSkills().stream().allMatch(new HashSet<>()::add), "Multiple skills with the same name and level were provided");


        for(HeistSkill skill : heist.getSkills()){
            checkSkill(skill);

            skill.setHeist(heist);
        }

        return heistRepository.save(heist);
    }

    private void checkSkill(HeistSkill skill){
        Assert.notNull(skill.getName(), "Heist skill name must be given");

        Assert.notNull(skill.getLevel(), "Heist skill level must be given");
        Assert.isTrue(skill.getLevel().matches(LEVEL_FORMAT), "Skill level must be made of 1 to 10 asterisk characters");

        Assert.notNull(skill.getMembers(), "Heist skill members count must be given");
    }

    @Override
    public void updateSkills(Long heistId, HeistSkillDto heistSkillDto) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        if(heist.getStatus().equals(HeistStatus.IN_PROGRESS))
            throw new HeistAlreadyStartedException("Heist already started");

        List<HeistSkill> skills = heist.getSkills();

        Assert.isTrue(heistSkillDto.getSkills().stream().allMatch(new HashSet<>()::add), "Multiple skills with the same name and level were provided");

        for(HeistSkill hs : heistSkillDto.getSkills()){
            checkSkill(hs);

            hs.setHeist(heist);
            skills.add(hs);
        }

        skills = skills.stream().distinct().collect(Collectors.toList());
        heist.setSkills(skills);

        heistRepository.save(heist);
    }

    private List<Member> getEligibleList(Heist heist) {
        List<HeistSkill> skills = heist.getSkills();
        Set<Long> memberIds = new HashSet<>();

        for(HeistSkill hs : skills)
            memberIds.addAll(skillRepository.selectCustom(hs.getName(), hs.getLevel()));

        List<Member> eligibleMembers = new LinkedList<>();

        for(long memberId : memberIds){
            Member member = memberRepository.findByMemberId(memberId);

            for(Heist confirmedHeist : member.getConfirmedHeists()){
                Assert.isTrue(!heist.getStartTime().isAfter(confirmedHeist.getStartTime())
                                && heist.getEndTime().isBefore(confirmedHeist.getEndTime()),
                        "Member is already a confirmed member of another heist happening at the same time window");
            }

            if(member.getStatus().equals(Status.AVAILABLE) || member.getStatus().equals(Status.RETIRED))
                eligibleMembers.add(member);
        }

        return eligibleMembers;
    }

    @Override
    public EligibleDto getEligible(Long heistId) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        if(heist.getStatus().equals(HeistStatus.READY))
            throw new HeistAlreadyConfirmedException("Heist members have already been confirmed");

        List<Member> eligibleMembers = getEligibleList(heist);
        return eligibleMapper.toDto(heist.getSkills(), eligibleMembers);
    }

    @Override
    public void confirmMembers(Long heistId, HeistMemberDto heistMemberDto) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        if(heist.getStatus().equals(HeistStatus.READY))
            throw new HeistReadyException("Heist status is 'ready'");

        List<Member> eligibleMembers = getEligibleList(heist);
        Set<Member> confirmedMembers = new HashSet<>();

        for(String memberName : heistMemberDto.getMembers()){
            Member member = memberRepository.findByName(memberName);

            Assert.notNull(member, "Member with name '" + memberName + "' does not exist");
            Assert.isTrue(eligibleMembers.contains(member), memberName + "'s skills do not match with required heist skills");

            for(Heist confirmedHeist : member.getConfirmedHeists()){
                Assert.isTrue(!heist.getStartTime().isAfter(confirmedHeist.getStartTime())
                    && heist.getEndTime().isBefore(confirmedHeist.getEndTime()),
                        "Member is already a confirmed member of another heist happening at the same time");
            }

            confirmedMembers.add(member);
        }

        heist.setConfirmedMembers(confirmedMembers);
        heist.setStatus(HeistStatus.READY);

        for(Member member : confirmedMembers){
            emailService.SendSimpleMessage(member.getEmail(),
                    "Heist conformation",
                    "Congrats, You have been confirmed to participate in a heist '" + heist.getName() + "'\nHeist API team");
        }


        threadPoolTaskScheduler.schedule(
                new StartTask(heistId, heistRepository, emailService),
                heist.getStartTime()
        );

        threadPoolTaskScheduler.schedule(
                new EndTask(heistId, heistRepository, emailService),
                heist.getEndTime()
        );


        heistRepository.save(heist);
    }

    @Override
    public void startHeist(Long heistId) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        if(!heist.getStatus().equals(HeistStatus.READY))
            throw new HeistNotReadyException("Heist status is not ready");


        for(Member member : heist.getConfirmedMembers()){
            emailService.SendSimpleMessage(member.getEmail(),
                    "Heist start",
                    "Heist '" + heist.getName() + "' has started\nHeist API team");
        }


        heist.setStatus(HeistStatus.IN_PROGRESS);
        heistRepository.save(heist);
    }

    @Override
    public Heist getHeist(Long heistId) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        return heist;
    }

    @Override
    public ConfirmedDto getConfirmedMembers(Long heistId) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        if(!heist.getStatus().equals(HeistStatus.READY))
            throw new HeistNotReadyException("Heist status is not ready");

        return confirmedMapper.toDto(new ArrayList<>(heist.getConfirmedMembers()));
    }

    @Override
    public List<HeistSkill> getRequiredSkills(Long heistId) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        return heist.getSkills();
    }

    @Override
    public HeistStatusDto getStatus(Long heistId) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        return new HeistStatusDto(heist.getStatus().toString());
    }

    @Override
    public OutcomeDto getOutcome(Long heistId) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        if(!heist.getStatus().equals(HeistStatus.FINISHED))
            throw new HeistNotFinishedException("Heist with id " + heistId + " is not finished");

        Long memberCount = getMemberCount(heistId);
        double requiredMembersPercentage = heist.getConfirmedMembers().size() / (double)memberCount;

        if(requiredMembersPercentage < 0.5){
            setMemberStatus(heistId, 1L, requiredMembersPercentage);
            return new OutcomeDto(HeistOutcome.FAILED.toString());
        } else if(requiredMembersPercentage >= 0.5 && requiredMembersPercentage < 0.75){
            if(Math.random() >= 0.5){
                setMemberStatus(heistId, 2/3., requiredMembersPercentage);
                return new OutcomeDto(HeistOutcome.FAILED.toString());
            } else {
                setMemberStatus(heistId, 1/3., requiredMembersPercentage);
                return new OutcomeDto(HeistOutcome.SUCCEEDED.toString());
            }
        } else if(requiredMembersPercentage >= 0.75 && requiredMembersPercentage < 1){
            setMemberStatus(heistId, 1/3., requiredMembersPercentage);
            return new OutcomeDto(HeistOutcome.SUCCEEDED.toString());
        } else {
            return new OutcomeDto(HeistOutcome.SUCCEEDED.toString());
        }

    }

    @Override
    public Long getMemberCount(Long heistId) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        Long count = 0L;
        for(HeistSkill hs : heist.getSkills())
            count += hs.getMembers();

        return count;
    }

    @Override
    public void setMemberStatus(Long heistId, double memberPercentage, double requiredMembersPercentage) {
        Heist heist = heistRepository.findByHeistId(heistId);

        if(heist == null)
            throw new HeistNotFoundException("Heist with id " + heistId + " does not exist");

        Set<Member> members = heist.getConfirmedMembers();

        double count = Math.floor(members.size() * memberPercentage);
        List<Member> memberList = new ArrayList<>(members);

        if(requiredMembersPercentage < 0.75) {
            for(int i = 0; i < count; i++)
                if(Math.random() >= 0.5)
                    memberList.get(i).setStatus(Status.EXPIRED);
                else
                    memberList.get(i).setStatus(Status.INCARCERATED);

        } else {
            for(int i = 0; i < count; i++)
                memberList.get(i).setStatus(Status.INCARCERATED);
        }

        heist.setConfirmedMembers(new HashSet<>(memberList));
        heistRepository.save(heist);
    }
}
