package online.rabko.basketball.repository;

import online.rabko.basketball.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Team} entities.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * Checks if a team exists with the given name (case-insensitive).
     *
     * @param name the team name to check.
     * @return true if a team exists with the given name, false otherwise.
     */
    boolean existsByNameIgnoreCase(String name);
}
