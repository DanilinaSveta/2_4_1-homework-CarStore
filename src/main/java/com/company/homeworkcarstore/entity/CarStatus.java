package com.company.homeworkcarstore.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum CarStatus implements EnumClass<String> {

    IN_STOCK("A"),
    SOLD("B");

    private final String id;

    CarStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static CarStatus fromId(String id) {
        for (CarStatus at : CarStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}