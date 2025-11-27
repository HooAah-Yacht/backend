package HooYah.Yacht.yacht.repository;

import HooYah.Yacht.yacht.domain.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface YachtRepository extends JpaRepository<Yacht, Long> {
}
