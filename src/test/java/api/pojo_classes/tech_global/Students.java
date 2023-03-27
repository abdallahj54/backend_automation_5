package api.pojo_classes.tech_global;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Students {

    /**
     * {
     *     "firstName": "{{$randomFirstName}}",
     *     "lastName": "{{$randomLastName}}",
     *     "email": "{{$randomEmail}}",
     *     "dob": "1992-12-15"
     * }
     */

    private String firstName;
    private String lastName;
    private String email;
    private String dob;
}
