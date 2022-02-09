package heist.service;

import heist.domain.Heist;
import heist.domain.HeistSkill;
import heist.dto.HeistMemberDto;
import heist.dto.view.ConfirmedDto;
import heist.dto.view.EligibleDto;
import heist.dto.HeistSkillDto;
import heist.dto.view.HeistStatusDto;
import heist.dto.view.OutcomeDto;

import java.util.List;

public interface HeistService {

    Heist addHeist(Heist heist);

    void updateSkills(Long heistId, HeistSkillDto heistSkillDto);

    EligibleDto getEligible(Long heistId);

    void confirmMembers(Long heistId, HeistMemberDto heistMemberDto);

    void startHeist(Long heistId);

    Heist getHeist(Long heistId);

    ConfirmedDto getConfirmedMembers(Long heistId);

    List<HeistSkill> getRequiredSkills(Long heistId);

    HeistStatusDto getStatus(Long heistId);

    OutcomeDto getOutcome(Long heistId);

    Long getMemberCount(Long heistId);

    void setMemberStatus(Long heistId, double memberPercentage, double requiredMembersPercentage);
}
