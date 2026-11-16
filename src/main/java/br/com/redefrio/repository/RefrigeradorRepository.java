package br.com.redefrio.repository;
import br.com.redefrio.domain.Refrigerador;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RefrigeradorRepository extends JpaRepository<Refrigerador, Long> { 
    boolean existsByCodigo(String codigo);
}
