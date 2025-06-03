package cybercooker.authservice.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequest {
    private String username;
    private String password;
    private String email;
    private String name;
}
