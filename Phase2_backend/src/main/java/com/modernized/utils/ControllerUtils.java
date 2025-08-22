package com.modernized.utils;

import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ControllerUtils {

    public static <T, ID> T findEntityById(JpaRepository<T, ID> repository, ID id, String entityName) {
        Optional<T> entityOpt = repository.findById(id);
        
        if (entityOpt.isEmpty()) {
            throw new EntityNotFoundException(entityName + " not found");
        }
        
        return entityOpt.get();
    }

    public static <T, ID> T findAccountById(JpaRepository<T, ID> repository, ID id) {
        return findEntityById(repository, id, "Account ID NOT found");
    }
}
