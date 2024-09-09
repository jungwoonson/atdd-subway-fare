package nextstep.path.domain;

import nextstep.path.application.exception.NotExistPathTypeException;

public enum PathType {

    DISTANCE,
    DURATION;

    public static PathType lookUp(String pathType) {
        try {
            return valueOf(pathType);
        } catch (IllegalArgumentException e) {
            throw new NotExistPathTypeException();
        }
    }
}
