package br.com.redefrio.repository;
import br.com.redefrio.domain.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> { 
}
