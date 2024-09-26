package nextstep.path.application;

import nextstep.line.domain.SectionRepository;
import nextstep.path.application.dto.PathsRequest;
import nextstep.path.application.dto.PathsResponse;
import nextstep.path.domain.PathType;
import nextstep.path.domain.ShortestPath;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.path.domain.PathType.DISTANCE;

@Service
@Transactional(readOnly = true)
public class PathService {

    private SectionRepository sectionRepository;

    public PathService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public PathsResponse findShortestPaths(PathsRequest pathsRequest) {
        ShortestPath shortestPath = createShortestPath(pathsRequest);

        Station start = shortestPath.lookUpStation(pathsRequest.getSource());
        Station end = shortestPath.lookUpStation(pathsRequest.getTarget());

        return PathsResponse.of(shortestPath.findShortestPathInfo(start, end, pathsRequest.getAge()));
    }

    public void validatePaths(Long source, Long target) {
        ShortestPath shortestPath = createShortestPath(source, target);

        Station start = shortestPath.lookUpStation(source);
        Station end = shortestPath.lookUpStation(target);

        shortestPath.validateConnected(start, end);
    }

    private ShortestPath createShortestPath(Long source, Long target) {
        validateSameSourceAndTarget(source, target);
        return DISTANCE.createShortestPath(sectionRepository.findAll());
    }

    private ShortestPath createShortestPath(PathsRequest pathsRequest) {
        validateSameSourceAndTarget(pathsRequest.getSource(), pathsRequest.getTarget());
        PathType pathType = PathType.lookUp(pathsRequest.getType());
        return pathType.createShortestPath(sectionRepository.findAll());
    }

    private static void validateSameSourceAndTarget(Long source, Long target) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetException();
        }
    }

    private List<StationResponse> createStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
