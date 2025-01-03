package CU.projet.ColocationUniversitaire.controller;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.UserDto;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.service.serviceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        ApiResponse<List<UserDto>> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() {
        // Appel du service pour récupérer le profil utilisateur à partir du token
        User user = userService.getUserProfile();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(
            @RequestPart(value = "photo", required = false) MultipartFile photoFile,
            @RequestParam(value = "numTel", required = false) String numTel,
            @RequestParam(value = "budget", required = false) Double budget,
            @RequestParam(value = "typelogementprefere", required = false) String typelogementprefere,
            @RequestParam(value = "localisationprefere", required = false) String localisationprefere
    ) throws IOException {

        // Créez un UserDto avec les paramètres nécessaires (attention à l'ordre et aux types)
        UserDto updatedUserDto = new UserDto(numTel, budget, typelogementprefere, localisationprefere);

        // Appelez la méthode du service pour mettre à jour le profil de l'utilisateur
        ApiResponse<UserDto> response = userService.updateUserProfile(updatedUserDto, photoFile);

        // Retourner la réponse API
        return ResponseEntity.ok(response);
    }


}