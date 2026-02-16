package kevinquarta.s7l1.excpetions;


import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private List<String> errorsMessages;

    public ValidationException(List<String> errorsMessages) {
        super("Ci sono stati degli errori nel payload");
        this.errorsMessages = errorsMessages;
    }
}