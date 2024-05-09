package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query("SELECT c FROM Company c WHERE c.companyName LIKE %:name%")
    Page<Company> searchByName(String name, Pageable pageable);

    @Query("SELECT c FROM Company c WHERE c.account.email LIKE %:email%")
    Page<Company> searchByEmail(String email, Pageable pageable);
}
