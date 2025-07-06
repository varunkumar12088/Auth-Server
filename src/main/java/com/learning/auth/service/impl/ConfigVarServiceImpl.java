package com.learning.auth.service.impl;

import com.learning.auth.entity.ConfigVar;
import com.learning.auth.exception.DataLayerException;
import com.learning.auth.exception.DataNotFoundException;
import com.learning.auth.repository.ConfigVarRepository;
import com.learning.auth.service.ConfigVarService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigVarServiceImpl implements ConfigVarService<ConfigVar, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigVarServiceImpl.class);

    @Autowired
    private ConfigVarRepository configVarRepository;


    @Override
    public <T> T getConfigVar(String id) {
        LOGGER.debug("Fetching config variable with id: {}", id);
        Optional<ConfigVar> configVar = configVarRepository.findById(id);
        if (configVar.isPresent()) {
            LOGGER.debug("Config variable found: {}", configVar.get());
            return (T) configVar.get();
        }
        throw new DataNotFoundException("Configuration is not available at " + id);
    }

    @Override
    public <T> T getConfigVar(String id, Class<T> valueType) {
        return getConfigVar(id, valueType, null);
    }

    @Override
    public <T> T getConfigVar(String id, Class<T> valueType, T defaultValue) {
        ConfigVar configVar = this.getConfigVar(id);
        if(ObjectUtils.isNotEmpty(configVar) && ObjectUtils.isNotEmpty(configVar.getValue()) && valueType != null){
            try{
                return valueType.cast(configVar.getValue());
            } catch (ClassCastException e) {
                LOGGER.error("Failed to case " + configVar.getValue() + " to " + valueType, e);
                throw new DataLayerException("Failed to case " + configVar.getValue() + " to " + valueType, e);
            }
        }
        return defaultValue;
    }
}
