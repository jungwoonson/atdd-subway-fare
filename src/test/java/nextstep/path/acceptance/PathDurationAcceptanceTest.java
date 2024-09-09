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

public class PathDurationAcceptanceTest implements En {

    private static final String DEFAULT_COLOR = "bg-red-600";
    private static final int 분당_홍대_시간 = 10;
    private static final int 홍대_강남_시간 = 4;
    private static final int 강남_성수_시간 = 1;
    private static final int 성수_분당_시간 = 8;

    public Long 분당역_ID;
    public Long 홍대역_ID;
    public Long 강남역_ID;
    public Long 성수역_ID;

    private ExtractableResponse<Response> response;

    public PathDurationAcceptanceTest() {
        Given("지하철역이 등록되어있음", () -> {
            분당역_ID = createStation("분당역");
            홍대역_ID = createStation("홍대역");
            강남역_ID = createStation("강남역");
            성수역_ID = createStation("성수역");
        });

        Given("지하철이 등록된 노선이 등록되어있음", () -> {
            createLine(createLineParam("신분당선", 분당역_ID, 홍대역_ID, 분당_홍대_시간));
            createLine(createLineParam("분당선", 홍대역_ID, 강남역_ID, 홍대_강남_시간));
            createLine(createLineParam("경의선", 강남역_ID, 성수역_ID, 강남_성수_시간));
            createLine(createLineParam("중앙선", 성수역_ID, 분당역_ID, 성수_분당_시간));
        });

        When("출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청", () -> {
            response = findShortestDurationPaths(강남역_ID, 분당역_ID);
        });

        Then("최소 시간 기준 경로를 응답", () -> {
            assertThat(getStationIds(response)).containsExactly(강남역_ID, 성수역_ID, 분당역_ID);
        });

        Then("총 거리와 소요 시간을 함께 응답함", () -> {
            assertThat(getDistance(response)).isEqualTo(20);
            assertThat(getDuration(response)).isEqualTo(강남_성수_시간 + 성수_분당_시간);
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

    private Map<String, Object> createLineParam(String name, Long upStationId, Long downStationId, int duration) {
        return Map.of(
                "name", name,
                "color", DEFAULT_COLOR,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", 10,
                "duration", duration
        );
    }

    private ExtractableResponse<Response> findShortestDurationPaths(Long source, Long target) {
        return RestAssured.given().log().all()
                .when().get(String.format("/paths?source=%d&target=%d&type=DURATION", source, target))
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
