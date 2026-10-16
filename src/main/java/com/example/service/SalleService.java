package com.example.service;

import com.example.model.Salle;
import com.example.util.PaginationResult;

import java.time.LocalDateTime;
import java.util.List;

public interface SalleService {
    List<Salle> obtenirSallesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin);
    List<Salle> rechercherSalles(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau, int page, int taille);
    long compterSallesFiltrees(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau);
    PaginationResult<Salle> rechercherSallesPaginees(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau, int page, int taille);
    void sauvegarderSalle(Salle salleAEnregistrer);
    Salle recupererParId(Long identifiant);
}
