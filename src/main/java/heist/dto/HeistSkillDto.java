package heist.dto;

import heist.domain.HeistSkill;
import java.util.List;

public class HeistSkillDto {

    List<HeistSkill> skills;

    public HeistSkillDto() {}

    public HeistSkillDto(List<HeistSkill> skills) {
        this.skills = skills;
    }

    public List<HeistSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<HeistSkill> skills) {
        this.skills = skills;
    }
}
