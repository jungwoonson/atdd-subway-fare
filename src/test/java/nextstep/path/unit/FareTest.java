package nextstep.path.unit;

import nextstep.path.domain.Fare;
import nextstep.path.domain.LessThanMinimumDistanceException;
import org.assertj.core.api.ThrowableAssert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("요금 단위 테스트")
public class FareTest {

    private static final long BASE_FARE = 1250L;

    @DisplayName("요금 생성 함수는, 1미만의 거리가 입력되면 예외를 발생한다.")
    @Test
    void createFareExceptionTest() {
        // when
        ThrowingCallable actual = () -> Fare.from(0);

        // then
        assertThatThrownBy(actual).isInstanceOf(LessThanMinimumDistanceException.class);
    }

    @DisplayName("요금 생성 함수는, 거리 10이하 가 주어지면 기본운임을 가진다.")
    @ValueSource(ints = {1, 10})
    @ParameterizedTest
    void calculateFareTest(int distance) {
        // when
        Fare fare = Fare.from(distance);

        // then
        assertThat(fare).isEqualTo(new Fare(BASE_FARE));
    }

    @DisplayName("요금 조회 함수는, 거리 10초과 50이하는 기본운임 + 초과된 거리 5마다 100이 추가된 요금을 반환한다.")
    @CsvSource({"11,1350", "15,1350", "16,1450", "50,2050"})
    @ParameterizedTest
    void calculateAdditionalTest(int distance, Long expected) {
        // when
        Fare fare = Fare.from(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(new Fare(expected).getFare());
    }

    @DisplayName("요금 조회 함수는, 거리 50초과는 50이하 요금 + 초과된 거리 8마다 100이 추가된 요금을 반환한다.")
    @CsvSource({"51,2150", "58,2150", "59,2250", "66,2250", "67,2350"})
    @ParameterizedTest
    void calculateReducedAdditionalFare(int distance, Long expected) {
        // when
        Fare fare = Fare.from(distance);

        // then
        assertThat(fare).isEqualTo(new Fare(expected));
    }
}
