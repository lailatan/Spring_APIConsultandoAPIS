package com.example.sprong_apiconsultandoapis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class PersonajeService {

    private final PersonajeRepository personajeRepo;

    @Autowired
    public PersonajeService(PersonajeRepository personajeRepo) {
        this.personajeRepo = personajeRepo;
    }


    public Personaje save(Personaje personaje) {
        personaje.corregirID();
        return personajeRepo.save(personaje);
    }

    public Optional<Personaje> findPersonajeById(Integer id) {
        return personajeRepo.findById(id);
    }

    public Personaje findPersonajesByNombre(String nombre) {
        Personaje personajeAPI=null;
        Optional<Personaje> personaje = personajeRepo.findPersonajeByNameEquals(nombre);

        if (!personaje.isPresent()) {
            RestTemplate apiStarWars = new RestTemplate();
            personajeAPI = apiStarWars
                    .getForEntity("https://swapi.dev/api/people/?search=" + nombre, Personaje.class)
                    .getBody();
            save(personajeAPI);
        }
        return  personaje.orElse(personajeAPI);
    }

    public List<Personaje> getAll() {
        return personajeRepo.findAll();
    }
}