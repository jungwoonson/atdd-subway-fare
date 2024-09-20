package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.path.application.exception.NotExistPathTypeException;

import java.util.List;

public enum PathType {

    DISTANCE,
    DURATION;

    public static PathType lookUp(String pathType) {
        try {
            return valueOf(pathType);
        } catch (IllegalArgumentException e) {
            throw new NotExistPathTypeException();
        }
    }

    public ShortestPath createShortestPath(List<Section> sections) {
        if (DISTANCE.equals(this)) {
            return ShortestDistancePath.from(sections);
        }
        if (DURATION.equals(this)) {
            return ShortestDurationPath.from(sections);
        }
        throw new IllegalArgumentException("지원하지 않는 타입입니다.");
    }
}
