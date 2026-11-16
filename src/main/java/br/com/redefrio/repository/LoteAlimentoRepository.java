package br.com.redefrio.repository;
import br.com.redefrio.domain.LoteAlimento;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface LoteAlimentoRepository extends JpaRepository<LoteAlimento,Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from LoteAlimento l where l.id = :id")
    Optional<LoteAlimento> buscarComBloqueio(@Param("id") Long id);
}
