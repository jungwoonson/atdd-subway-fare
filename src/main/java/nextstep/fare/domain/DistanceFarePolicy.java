package nextstep.fare.domain;

import nextstep.fare.application.dto.CalculateFareRequest;
import org.springframework.stereotype.Component;

@Component
public class DistanceFarePolicy extends AbstractFarePolicy {

    private static final int BASE_FARE_DISTANCE_LIMIT = 10;
    private static final int EXTENDED_FARE_DISTANCE_LIMIT = 50;
    private static final int EXTENDED_FARE_INTERVAL = 5;
    private static final int ADDITIONAL_FARE_INTERVAL = 8;
    private static final int FARE_ADJUSTMENT = 1;
    private static final Fare BASE_FARE = Fare.from(1250L);
    private static final Fare ADDITIONAL_FARE = Fare.from(100L);

    @Override
    public Fare calculateFare(Fare fare, CalculateFareRequest request) {
        int distance = request.getDistance();
        if (distance <= BASE_FARE_DISTANCE_LIMIT) {
            return fare.add(BASE_FARE);
        }
        if (distance <= EXTENDED_FARE_DISTANCE_LIMIT) {
            return fare.add(calculateAdditionalFareForExtendedDistance(distance));
        }
        return fare.add(calculateAdditionalFareForLongDistance(distance));
    }

    private Fare calculateAdditionalFareForExtendedDistance(int distance) {
        int additionalDistance = Math.min(distance, EXTENDED_FARE_DISTANCE_LIMIT) - BASE_FARE_DISTANCE_LIMIT - FARE_ADJUSTMENT;
        Fare additional = ADDITIONAL_FARE.add(ADDITIONAL_FARE.multiply(additionalDistance / EXTENDED_FARE_INTERVAL));
        return BASE_FARE.add(additional);
    }

    private Fare calculateAdditionalFareForLongDistance(int distance) {
        int additionalDistance = distance - EXTENDED_FARE_DISTANCE_LIMIT - FARE_ADJUSTMENT;
        Fare extendedFare = calculateAdditionalFareForExtendedDistance(EXTENDED_FARE_DISTANCE_LIMIT);
        Fare longDistanceFare = ADDITIONAL_FARE.add(ADDITIONAL_FARE.multiply(additionalDistance / ADDITIONAL_FARE_INTERVAL));
        return extendedFare.add(longDistanceFare);
    }
}