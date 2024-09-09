package nextstep.path.unit;

import nextstep.path.application.exception.NotExistPathTypeException;
import nextstep.path.domain.PathType;
import org.assertj.core.api.ThrowableAssert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
