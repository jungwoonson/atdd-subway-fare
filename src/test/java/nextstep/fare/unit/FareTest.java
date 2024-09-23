package nextstep.fare.unit;

import nextstep.fare.domain.Fare;
import nextstep.fare.domain.LessThanMinimumDistanceException;
import nextstep.fare.domain.LessThanZeroFareException;
import org.assertj.core.api.ThrowableAssert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("요금 단위 테스트")
public class FareTest {

    private static final Long BASE_FARE = 1250L;

    @DisplayName("요금 생성 함수는, 1미만의 거리가 입력되면 예외가 발생한다.")
    @Test
    void createFareForDistanceExceptionTest() {
        // given
        int lessThenMinimumDistance = 0;

        // when
        ThrowingCallable actual = () -> Fare.from(lessThenMinimumDistance);

        // then
        assertThatThrownBy(actual).isInstanceOf(LessThanMinimumDistanceException.class);
    }

    @DisplayName("요금 생성 함수는, 0미만의 요금이 입력되면 예외가 발생한다.")
    @Test
    void createFareLessThenZeroExceptionTest() {
        // given
        Long lessThenZeroFare = -1L;

        // when
        ThrowingCallable actual = () -> Fare.from(lessThenZeroFare);

        // then
        assertThatThrownBy(actual).isInstanceOf(LessThanZeroFareException.class);
    }

    @DisplayName("요금 생성 함수는, 거리 1에서 10까지는 기본 요금을 생성한다.")
    @ValueSource(ints = {1, 5, 10})
    @ParameterizedTest
    void createBaseFareTest(int distance) {
        assertFareForDistance(distance, BASE_FARE);
    }

    @DisplayName("요금 생성 함수는, 거리 11에서 50까지는 초과된 거리 5마다 100원이 추가된 요금을 생성한다.")
    @CsvSource({
            "11, 1350",
            "15, 1350",
            "16, 1450",
            "50, 2050"
    })
    @ParameterizedTest
    void createAdditionalFareForExtendedDistanceTest(int distance, Long expected) {
        assertFareForDistance(distance, expected);
    }

    @DisplayName("요금 생성 함수는, 거리 50초과는 50까지 계산된 요금에 초과된 거리 8마다 100원이 추가된 요금을 생성한다.")
    @CsvSource({
            "51, 2150",
            "58, 2150",
            "59, 2250",
            "66, 2250",
            "67, 2350"
    })
    @ParameterizedTest
    void createAdditionalFareForLongDistanceTest(int distance, Long expected) {
        assertFareForDistance(distance, expected);
    }

    private void assertFareForDistance(int distance, Long expectedFare) {
        // when
        Fare fare = Fare.from(distance);

        // then
        assertThat(fare).isEqualTo(Fare.from(expectedFare));
    }

    @DisplayName("제로 요금 생성 함수는, 0원의 요금을 생성한다.")
    @Test
    void createZeroFareTest() {
        // when
        Fare fare = Fare.zero();

        // then
        assertThat(fare).isEqualTo(Fare.from(0L));
    }

    @DisplayName("기본 요금 생성 함수는, 1250원의 요금을 생성한다.")
    @Test
    void createBaseFareTest() {
        // when
        Fare fare = Fare.baseFare();

        // then
        assertThat(fare).isEqualTo(Fare.from(BASE_FARE));
    }

    @DisplayName("더하기 함수는, 주어진 요금중 가장 비싼 요금을 더해서 반환한다.")
    @Test
    void addMostExpensiveFareTest() {
        // given
        Fare fare = Fare.zero();
        Long amount1 = 1000L;
        Long amount2 = 2000L;
        Long amount3 = 3000L;
        List<Fare> fares = List.of(Fare.from(amount1), Fare.from(amount2), Fare.from(amount3));

        // when
        Fare actual = fare.addMostExpensiveFare(fares);

        // then
        assertThat(actual).isEqualTo(Fare.from(amount3));
    }

    @DisplayName("더하기 함수는, 주어진 요금을 더해서 반환한다.")
    @CsvSource({
            "0, 0, 0",
            "0, 1, -1",
            "1, 0, 1"
    })
    @ParameterizedTest
    void compareToTest(Long amount1, Long amount2, int expected) {
        // given
        Fare fare1 = Fare.from(amount1);
        Fare fare2 = Fare.from(amount2);

        // when
        int actual = fare1.compareTo(fare2);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
