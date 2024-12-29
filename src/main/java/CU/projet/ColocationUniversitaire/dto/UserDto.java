package CU.projet.ColocationUniversitaire.dto;

import CU.projet.ColocationUniversitaire.entity.Role;
import CU.projet.ColocationUniversitaire.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String lastname;
    private String firstname;
    private String photo;
    private String email;
    private String numTel;
    private Double budget;
    private String typelogementprefere;
    private String localisationprefere;
    private Role role;

    public UserDto(User user) {
        this.id = user.getId();
        this.lastname = user.getLastname();
        this.firstname = user.getFirstname();
        this.photo = user.getPhoto();
        this.email = user.getEmail();
        this.numTel = user.getNum_tel();
        this.budget = user.getBudget();
        this.typelogementprefere = user.getTypelogementprefere();
        this.localisationprefere = user.getLocalisationprefere();
        this.role = user.getRole();
    }
    public UserDto(String numTel, Double budget, String typelogementprefere, String localisationprefere) {
        this.numTel = numTel;
        this.budget = budget;
        this.typelogementprefere = typelogementprefere;
        this.localisationprefere = localisationprefere;
    }

    public User DtoToUser() {
        User user = new User();
        user.setId(this.id);
        user.setLastname(this.lastname);
        user.setFirstname(this.firstname);
        user.setPhoto(this.photo);
        user.setEmail(this.email);
        user.setNum_tel(this.numTel);
        user.setBudget(this.budget);
        user.setTypelogementprefere(this.typelogementprefere);
        user.setLocalisationprefere(this.localisationprefere);
        user.setRole(this.role);
        return user;
    }
}
