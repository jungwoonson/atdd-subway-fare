package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;

import java.util.List;

public class ShortestDurationPath extends ShortestPath {

    public ShortestDurationPath(List<Section> sections) {
        super(sections);
    }

    @Override
    protected int getWeight(Section section) {
        return section.getDuration();
    }

    @Override
    public int getDistance(Station start, Station end) {
        validateContains(start, end);
        return calculateShortestPath(start, end)
                .getEdgeList()
                .stream()
                .mapToInt(edge -> getSectionByEdge(edge).getDistance())
                .sum();
    }

    @Override
    public int getDuration(Station start, Station end) {
        validateContains(start, end);
        return (int) calculateShortestPath(start, end).getWeight();
    }
}
