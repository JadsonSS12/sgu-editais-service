package br.edu.ufape.sguEditaisService.features.tipoedital;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoEditalRepository extends JpaRepository<TipoEdital, Long> {
    // Útil para garantir isolamento de domínio no futuro: "Listar templates do módulo X"
    List<TipoEdital> findByModuloOrigem(String moduloOrigem);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndModuloOrigem(String nome, String moduloOrigem);

    List<TipoEdital> findByModuloOrigemAndAtivoTrue(String moduloOrigem);
}