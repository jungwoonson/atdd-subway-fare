package nextstep.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathAcceptanceTestFixture {

    private static final String DEFAULT_COLOR = "bg-red-600";

    public static final int DEFAULT_DISTANCE = 10;
    public static final int DEFAULT_DURATION = 10;

    public static Long createStation(String name) {
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

    public static ExtractableResponse<Response> createLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static Map<String, Object> createLineParam(String name, Long upStationId, Long downStationId, int distance, int duration) {
        return Map.of(
                "name", name,
                "color", DEFAULT_COLOR,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance,
                "duration", duration
        );
    }

    public static ExtractableResponse<Response> findShortestPaths(Long source, Long target, String type) {
        return RestAssured.given().log().all()
                .when().get(String.format("/paths?source=%d&target=%d&type=%s", source, target, type))
                .then().log().all()
                .extract();
    }

    public static List<Long> getStationIds(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("stations.id", Long.class);
    }

    public static int getDistance(ExtractableResponse<Response> response) {
        return response.jsonPath().getInt("distance");
    }

    public static int getDuration(ExtractableResponse<Response> response) {
        return response.jsonPath().getInt("duration");
    }

    public static long getFare(ExtractableResponse<Response> response) {
        return response.jsonPath().getInt("fare");
    }
}
