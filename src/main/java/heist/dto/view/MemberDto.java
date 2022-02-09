package heist.dto.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MemberDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("skills")
    private List<SkillDto> skills;

    public MemberDto() {}

    public MemberDto(String name, List<SkillDto> skills) {
        this.name = name;
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }
}
