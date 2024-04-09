package org.dhv.pbl5server.constant_service.repository;

import org.dhv.pbl5server.constant_service.entity.Constant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConstantRepository extends JpaRepository<Constant, UUID> {
    Optional<Constant> findByConstantType(String prefix);

    @Query("SELECT c FROM Constant c WHERE c.constantType LIKE ?1% ORDER BY c.constantType ASC")
    List<Constant> findByConstantTypeStartsWith(String constantTypePrefix);

    @Query("SELECT DISTINCT c.constantType FROM Constant c ORDER BY c.constantType ASC")
    List<Object> getDistinctConstantType();
}
