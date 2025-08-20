package com.modernized.repositories;

import com.modernized.entities.DisclosureGroup;
import com.modernized.entities.DisclosureGroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisclosureGroupRepository extends JpaRepository<DisclosureGroup, DisclosureGroupId> {
}
