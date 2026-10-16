package com.example.service;

import com.example.model.Salle;
import com.example.repository.SalleRepository;
import com.example.repository.SalleRepositoryImpl;
import com.example.util.PaginationResult;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class SalleServiceImpl implements SalleService {

    @Inject
    private SalleRepository depotSalles;

    public SalleServiceImpl() {
        this.depotSalles = new SalleRepositoryImpl();
    }

    public SalleServiceImpl(SalleRepository depotSalles) {
        this.depotSalles = depotSalles;
    }

    public SalleServiceImpl(EntityManager gestionnaireEntites) {
        this.depotSalles = new SalleRepositoryImpl(gestionnaireEntites);
    }

    public SalleRepository getDepotSalles() {
        return depotSalles;
    }

    public void setDepotSalles(SalleRepository depotSalles) {
        this.depotSalles = depotSalles;
    }

    public void setEntityManager(EntityManager gestionnaireEntites) {
        if (this.depotSalles == null) {
            this.depotSalles = new SalleRepositoryImpl(gestionnaireEntites);
        } else if (this.depotSalles instanceof SalleRepositoryImpl) {
            ((SalleRepositoryImpl) this.depotSalles).setGestionnaireEntites(gestionnaireEntites);
        }
    }

    public void setGestionnaireEntites(EntityManager gestionnaireEntites) {
        setEntityManager(gestionnaireEntites);
    }

    @Override
    public List<Salle> obtenirSallesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return depotSalles.trouverSallesDisponibles(dateDebut, dateFin);
    }

    @Override
    public List<Salle> rechercherSalles(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau, int page, int taille) {
        return depotSalles.filtrerSalles(motifNom, capaciteMin, debutCreneau, finCreneau, page, taille);
    }

    @Override
    public long compterSallesFiltrees(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau) {
        return depotSalles.compterSalles(motifNom, capaciteMin, debutCreneau, finCreneau);
    }

    @Override
    public PaginationResult<Salle> rechercherSallesPaginees(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau, int page, int taille) {
        List<Salle> listeSalles = depotSalles.filtrerSalles(motifNom, capaciteMin, debutCreneau, finCreneau, page, taille);
        long totalSalles = depotSalles.compterSalles(motifNom, capaciteMin, debutCreneau, finCreneau);
        return new PaginationResult<>(listeSalles, page, taille, totalSalles);
    }

    @Override
    public void sauvegarderSalle(Salle salleAEnregistrer) {
        depotSalles.enregistrer(salleAEnregistrer);
    }

    @Override
    public Salle recupererParId(Long identifiant) {
        return depotSalles.chercherParId(identifiant);
    }
}
