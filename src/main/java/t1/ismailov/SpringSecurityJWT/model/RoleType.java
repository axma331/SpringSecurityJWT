package t1.ismailov.SpringSecurityJWT.model;

public enum RoleType {
    USER, ADMIN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
