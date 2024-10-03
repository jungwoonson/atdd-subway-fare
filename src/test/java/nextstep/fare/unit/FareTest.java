package nextstep.fare.unit;

import nextstep.fare.domain.Fare;
import nextstep.fare.domain.LessThanZeroFareException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("요금 단위 테스트")
public class FareTest {

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

    @DisplayName("제로 요금 생성 함수는, 0원의 요금을 생성한다.")
    @Test
    void createZeroFareTest() {
        // when
        Fare fare = Fare.zero();

        // then
        assertThat(fare).isEqualTo(Fare.from(0L));
    }

    @DisplayName("더하기 함수는, 주어진 요금을 더하여 반환한다.")
    @Test
    void addTest() {
        // given
        Fare fare = Fare.from(2000);
        Fare fareAdded = Fare.from(1000);

        // when
        Fare actual = fare.add(fareAdded);

        // then
        assertThat(actual).isEqualTo(Fare.from(3000));
    }

    @DisplayName("빼기 함수는, 주어진 요금을 빼서 반환한다.")
    @Test
    void minusTest() {
        // given
        Fare fare = Fare.from(2000);

        // when
        Fare actual = fare.minus(Fare.from(1000));

        // then
        assertThat(actual).isEqualTo(Fare.from(1000));
    }

    @DisplayName("곱하기 함수는, 주어진 수를 곱하여 반환한다.")
    @Test
    void multiplyTest() {
        // given
        Fare fare = Fare.from(2000);

        // when
        Fare actual = fare.multiply(3);

        // then
        assertThat(actual).isEqualTo(Fare.from(6000));
    }

    @DisplayName("곱하기 함수는, 주어진 double 타입 수를 곱하여 반환한다.")
    @Test
    void multiplyDoubleTest() {
        // given
        Fare fare = Fare.from(2000);

        // when
        Fare actual = fare.multiply(0.5);

        // then
        assertThat(actual).isEqualTo(Fare.from(1000));
    }

    @DisplayName("비교 함수는, 요금을 두 요금을 비교 하여 -1, 0, 1중 하나를 반환한다.")
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
