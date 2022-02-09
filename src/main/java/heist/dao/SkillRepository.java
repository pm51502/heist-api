package heist.dao;

import heist.domain.Member;
import heist.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Transactional
public interface SkillRepository extends JpaRepository<Skill, Long> {
    @Modifying
    @Query("DELETE FROM Skill s WHERE s.name = ?1 and s.member.memberId = ?2")
    Integer deleteCustom(String name, Long memberId);

    @Modifying
    @Query("SELECT s.member.memberId FROM Skill s WHERE s.name = ?1 and s.level >= ?2")
    Set<Long> selectCustom(String name, String level);
}
