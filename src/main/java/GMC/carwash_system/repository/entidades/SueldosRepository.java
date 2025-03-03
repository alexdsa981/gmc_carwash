package GMC.carwash_system.repository.entidades;

import GMC.carwash_system.model.entidades.Sueldos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SueldosRepository extends JpaRepository<Sueldos, Long> {
    @Query("SELECT s FROM Sueldos s WHERE YEAR(s.fecha) = :year AND MONTH(s.fecha) = :month")
    List<Sueldos> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

}
