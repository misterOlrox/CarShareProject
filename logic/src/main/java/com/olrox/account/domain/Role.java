package com.olrox.account.domain;

public enum Role {
    USER, ADMIN;

    @Override
    public String toString() {
        return Role.this.name().toLowerCase();
    }
}
