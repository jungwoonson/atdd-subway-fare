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
    public static final Section 강남역_양재역 = Section.builder()
            .line(신분당선)
            .upStation(강남역)
            .downStation(양재역)
            .distance(DEFAULT_DISTANCE)
            .duration(DEFAULT_DURATION)
            .build();
    public static final Section 양재역_교대역 = Section.builder()
            .line(신분당선)
            .upStation(양재역)
            .downStation(교대역)
            .distance(DISTANCE_4)
            .duration(DURATION_3)
            .build();
    public static final Section 교대역_홍대역 = Section.builder()
            .line(신분당선)
            .upStation(교대역)
            .downStation(홍대역)
            .distance(DISTANCE_6)
            .duration(DURATION_2)
            .build();
    public static final Section 홍대역_강남역 = Section.builder()
            .line(신분당선)
            .upStation(홍대역)
            .downStation(강남역)
            .distance(DISTANCE_7)
            .duration(DEFAULT_DURATION)
            .build();
    public static final List<Section> 연결된구간 = List.of(강남역_양재역, 양재역_교대역, 교대역_홍대역, 홍대역_강남역);

    public static Line 신분당선(Station upStation, Station downStation) {
        return Line.builder()
                .name("신분당선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
    }

    public static Line 분당선(Station upStation, Station downStation) {
        return Line.builder()
                .name("분당선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
    }

    public static Line 중앙선(Station upStation, Station downStation) {
        return Line.builder()
                .name("중앙선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DISTANCE_6)
                .duration(DEFAULT_DURATION)
                .build();
    }

    public static Line 경의선(Station upStation, Station downStation) {
        return Line.builder()
                .name("경의선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DISTANCE_7)
                .duration(DEFAULT_DURATION)
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
