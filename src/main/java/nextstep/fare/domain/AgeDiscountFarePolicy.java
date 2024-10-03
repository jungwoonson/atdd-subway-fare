package nextstep.fare.domain;

import nextstep.fare.application.dto.CalculateFareRequest;
import org.springframework.stereotype.Component;

@Component
public class AgeDiscountFarePolicy extends AbstractFarePolicy {

    public static final double YOUTH_FARE_RATE = 0.8;
    public static final double CHILD_FARE_RATE = 0.5;
    public static final int MIN_CHILD_AGE = 6;
    public static final int MAX_CHILD_AGE = 12;
    public static final int MAX_YOUTH_AGE = 18;

    private static final Fare DEFAULT_DISCOUNT_FARE = Fare.from(350L);

    @Override
    public Fare calculateFare(Fare fare, CalculateFareRequest request) {
        int age = request.getAge();
        if (age > MAX_YOUTH_AGE || age < MIN_CHILD_AGE) {
            return fare;
        }
        Fare discounted = fare.minus(DEFAULT_DISCOUNT_FARE);
        if (age <= MAX_CHILD_AGE) {
            return discounted.multiply(CHILD_FARE_RATE);
        }
        return discounted.multiply(YOUTH_FARE_RATE);
    }
}