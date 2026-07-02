package br.edu.ufape.sguEditaisService.features.tipoedital;

import br.edu.ufape.sguEditaisService.features.tipoedital.dto.AtualizarTipoEditalRequest;
import br.edu.ufape.sguEditaisService.features.tipoedital.dto.CriarTipoEditalRequest;
import br.edu.ufape.sguEditaisService.features.tipoedital.dto.TipoEditalResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEditalService {

    private final TipoEditalRepository tipoEditalRepository;

    @Transactional
    public TipoEditalResponse criar(CriarTipoEditalRequest tipoEditalRequest) {
        if(tipoEditalRepository.existsByNomeIgnoreCaseAndModuloOrigem(tipoEditalRequest.nome(), tipoEditalRequest.moduloOrigem()))
        {
            throw new IllegalArgumentException("Já existe um tipo de edital com esse nome.");
        }
                TipoEdital tipo = TipoEdital.criar(
                tipoEditalRequest.nome(),
                tipoEditalRequest.descricao(),
                tipoEditalRequest.moduloOrigem()
        );

        return TipoEditalResponse.from(tipoEditalRepository.save(tipo));
    }

    @Transactional(readOnly = true)
    public TipoEditalResponse buscarPorId(Long id) {
        return TipoEditalResponse.from(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<TipoEditalResponse> listar() {
        return tipoEditalRepository.findAll()
                .stream()
                .map(TipoEditalResponse::from)
                .toList();
    }

    @Transactional
    public TipoEditalResponse atualizar(Long id, AtualizarTipoEditalRequest request) {
        TipoEdital tipo = buscarEntidade(id);

        tipo.atualizar(
                request.nome(),
                request.descricao(),
                request.moduloOrigem()
        );

        return TipoEditalResponse.from(tipo);
    }

    @Transactional
    public void ativar(Long id) {
        buscarEntidade(id).ativar();
    }

    @Transactional
    public void desativar(Long id) {
        buscarEntidade(id).desativar();
    }

    private TipoEdital buscarEntidade(Long id) {
        return tipoEditalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Tipo de edital não encontrado."));
    }
}