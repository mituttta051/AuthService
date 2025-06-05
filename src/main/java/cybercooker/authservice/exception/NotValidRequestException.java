package cybercooker.authservice.exception;

import cybercooker.authservice.exception.details.NotValidRequestDetails;

public class NotValidRequestException extends BaseException {
    public NotValidRequestException(NotValidRequestDetails details) {
        super(details);
    }

}
