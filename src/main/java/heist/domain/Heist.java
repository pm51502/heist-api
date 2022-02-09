package heist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import heist.domain.enums.HeistStatus;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Set;

@Entity
public class Heist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long heistId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @OneToMany(mappedBy = "heist", cascade = CascadeType.ALL)
    private List<HeistSkill> skills;

    @Enumerated(EnumType.STRING)
    private HeistStatus status = HeistStatus.PLANNING;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "HeistMembers",
            joinColumns = @JoinColumn(name = "heistId"),
            inverseJoinColumns = @JoinColumn(name = "memberId")
    )
    @JsonIgnore
    private Set<Member> confirmedMembers;

    public Heist() {}

    public Heist(Long heistId, String name, String location, Instant startTime, Instant endTime, List<HeistSkill> skills, HeistStatus status, Set<Member> confirmedMembers) {
        this.heistId = heistId;
        this.name = name;
        this.location = location;
        this.startTime = startTime.minus(2, ChronoUnit.HOURS);
        this.endTime = endTime.minus(2, ChronoUnit.HOURS);
        this.skills = skills;
        this.status = status;
        this.confirmedMembers = confirmedMembers;
    }

    public Long getHeistId() {
        return heistId;
    }

    public void setHeistId(Long heistId) {
        this.heistId = heistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime.minus(2, ChronoUnit.HOURS);
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime.minus(2, ChronoUnit.HOURS);
    }

    public List<HeistSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<HeistSkill> skills) {
        this.skills = skills;
    }

    public HeistStatus getStatus() {
        return status;
    }

    public void setStatus(HeistStatus status) {
        this.status = status;
    }

    public Set<Member> getConfirmedMembers() {
        return confirmedMembers;
    }

    public void setConfirmedMembers(Set<Member> confirmedMembers) {
        this.confirmedMembers = confirmedMembers;
    }
}
