package online.rabko.basketball.repository;

import online.rabko.basketball.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Match} entities.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

}
