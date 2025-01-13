package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.UserDto;
import CU.projet.ColocationUniversitaire.dto.UserSearchCriteria;
import CU.projet.ColocationUniversitaire.entity.Match;
import CU.projet.ColocationUniversitaire.entity.Role;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.entity.UserScore;
import CU.projet.ColocationUniversitaire.repository.MatchRepository;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;


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
    public ApiResponse<List<UserDto>> getSuggestedMatches() {
        User currentUser = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<User> potentialMatches = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.COLOCATAIRE)
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .collect(Collectors.toList());

        List<UserScore> userScores = potentialMatches.stream()
                .map(user -> {
                    double score = 0;
                    double totalCriteria = 0;

                    if (currentUser.getTypelogementprefere() != null && user.getTypelogementprefere() != null &&
                            user.getTypelogementprefere().equalsIgnoreCase(currentUser.getTypelogementprefere())) {
                        score += 1;
                    }
                    totalCriteria++;

                    if (currentUser.getLocalisationprefere() != null && user.getLocalisationprefere() != null &&
                            user.getLocalisationprefere().equalsIgnoreCase(currentUser.getLocalisationprefere())) {
                        score += 1;
                    }
                    totalCriteria++;

                    if (currentUser.getBudget() != null && user.getBudget() != null &&
                            user.getBudget() >= currentUser.getBudget() * 0.8 &&
                            user.getBudget() <= currentUser.getBudget() * 1.2) {
                        score += 1;
                    }
                    totalCriteria++;

                    if (currentUser.getFumeur() != null && user.getFumeur() != null &&
                            user.getFumeur().equals(currentUser.getFumeur())) {
                        score += 1;
                    }
                    totalCriteria++;

                    if (currentUser.getAnimauxAcceptes() != null && user.getAnimauxAcceptes() != null &&
                            user.getAnimauxAcceptes().equals(currentUser.getAnimauxAcceptes())) {
                        score += 1;
                    }
                    totalCriteria++;


                    double percentage = (score / totalCriteria) * 100;


                    Match match = new Match();
                    match.setUser(currentUser);
                    match.setMatchedUser(user);
                    match.setMatchScore(percentage);
                    matchRepository.save(match);

                    return new UserScore(user, percentage);
                })
                .collect(Collectors.toList());

        userScores.sort((us1, us2) -> Double.compare(us2.getScore(), us1.getScore()));

        List<UserDto> matchedDtos = userScores.stream()
                .map(userScore -> new UserDto(userScore.getUser()))
                .collect(Collectors.toList());

        return new ApiResponse<>("Correspondances suggérées récupérées avec succès", matchedDtos);
    }


}
