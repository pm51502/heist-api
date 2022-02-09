package heist.dao;

import heist.domain.Heist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeistRepository extends JpaRepository<Heist, Long> {
    int countByName(String name);
    Heist findByHeistId(Long heistId);
}
