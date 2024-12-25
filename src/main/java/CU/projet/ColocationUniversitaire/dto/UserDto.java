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
    int id;
    private String firstname;
    private String lastname;
    private String photo;
    private String num_tel;
    private String email;
    private Double budget;
    private String typelogementprefere;
    private String localisationprefere;
    private Role role;

    public UserDto(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.photo = user.getPhoto();
        this.num_tel = user.getNum_tel();
        this.email = user.getEmail();
        this.budget = user.getBudget();
        this.typelogementprefere = user.getTypelogementprefere();
        this.localisationprefere = user.getLocalisationprefere();
        this.role = user.getRole();
    }

    public User DtoToUser() {
        User user = new User();
        user.setFirstname(this.firstname);
        user.setLastname(this.lastname);
        user.setPhoto(this.photo);
        user.setNum_tel(this.num_tel);
        user.setEmail(this.email);
        user.setBudget(this.budget);
        user.setTypelogementprefere(this.typelogementprefere);
        user.setLocalisationprefere(this.localisationprefere);
        user.setRole(this.role);
        user.setRole(role);
        return user;
    }
}

