package javajedi.com.exception.domain;

public class EmailExistException extends RuntimeException {

    public EmailExistException(String message){
        super(message);
    }

}
