package nextstep.path.unit;

import nextstep.path.domain.PathPoint;
import nextstep.path.domain.ShortestDistancePath;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static nextstep.utils.UnitTestFixture.*;

@DisplayName("최단 시간 경로 테스트")
public class ShortestDurationPathTest {

    private static final ShortestDistancePath 연결된경로;
    private static final ShortestDistancePath 열결되지않은경로;

    static {
        PathPoint pathPoint = PathPoint.of(강남역, 교대역);
        연결된경로 = new ShortestDistancePath(연결된구간, pathPoint);
        열결되지않은경로 = new ShortestDistancePath(List.of(강남역_양재역, 교대역_홍대역), pathPoint);
    }
}
