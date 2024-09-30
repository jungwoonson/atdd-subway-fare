package nextstep.fare.domain;

import nextstep.fare.application.dto.CalculateFareRequest;

public interface FarePolicyHandler {

    Fare calculateFare(Fare fare, CalculateFareRequest request);
    void setNextHandler(FarePolicyHandler nextHandler);
}