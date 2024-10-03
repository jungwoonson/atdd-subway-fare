package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Path {

    private int distance;
    private int duration;
    private List<Station> stations;
    private Set<Section> sections;

    private Path(Builder builder) {
        this.distance = builder.distance;
        this.duration = builder.duration;
        this.stations = builder.stations;
        this.sections = builder.sections;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int distance;
        private int duration;
        private List<Station> stations;
        private Set<Section> sections;

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder stations(List<Station> stations) {
            this.stations = stations;
            return this;
        }

        public Builder sections(Set<Section> sections) {
            this.sections = sections;
            return this;
        }

        public Path build() {
            return new Path(this);
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
        Path path = (Path) o;
        return distance == path.distance
                && duration == path.duration
                && Objects.equals(stations, path.stations)
                && Objects.equals(sections, path.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, duration, stations, sections);
    }

    @Override
    public String toString() {
        return "Path{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", stations=" + stations +
                ", sections=" + sections +
                '}';
    }
}
