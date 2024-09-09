package nextstep.path.acceptance;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathDistanceAcceptanceTest implements En {

    private static final String DEFAULT_COLOR = "bg-red-600";
    private static final int 분당_홍대_거리 = 10;
    private static final int 홍대_강남_거리 = 4;
    private static final int 강남_성수_거리 = 1;
    private static final int 성수_분당_거리 = 8;

    public Long 분당역_ID;
    public Long 홍대역_ID;
    public Long 강남역_ID;
    public Long 성수역_ID;

    private ExtractableResponse<Response> response;

    public PathDistanceAcceptanceTest() {
        Given("지하철역이 등록되어 있다", () -> {
            분당역_ID = createStation("분당역");
            홍대역_ID = createStation("홍대역");
            강남역_ID = createStation("강남역");
            성수역_ID = createStation("성수역");
        });

        Given("지하철 노선이 등록되어 있다", () -> {
            createLine(createLineParam("신분당선", 분당역_ID, 홍대역_ID, 분당_홍대_거리));
            createLine(createLineParam("분당선", 홍대역_ID, 강남역_ID, 홍대_강남_거리));
            createLine(createLineParam("경의선", 강남역_ID, 성수역_ID, 강남_성수_거리));
            createLine(createLineParam("중앙선", 성수역_ID, 분당역_ID, 성수_분당_거리));
        });

        When("출발역과 도착역을 입력하여 최단거리 경로를 조회하면", () -> {
            response = findShortestDistancePaths(강남역_ID, 분당역_ID);
        });

        Then("출발역에서 도착역까지 최단거리의 경로가 조회된다", () -> {
            assertThat(getStationIds(response)).containsExactly(강남역_ID, 성수역_ID, 분당역_ID);
        });

        Then("출발역에서 도착역까지 최단거리와 소요시간을 함께 응답한다", () -> {
            assertThat(getDistance(response)).isEqualTo(강남_성수_거리 + 성수_분당_거리);
            assertThat(getDuration(response)).isEqualTo(20);
        });
    }

    public Long createStation(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract()
                .jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> createLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private Map<String, Object> createLineParam(String name, Long upStationId, Long downStationId, int distance) {
        return Map.of(
                "name", name,
                "color", DEFAULT_COLOR,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance,
                "duration", 10
        );
    }

    private ExtractableResponse<Response> findShortestDistancePaths(Long source, Long target) {
        return RestAssured.given().log().all()
                .when().get(String.format("/paths?source=%d&target=%d&type=DISTANCE", source, target))
                .then().log().all()
                .extract();
    }

    private List<Long> getStationIds(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("stations.id", Long.class);
    }

    private static int getDistance(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getInt("distance");
    }

    private static int getDuration(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getInt("duration");
    }
}
