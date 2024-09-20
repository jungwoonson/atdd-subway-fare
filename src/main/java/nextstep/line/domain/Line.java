package nextstep.line.domain;

import nextstep.fare.domain.Fare;
import nextstep.station.domain.Station;
import nextstep.station.domain.Stations;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Embedded
    private Fare fare;

    @Embedded
    private Sections sections;

    public Line() {
    }

    private Line(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.fare = builder.fare;
        this.sections = Sections.from(Section.builder()
                .line(this)
                .upStation(builder.upStation)
                .downStation(builder.downStation)
                .distance(builder.distance)
                .duration(builder.duration)
                .build());
    }

    public Line(String name, String color, Station upStation, Station downStation, Integer distance, Integer duration, long fare) {
        this.name = name;
        this.color = color;
        this.fare = Fare.from(fare);
        this.sections = Sections.from(Section.builder()
                .line(this)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .build());
    }

    public void modify(String name, String color, Fare fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public void addSection(Station upStation, Station downStation, Integer distance, Integer duration) {
        sections.add(Section.builder()
                .line(this)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .build());
    }
    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public Stations getStations() {
        return sections.getSortedStations();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Fare getFare() {
        return fare;
    }

    public Sections getSections() {
        return sections;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private Integer distance;
        private Integer duration;
        private Fare fare = Fare.zero();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(Integer distance) {
            this.distance = distance;
            return this;
        }

        public Builder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public Builder fare(long fare) {
            this.fare = Fare.from(fare);
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id)
                && Objects.equals(name, line.name)
                && Objects.equals(color, line.color)
                && Objects.equals(fare, line.fare)
                && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, fare, sections);
    }
}
