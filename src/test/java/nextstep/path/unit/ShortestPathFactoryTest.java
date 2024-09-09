package nextstep.path.unit;

import nextstep.path.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("최단 경로 팩토리 테스트")
public class ShortestPathFactoryTest {

    @DisplayName("최단 경로 클래스 생성 함수는, 입력된 구분에 따라 클래스를 생성한다.")
    @ParameterizedTest
    @MethodSource("provideTypesArguments")
    void createShortestPathTest(PathType pathType, Class<?> expected) {
        // when
        ShortestPath actual = ShortestPathFactory.createShortestPath(new ArrayList<>(), pathType);

        // then
        assertThat(actual).isInstanceOf(expected);
    }

    private static Stream<Arguments> provideTypesArguments() {
        return Stream.of(
                Arguments.of(PathType.DISTANCE, ShortestDistancePath.class),
                Arguments.of(PathType.DURATION, ShortestDurationPath.class)
        );
    }
}
