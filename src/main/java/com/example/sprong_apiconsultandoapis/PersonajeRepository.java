package com.example.sprong_apiconsultandoapis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonajeRepository extends JpaRepository<Personaje,Integer> {
    Optional<Personaje> findPersonajeByNameEquals(String nombre);
}
