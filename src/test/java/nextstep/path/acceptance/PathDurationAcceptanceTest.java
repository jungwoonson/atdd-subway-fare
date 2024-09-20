package nextstep.path.acceptance;

import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static nextstep.path.acceptance.PathAcceptanceTestFixture.*;
import static nextstep.path.acceptance.PathCommonSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PathDurationAcceptanceTest implements En {

    private static final int 분당_홍대_시간 = 10;
    private static final int 홍대_강남_시간 = 4;
    private static final int 강남_성수_시간 = 1;
    private static final int 성수_분당_시간 = 8;
    private ExtractableResponse<Response> response;

    public PathDurationAcceptanceTest() {
        Given("시간 계산을 위한 지하철 노선이 등록돼 있다", () -> {
            createLine(createLineParam("신분당선", 분당역_ID, 홍대역_ID, DEFAULT_DISTANCE, 분당_홍대_시간));
            createLine(createLineParam("분당선", 홍대역_ID, 강남역_ID, DEFAULT_DISTANCE, 홍대_강남_시간));
            createLine(createLineParam("경의선", 강남역_ID, 성수역_ID, DEFAULT_DISTANCE, 강남_성수_시간));
            createLine(createLineParam("중앙선", 성수역_ID, 분당역_ID, DEFAULT_DISTANCE, 성수_분당_시간));
        });

        When("출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면", () -> {
            response = findShortestPaths(강남역_ID, 분당역_ID, "DURATION");
        });

        Then("최소 시간 기준 경로를 응답한다", () -> {
            assertThat(getStationIds(response)).containsExactly(강남역_ID, 성수역_ID, 분당역_ID);
        });

        Then("총 거리와 소요 시간을 함께 응답한다", () -> {
            assertThat(getDistance(response)).isEqualTo(DEFAULT_DISTANCE + DEFAULT_DISTANCE);
            assertThat(getDuration(response)).isEqualTo(강남_성수_시간 + 성수_분당_시간);
        });
    }
}
