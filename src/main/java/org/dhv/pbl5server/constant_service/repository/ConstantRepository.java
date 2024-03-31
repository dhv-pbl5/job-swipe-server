package org.dhv.pbl5server.constant_service.repository;

import org.dhv.pbl5server.constant_service.entity.Constant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConstantRepository extends JpaRepository<Constant, UUID> {
    List<Constant> findByConstantType(String constantType);

    List<Constant> findByConstantTypeStartsWith(String constantTypePrefix);
}
