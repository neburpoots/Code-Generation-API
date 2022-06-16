package io.swagger.utils;

import io.swagger.exception.BadRequestException;
import io.swagger.model.utils.DTOEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DtoUtils
{
    public DTOEntity convertToDto(Object obj, DTOEntity mapper)
    {
        return new ModelMapper().map(obj, mapper.getClass());
    }

    public Object convertToEntity(Object obj, DTOEntity mapper)
    {
        return new ModelMapper().map(mapper, obj.getClass());
    }

    public List<DTOEntity> convertListToDto(List<?> objList, DTOEntity mapper) {
        return objList
                .stream()
                .map(source -> new ModelMapper().map(source, mapper.getClass()))
                .collect(Collectors.toList());
    }

    public UUID convertToUUID(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            throw new BadRequestException("Invalid UUID string: " + id);
        }
        return uuid;
    }
}