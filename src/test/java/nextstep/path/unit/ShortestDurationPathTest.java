package nextstep.path.unit;

import nextstep.path.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("최단 시간 경로 테스트")
public class ShortestDurationPathTest {

    @DisplayName("경로 조회 함수는, 주어진 출발역, 도착역의 최단 시간 경로를 반환한다.")
    @Test
    void findTest() {
        // given
        ShortestDurationPath 연결된경로 = new ShortestDurationPath(연결된구간, PathPoint.of(강남역, 교대역));
        Path expected = Path.builder()
                .distance(DISTANCE_6 + DISTANCE_7)
                .duration(DURATION_2 + DEFAULT_DURATION)
                .stations(List.of(강남역, 홍대역, 교대역))
                .sections(Set.of(교대역_홍대역, 홍대역_강남역))
                .build();

        // when
        Path actual = 연결된경로.find();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
