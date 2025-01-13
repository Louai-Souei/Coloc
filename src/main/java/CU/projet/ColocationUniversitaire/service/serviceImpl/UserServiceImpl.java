package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.UserDto;
import CU.projet.ColocationUniversitaire.dto.UserSearchCriteria;
import CU.projet.ColocationUniversitaire.entity.Role;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;


    @Override
    public User getUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
    @Override

    public ApiResponse<UserDto> updateUserProfile(UserDto updatedUserDto, MultipartFile photoFile) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (photoFile != null && !photoFile.isEmpty()) {
            existingUser.setPhoto(photoFile.getBytes());
        }

        if (updatedUserDto.getNumTel() != null && !updatedUserDto.getNumTel().isEmpty()) {
            existingUser.setNum_tel(updatedUserDto.getNumTel());
        }
        if (updatedUserDto.getBudget() != null) {
            existingUser.setBudget(updatedUserDto.getBudget());
        }
        if (updatedUserDto.getTypelogementprefere() != null && !updatedUserDto.getTypelogementprefere().isEmpty()) {
            existingUser.setTypelogementprefere(updatedUserDto.getTypelogementprefere());
        }
        if (updatedUserDto.getLocalisationprefere() != null && !updatedUserDto.getLocalisationprefere().isEmpty()) {
            existingUser.setLocalisationprefere(updatedUserDto.getLocalisationprefere());
        }
        if (updatedUserDto.getAge() != null) {
            existingUser.setAge(updatedUserDto.getAge());
        }
        if (updatedUserDto.getSexe() != null && !updatedUserDto.getSexe().isEmpty()) {
            existingUser.setSexe(updatedUserDto.getSexe());
        }
        if (updatedUserDto.getFumeur() != null) {
            existingUser.setFumeur(updatedUserDto.getFumeur());
        }
        if (updatedUserDto.getAnimauxAcceptes() != null) {
            existingUser.setAnimauxAcceptes(updatedUserDto.getAnimauxAcceptes());
        }

        userRepository.save(existingUser);

        UserDto userDto = new UserDto(existingUser);

        return new ApiResponse<>("Profil mis à jour avec succès.", userDto);
    }

    @Override
    public ApiResponse<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findByRoleNot(Role.ADMIN);

        List<UserDto> userDtos = users.stream()
                .map(UserDto::new)  // Conversion de User à UserDto
                .collect(Collectors.toList());

        return new ApiResponse<>("Utilisateurs récupérés avec succès", userDtos);
    }

    @Override
    public ApiResponse<List<UserDto>> searchUsers(UserSearchCriteria criteria) {
        List<User> users = userRepository.findUsersByCriteria(
                criteria.getAgeMin(),
                criteria.getAgeMax(),
                criteria.getSexe(),
                criteria.getBudgetMin(),
                criteria.getBudgetMax(),
                criteria.getFumeur(),
                criteria.getAnimauxAcceptes()
        );

        List<UserDto> userDtos = users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());

        return new ApiResponse<>("Utilisateurs filtrés avec succès", userDtos);
    }
    @Override
    public UserDto deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        UserDto deletedUserDto = new UserDto(user);

        userRepository.delete(user);

        return deletedUserDto;
    }
    @Override
    public ApiResponse<UserDto> addUser(UserDto userDto) {
        User user = userDto.DtoToUser();
        user = userRepository.save(user);
        UserDto savedUserDto = new UserDto(user);
        return new ApiResponse<>("Utilisateur créé avec succès", savedUserDto);
    }

    @Override
    public Map<String, Long> getActiveUserStats() {
        try {
            LocalDate startDate = LocalDate.now().minusWeeks(4);
            LocalDate endDate = LocalDate.now().plusDays(1);

            Map<String, Long> userStats = new TreeMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 4; i++) {
                LocalDate weekStart = startDate.plusWeeks(i);
                LocalDate weekEnd = (i == 3)
                        ? weekStart.plusDays(7).minusDays(1)
                        : weekStart.plusDays(6);

                Date start = Date.from(weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(weekEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

                Long count = userRepository.countActiveUsers(start, end);

                String dateRangeKey = weekStart.format(formatter) + " -->    " + weekEnd.format(formatter);
                userStats.put(dateRangeKey, count);
            }
            
            return userStats;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch active user stats.");
        }
    }




}
