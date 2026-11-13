package com.jcaa.appcrudusers.domain.vo;

/** Immutable flight identifier. */
public final class VueloId {
    private final String value;

    public VueloId(final String value) {
        ValueValidation.required("id", value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || (other instanceof VueloId
                && value.equals(((VueloId) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
