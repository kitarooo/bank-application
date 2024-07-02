package backend.microservices.userservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String email;
    String username;
    String password;
    String firstname;
    String lastname;
    String phoneNumber;
}
