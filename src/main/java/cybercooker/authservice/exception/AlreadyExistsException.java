package cybercooker.authservice.exception;


import cybercooker.authservice.exception.details.DatabaseDetails;

public class AlreadyExistsException extends BaseException {
    public AlreadyExistsException(DatabaseDetails details) {
        super(details);
    }

}
