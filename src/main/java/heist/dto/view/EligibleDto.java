package heist.dto.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import heist.domain.HeistSkill;

import java.util.List;

public class EligibleDto {

    @JsonProperty("skills")
    private List<HeistSkill> heistSkills;

    @JsonProperty("members")
    private List<MemberDto> members;

    public EligibleDto() {}

    public EligibleDto(List<HeistSkill> heistSkills, List<MemberDto> members) {
        this.heistSkills = heistSkills;
        this.members = members;
    }

    public List<HeistSkill> getHeistSkills() {
        return heistSkills;
    }

    public void setHeistSkills(List<HeistSkill> heistSkills) {
        this.heistSkills = heistSkills;
    }

    public List<MemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDto> members) {
        this.members = members;
    }
}
