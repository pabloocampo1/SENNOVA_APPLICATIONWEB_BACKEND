package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.application.dto.customer.CustomerResponseDto;
import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.projection.CustomerTestRequestProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomersRepositoryJpa extends JpaRepository<CustomerEntity, Long> {

    @Query(value = """
    SELECT
    c.customer_id as customerId,
      LOWER(c.customer_name) AS customerName,
      LOWER(c.email) AS email,
      c.phone_number AS phoneNumber,
      c.city AS city,
      c.address AS address,
      c.create_at AS createAt,
      tr.test_request_id AS testRequestId,
      tr.request_code AS requestCode,
      tr.delivery_status AS state ,
      tr.price as price
    FROM customer c
    LEFT JOIN test_request tr
      ON tr.customer_id = c.customer_id"""
   
   ,
            nativeQuery = true
    )
    Page<CustomerTestRequestProjection> getAll(Pageable pageable);

    @Query(value = "SELECT * FROM test_request t WHERE  t.customer_id = :customerId", nativeQuery = true)
    List<TestRequestEntity> findAllTestRequestByCustomerId(@Param("customerId") Long customerId);

}
