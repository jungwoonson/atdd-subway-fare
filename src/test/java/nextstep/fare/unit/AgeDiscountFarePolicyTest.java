package nextstep.fare.unit;

import nextstep.fare.application.dto.CalculateFareRequest;
import nextstep.fare.domain.AbstractFarePolicy;
import nextstep.fare.domain.AgeDiscountFarePolicy;
import nextstep.fare.domain.Fare;
import nextstep.line.domain.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static nextstep.fare.unit.FarePolicyTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("나이 요금 정책 단위 테스트")
public class AgeDiscountFarePolicyTest {

    @DisplayName("요금 계산 함수는, 어린이와, 청소년의 나이를 입력받으면 350원 공제 후 50%, 80% 요금을 적요한다.")
    @ParameterizedTest
    @CsvSource({
            "5, 1250",
            "6, 450",
            "12, 450",
            "13, 720",
            "18, 720",
            "19, 1250",
    })
    void calculateFareForAgeTest(Integer age, Long fare) {

        assertCalculateFare(BASE_DISTANCE, age, EMPTY_SECTIONS, Fare.from(fare));
    }

    public static void assertCalculateFare(Integer distance, Integer age, Set<Section> sections, Fare expected) {
        // given
        CalculateFareRequest request = new CalculateFareRequest(distance, age, sections);
        AgeDiscountFarePolicy ageDiscountFarePolicy = new AgeDiscountFarePolicy();

        // when
        Fare fare = ageDiscountFarePolicy.calculateFare(Fare.from(BASE_FARE), request);

        // then
        assertThat(fare).isEqualTo(expected);
    }
}
