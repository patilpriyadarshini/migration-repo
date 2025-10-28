package com.modernized.controllers;

import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public abstract class BaseController {

    protected <T, ID> T findEntityById(JpaRepository<T, ID> repository, ID id, String entityName) {
        Optional<T> entityOpt = repository.findById(id);
        if (entityOpt.isEmpty()) {
            throw new EntityNotFoundException(entityName + " not found");
        }
        return entityOpt.get();
    }

    protected <T, ID> T findAccountById(JpaRepository<T, ID> repository, ID id) {
        Optional<T> entityOpt = repository.findById(id);
        if (entityOpt.isEmpty()) {
            throw new EntityNotFoundException("Account ID NOT found");
        }
        return entityOpt.get();
    }
}
