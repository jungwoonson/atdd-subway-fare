package nextstep.path.domain;

import nextstep.line.domain.Section;

import java.util.List;

public class ShortestDistancePath extends ShortestPath {

    public ShortestDistancePath(List<Section> sections, PathPoint pathPoint) {
        super(sections, pathPoint);
    }

    @Override
    protected int getWeight(Section section) {
        return section.getDistance();
    }

    @Override
    protected int getDistance() {
        validateContains();
        return (int) calculateShortestPath().getWeight();
    }

    @Override
    protected int getDuration() {
        validateContains();
        return calculateShortestPath()
                .getEdgeList()
                .stream()
                .mapToInt(edge -> getSectionByEdge(edge).getDuration())
                .sum();
    }
}
