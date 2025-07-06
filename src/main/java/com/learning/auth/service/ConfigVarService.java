package com.learning.auth.service;

import java.io.Serializable;

public interface ConfigVarService<T, ID extends Serializable> {

    <T> T getConfigVar(ID id);
    <T> T getConfigVar(ID id, Class<T> valueType);
    <T> T getConfigVar(ID id, Class<T> valueType, T defaultValue);


}
