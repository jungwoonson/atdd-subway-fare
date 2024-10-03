package nextstep.fare.domain;

import nextstep.fare.application.dto.CalculateFareRequest;

public abstract class AbstractFarePolicy implements FarePolicyHandler {

    protected FarePolicyHandler nextHandler;

    @Override
    public void setNextHandler(FarePolicyHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected Fare next(Fare fare, CalculateFareRequest request) {
        if (nextHandler != null) {
            return nextHandler.calculateFare(fare, request);
        }
        return fare;
    }
}