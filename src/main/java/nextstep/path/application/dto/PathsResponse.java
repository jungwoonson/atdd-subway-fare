package nextstep.path.application.dto;

import nextstep.fare.domain.Fare;
import nextstep.path.domain.Path;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathsResponse {
    private int distance;
    private int duration;
    private long fare;
    private List<StationResponse> stations;

    public PathsResponse() {
    }

    private PathsResponse(Builder builder) {
        this.distance = builder.distance;
        this.duration = builder.duration;
        this.fare = builder.fare;
        this.stations = builder.stations;
    }

    public static PathsResponse of(Path path, Fare fare) {
        return PathsResponse.builder()
                .distance(path.getDistance())
                .duration(path.getDuration())
                .fare(fare)
                .stations(createStationResponse(path.getStations()))
                .build();
    }

    private static List<StationResponse> createStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public long getFare() {
        return fare;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int distance;
        private int duration;
        private long fare;
        private List<StationResponse> stations;

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder fare(Fare fare) {
            this.fare = fare.getFare();
            return this;
        }

        public Builder stations(List<StationResponse> stations) {
            this.stations = stations;
            return this;
        }

        public PathsResponse build() {
            return new PathsResponse(this);
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
        PathsResponse that = (PathsResponse) o;
        return distance == that.distance && duration == that.duration && fare == that.fare && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, duration, fare, stations);
    }
}
