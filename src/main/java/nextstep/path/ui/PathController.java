package nextstep.path.ui;

import nextstep.authentication.domain.LoginMember;
import nextstep.authentication.ui.AuthenticationPrincipal;
import nextstep.path.application.PathService;
import nextstep.path.application.dto.PathsRequest;
import nextstep.path.application.dto.PathsResponse;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathsResponse> findShortestPath(@AuthenticationPrincipal(loginRequired = false) LoginMember loginMember, @RequestParam("source") Long source, @RequestParam("target") Long target, @RequestParam("type") String type) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetException();
        }

        PathsRequest pathsRequest = PathsRequest.builder()
                .source(source)
                .target(target)
                .type(type)
                .age(loginMember.getAge())
                .build();

        return ResponseEntity.ok()
                .body(pathService.findShortestPaths(pathsRequest));
    }
}
