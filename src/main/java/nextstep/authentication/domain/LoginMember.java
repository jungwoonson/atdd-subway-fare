package nextstep.authentication.domain;

public class LoginMember {

    public static final LoginMember ANONYMOUS = new LoginMember("anonymous", -1L, -1);

    private String email;
    private Long id;
    private Integer age;

    public LoginMember(String email, Long id, Integer age) {
        this.email = email;
        this.id = id;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public Integer getAge() {
        return age;
    }
}
