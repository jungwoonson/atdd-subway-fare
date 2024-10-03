package nextstep.fare.unit;

import nextstep.fare.application.dto.CalculateFareRequest;
import nextstep.fare.domain.AgeDiscountFarePolicy;
import nextstep.fare.domain.Fare;
import nextstep.fare.domain.SectionFarePolicy;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static nextstep.fare.unit.FarePolicyTestFixture.*;
import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 요금 정책 단위 테스트")
public class SectionFarePolicyTest {

    @DisplayName("요금 계산 함수는, 요금이 있는 구간이 있으면 구간 요금 중 가장 비싼 요금을 추가한다.")
    @Test
    void calculateFareForSectionsTest() {
        Line 요금없는노선 = 신분당선(강남역, 양재역);
        Line 요금250노선 = 분당선(교대역, 홍대역);
        Line 요금350노선 = 중앙선(홍대역, 양재역);
        Section 요금없는구간 = Section.builder()
                .line(요금없는노선)
                .upStation(강남역)
                .downStation(양재역)
                .distance(BASE_DISTANCE)
                .build();
        Section 요금250구간 = Section.builder()
                .line(요금250노선)
                .upStation(교대역)
                .downStation(홍대역)
                .distance(BASE_DISTANCE)
                .build();
        Section 요금350구간 = Section.builder()
                .line(요금350노선)
                .upStation(홍대역)
                .downStation(양재역)
                .distance(BASE_DISTANCE)
                .build();
        Set<Section> sections = Set.of(요금없는구간, 요금250구간, 요금350구간);

        assertCalculateFare(BASE_DISTANCE, NONE_DISCOUNT_AGE, sections, Fare.from(350L));
    }

    public static void assertCalculateFare(Integer distance, Integer age, Set<Section> sections, Fare expected) {
        // given
        CalculateFareRequest request = new CalculateFareRequest(distance, age, sections);
        SectionFarePolicy sectionFarePolicy = new SectionFarePolicy();

        // when
        Fare fare = sectionFarePolicy.calculateFare(Fare.zero(), request);

        // then
        assertThat(fare).isEqualTo(expected);
    }
}
