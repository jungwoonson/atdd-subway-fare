package nextstep.fare.unit;

import nextstep.fare.application.dto.CalculateFareRequest;
import nextstep.fare.domain.AgeDiscountFarePolicy;
import nextstep.fare.domain.DistanceFarePolicy;
import nextstep.fare.domain.Fare;
import nextstep.line.domain.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static nextstep.fare.unit.FarePolicyTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리 요금 정책 단위 테스트")
public class DistanceFarePolicyTest {

    @DisplayName("요금 계산 함수는, 거리 1에서 10까지는 기본 요금을 생성한다.")
    @ValueSource(ints = {1, 5, 10})
    @ParameterizedTest
    void calculateFareTest(int distance) {
        assertCalculateFare(distance, NONE_DISCOUNT_AGE, EMPTY_SECTIONS, Fare.from(BASE_FARE));
    }

    @DisplayName("요금 계산 함수는, 거리 11에서 50까지는 초과된 거리 5마다 100원이 추가된 요금을 생성한다.")
    @CsvSource({
            "11, 1350",
            "15, 1350",
            "16, 1450",
            "50, 2050"
    })
    @ParameterizedTest
    void calculateAdditionalFareForExtendedDistanceTest(int distance, Long expected) {
        assertCalculateFare(distance, NONE_DISCOUNT_AGE, EMPTY_SECTIONS, Fare.from(expected));
    }

    @DisplayName("요금 계산 함수는, 거리 50초과는 50까지 계산된 요금에 초과된 거리 8마다 100원이 추가된 요금을 생성한다.")
    @CsvSource({
            "51, 2150",
            "58, 2150",
            "59, 2250",
            "66, 2250",
            "67, 2350"
    })
    @ParameterizedTest
    void calculateAdditionalFareForLongDistanceTest(int distance, Long expected) {
        assertCalculateFare(distance, NONE_DISCOUNT_AGE, EMPTY_SECTIONS, Fare.from(expected));
    }

    public static void assertCalculateFare(Integer distance, Integer age, Set<Section> sections, Fare expected) {
        // given
        CalculateFareRequest request = new CalculateFareRequest(distance, age, sections);
        DistanceFarePolicy distanceFarePolicy = new DistanceFarePolicy();

        // when
        Fare fare = distanceFarePolicy.calculateFare(Fare.zero(), request);

        // then
        assertThat(fare).isEqualTo(expected);
    }
}
