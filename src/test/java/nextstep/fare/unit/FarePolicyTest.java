package nextstep.fare.unit;

import nextstep.fare.domain.Fare;
import nextstep.fare.domain.FarePolicy;
import nextstep.line.domain.Line;
import nextstep.line.domain.Sections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 정책 단위 테스트")
public class FarePolicyTest {

    private static final Long BASE_FARE = 1250L;
    private static final Sections EMPTY_SECTIONS = new Sections();
    private static final Integer NONE_DISCOUNT_AGE = 19;
    private static final Integer BASE_DISTANCE = 1;

    @DisplayName("기본요금 함수는, 기본 요금을 생성한다.")
    @Test
    void createBaseFareTest() {
        // when
        Fare fare = FarePolicy.baseFare();

        // then
        assertThat(fare).isEqualTo(Fare.from(BASE_FARE));
    }

    @DisplayName("요금 계산 함수는, 거리 1에서 10까지는 기본 요금을 생성한다.")
    @ValueSource(ints = {1, 5, 10})
    @ParameterizedTest
    void calculateFareTest(int distance) {
        assertCalculateFare(distance, EMPTY_SECTIONS, NONE_DISCOUNT_AGE, Fare.from(BASE_FARE));
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
        assertCalculateFare(distance, EMPTY_SECTIONS, NONE_DISCOUNT_AGE, Fare.from(expected));
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
        assertCalculateFare(distance, EMPTY_SECTIONS, NONE_DISCOUNT_AGE, Fare.from(expected));
    }

    @DisplayName("요금 계산 함수는, 요금이 있는 구간이 있으면 구간 요금 중 가장 비싼 요금을 추가한다.")
    @Test
    void calculateFareForSectionsTest() {
        Line 요금없는노선 = 신분당선(강남역, 양재역);
        Line 요금250노선 = 분당선(교대역, 홍대역);
        Line 요금350노선 = 중앙선(홍대역, 양재역);
        Sections 요금있는구간 = 요금없는노선.getSections();
        Sections 요금250구간 = 요금250노선.getSections();
        Sections 요금350구간 = 요금350노선.getSections();
        Sections sections = Sections.from(List.of(요금있는구간, 요금250구간, 요금350구간));
        Fare baseFare = FarePolicy.baseFare();

        assertCalculateFare(BASE_DISTANCE, sections, NONE_DISCOUNT_AGE, baseFare.add(Fare.from(350L)));
    }

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

        assertCalculateFare(BASE_DISTANCE, EMPTY_SECTIONS, age, Fare.from(fare));
    }

    private void assertCalculateFare(Integer distance, Sections sections, Integer age, Fare expected) {
        // given
        FarePolicy farePolicy = new FarePolicy();

        // when
        Fare fare = farePolicy.calculateFare(distance, sections, age);

        // then
        assertThat(fare).isEqualTo(expected);
    }
}