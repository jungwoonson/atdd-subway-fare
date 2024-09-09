package nextstep.path.domain;

import nextstep.station.domain.Station;

import java.util.List;

public interface ShortestPath {

    List<Station> getStations(Station start, Station end);

    void validateConnected(Station start, Station end);

    void validateContains(Station start, Station end);

    int getDistance(Station start, Station end);

    int getDuration(Station start, Station end);
}
