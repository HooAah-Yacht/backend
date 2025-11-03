package HooYah.Yacht.yacht.repository;

import HooYah.Yacht.yacht.domain.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YachtRepository extends JpaRepository<Yacht, Long> {
}
