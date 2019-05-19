package com.olrox.domain.account;

public enum Role {
    USER, ADMIN;

    @Override
    public String toString() {
        return Role.this.name().toLowerCase();
    }
}
