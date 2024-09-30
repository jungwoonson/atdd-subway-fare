package nextstep.fare.application.dto;

import nextstep.line.domain.Section;
import nextstep.path.domain.Path;

import java.util.Set;

public class CalculateFareRequest {

    private final Integer distance;
    private final Integer age;
    private final Set<Section> sections;

    public CalculateFareRequest(Path path, Integer age) {
        this.distance = path.getDistance();
        this.age = age;
        this.sections = path.getSections();
    }

    public CalculateFareRequest(Integer distance, Integer age, Set<Section> sections) {
        this.distance = distance;
        this.age = age;
        this.sections = sections;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getAge() {
        return age;
    }

    public Set<Section> getSections() {
        return sections;
    }
}
