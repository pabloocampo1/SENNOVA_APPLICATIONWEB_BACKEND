package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.requestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DashboardRepositoryJpa extends JpaRepository<TestRequestEntity, Long> {

    @Query( value = "SELECT COUNT(*)\n" +
            "FROM test_request t\n" +
            "WHERE t.delivery_status = 'COMPLETADO Y ENTREGADO'\n" +
            "  AND YEAR(t.submission_date) = YEAR(current_date())\n" +
            "  AND MONTH(t.submission_date) = MONTH(current_date());", nativeQuery = true)
    Integer countDeliveredRequestsCurrentMonth();

     @Query(value = "SELECT COUNT(*)\n" +
             "FROM test_request t\n" +
             "WHERE t.delivery_status = 'COMPLETADO Y ENTREGADO'\n" +
             "  AND YEAR(t.submission_date) = YEAR(CURDATE() - INTERVAL 1 MONTH)\n" +
             "  AND MONTH(t.submission_date) = MONTH(CURDATE() - INTERVAL 1 MONTH);" , nativeQuery = true)
    Integer countDeliveredRequestsLastMonth();

     @Query(value = "SELECT COUNT(*)\n" +
             "FROM sample s\n" +
             "WHERE MONTH(s.create_at) = MONTH(CURDATE() )\n" +
             "AND YEAR(s.create_at) = YEAR(CURDATE());", nativeQuery = true)
     Integer countSamplesCreatedCurrentMonth();

     @Query(value = "SELECT COUNT(*)\n" +
             "FROM sample s\n" +
             "WHERE MONTH(s.create_at) = MONTH(CURDATE() - interval 1 month)\n" +
             "AND YEAR(s.create_at) = YEAR(CURDATE() - interval 1 month);", nativeQuery = true)
     Integer countSamplesCreatedLastMonth();

     @Query(value = "SELECT COUNT(*)\n" +
             "FROM sample s\n" +
             "WHERE s.due_date < CURDATE()\n" +
             "AND s.status_reception = true\n" +
             "AND s.is_delivered = false;", nativeQuery = true)
     Integer countSamplesExpiredAndNotSend();

     @Query(value = "SELECT COUNT(*) FROM equipment e WHERE e.maintenance_date < curdate();", nativeQuery = true)
     Integer countMaintenanceEquipment();

     @Query(value = "SELECT COUNT(*) FROM reagents r WHERE r.expiration_date < curdate();", nativeQuery = true)
    Integer countExpiredReagent();

      @Query(value = "SELECT COUNT(*) FROM test_request t WHERE MONTH(t.create_at ) = MONTH(CURDATE()) AND year(t.create_at) = year(CURDATE())", nativeQuery = true)
     Integer countTestRequestCreatedCurrentMonth();

     @Query(value = "SELECT COUNT(*) FROM test_request t WHERE MONTH(t.create_at ) = MONTH(CURDATE() - INTERVAL 1 MONTH) AND year(t.create_at) = year(CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
     Integer countTestRequestCreatedLastMonth();


     @Query(value = "\n" +
             "SELECT count(*)\n" +
             "FROM sample s\n" +
             "WHERE s.due_date >= CURDATE()\n" +
             "AND s.status_reception = true\n" +
             "AND s.is_delivered = false;", nativeQuery = true)
     Integer  countSamplesToExecute();

     @Query(value = "SELECT COUNT(*) FROM test_request t WHERE t.state = \"PENDIENTE\";", nativeQuery = true)
     Integer countQuotationPending();

     @Query( value = "SELECT COUNT(*) FROM reagents r WHERE r.quantity < 1;", nativeQuery = true)
     Integer countReagentWithoutStock();

    @Query( value = "SELECT COUNT(*) FROM reagents r WHERE r.quantity >= 1 AND r.expiration_date > curdate() AND r.state = \"CON CONTENIDO DISPONIBLE\"", nativeQuery = true)
    Integer countReagentActives();



    @Query(value = "SELECT\n" +
            "    COUNT(CASE \n" +
            "        WHEN e.maintenance_date < CURDATE() \n" +
            "        THEN 1 \n" +
            "    END) AS toMaintenance,\n" +
            "\n" +
            "    COUNT(CASE \n" +
            "        WHEN e.mark_report = FALSE \n" +
            "         AND e.available = TRUE \n" +
            "         AND e.state = 'Activo'\n" +
            "        THEN 1 \n" +
            "    END) AS active,\n" +
            "\n" +
            "    COUNT(CASE \n" +
            "        WHEN e.mark_report = TRUE \n" +
            "        THEN 1 \n" +
            "    END) AS reported\n" +
            "FROM equipment e;", nativeQuery = true)
    EquipmentsMetricsProjection getEquipmentsMetricsProjection();


    @Query(value = "SELECT\n" +
            "    COUNT(CASE WHEN r.quantity < 1 THEN 1 END) AS withoutStock,\n" +
            "    COUNT(\n" +
            "        CASE \n" +
            "            WHEN r.quantity >= 1 \n" +
            "             AND r.expiration_date > CURDATE()\n" +
            "             AND r.state = 'CON CONTENIDO DISPONIBLE'\n" +
            "            THEN 1 \n" +
            "        END\n" +
            "    ) AS actives,\n" +
            "    COUNT(CASE WHEN r.expiration_date <= CURDATE() THEN 1 END) AS expired\n" +
            "FROM reagents r;", nativeQuery = true)
    ReagentsMetricsProjection getReagentMetricsProjection();


    @Query(value = "SELECT \n" +
            "a.analysis_name AS productName,\n" +
            " COUNT(*) AS totalAnalysis \n" +
            "FROM analysis a \n" +
            "LEFT JOIN \n" +
            "\tsample_product_analysis s on s.analysis_id = a.analysis_id \n" +
            "WHERE s.create_at >=  :start AND s.create_at <= :end \n" +
            "GROUP BY a.analysis_name, a.analysis_id \n" +
            "ORDER BY totalAnalysis DESC \n" +
            "LIMIT 15;",
            nativeQuery = true)
    List<ProductsMoreUsedProjection> productsMoreUsed( @Param("start") LocalDateTime start,
                                                       @Param("end")   LocalDateTime end);


    @Query(value = "SELECT  s.matrix AS matrix, COUNT(*) AS total\n" +
            "FROM sample s\n" +
            "WHERE s.create_at >= :start AND s.create_at <= :end\n" +
            "GROUP BY  s.matrix\n" +
            "order by total DESC LIMIT 10;", nativeQuery = true)
    List<MatrixMoreUsedProjection> matrixMoreUsed(@Param("start") LocalDateTime start,
                                                  @Param("end")   LocalDateTime end);

    @Query(value = "SELECT \n" +
            "    DATE_FORMAT(sample.delivery_date, '%Y-%m') AS deliveryDate, \n" +
            "    COUNT(*) AS total\n" +
            "FROM sample \n" +
            "WHERE sample.delivery_date >= DATE_SUB(NOW(), INTERVAL 12 MONTH) \n" +
            "  AND sample.is_delivered = true \n" +
            "GROUP BY deliveryDate\n" +
            "ORDER BY deliveryDate ASC;", nativeQuery = true)
     List<SamplesDeliveredLast12MonthsProjection> SamplesDeliveredLast12Months();


    @Query(value = "SELECT COUNT(CASE WHEN s.status_reception = FALSE THEN 1 END) AS \"withoutReception\",\n" +
            "COUNT(CASE WHEN s.status_reception = true AND s.due_date > current_date() AND s.is_delivered = false THEN 1 END ) AS \"process\",\n" +
            "COUNT(CASE WHEN (s.is_delivered = FALSE OR s.is_delivered IS NULL) AND (s.due_date IS NULL OR s.due_date < current_date())    AND s.status_reception = true  THEN 1 END ) AS \"overDue\",\n" +
            "COUNT(CASE WHEN s.is_delivered = true THEN 1 END) AS \"delivered\"\n" +
            "FROM sample s  WHERE s.create_at >= :start AND s.create_at <= :end", nativeQuery = true)
    SampleStatusMetricsProjection countSampleStatusMetrics(@Param("start") LocalDate start,
                                                                 @Param("end")   LocalDate end);

    @Query(value = "SELECT \n" +
            "    COUNT(CASE WHEN t.state = \"PENDIENTE\" THEN 1 END) AS pending,\n" +
            "    COUNT(CASE WHEN t.state = \"ACEPTADA\" THEN 1 END) AS accepted,\n" +
            "    COUNT(CASE WHEN t.state = \"RECHAZADA\" THEN 1 END) AS rejected\n" +
            "FROM test_request t  WHERE t.create_at >= :start AND t.create_at <= :end\n" , nativeQuery = true)
    TestRequestStateMetricsProjection countTestRequestState(@Param("start") LocalDate start,
                                                                  @Param("end")   LocalDate end);


    @Query(value = """
    SELECT
        COALESCE(s.result_generated_by, 'Sin responsable') AS name,
        YEAR(s.result_date)  AS year,
        MONTH(s.result_date) AS month,
        COUNT(*) AS total
    FROM sample_product_analysis s
    WHERE s.result_date BETWEEN :start AND :end
    GROUP BY
        COALESCE(s.result_generated_by, 'Sin responsable'),
        YEAR(s.result_date),
        MONTH(s.result_date)
    ORDER BY year, month, total DESC
""", nativeQuery = true)
    List<ResultsByUserProjection
            > findResultsByUserMonthly(
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end
    );


    @Query(value = "SELECT \n" +
            "  COUNT(*) AS total,\n" +
            "  DATE_FORMAT(t.create_at, '%Y-%m') AS period\n" +
            "FROM test_request t\n" +
            "WHERE t.create_at >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)\n" +
            "GROUP BY period\n" +
            "ORDER BY period;", nativeQuery = true)
    List<MonthlyTestRequestsProjection> findTestRequestsByMonth();


    @Query(value = "SELECT COUNT(*) FROM test_request t  WHERE t.delivery_status = \"En proceso\"" , nativeQuery = true)
    Integer countTestRequestInProcess();

}
