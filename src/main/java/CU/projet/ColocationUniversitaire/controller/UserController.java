package CU.projet.ColocationUniversitaire.controller;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.UserDto;
import CU.projet.ColocationUniversitaire.dto.UserSearchCriteria;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.service.serviceInterface.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/addUser")
    public ResponseEntity<ApiResponse<UserDto>> addUser(@RequestBody UserDto userDto) {
        System.out.println("Received userDto: " + userDto);
        ApiResponse<UserDto> response = userService.addUser(userDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        ApiResponse<List<UserDto>> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() {
        User user = userService.getUserProfile();
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAnyRole('COLOCATAIRE')")
    @GetMapping("/suggestions")
    public ApiResponse<List<UserDto>> getSuggestedMatches() {
        return userService.getSuggestedMatches();
    }


    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(
            @RequestPart(value = "photo", required = false) MultipartFile photoFile,
            @RequestParam(value = "numTel", required = false) String numTel,
            @RequestParam(value = "budget", required = false) Double budget,
            @RequestParam(value = "typelogementprefere", required = false) String typelogementprefere,
            @RequestParam(value = "localisationprefere", required = false) String localisationprefere,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "sexe", required = false) String sexe,
            @RequestParam(value = "fumeur", required = false) Boolean fumeur,
            @RequestParam(value = "animauxAcceptes", required = false) Boolean animauxAcceptes
    ) throws IOException {

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setNumTel(numTel);
        updatedUserDto.setBudget(budget);
        updatedUserDto.setTypelogementprefere(typelogementprefere);
        updatedUserDto.setLocalisationprefere(localisationprefere);
        updatedUserDto.setAge(age);
        updatedUserDto.setSexe(sexe);
        updatedUserDto.setFumeur(fumeur);
        updatedUserDto.setAnimauxAcceptes(animauxAcceptes);

        ApiResponse<UserDto> response = userService.updateUserProfile(updatedUserDto, photoFile);

        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasAnyRole('COLOCATAIRE')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsers(
            @RequestParam(required = false) Integer ageMin,
            @RequestParam(required = false) Integer ageMax,
            @RequestParam(required = false) String sexe,
            @RequestParam(required = false) Boolean fumeur,
            @RequestParam(required = false) Boolean animauxAcceptes,
            @RequestParam(required = false) Double budgetMin,
            @RequestParam(required = false) Double budgetMax,
            @RequestParam(required = false) String typelogementprefere,
            @RequestParam(required = false) String localisationprefere
    ) {
        UserSearchCriteria criteria = new UserSearchCriteria(
                ageMin, ageMax, sexe, fumeur, animauxAcceptes, budgetMin, budgetMax, typelogementprefere, localisationprefere
        );

        ApiResponse<List<UserDto>> response = userService.searchUsers(criteria);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Integer id) {
        UserDto deletedUser = userService.deleteUser(id);
        return ResponseEntity.ok(deletedUser);
    }

    @GetMapping("/active-user-stats")
    public ResponseEntity<Object> getActiveUserStats() {
        try {
            Map<String, Long> stats = userService.getActiveUserStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch active user stats.");
        }
    }






}