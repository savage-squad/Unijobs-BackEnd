package com.uniamerica.unijobsbackend.controllers;

import com.uniamerica.unijobsbackend.dto.ListaTipoProdutoDTO;
import com.uniamerica.unijobsbackend.dto.NovoTipoProdutoDTO;
import com.uniamerica.unijobsbackend.dto.ProdutoDTO;
import com.uniamerica.unijobsbackend.models.Produto;
import com.uniamerica.unijobsbackend.models.TipoProduto;
import com.uniamerica.unijobsbackend.services.TipoProdutoService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/tipos_produtos"})
@OpenAPIDefinition
@SecurityRequirement(name = "bearerAuth")
public class TipoProdutoController {
    @Autowired
    private TipoProdutoService tipoProdutoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Retorna uma lista de Categorias de Produto.")
    public List<ListaTipoProdutoDTO> visualizar(){
        return tipoProdutoService.findAll()
                .stream()
                .map(this::toListaTipoProdutoDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @Operation(summary = "Cadastra uma Categoria de Produto.")
    @ResponseStatus(HttpStatus.CREATED)
    public TipoProduto cadastrar(@Valid @RequestBody NovoTipoProdutoDTO tipoProduto){
        return tipoProdutoService.save(tipoProduto.converteModelo());
    }

    @PutMapping(path = "{id_tipoProduto}")
    @Operation(summary = "Edita uma Categoria de Produto.")
    @ResponseStatus(HttpStatus.OK)
    public TipoProduto editar(@Valid @RequestBody TipoProduto novoTipoProduto, @PathVariable("id_tipoProduto") Integer id_tipoProduto){
        return tipoProdutoService.update(id_tipoProduto, novoTipoProduto);
    }

    @DeleteMapping(path = "{id_tipoProduto}")
    @Operation(summary = "Deleta uma Categoria de Produto.")
    @ResponseStatus(HttpStatus.OK)
    public String deletar(@PathVariable("id_tipoProduto") Integer id_tipoProduto){
        return tipoProdutoService.delete(id_tipoProduto);
    }

    private ListaTipoProdutoDTO toListaTipoProdutoDTO(TipoProduto tipoProduto){
        return modelMapper.map(tipoProduto, ListaTipoProdutoDTO.class);
    }

    private ProdutoDTO toProdutoDTO(Produto produto){
        return modelMapper.map(produto, ProdutoDTO.class);
    }

    @GetMapping(path = "{id}/produtos")
    public ResponseEntity<List<ProdutoDTO>> produtosByTipoProduto(@PathVariable("id") Integer id){
        return ResponseEntity.ok(tipoProdutoService.servicosByTipoProdutos(id).stream().map(this::toProdutoDTO).collect(Collectors.toList()));
    }
}
