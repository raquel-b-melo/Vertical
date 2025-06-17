package com.rmelo.vertical.core.domain.model.enums;

public enum Roles {

    ADMIN("admin"),
    USER("user");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Roles fromString(String role) {
        for (Roles r : Roles.values()) {
            if (r.getRole().equals(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Role inválida: " + role);
    }
}
