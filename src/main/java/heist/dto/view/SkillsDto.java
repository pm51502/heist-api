package heist.dto.view;

import heist.domain.Skill;

import java.util.List;

public class SkillsDto {

    List<Skill> skills;

    String mainSkill;

    public SkillsDto() {}

    public SkillsDto(List<Skill> skills, String mainSkill) {
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
