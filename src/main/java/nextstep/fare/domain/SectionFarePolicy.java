package nextstep.fare.domain;

import nextstep.fare.application.dto.CalculateFareRequest;
import nextstep.line.domain.Section;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SectionFarePolicy extends AbstractFarePolicy {

    @Override
    public Fare calculateFare(Fare fare, CalculateFareRequest request) {
        Set<Section> sections = request.getSections();
        Fare sectionFare = sections.stream()
                .map(Section::getFare)
                .max(Fare::compareTo)
                .orElse(Fare.zero());
        return next(fare.add(sectionFare), request);
    }
}