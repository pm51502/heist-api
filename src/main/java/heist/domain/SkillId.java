package heist.domain;

import java.io.Serializable;

public class SkillId implements Serializable {

    private String name;

    private Long member;

    public SkillId(){}

    public SkillId(String name, Long member) {
        this.name = name;
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMember() {
        return member;
    }

    public void setMember(Long member) {
        this.member = member;
    }
}
