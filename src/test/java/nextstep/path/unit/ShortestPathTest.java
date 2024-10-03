package nextstep.path.unit;

import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.path.domain.PathPoint;
import nextstep.path.domain.PathType;
import nextstep.path.domain.ShortestPath;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("최단 경로 단위 테스트")
public class ShortestPathTest {

    @DisplayName("경로 조회 함수는, 경로 지점 중 구간에 존재 하지 않는 역이 포함된 경우 예외를 반환한다.")
    @ParameterizedTest
    @MethodSource("notAddedStations")
    void notAddedStationsToPathsExceptionTest(PathPoint pathPoint) {
        PathType[] pathType = PathType.values();
        for (PathType type : pathType) {
            // given
            ShortestPath 연결된경로 = type.createShortestPath(연결된구간, pathPoint);

            // when
            ThrowableAssert.ThrowingCallable actual = () -> 연결된경로.find();

            // then
            assertThatThrownBy(actual).isInstanceOf(NotAddedStationsToPathsException.class);
        }
    }

    private static Stream<Arguments> notAddedStations() {
        Long notAddedStationId = -1L;
        return Stream.of(
                Arguments.of(PathPoint.of(notAddedStationId, 강남역.getId())),
                Arguments.of(PathPoint.of(강남역.getId(), notAddedStationId))
        );
    }

    @DisplayName("경로 조회 함수는, 경로 지점이 연결된 경로가 없는 경우 예외를 반환한다.")
    @ParameterizedTest
    @EnumSource
    void notConnectedPathsExceptionTest(PathType pathType) {
        // given
        ShortestPath 연결안된경로 = pathType.createShortestPath(List.of(강남역_양재역, 교대역_홍대역), PathPoint.of(강남역, 교대역));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 연결안된경로.find();

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }

    @DisplayName("경로 검사 함수는, 경로 지점이 연결된 경로가 없는 경우 예외를 반환한다.")
    @ParameterizedTest
    @EnumSource
    void validateConnectedTest(PathType pathType) {
        // given
        ShortestPath 연결안된경로 = pathType.createShortestPath(List.of(강남역_양재역, 교대역_홍대역), PathPoint.of(강남역, 교대역));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 연결안된경로.validateConnected();

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }
}
