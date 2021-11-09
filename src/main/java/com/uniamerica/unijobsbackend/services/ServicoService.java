package com.uniamerica.unijobsbackend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.uniamerica.unijobsbackend.Excessoes.RecursoNaoEncontradoExcessao;
import com.uniamerica.unijobsbackend.configs.CloudinarySingleton;
import com.uniamerica.unijobsbackend.dto.ServicoDTO;
import com.uniamerica.unijobsbackend.models.Servico;
import com.uniamerica.unijobsbackend.repositories.ServicoRepository;
import com.uniamerica.unijobsbackend.repositories.TipoServicoRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ServicoService {

    private final ServicoRepository repository;

    private final TipoServicoRepository tipoServicoRepository;

    public Page<ServicoDTO> findAll(Pageable pageable) {
        Page<Servico> servicos = repository.findAll(pageable);
        return servicos.map(ServicoDTO::new);
    }

    public ServicoDTO store(Servico servico) {
        Integer idTipoServico = servico.getTipoServico().getId_tipo_servico();

        var tipoServico = tipoServicoRepository.findById(idTipoServico)
                .orElseThrow(() -> new RecursoNaoEncontradoExcessao("Tipo Serviço não Encontrado! id:" + idTipoServico));

        servico.setTipoServico(tipoServico);
        Cloudinary cloudinary = CloudinarySingleton.getCloudinary();
        try {
            var uploadResult = cloudinary.uploader().upload(servico.getMiniatura(), ObjectUtils.emptyMap());
            servico.setMiniatura((String) uploadResult.get("url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ServicoDTO(repository.save(servico));
    }

    public String destroy(Integer id) {
        repository.findById(id).orElseThrow(RecursoNaoEncontradoExcessao::new);
        repository.deleteById(id);
        return "Serviço deletado com sucesso!";
    }

    public ServicoDTO update(Integer id, Servico novoServico) {
        var servico = repository.findById(id).orElseThrow(RecursoNaoEncontradoExcessao::new);

        Integer idTipoServico = novoServico.getTipoServico().getId_tipo_servico();

        var tipoServico = tipoServicoRepository.findById(idTipoServico)
                .orElseThrow(() -> new RecursoNaoEncontradoExcessao("Tipo Serviço não Encontrado! id:" + idTipoServico));

        servico.setTitulo(novoServico.getTitulo());
        servico.setDescricao(novoServico.getDescricao());
        servico.setMiniatura(novoServico.getMiniatura());
        servico.setPrazo(novoServico.getPrazo());
        servico.setPreco(novoServico.getPreco());
        servico.setTipoServico(tipoServico);
        servico.setAtivo(novoServico.isAtivo());

        return new ServicoDTO(servico);
    }

    public ServicoDTO find(Integer id) {
        return new ServicoDTO(repository.findById(id).orElseThrow(RecursoNaoEncontradoExcessao::new));
    }
}
