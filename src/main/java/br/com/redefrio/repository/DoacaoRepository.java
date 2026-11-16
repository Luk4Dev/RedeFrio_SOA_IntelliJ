package br.com.redefrio.repository;
import br.com.redefrio.domain.Doacao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface DoacaoRepository extends JpaRepository<Doacao,Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from Doacao d where d.id = :id")
    Optional<Doacao> buscarComBloqueio(@Param("id") Long id);
}
