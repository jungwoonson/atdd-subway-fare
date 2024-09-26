package nextstep.path.application.dto;

import nextstep.fare.domain.Fare;
import nextstep.station.domain.Station;

import java.util.List;

public class PathInformation {

    private int distance;
    private int duration;
    private List<Station> stations;
    private Fare fare;

    private PathInformation(Builder builder) {
        this.distance = builder.distance;
        this.duration = builder.duration;
        this.stations = builder.stations;
        this.fare = builder.fare;
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

    public Fare getFare() {
        return fare;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int distance;
        private int duration;
        private List<Station> stations;
        private Fare fare;

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

        public Builder fare(Fare fare) {
            this.fare = fare;
            return this;
        }

        public PathInformation build() {
            return new PathInformation(this);
        }
    }
}
