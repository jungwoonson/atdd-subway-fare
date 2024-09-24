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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ShortestPath {

    private static final String CACHE_KEY_FORMAT = "%s=>%s";
    private final Map<String, GraphPath<Station, DefaultWeightedEdge>> pathCache = new HashMap<>();

    protected List<Section> sections;
    protected WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    protected ShortestPath(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.sections = sections;
        this.graph = graph;
    }

    public List<Station> getStations(Station start, Station end) {
        validateContains(start, end);
        return calculateShortestPath(start, end).getVertexList();
    }

    protected GraphPath<Station, DefaultWeightedEdge> calculateShortestPath(Station start, Station end) {
        if (pathCache.containsKey(getCacheKey(start, end))) {
            return pathCache.get(getCacheKey(start, end));
        }
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(start, end);
        validateConnected(path);
        pathCache.put(getCacheKey(start, end), path);
        return path;
    }

    private static String getCacheKey(Station start, Station end) {
        return String.format(CACHE_KEY_FORMAT, start.getName(), end.getName());
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

    public Set<Section> getUsedSections(Station start, Station end) {
        return calculateShortestPath(start, end)
                .getEdgeList()
                .stream()
                .map(this::getSectionByEdge)
                .collect(Collectors.toSet());
    }

    protected void validateConnected(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new NotConnectedPathsException();
        }
    }

    public void validateConnected(Station start, Station end) {
        if (calculateShortestPath(start, end) == null) {
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

    public abstract int getDistance(Station start, Station end);

    public abstract int getDuration(Station start, Station end);
}
