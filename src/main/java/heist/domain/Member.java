package heist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import heist.domain.enums.Sex;
import heist.domain.enums.Status;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Skill> skills;

    private String mainSkill;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(mappedBy = "confirmedMembers", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Heist> confirmedHeists;

    public Member(){}

    public Member(Long memberId, String name, Sex sex, String email, List<Skill> skills, String mainSkill, Status status, Set<Heist> confirmedHeists) {
        this.memberId = memberId;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.skills = skills;
        this.mainSkill = mainSkill;
        this.status = status;
        this.confirmedHeists = confirmedHeists;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Heist> getConfirmedHeists() {
        return confirmedHeists;
    }

    public void setConfirmedHeists(Set<Heist> confirmedHeists) {
        this.confirmedHeists = confirmedHeists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId) &&
                Objects.equals(name, member.name) &&
                sex == member.sex &&
                Objects.equals(email, member.email) &&
                Objects.equals(skills, member.skills) &&
                Objects.equals(mainSkill, member.mainSkill) &&
                status == member.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, name, sex, email, skills, mainSkill, status);
    }
}
