package br.com.redefrio.repository;
import br.com.redefrio.domain.LeituraTemperatura;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LeituraTemperaturaRepository extends JpaRepository<LeituraTemperatura, Long> { 
}
