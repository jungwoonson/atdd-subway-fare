package nextstep.path.domain;

import nextstep.line.domain.Section;

import java.util.List;

public class ShortestDurationPath extends ShortestPath {

    public ShortestDurationPath(List<Section> sections, PathPoint pathPoint) {
        super(sections, pathPoint);
    }

    @Override
    protected int getWeight(Section section) {
        return section.getDuration();
    }

    @Override
    protected int getDistance() {
        validateContains();
        return calculateShortestPath()
                .getEdgeList()
                .stream()
                .mapToInt(edge -> getSectionByEdge(edge).getDistance())
                .sum();
    }

    @Override
    protected int getDuration() {
        validateContains();
        return (int) calculateShortestPath().getWeight();
    }
}
