package com.uniamerica.unijobsbackend.models;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@EqualsAndHashCode(of = {"id_produto", "titulo", "ativo"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Produto extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_produto;

    private String titulo;

    private String descricao;

    private Double preco;

    private String miniatura;

    @Transient
    private byte[] miniaturaBytes;

    private Boolean ativo;

    private Integer prazo;

    @ManyToOne
    @JoinColumn(name = "id_tipo_produto")
    private TipoProduto tipoProduto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
