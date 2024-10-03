package nextstep.path.domain;

import nextstep.path.ui.exception.SameSourceAndTargetException;
import nextstep.station.domain.Station;

import java.util.Objects;

public class PathPoint {

    private Long sourceId;
    private Long targetId;

    private PathPoint(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static PathPoint of(Long sourceId, Long targetId) {
        validate(sourceId, targetId);
        return new PathPoint(sourceId, targetId);
    }

    public static PathPoint of(Station source, Station target) {
        Long sourceId = source.getId();
        Long targetId = target.getId();
        validate(sourceId, targetId);
        return new PathPoint(sourceId, targetId);
    }

    private static void validate(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new SameSourceAndTargetException();
        }
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathPoint that = (PathPoint) o;
        return Objects.equals(sourceId, that.sourceId) && Objects.equals(targetId, that.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId, targetId);
    }

    @Override
    public String toString() {
        return "BaseStation{" +
                "sourceId=" + sourceId +
                ", targetId=" + targetId +
                '}';
    }
}
