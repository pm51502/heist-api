package heist.dto.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SkillDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("level")
    private String level;

    public SkillDto() {}

    public SkillDto(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
