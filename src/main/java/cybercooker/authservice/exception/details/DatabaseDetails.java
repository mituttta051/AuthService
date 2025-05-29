package cybercooker.authservice.exception.details;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DatabaseDetails extends ErrorDetails {
    String message;
}
