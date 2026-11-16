package br.com.redefrio.repository;
import br.com.redefrio.domain.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AlertaRepository extends JpaRepository<Alerta, Long> { 
    java.util.List<Alerta> findByStatusOrderByCriadoEmDesc(br.com.redefrio.domain.StatusAlerta status);
}
