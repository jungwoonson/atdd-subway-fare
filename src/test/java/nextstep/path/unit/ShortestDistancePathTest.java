package nextstep.path.unit;

import nextstep.line.domain.Section;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.path.domain.ShortestDistancePath;
import nextstep.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("최단 거리 경로 테스트")
public class ShortestDistancePathTest {

    private static final ShortestDistancePath 연결된경로 = new ShortestDistancePath(연결된구간);
    private static final ShortestDistancePath 열결되지않은경로 = new ShortestDistancePath(List.of(강남역_양재역, 교대역_홍대역));

    @DisplayName("지하철역 조회 함수는, 가장 짧은 지하철 역 목록을 반환한다.")
    @Test
    void getStations() {
        // when
        List<Station> actual =  연결된경로.getStations(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(List.of(강남역, 홍대역, 교대역));
    }

    @DisplayName("지하철역 조회 함수는, 출발역과 도착역이 연결되어있지 않으면 예외를 발생한다.")
    @Test
    void getStationsNotConnectedStationsExceptionTest() {
        // when
        ThrowingCallable actual = () -> 열결되지않은경로.getStations(강남역, 교대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }

    @DisplayName("거리 조회 함수는, 가장 짧은 거리를 반환한다.")
    @Test
    void getDistance() {
        // when
        int actual =  연결된경로.getDistance(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(DISTANCE_6 + DISTANCE_7);
    }

    @DisplayName("소요 시간 조회 함수는, 가장 짧은 거리의 소요시간을 반환한다.")
    @Test
    void getDuration() {
        // when
        int actual =  연결된경로.getDuration(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(DURATION_2 + DEFAULT_DURATION);
    }

    @DisplayName("거리 조회 함수는, 출발역과 도착역이 연결되어있지 않으면 예외를 발생한다.")
    @Test
    void getDistanceNotConnectedStationsExceptionTest() {
        // when
        ThrowingCallable actual = () -> 열결되지않은경로.getDistance(강남역, 교대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }

    @DisplayName("포함 확인 함수는, 주어진 출발 역 또는 도착 역이 경로에 포함되어 있지 않으면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("validateContainsParams")
    void validateContainsTest(Station start, Station end, String expectedMessage) {
        // when
        ThrowingCallable actual = () -> 연결된경로.validateContains(start, end);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedStationsToPathsException.class)
                .hasMessageContaining(expectedMessage);
    }

    private static Stream<Arguments> validateContainsParams() {
        Station 포함되지_않은_역 = Station.of(-1L, "포함되지 않은 역");
        return Stream.of(
                Arguments.of(포함되지_않은_역, 강남역, String.format("출발역(%s)", 포함되지_않은_역.getName())),
                Arguments.of(강남역, 포함되지_않은_역, String.format("도착역(%s)", 포함되지_않은_역.getName()))
        );
    }

    @DisplayName("연결 확인 함수는, 주어진 출발 역과 도착 역이 연결되지 않으면 예외를 발생시킨다.")
    @Test
    void validateConnectedTest() {
        // when
        ThrowingCallable actual = () -> 열결되지않은경로.validateConnected(강남역, 교대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }

    @DisplayName("역 조회 함수는, 주어진 역 id에 해당하는 역을 반환한다.")
    @Test
    void lookUpStationTest() {
        // when
        var actual = 연결된경로.lookUpStation(강남역.getId());

        // then
        assertThat(actual).isEqualTo(강남역);
    }

    @DisplayName("연결 확인 함수는, 주어진 역을 찾을 수 없을 경우 예외를 발생시킨다.")
    @Test
    void lookUpNotAddedStationsToPathsExceptionTest() {
        // when
        ThrowingCallable actual = () -> 연결된경로.lookUpStation(0L);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedStationsToPathsException.class);
    }

    @DisplayName("사용된 구간 조회 함수는, 중복되지 않는 사용된 구간을 반환한다.")
    @Test
    void getUsedSectionsTest() {
        // when
        Set<Section> actual =  연결된경로.getUsedSections(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(Set.of(교대역_홍대역, 홍대역_강남역));
    }
}
