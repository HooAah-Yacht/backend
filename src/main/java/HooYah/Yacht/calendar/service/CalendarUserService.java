package HooYah.Yacht.calendar.service;

import HooYah.Yacht.calendar.domain.Calendar;
import HooYah.Yacht.calendar.domain.CalendarUser;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.repository.UserRepository;
import HooYah.Yacht.user.repository.YachtUserPort;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarUserService {

    private final UserRepository userRepository;

    @Transactional
    public void addUser(Calendar calendar, List<Long> userIdList) {
        List<User> userList = validateUserList(userIdList);

        List<CalendarUser> calendarUserList = userList
                .stream()
                .map(u->CalendarUser.builder().calendar(calendar).user(u).build())
                .toList();

        calendar.setCalendarUsers(calendarUserList);
    }

    private List<User> validateUserList(List<Long> userList) {
        if(userList==null || userList.isEmpty())
            return List.of();

        Set<Long> userIdSet = new HashSet<>(userList);

        if(userIdSet.size()!=userList.size())
            return List.of();

        List<User> selectedUser = userRepository.findAllById(userList);

        if(selectedUser.size()!=userList.size())
            return List.of();

        return selectedUser;
    }

}
