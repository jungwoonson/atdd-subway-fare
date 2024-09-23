package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShortestDurationPath extends ShortestPath {

    public ShortestDurationPath(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        super(sections, graph);
    }

    public static ShortestDurationPath from(List<Section> sections) {
        return new ShortestDurationPath(sections, createGraph(sections));
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDuration());
        });
        return graph;
    }

    @Override
    public int getDuration(Station start, Station end) {
        validateContains(start, end);
        return (int) findShortestPath(start, end).getWeight();
    }

    @Override
    public int getDistance(Station start, Station end) {
        validateContains(start, end);
        return findShortestPath(start, end)
                .getEdgeList()
                .stream()
                .mapToInt(edge -> getSectionByEdge(edge).getDistance())
                .sum();
    }

    @Override
    public Set<Section> getUsedSections(Station start, Station end) {
        return findShortestPath(start, end)
                .getEdgeList().stream()
                .map(this::getSectionByEdge)
                .collect(Collectors.toSet());
    }
}
