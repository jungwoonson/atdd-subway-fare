package nextstep.path.domain;

import nextstep.line.domain.Section;

import java.util.List;

import static nextstep.path.domain.PathType.DISTANCE;
import static nextstep.path.domain.PathType.DURATION;

public class ShortestPathFactory {

    public static ShortestPath createShortestPath(List<Section> sections, PathType pathType) {
        if (DISTANCE.equals(pathType)) {
            return ShortestDistancePath.from(sections);
        }
        if (DURATION.equals(pathType)) {
            return ShortestDurationPath.from(sections);
        }
        throw new IllegalArgumentException("지원하지 않는 타입입니다.");
    }
}
