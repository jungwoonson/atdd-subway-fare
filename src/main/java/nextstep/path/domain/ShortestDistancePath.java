package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class ShortestDistancePath extends ShortestPath {

    private ShortestDistancePath(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        super(sections, graph);
    }

    public static ShortestDistancePath from(List<Section> sections) {
        return new ShortestDistancePath(sections, createGraph(sections));
    }

    protected static WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance());
        });
        return graph;
    }

    @Override
    public int getDistance(Station start, Station end) {
        validateContains(start, end);
        return (int) calculateShortestPath(start, end).getWeight();
    }

    @Override
    public int getDuration(Station start, Station end) {
        validateContains(start, end);
        return calculateShortestPath(start, end)
                .getEdgeList()
                .stream()
                .mapToInt(edge -> getSectionByEdge(edge).getDuration())
                .sum();
    }
}
