package nextstep.utils;

import nextstep.line.application.dto.SectionRequest;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class UnitTestFixture {
    public static final String RED = "bg-red-600";
    public static final int DEFAULT_DISTANCE = 10;
    public static final int DISTANCE_4 = 4;
    public static final int DISTANCE_6 = 6;
    public static final int DISTANCE_7 = 7;
    public static final int DEFAULT_DURATION = 5;
    public static final int DURATION_2 = 2;
    public static final int DURATION_3 = 3;
    public static final Station 강남역 = Station.of(1L, "강남역");
    public static final Station 양재역 = Station.of(2L, "양재역");
    public static final Station 교대역 = Station.of(3L, "교대역");
    public static final Station 홍대역 = Station.of(4L, "홍대역");
    public static final Line 신분당선 = 신분당선(강남역, 양재역);
    public static final Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE, DEFAULT_DURATION);
    public static final Section 양재역_교대역 = createSection(신분당선, 양재역, 교대역, DISTANCE_4, DURATION_3);
    public static final Section 교대역_홍대역 = createSection(신분당선, 교대역, 홍대역, DISTANCE_6, DURATION_2);
    public static final Section 홍대역_강남역 = createSection(신분당선, 홍대역, 강남역, DISTANCE_7, DEFAULT_DURATION);
    public static final List<Section> 연결된구간 = List.of(강남역_양재역, 양재역_교대역, 교대역_홍대역, 홍대역_강남역);

    public static Line 신분당선(Station upStation, Station downStation) {
        return createLine("신분당선", RED, upStation, downStation, DEFAULT_DISTANCE, DEFAULT_DURATION);
    }

    public static Line 분당선(Station upStation, Station downStation) {
        return createLine("분당선", RED, upStation, downStation, DEFAULT_DISTANCE, DEFAULT_DURATION);
    }

    public static Line 중앙선(Station upStation, Station downStation) {
        return createLine("중앙선", RED, upStation, downStation, DISTANCE_6, DEFAULT_DURATION);
    }

    public static Line 경의선(Station upStation, Station downStation) {
        return createLine("경의선", RED, upStation, downStation, DISTANCE_7, DEFAULT_DURATION);
    }

    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance, int duration) {
        return Line.builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .build();
    }

    public static Section createSection(Line line, Station upStation, Station downStation, int distance) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(DEFAULT_DURATION)
                .build();
    }

    public static Section createSection(Line line, Station upStation, Station downStation, int distance, int duration) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .build();
    }

    public static SectionRequest createSectionRequest(Long upStationId, Long downStationId) {
        return new SectionRequest(upStationId, downStationId, DEFAULT_DISTANCE, DEFAULT_DURATION);
    }

    public static List<StationResponse> createStationResponse(Station ...station) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station s : station) {
            stationResponses.add(new StationResponse(s.getId(), s.getName()));
        }
        return stationResponses;
    }
}
