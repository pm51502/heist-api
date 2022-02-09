package heist.dto;

import heist.domain.Skill;
import java.util.List;


public class MemberSkillDto {
    List<Skill> skills;
    String mainSkill;

    public MemberSkillDto(){}

    public MemberSkillDto(List<Skill> skills, String mainSkill) {
        this.skills = skills;
        this.mainSkill = mainSkill;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public String getMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(String mainSkill) {
        this.mainSkill = mainSkill;
    }
}
