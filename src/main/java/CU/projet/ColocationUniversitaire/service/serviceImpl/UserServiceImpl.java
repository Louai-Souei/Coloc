package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.UserDto;
import CU.projet.ColocationUniversitaire.entity.Role;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ApiResponse<List<UserDto>> getAllUsers() {
        // Récupérer tous les utilisateurs dont le rôle n'est pas "ADMIN"
        List<User> users = userRepository.findByRoleNot(Role.ADMIN);

        // Convertir la liste d'entités en liste de DTO
        List<UserDto> userDtos = users.stream()
                .map(UserDto::new)  // Conversion de User à UserDto
                .collect(Collectors.toList());

        return new ApiResponse<>("Utilisateurs récupérés avec succès", userDtos);
    }
}
