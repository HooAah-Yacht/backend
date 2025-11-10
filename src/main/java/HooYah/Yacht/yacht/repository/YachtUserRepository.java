package HooYah.Yacht.yacht.repository;

import HooYah.Yacht.yacht.domain.YachtUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YachtUserRepository extends JpaRepository<YachtUser, Long> {
    
    List<YachtUser> findByUserId(Long userId);
    
    List<YachtUser> findByYachtId(Long yachtId);
}
