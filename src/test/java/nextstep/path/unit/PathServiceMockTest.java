package nextstep.path.unit;

import nextstep.fare.application.FareService;
import nextstep.fare.domain.Fare;
import nextstep.line.domain.Section;
import nextstep.line.domain.SectionRepository;
import nextstep.path.application.PathService;
import nextstep.path.application.dto.PathsRequest;
import nextstep.path.application.dto.PathsResponse;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import nextstep.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.path.domain.PathType.DISTANCE;
import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Mock을 활용한 지하철 경로 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {

    private static final long 구간에없는역_ID = -1L;
    private static final String 구간에없는역_NAME = "구간에없는역";

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private FareService fareService;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(sectionRepository, fareService);
    }

    @DisplayName("최단경로 조회 함수는, 출발역과 도착역을 입력하면 최단 경로 지하철 역 목록과 총 거리를 반환한다.")
    @Test
    void findShortestPathsTest() {
        // given
        Fare fare = Fare.zero();

        when(sectionRepository.findAll()).thenReturn(연결된구간);
        when(fareService.calculateFare(any())).thenReturn(fare);

        PathsRequest request = PathsRequest.builder()
                .source(강남역.getId())
                .target(교대역.getId())
                .type(DISTANCE.name())
                .age(0)
                .build();

        PathsResponse expected = PathsResponse.builder()
                .distance(DISTANCE_6 + DISTANCE_7)
                .duration(DEFAULT_DURATION + DURATION_2)
                .fare(fare)
                .stations(createStationResponse(강남역, 홍대역, 교대역))
                .build();

        // when
        PathsResponse actual = pathService.findShortestPaths(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
