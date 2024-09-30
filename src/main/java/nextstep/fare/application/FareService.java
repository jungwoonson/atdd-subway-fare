package nextstep.fare.application;

import nextstep.fare.application.dto.CalculateFareRequest;
import nextstep.fare.domain.AgeDiscountFarePolicy;
import nextstep.fare.domain.DistanceFarePolicy;
import nextstep.fare.domain.Fare;
import nextstep.fare.domain.SectionFarePolicy;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    private final DistanceFarePolicy distanceFarePolicy;
    private final SectionFarePolicy sectionFarePolicy;
    private final AgeDiscountFarePolicy ageDiscountFarePolicy;

    public FareService(DistanceFarePolicy distanceFarePolicy, SectionFarePolicy sectionFarePolicy, AgeDiscountFarePolicy ageDiscountFarePolicy) {
        this.distanceFarePolicy = distanceFarePolicy;
        this.sectionFarePolicy = sectionFarePolicy;
        this.ageDiscountFarePolicy = ageDiscountFarePolicy;

        distanceFarePolicy.setNextHandler(sectionFarePolicy);
        sectionFarePolicy.setNextHandler(ageDiscountFarePolicy);
    }

    // 요금 계산 메서드
    public Fare calculateFare(CalculateFareRequest request) {
        return distanceFarePolicy.calculateFare(Fare.zero(), request);
    }
}
