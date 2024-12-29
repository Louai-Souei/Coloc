package CU.projet.ColocationUniversitaire.service.serviceInterface;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.UserDto;

import java.util.List;

public interface UserService {
    ApiResponse<List<UserDto>> getAllUsers();
}
