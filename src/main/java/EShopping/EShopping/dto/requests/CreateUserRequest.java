package EShopping.EShopping.dto.requests;

import lombok.Data;

import java.util.Date;

@Data
public class CreateUserRequest {
    private String userName;
    private String password;
    private String email;
    private Date birthDate;
    private int age;
}
