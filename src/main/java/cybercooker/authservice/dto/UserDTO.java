package cybercooker.authservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    private String name;
}
