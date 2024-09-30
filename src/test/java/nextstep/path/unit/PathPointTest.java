package nextstep.path.unit;

import nextstep.path.domain.PathPoint;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import org.assertj.core.api.ThrowableAssert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("경로 지점 단위 테스트")
public class PathPointTest {

    @DisplayName("경로 지점 생성 함수는, 같은 경로 지점이 입력되면 예외가 발생한다.")
    @Test
    void pathPointOfException() {
        // given
        Long sourceId = 1L;
        Long targetId = 1L;

        // when
        ThrowingCallable actual = () -> PathPoint.of(sourceId, targetId);

        // then
        assertThatThrownBy(actual).isInstanceOf(SameSourceAndTargetException.class);
    }
}
