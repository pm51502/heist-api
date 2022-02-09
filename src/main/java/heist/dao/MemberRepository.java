package heist.dao;

import heist.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    int countByEmail(String email);
    Member findByMemberId(Long memberId);
    Member findByName(String name);
}
