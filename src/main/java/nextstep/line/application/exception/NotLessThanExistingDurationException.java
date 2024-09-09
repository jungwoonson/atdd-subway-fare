package nextstep.line.application.exception;

public class NotLessThanExistingDurationException extends IllegalArgumentException {
    private static final String MESSAGE = "새로운 구간의 소요시간은 기존 구간의 소요시간보다 작아야합니다.";

    public NotLessThanExistingDurationException() {
        super(MESSAGE);
    }
}
