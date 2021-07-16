package com.example.sprong_apiconsultandoapis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/*
Al sistema del universo SW, agregarle endpoints para lo siguiente:

1) Buscar a un personaje por nombre.
    Primero buscar en la base de datos local.
    Si lo encuentra, mostrarlo.
    Si no, consultar a la API usando este endpoint:
        https://swapi.dev/api/people/?search=luke
    Si no lo encuentra, informar que no se pudo encontrar.
    Si lo encuentra en la API, guardarlo en la DB.

2) Agregar un endpoint para ver todos los personajes de la BASE DE DATOS.
 */
@RestController
@RequestMapping("api/v1")
public class APIController {

    private final PersonajeService personajeService;

    @Autowired
    public APIController(PersonajeService personajeService) {
        this.personajeService = personajeService;
    }

    @GetMapping(value="/personaje/{id}/guardar", produces = "application/json")
    public Personaje leerAPIyGuardar(@PathVariable Integer id){
        RestTemplate apiStarWars = new RestTemplate();
        Personaje personaje = apiStarWars
                .getForEntity("https://swapi.dev/api/people/"+id+"/", Personaje.class)
                .getBody();
        personajeService.save(personaje);
        return personaje;
    }

    @GetMapping(value="/personajeAPI/{id}")
    public Personaje getPersonaje(@PathVariable Integer id){
        RestTemplate apiStarWars = new RestTemplate();
        ResponseEntity<Personaje> personaje = apiStarWars.getForEntity("https://swapi.dev/api/people/"+id+"/", Personaje.class);
        return personaje.getBody();
    }

    @GetMapping(value="/personaje/id/{id}", produces = "application/json")
    public Personaje getPersonajeLocalODeAPI(@PathVariable Integer id){
        Optional<Personaje> personaje = personajeService.findPersonajeById(id);
        return personaje.orElse(leerAPIyGuardar(id));
    }

    @GetMapping(value="/personaje/nombre/{nombre}")
    public Personaje getPersonajesByNombre(@PathVariable String nombre){
        return personajeService.findPersonajesByNombre(nombre);
    }

    @GetMapping(value="/personajeAPI/{id}/nombre")
    public String getNombrePersonaje(@PathVariable Integer id){
        RestTemplate apiStarWars = new RestTemplate();
        ResponseEntity<Personaje> personaje = apiStarWars.getForEntity("https://swapi.dev/api/people/"+id+"/", Personaje.class);
        return personaje.getBody().getName();
    }

    @GetMapping("/personajes/BD")
    public List<Personaje> getPersonajesBD(){
        return personajeService.getAll();
    }
}