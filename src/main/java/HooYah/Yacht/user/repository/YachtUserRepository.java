package HooYah.Yacht.user.repository;

import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.domain.YachtUser;
import HooYah.Yacht.yacht.domain.Yacht;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface YachtUserRepository extends JpaRepository<YachtUser,Integer> {

    @Query("select yu.yacht from YachtUser yu where yu.user.id = :userId and yu.yacht.id = :yachtId")
    Optional<Yacht> findYacht(@Param("yachtId") Long yachtId, @Param("userId") Long userId);

    List<YachtUser> findByUser(User user);

    List<YachtUser> findByYacht(Yacht yacht);
}
