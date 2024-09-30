package nextstep.fare.unit;

import nextstep.fare.application.FareService;
import nextstep.fare.domain.AgeDiscountFarePolicy;
import nextstep.fare.domain.DistanceFarePolicy;
import nextstep.fare.domain.SectionFarePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FareServiceTest {

    private DistanceFarePolicy distanceFarePolicy;
    private SectionFarePolicy sectionFarePolicy;
    private AgeDiscountFarePolicy ageDiscountFarePolicy;

    private FareService fareService;

    @BeforeEach
    public void setUp() {
        distanceFarePolicy = mock(DistanceFarePolicy.class);
        sectionFarePolicy = mock(SectionFarePolicy.class);
        ageDiscountFarePolicy = mock(AgeDiscountFarePolicy.class);

        fareService = new FareService(distanceFarePolicy, sectionFarePolicy, ageDiscountFarePolicy);
    }

    @Test
    public void testFarePolicyChain() {
        // then
        verify(distanceFarePolicy).setNextHandler(sectionFarePolicy);
        verify(sectionFarePolicy).setNextHandler(ageDiscountFarePolicy);
    }
}