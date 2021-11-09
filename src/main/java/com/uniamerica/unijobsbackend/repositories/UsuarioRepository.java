package com.uniamerica.unijobsbackend.repositories;

import com.uniamerica.unijobsbackend.models.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @EntityGraph("graph.user")
    Optional<Usuario> findByEmail(String email);
}
