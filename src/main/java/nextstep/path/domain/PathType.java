package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.path.application.exception.NotExistPathTypeException;

import java.util.List;

public enum PathType {

    DISTANCE{
        @Override
        public ShortestPath createShortestPath(List<Section> sections) {
            return new ShortestDistancePath(sections);
        }
    },
    DURATION{
        @Override
        public ShortestPath createShortestPath(List<Section> sections) {
            return new ShortestDurationPath(sections);
        }
    };

    public static PathType lookUp(String pathType) {
        try {
            return valueOf(pathType);
        } catch (IllegalArgumentException e) {
            throw new NotExistPathTypeException();
        }
    }

    public abstract ShortestPath createShortestPath(List<Section> sections);
}
