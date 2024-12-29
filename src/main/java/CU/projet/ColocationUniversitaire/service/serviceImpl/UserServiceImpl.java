package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.UserDto;
import CU.projet.ColocationUniversitaire.entity.Role;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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
            String photoName = photoFile.getOriginalFilename();

            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/images";
            Path filePath = Paths.get(uploadDir, photoName);

            Files.createDirectories(filePath.getParent());

            Files.write(filePath, photoFile.getBytes());

            existingUser.setPhoto(photoName);
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
}
