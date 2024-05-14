package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

// git commit -m "PBL-513 register for company"
// git commit -m "PBL-538 company profile"
// git commit -m "PBL-523 update company profile"

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query("SELECT c FROM Company c WHERE c.companyName LIKE %:name%")
    Page<Company> searchByName(String name, Pageable pageable);

    @Query("SELECT c FROM Company c WHERE c.account.email LIKE %:email%")
    Page<Company> searchByEmail(String email, Pageable pageable);
}
