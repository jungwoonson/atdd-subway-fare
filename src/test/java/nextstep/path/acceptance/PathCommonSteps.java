package nextstep.path.acceptance;

import io.cucumber.java8.En;

import static nextstep.path.acceptance.PathAcceptanceTestFixture.createStation;

public class PathCommonSteps implements En {

    public static Long 분당역_ID;
    public static Long 홍대역_ID;
    public static Long 강남역_ID;
    public static Long 성수역_ID;

    public PathCommonSteps() {
        Given("지하철역이 등록돼 있다", () -> {
            분당역_ID = createStation("분당역");
            홍대역_ID = createStation("홍대역");
            강남역_ID = createStation("강남역");
            성수역_ID = createStation("성수역");
        });
    }
}
