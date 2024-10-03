package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.path.application.exception.NotAddedEndToPathsException;
import nextstep.path.application.exception.NotAddedStartToPathsException;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ShortestPath {

    private GraphPath<Station, DefaultWeightedEdge> path;

    protected PathPoint pathPoint;
    protected List<Section> sections;
    protected WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    protected ShortestPath(List<Section> sections, PathPoint pathPoint) {
        this.sections = sections;
        this.graph = createGraph(sections);
        this.pathPoint = pathPoint;
    }

    protected WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, getWeight(section));
        });
        return graph;
    }

    public Path find() {
        int distance = getDistance();
        return Path.builder()
                .distance(distance)
                .duration(getDuration())
                .stations(getStations())
                .sections(getUsedSections())
                .build();
    }

    private List<Station> getStations() {
        validateContains();
        return calculateShortestPath().getVertexList();
    }

    protected GraphPath<Station, DefaultWeightedEdge> calculateShortestPath() {
        if (this.path != null) {
            return this.path;
        }
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        Station source = lookUpStation(pathPoint.getSourceId());
        Station target = lookUpStation(pathPoint.getTargetId());
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        validateConnected(path);
        return path;
    }

    protected Section getSectionByEdge(DefaultWeightedEdge edge) {
        Station upStation = graph.getEdgeSource(edge);
        Station downStation = graph.getEdgeTarget(edge);

        return sections.stream()
                .filter(section -> upStation.equals(section.getUpStation()) && downStation.equals(section.getDownStation()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("엣지에 해당하는 구간을 찾을 수 없습니다."));
    }

    private Station lookUpStation(Long stationId) {
        return graph.vertexSet().stream()
                .filter(station -> stationId.equals(station.getId()))
                .findFirst()
                .orElseThrow(() -> new NotAddedStationsToPathsException("stationId " + stationId));
    }

    private Set<Section> getUsedSections() {
        return calculateShortestPath()
                .getEdgeList()
                .stream()
                .map(this::getSectionByEdge)
                .collect(Collectors.toSet());
    }

    private void validateConnected(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new NotConnectedPathsException();
        }
    }

    public void validateConnected() {
        calculateShortestPath();
    }

    protected void validateContains() {
        Station start = lookUpStation(pathPoint.getSourceId());
        if (!graph.containsVertex(start)) {
            throw new NotAddedStartToPathsException(start.getName());
        }
        Station end = lookUpStation(pathPoint.getTargetId());
        if (!graph.containsVertex(end)) {
            throw new NotAddedEndToPathsException(end.getName());
        }
    }

    protected abstract int getWeight(Section section);

    protected abstract int getDistance();

    protected abstract int getDuration();
}
