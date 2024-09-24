package nextstep.authentication.domain;

public class LoginMember {

    public static final LoginMember ANONYMOUS = new LoginMember("anonymous", null);

    private String email;
    private Long id;

    public LoginMember(String email, Long id) {
        this.email = email;
        this.id = id;
    }

    public boolean isAnonymous() {
        return this == ANONYMOUS;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
