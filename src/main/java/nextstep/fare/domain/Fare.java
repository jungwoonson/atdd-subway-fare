package nextstep.fare.domain;

import javax.persistence.Column;
import java.util.List;
import java.util.Objects;

public class Fare implements Comparable<Fare> {

    public static final Long ZERO_FARE = 0L;

    @Column(nullable = false)
    private Long fare;

    public Fare() {
    }

    private Fare(final Long fare) {
        this.fare = fare;
    }

    public static Fare zero() {
        return new Fare(ZERO_FARE);
    }

    public static Fare from(final Integer fare) {
        Long fareLong = fare.longValue();
        validateFare(fareLong);
        return new Fare(fareLong);
    }

    public static Fare from(final Long fare) {
        validateFare(fare);
        return new Fare(fare);
    }

    private static void validateFare(final Long fare) {
        if (fare < ZERO_FARE) {
            throw new LessThanZeroFareException(fare);
        }
    }

    public Fare add(final Fare fare) {
        return Fare.from(this.fare + fare.fare);
    }

    public Fare minus(Fare minusFare) {
        return Fare.from(this.fare - minusFare.fare);
    }

    public Fare multiply(int number) {
        return Fare.from(this.fare * number);
    }

    public Fare multiply(Double number) {
        Double multiplied = this.fare * number;
        return Fare.from(multiplied.longValue());
    }

    public Long getFare() {
        return fare;
    }

    @Override
    public int compareTo(Fare o) {
        return fare.compareTo(o.fare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return Objects.equals(fare, fare1.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "fare=" + fare +
                '}';
    }
}
