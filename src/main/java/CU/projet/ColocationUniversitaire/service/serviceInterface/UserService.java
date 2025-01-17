package CU.projet.ColocationUniversitaire.service.serviceInterface;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.UserDto;
import CU.projet.ColocationUniversitaire.dto.UserSearchCriteria;
import CU.projet.ColocationUniversitaire.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    ApiResponse<List<UserDto>> getAllUsers();
    User getUserProfile();
    ApiResponse<UserDto> updateUserProfile(UserDto updatedUserDto, MultipartFile photoFile) throws IOException; // Modifier le paramètre pour inclure MultipartFile
    ApiResponse<List<UserDto>> searchUsers(UserSearchCriteria criteria);
    UserDto deleteUser(Integer id);
    ApiResponse<UserDto> addUser(UserDto userDto);
    ApiResponse<List<UserDto>> getSuggestedMatches();



    Map<String, Long> getActiveUserStats();
}
