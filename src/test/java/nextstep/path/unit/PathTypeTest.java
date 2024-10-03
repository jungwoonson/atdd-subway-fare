package nextstep.path.unit;

import nextstep.path.application.exception.NotExistPathTypeException;
import nextstep.path.domain.*;
import org.assertj.core.api.ThrowableAssert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PathType 단위 테스트")
public class PathTypeTest {

    @DisplayName("구분 조회 함수는, 존재하지 않는 경로 구분을 입력한 경우 예외를 발생한다.")
    @Test
    void findPathType() {
        // given
        String pathTypeName = "NONE";

        // when
        ThrowingCallable actual = () -> PathType.lookUp(pathTypeName);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotExistPathTypeException.class);
    }

    @DisplayName("최단 경로 클래스 생성 함수는, 입력된 구분에 따라 클래스를 생성한다.")
    @ParameterizedTest
    @MethodSource("provideTypesArguments")
    void createShortestPathTest(PathType pathType, Class<?> expected) {
        // given
        Long source = 1L;
        Long target = 2L;

        // when
        ShortestPath actual = pathType.createShortestPath(new ArrayList<>(), PathPoint.of(source, target));

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
