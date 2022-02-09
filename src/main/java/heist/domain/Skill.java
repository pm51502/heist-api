package heist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@IdClass(SkillId.class)
public class Skill {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

    @Id
    @Column(nullable = false)
    private String name;

    @Size(min = 1, max = 10)
    @Column(length = 10)
    private String level = "*";

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "memberId")
    @JsonIgnore
    private Member member;

    public Skill(String name, String level, Member member) {
        //this.id = id;
        this.name = name;
        this.level = level;
        this.member = member;
    }

    public Skill() {}

    /*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

     */

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(name, skill.name) && Objects.equals(member, skill.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, member);
    }
}
