package edu.setokk.atm.error.exception.user;

import edu.setokk.atm.error.exception.BusinessLogicException;
import org.springframework.http.HttpStatus;

public class UsernameExistsException extends BusinessLogicException {
    public UsernameExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
