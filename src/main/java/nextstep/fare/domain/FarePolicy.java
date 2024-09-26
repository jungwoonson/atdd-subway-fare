package nextstep.fare.domain;

import nextstep.line.domain.Section;

import java.util.Set;

public class FarePolicy {

    public static final Fare BASE_FARE = Fare.from(1250L);
    private static final Fare ADDITIONAL_FARE = Fare.from(100L);
    private static final Fare DEFAULT_DISCOUNT_FARE = Fare.from(350L);

    private static final int MINIMUM_DISTANCE = 1;
    private static final int BASE_FARE_DISTANCE_LIMIT = 10;
    private static final int EXTENDED_FARE_DISTANCE_LIMIT = 50;
    private static final int EXTENDED_FARE_INTERVAL = 5;
    private static final int ADDITIONAL_FARE_INTERVAL = 8;
    private static final int FARE_ADJUSTMENT = 1;

    public static final double YOUTH_FARE_RATE = 0.8;
    public static final double CHILD_FARE_RATE = 0.5;
    public static final int MIN_CHILD_AGE = 6;
    public static final int MAX_CHILD_AGE = 12;
    public static final int MAX_YOUTH_AGE = 18;

    public static Fare baseFare() {
        return BASE_FARE;
    }

    public Fare calculateFare(final Integer distance, final Set<Section> sections, final Integer age) {
        Fare distanceFare = calculateFareForDistance(distance);
        Fare sectionFare = calculateFareForSections(sections);
        Fare fare = distanceFare.add(sectionFare);
        return discount(fare, age);
    }

    private Fare calculateFareForDistance(Integer distance) {
        validateDistance(distance);
        if (distance <= BASE_FARE_DISTANCE_LIMIT) {
            return baseFare();
        }
        if (distance <= EXTENDED_FARE_DISTANCE_LIMIT) {
            return calculateAdditionalFareForExtendedDistance(distance);
        }
        return calculateAdditionalFareForLongDistance(distance);
    }

    private void validateDistance(final int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new LessThanMinimumDistanceException(MINIMUM_DISTANCE, distance);
        }
    }

    private Fare calculateAdditionalFareForExtendedDistance(final int distance) {
        int additionalDistance = Math.min(distance, EXTENDED_FARE_DISTANCE_LIMIT) - BASE_FARE_DISTANCE_LIMIT - FARE_ADJUSTMENT;
        return BASE_FARE.add(calculateAdditionalFare(additionalDistance, EXTENDED_FARE_INTERVAL));
    }

    private Fare calculateAdditionalFareForLongDistance(final int distance) {
        int additionalDistance = distance - EXTENDED_FARE_DISTANCE_LIMIT - FARE_ADJUSTMENT;
        Fare fareForExtendedDistance = calculateAdditionalFareForExtendedDistance(distance);
        Fare additionalFare = calculateAdditionalFare(additionalDistance, ADDITIONAL_FARE_INTERVAL);
        return fareForExtendedDistance.add(additionalFare);
    }

    private Fare calculateAdditionalFare(final int additionalDistance, final int fareInterval) {
        return ADDITIONAL_FARE.add(ADDITIONAL_FARE.multiply(additionalDistance / fareInterval));
    }

    private Fare calculateFareForSections(Set<Section> sections) {

        return sections.stream()
                .map(Section::getFare)
                .max(Fare::compareTo)
                .orElse(Fare.zero());
    }

    private Fare discount(final Fare fare, final Integer age) {
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
