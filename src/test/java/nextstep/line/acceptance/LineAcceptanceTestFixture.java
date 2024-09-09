package nextstep.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineAcceptanceTestFixture {
    
    public static final String 신분당선 = "신분당선";
    public static final String 분당선 = "분당선";
    
    public static final String RED = "bg-red-600";
    public static final String GREEN = "bg-green-600";

    public static final Integer DEFAULT_DISTANCE = 10;
    public static final Integer DEFAULT_DURATION = 5;

    public static final String 분당역 = "분당역";
    public static final String 홍대역 = "홍대역";
    public static final String 강남역 = "강남역";
    public static final String 성수역 = "성수역";
    
    public static final Long 생성된적없는_역_ID = -1L;

    public static final Integer 분당_강남_거리 = 4;
    public static final Integer 분당_성수_거리 = 6;
    public static final Integer 분당_성수_시간 = 3;

    public static final Map<String, Object> MODIFY_PARAM = Map.of(
            "name", 분당선,
            "color", GREEN
    );

    public static Map<String, Object> createSectionParam(Long upStationId, Long downStationId, int distance) {
        return Map.of(
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance,
                "duration", DEFAULT_DURATION
        );
    }

    public static Map<String, Object> createSectionParam(Long upStationId, Long downStationId, int distance, int duration) {
        return Map.of(
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance,
                "duration", duration
        );
    }
    
    public static Map<String, Object> createLineParam(String name, Long upStationId, Long downStationId, int distance) {
        return Map.of(
                "name", name,
                "color", RED,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance,
                "duration", DEFAULT_DURATION
        );
    }

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

    public static ExtractableResponse<Response> findLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> lookUpLine(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> modifyLine(Long id) {
        return RestAssured.given().log().all()
                .body(MODIFY_PARAM)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> addSection(Long id, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(String.format("/lines/%d/sections", id))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteSection(Long id, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete(String.format("/lines/%s/sections?stationId=%s", id, stationId))
                .then().log().all()
                .extract();
    }

    public static List<Long> getStationIds(Long lindId) {
        return lookUpLine(lindId).jsonPath()
                .getList("stations.id", Long.class);
    }

    public static List<String> getNames(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("name", String.class);
    }

    public static String getName(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getString("name");
    }

    public static long getId(ExtractableResponse<Response> createdLineResponse) {
        return createdLineResponse.jsonPath()
                .getLong("id");
    }
}
