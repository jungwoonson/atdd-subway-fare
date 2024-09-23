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

public abstract class ShortestPath {

    protected List<Section> sections;
    protected WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    protected ShortestPath(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.sections = sections;
        this.graph = graph;
    }

    public List<Station> getStations(Station start, Station end) {
        validateContains(start, end);
        return findShortestPath(start, end).getVertexList();
    }

    protected void validateConnected(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
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

    protected GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station start, Station end) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(start, end);
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

    public Station lookUpStation(Long station_id) {
        return graph.vertexSet().stream()
                .filter(station -> station_id.equals(station.getId()))
                .findFirst()
                .orElseThrow(() -> new NotAddedStationsToPathsException("station_id " + station_id));
    }

    public abstract int getDistance(Station start, Station end);

    public abstract int getDuration(Station start, Station end);
}
