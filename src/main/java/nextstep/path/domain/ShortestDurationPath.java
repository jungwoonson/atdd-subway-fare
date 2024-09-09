package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.path.application.exception.NotAddedEndToPathsException;
import nextstep.path.application.exception.NotAddedStartToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class ShortestDurationPath implements ShortestPath {

    private List<Section> sections;
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private int maxDuration;

    private ShortestDurationPath(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph, int maxDuration) {
        this.sections = sections;
        this.graph = graph;
        this.maxDuration = maxDuration;
    }

    public static ShortestDurationPath from(List<Section> sections) {
        return new ShortestDurationPath(sections, createGraph(sections), maxDuration(sections));
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

    private static int maxDuration(List<Section> sections) {
        return sections.stream()
                .mapToInt(Section::getDuration)
                .max()
                .orElse(0);
    }

    public List<Station> getStations(Station start, Station end) {
        validateContains(start, end);
        GraphPath<Station, DefaultWeightedEdge> paths = findShortestPath(start, end);
        validateConnected(paths);
        return paths.getVertexList();
    }

    private void validateConnected(GraphPath<Station, DefaultWeightedEdge> paths) {
        if (paths == null) {
            throw new NotConnectedPathsException();
        }
    }

    public void validateConnected(Station start, Station end) {
        if (findShortestPath(start, end) == null) {
            throw new NotConnectedPathsException();
        }
    }

    public void validateContains(Station start, Station end) {
        if (!graph.containsVertex(start)) {
            throw new NotAddedStartToPathsException(start.getName());
        }
        if (!graph.containsVertex(end)) {
            throw new NotAddedEndToPathsException(end.getName());
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station start, Station end) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(start, end);
    }

    @Override
    public int getDuration(Station start, Station end) {
        validateContains(start, end);
        List<GraphPath<Station, DefaultWeightedEdge>> paths = new KShortestPaths<>(graph, maxDuration).getPaths(start, end);
        validateConnected(paths);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = paths.get(0);
        return (int) shortestPath.getWeight();
    }

    @Override
    public int getDistance(Station start, Station end) {
        validateContains(start, end);
        GraphPath<Station, DefaultWeightedEdge> paths = findShortestPath(start, end);
        validateConnected(paths);

        return paths.getEdgeList().stream()
                .mapToInt(edge -> getSectionByEdge(edge).getDistance())
                .sum();
    }

    private Section getSectionByEdge(DefaultWeightedEdge edge) {
        Station upStation = graph.getEdgeSource(edge);
        Station downStation = graph.getEdgeTarget(edge);

        return sections.stream()
                .filter(section -> upStation.equals(section.getUpStation()) && downStation.equals(section.getDownStation()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("엣지에 해당하는 구간을 찾을 수 없습니다."));
    }

    private void validateConnected(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
        if (paths.isEmpty()) {
            throw new NotConnectedPathsException();
        }
    }
}
