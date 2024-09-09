package nextstep.path.application.dto;

import nextstep.station.application.dto.StationResponse;

import java.util.List;

public class PathsResponse {
    private int distance;
    private int duration;
    private List<StationResponse> stations;

    public PathsResponse() {
    }

    public PathsResponse(int distance, int duration, List<StationResponse> stations) {
        this.distance = distance;
        this.duration = duration;
        this.stations = stations;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
