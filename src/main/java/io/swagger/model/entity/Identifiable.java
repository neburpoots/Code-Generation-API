package io.swagger.model.entity;

import java.io.Serializable;

public interface Identifiable<T extends Serializable> {
    T getId();
}
