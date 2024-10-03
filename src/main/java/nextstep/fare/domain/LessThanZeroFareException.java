package nextstep.fare.domain;

public class LessThanZeroFareException extends IllegalStateException {

    private static final String MESSAGE = "요금은 0이상이어야 합니다. 현재 요금: %d";

    public LessThanZeroFareException(long fare) {
        super(String.format(MESSAGE, fare));
    }
}