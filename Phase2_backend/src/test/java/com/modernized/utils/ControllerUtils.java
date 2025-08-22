package com.modernized.utils;

import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
import java.util.Optional;

public class ControllerUtils {

    public static <T> T findEntityOrThrow(Optional<T> entityOpt, String errorMessage) {
        if (entityOpt.isEmpty()) {
            throw new EntityNotFoundException(errorMessage);
        }
        return entityOpt.get();
    }
}
