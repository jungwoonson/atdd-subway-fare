package nextstep.path.application.exception;

public class NotExistPathTypeException extends IllegalStateException {
    private static final String MESSAGE = "존재하지 않는 경로 구분입니다.";

    public NotExistPathTypeException() {
        super(String.format(MESSAGE));
    }
}
