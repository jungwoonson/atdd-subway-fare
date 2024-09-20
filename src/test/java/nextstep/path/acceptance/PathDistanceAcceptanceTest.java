package nextstep.path.acceptance;

import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static nextstep.path.acceptance.PathAcceptanceTestFixture.*;
import static nextstep.path.acceptance.PathCommonSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PathDistanceAcceptanceTest implements En {

    private static final int 분당_홍대_거리 = 10;
    private static final int 홍대_강남_거리 = 4;
    private static final int 강남_성수_거리 = 1;
    private static final int 성수_분당_거리 = 8;

    private static final long DEFAULT_FARE = 1250;

    private ExtractableResponse<Response> response;

    public PathDistanceAcceptanceTest() {
        Given("거리 계산을 위한 지하철역이 등록돼 있다", () -> {
            분당역_ID = createStation("분당역");
            홍대역_ID = createStation("홍대역");
            강남역_ID = createStation("강남역");
            성수역_ID = createStation("성수역");
        });

        Given("거리 계산을 위한 지하철 노선이 등록돼 있다", () -> {
            createLine(createLineParam("신분당선", 분당역_ID, 홍대역_ID, 분당_홍대_거리, DEFAULT_DURATION));
            createLine(createLineParam("분당선", 홍대역_ID, 강남역_ID, 홍대_강남_거리, DEFAULT_DURATION));
            createLine(createLineParam("경의선", 강남역_ID, 성수역_ID, 강남_성수_거리, DEFAULT_DURATION));
            createLine(createLineParam("중앙선", 성수역_ID, 분당역_ID, 성수_분당_거리, DEFAULT_DURATION));
        });

        When("출발역과 도착역을 입력하여 최단거리 경로를 조회하면", () -> {
            response = findShortestPaths(강남역_ID, 분당역_ID, "DISTANCE");
        });

        Then("출발역에서 도착역까지 최단거리의 경로가 조회된다", () -> {
            assertThat(getStationIds(response)).containsExactly(강남역_ID, 성수역_ID, 분당역_ID);
        });

        Then("출발역에서 도착역까지 최단거리와 소요시간을 함께 응답한다", () -> {
            assertThat(getDistance(response)).isEqualTo(강남_성수_거리 + 성수_분당_거리);
            assertThat(getDuration(response)).isEqualTo(DEFAULT_DURATION + DEFAULT_DURATION);
        });

        Then("지하철 이용 요금도 응답한다", () -> {
            assertThat(getFare(response)).isEqualTo(DEFAULT_FARE);
        });
    }
}