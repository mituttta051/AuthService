package cybercooker.authservice.exception;


import cybercooker.authservice.exception.details.DatabaseDetails;

public class NotFoundException extends BaseException {
    public NotFoundException(DatabaseDetails details) {
        super(details);
    }

}
