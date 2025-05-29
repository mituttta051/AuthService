package cybercooker.authservice.mapper;

import cybercooker.authservice.exception.AlreadyExistsException;
import cybercooker.authservice.exception.BaseException;
import cybercooker.authservice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorMapper {
    public static ResponseEntity<BaseException> map(BaseException exception) {
        return switch (exception) {
            case NotFoundException e -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
            case AlreadyExistsException e -> ResponseEntity.status(HttpStatus.CONFLICT).body(exception);
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
        };
    }
}
