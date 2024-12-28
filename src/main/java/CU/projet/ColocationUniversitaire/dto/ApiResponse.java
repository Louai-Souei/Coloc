package CU.projet.ColocationUniversitaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
}