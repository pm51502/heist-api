package heist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class HeistSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long heistSkillId;

    @Column(nullable = false)
    private String name;

    @Size(min = 1, max = 10)
    @Column(length = 10)
    private String level;

    @Column(nullable = false)
    @Min(1)
    private Long members;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "heistId")
    @JsonIgnore
    private Heist heist;

    public HeistSkill() {}

    public HeistSkill(Long heistSkillId, String name, String level, Long members, Heist heist) {
        this.heistSkillId = heistSkillId;
        this.name = name;
        this.level = level;
        this.members = members;
        this.heist = heist;
    }


    public Long getHeistSkillId() {
        return heistSkillId;
    }

    public void setHeistSkillId(Long heistSkillId) {
        this.heistSkillId = heistSkillId;
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

    public Long getMembers() {
        return members;
    }

    public void setMembers(Long members) {
        this.members = members;
    }

    public Heist getHeist() {
        return heist;
    }

    public void setHeist(Heist heist) {
        this.heist = heist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeistSkill that = (HeistSkill) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(level, that.level) &&
                Objects.equals(heist, that.heist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, heist);
    }
}
