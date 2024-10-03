package nextstep.line.unit;

import nextstep.fare.domain.Fare;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.domain.Sections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 도메인 테스트")
class LineTest {

    private Line 신분당선;
    private Section 강남역_양재역;
    private Section 양재역_교대역;

    @BeforeEach
    void setUp() {
        신분당선 = 신분당선(강남역, 양재역);
        강남역_양재역 = Section.builder()
                .line(신분당선)
                .upStation(강남역)
                .downStation(양재역)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
        양재역_교대역 = Section.builder()
                .line(신분당선)
                .upStation(양재역)
                .downStation(교대역)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
    }

    @DisplayName("노선 빌더는, 노선의 필수정보를 입력하면 노선이 생성된다.")
    @Test
    void createLineTest() {
        // given
        String 새로운역 = "새로운역";
        Line expected = new Line(새로운역, RED, 강남역, 양재역, DEFAULT_DISTANCE, DEFAULT_DURATION, ZERO_FARE);

        // when
        Line actual = Line.builder()
                .name(새로운역)
                .color(RED)
                .upStation(강남역)
                .downStation(양재역)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("노선 수정 함수는, 노선의 정보를 수정한다.")
    @Test
    void modifyTest() {
        // given
        String 수정된이름 = "수정된이름";
        String 수정된색상 = "수정된색상";
        long 수정된요금 = 1250L;
        Line expected = Line.builder()
                .name(수정된이름)
                .color(수정된색상)
                .upStation(강남역)
                .downStation(양재역)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .fare(수정된요금)
                .build();

        // when
        신분당선.modify(수정된이름, 수정된색상, Fare.from(수정된요금));

        // then
        assertThat(신분당선).isEqualTo(expected);
    }

    @DisplayName("구간 추가 함수는, 노선의 구간을 추가하고 해당 노선의 목록을 조회하면 해당 구간이 추가된다.")
    @Test
    void addSection() {
        // given
        Sections expected = Sections.of(강남역_양재역, 양재역_교대역);

        // when
        신분당선.addSection(양재역, 교대역, DEFAULT_DISTANCE, DEFAULT_DURATION);

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }

    @DisplayName("구간 목록을 조회 함수는, 전체 구간 목록이 조회한다.")
    @Test
    void getStations() {
        //given
        Sections expected = Sections.from(강남역_양재역);

        // when & then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }

    @DisplayName("구간 제거거 함수는, 해당 노선의 구간을 제거하면 해당 구간이 제거된다.")
    @Test
    void removeSection() {
        // given
        Sections expected = Sections.from(강남역_양재역);
        신분당선.addSection(양재역, 교대역, DEFAULT_DISTANCE, DEFAULT_DURATION);

        // when
        신분당선.deleteSection(교대역);

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }
}
