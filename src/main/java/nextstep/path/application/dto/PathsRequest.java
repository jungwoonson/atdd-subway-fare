package nextstep.path.application.dto;

public class PathsRequest {

    private Long source;
    private Long target;
    private String type;
    private Integer age;

    private PathsRequest(Builder builder) {
        this.source = builder.source;
        this.target = builder.target;
        this.type = builder.type;
        this.age = builder.age;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

    public Integer getAge() {
        return age;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long source;
        private Long target;
        private String type;
        private Integer age;

        public Builder source(Long source) {
            this.source = source;
            return this;
        }

        public Builder target(Long target) {
            this.target = target;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public PathsRequest build() {
            return new PathsRequest(this);
        }
    }
}
