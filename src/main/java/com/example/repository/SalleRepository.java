package com.example.repository;

import com.example.model.Salle;

import java.time.LocalDateTime;
import java.util.List;

public interface SalleRepository {
    List<Salle> trouverSallesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin);
    List<Salle> filtrerSalles(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau, int page, int taille);
    long compterSalles(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau);
    void enregistrer(Salle salleAEnregistrer);
    Salle chercherParId(Long identifiant);
}
