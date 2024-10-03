package nextstep.path.application;

import nextstep.fare.application.FareService;
import nextstep.fare.application.dto.CalculateFareRequest;
import nextstep.line.domain.SectionRepository;
import nextstep.path.application.dto.PathsRequest;
import nextstep.path.application.dto.PathsResponse;
import nextstep.path.domain.Path;
import nextstep.path.domain.PathPoint;
import nextstep.path.domain.PathType;
import nextstep.path.domain.ShortestPath;
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
    private FareService fareService;

    public PathService(SectionRepository sectionRepository, FareService fareService) {
        this.sectionRepository = sectionRepository;
        this.fareService = fareService;
    }

    public PathsResponse findShortestPaths(PathsRequest pathsRequest) {
        ShortestPath shortestPath = createShortestPath(pathsRequest);
        Path path = shortestPath.find();
        return PathsResponse.of(path, fareService.calculateFare(new CalculateFareRequest(path, pathsRequest.getAge())));
    }

    public void validatePaths(Long source, Long target) {
        ShortestPath shortestPath = createShortestPath(source, target);
        shortestPath.validateConnected();
    }

    private ShortestPath createShortestPath(Long source, Long target) {
        PathPoint pathPoint = PathPoint.of(source, target);
        return DISTANCE.createShortestPath(sectionRepository.findAll(), pathPoint);
    }

    private ShortestPath createShortestPath(PathsRequest pathsRequest) {
        PathPoint pathPoint = PathPoint.of(pathsRequest.getSource(), pathsRequest.getTarget());
        PathType pathType = PathType.lookUp(pathsRequest.getType());
        return pathType.createShortestPath(sectionRepository.findAll(), pathPoint);
    }

    private List<StationResponse> createStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
