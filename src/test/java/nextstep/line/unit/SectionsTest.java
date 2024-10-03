package nextstep.line.unit;

import nextstep.fare.domain.Fare;
import nextstep.line.application.exception.DuplicateStationException;
import nextstep.line.domain.Section;
import nextstep.line.domain.Sections;
import nextstep.station.domain.Station;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간 일급 컬렉션 도메인 테스트")
public class SectionsTest {

    @DisplayName("구간 추가 함수는, 유효한 구간 추가를 요청할 경우 정상적으로 추가된다.")
    @ParameterizedTest
    @MethodSource("addSectionParameters")
    void addSectionTest(Section section, Section newSection, Sections expected) {
        // given
        Sections sections = Sections.from(section);

        // when
        sections.add(newSection);

        // then
        assertThat(sections).isEqualTo(expected);
    }

    private static Stream<Arguments> addSectionParameters() {
        return Stream.of(
                Arguments.of(양재역_교대역(), 강남역_양재역(), Sections.of(강남역_양재역(), 양재역_교대역())),
                Arguments.of(강남역_양재역(), 강남역_홍대역(), Sections.of(강남역_홍대역(), 홍대역_양재역())),
                Arguments.of(강남역_양재역(), 양재역_교대역(), Sections.of(강남역_양재역(), 양재역_교대역()))
        );
    }

    @DisplayName("중복된 구간을 추가하면 오류가 발생한다.")
    @ParameterizedTest
    @MethodSource("duplicateStationParameters")
    void duplicateStationExceptionTest(Sections sections, Section newSection, String expectedDuplicated) {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> sections.add(newSection);

        // then
        assertThatThrownBy(actual).isInstanceOf(DuplicateStationException.class)
                .message().contains(expectedDuplicated);
    }

    private static Stream<Arguments> duplicateStationParameters() {
        return Stream.of(
                Arguments.of(Sections.of(강남역_양재역(), 양재역_교대역()), 양재역_강남역(), 양재역.getName()),
                Arguments.of(Sections.of(강남역_양재역(), 양재역_교대역()), 강남역_양재역(), 양재역.getName()),
                Arguments.of(Sections.of(강남역_양재역(), 양재역_교대역()), 교대역_양재역(), 양재역.getName())
        );
    }

    @DisplayName("구간 삭제 함수는, 유효한 구간 삭제를 요청할 경우 정상적으로 삭제된다.")
    @ParameterizedTest
    @MethodSource("deleteSectionParameters")
    void deleteSectionTest(Station station, Sections expected) {
        // given
        Sections sections = Sections.of(강남역_양재역(), 양재역_교대역());

        // when
        sections.delete(station);

        // then
        assertThat(sections).isEqualTo(expected);
    }

    private static Stream<Arguments> deleteSectionParameters() {
        return Stream.of(
                Arguments.of(강남역, Sections.from(양재역_교대역())),
                Arguments.of(양재역, Sections.from(강남역_교대역())),
                Arguments.of(교대역, Sections.from(강남역_양재역()))
        );
    }

    private static Section 양재역_교대역() {
        return Section.builder()
                .line(신분당선)
                .upStation(양재역)
                .downStation(교대역)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
    }

    private static Section 강남역_양재역() {
        return Section.builder()
                .line(신분당선)
                .upStation(강남역)
                .downStation(양재역)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
    }

    private static Section 강남역_홍대역() {
        return Section.builder()
                .line(신분당선)
                .upStation(강남역)
                .downStation(홍대역)
                .distance(DISTANCE_4)
                .duration(DURATION_3)
                .build();
    }

    private static Section 홍대역_양재역() {
        return Section.builder()
                .line(신분당선)
                .upStation(홍대역)
                .downStation(양재역)
                .distance(DISTANCE_6)
                .duration(DURATION_2)
                .build();
    }

    private static Section 양재역_강남역() {
        return Section.builder()
                .line(신분당선)
                .upStation(양재역)
                .downStation(강남역)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
    }

    private static Section 교대역_양재역() {
        return Section.builder()
                .line(신분당선)
                .upStation(교대역)
                .downStation(양재역)
                .distance(DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
    }

    private static Section 강남역_교대역() {
        return Section.builder()
                .line(신분당선)
                .upStation(강남역)
                .downStation(교대역)
                .distance(DEFAULT_DISTANCE + DEFAULT_DISTANCE)
                .duration(DEFAULT_DURATION)
                .build();
    }
}
