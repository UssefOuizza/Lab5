package com.example.repository;

import com.example.model.Reservation;
import com.example.model.Salle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SalleRepositoryImpl implements SalleRepository {

    @PersistenceContext(unitName = "SallePU")
    private EntityManager gestionnaireEntites;

    public SalleRepositoryImpl() {
    }

    public SalleRepositoryImpl(EntityManager gestionnaireEntites) {
        this.gestionnaireEntites = gestionnaireEntites;
    }

    public EntityManager getGestionnaireEntites() {
        return gestionnaireEntites;
    }

    public void setGestionnaireEntites(EntityManager gestionnaireEntites) {
        this.gestionnaireEntites = gestionnaireEntites;
    }

    @Override
    public List<Salle> trouverSallesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) {
        String requeteJpql = "SELECT s FROM Salle s WHERE s.id NOT IN (" +
                             "SELECT r.salleConcernee.id FROM Reservation r WHERE (:momentDebut < r.dateFin AND :momentFin > r.dateDebut))";
        return gestionnaireEntites.createQuery(requeteJpql, Salle.class)
                .setParameter("momentDebut", dateDebut)
                .setParameter("momentFin", dateFin)
                .getResultList();
    }

    @Override
    public List<Salle> filtrerSalles(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau, int page, int taille) {
        CriteriaBuilder fabriqueCritere = gestionnaireEntites.getCriteriaBuilder();
        CriteriaQuery<Salle> requeteCritere = fabriqueCritere.createQuery(Salle.class);
        Root<Salle> racine = requeteCritere.from(Salle.class);

        List<Predicate> filtres = new ArrayList<>();

        // Filtrage textuel sur la désignation
        if (motifNom != null && !motifNom.trim().isEmpty()) {
            filtres.add(fabriqueCritere.like(fabriqueCritere.lower(racine.get("designation")), "%" + motifNom.trim().toLowerCase() + "%"));
        }

        // Filtrage sur la capacité d'accueil minimale
        if (capaciteMin != null) {
            filtres.add(fabriqueCritere.ge(racine.get("capaciteAccueil"), capaciteMin));
        }

        // Filtrage des créneaux en conflit
        if (debutCreneau != null && finCreneau != null) {
            Subquery<Long> sousRequeteChevauchement = requeteCritere.subquery(Long.class);
            Root<Reservation> racineReservation = sousRequeteChevauchement.from(Reservation.class);
            sousRequeteChevauchement.select(racineReservation.get("salleConcernee").get("id"));

            Predicate conflitHoraire = fabriqueCritere.and(
                fabriqueCritere.lessThan(racineReservation.get("dateDebut"), finCreneau),
                fabriqueCritere.greaterThan(racineReservation.get("dateFin"), debutCreneau)
            );
            sousRequeteChevauchement.where(conflitHoraire);

            filtres.add(fabriqueCritere.not(racine.get("id").in(sousRequeteChevauchement)));
        }

        if (!filtres.isEmpty()) {
            requeteCritere.select(racine).where(fabriqueCritere.and(filtres.toArray(new Predicate[0])));
        } else {
            requeteCritere.select(racine);
        }

        TypedQuery<Salle> requeteExecutable = gestionnaireEntites.createQuery(requeteCritere);
        if (page > 0 && taille > 0) {
            requeteExecutable.setFirstResult((page - 1) * taille);
            requeteExecutable.setMaxResults(taille);
        }

        return requeteExecutable.getResultList();
    }

    @Override
    public long compterSalles(String motifNom, Integer capaciteMin, LocalDateTime debutCreneau, LocalDateTime finCreneau) {
        CriteriaBuilder constructeur = gestionnaireEntites.getCriteriaBuilder();
        CriteriaQuery<Long> requeteTotal = constructeur.createQuery(Long.class);
        Root<Salle> racineComptage = requeteTotal.from(Salle.class);
        requeteTotal.select(constructeur.count(racineComptage));

        List<Predicate> conditions = new ArrayList<>();

        if (motifNom != null && !motifNom.trim().isEmpty()) {
            conditions.add(constructeur.like(constructeur.lower(racineComptage.get("designation")), "%" + motifNom.trim().toLowerCase() + "%"));
        }

        if (capaciteMin != null) {
            conditions.add(constructeur.ge(racineComptage.get("capaciteAccueil"), capaciteMin));
        }

        if (debutCreneau != null && finCreneau != null) {
            Subquery<Long> sousRequete = requeteTotal.subquery(Long.class);
            Root<Reservation> racineResa = sousRequete.from(Reservation.class);
            sousRequete.select(racineResa.get("salleConcernee").get("id"));

            Predicate chevauchement = constructeur.and(
                constructeur.lessThan(racineResa.get("dateDebut"), finCreneau),
                constructeur.greaterThan(racineResa.get("dateFin"), debutCreneau)
            );
            sousRequete.where(chevauchement);

            conditions.add(constructeur.not(racineComptage.get("id").in(sousRequete)));
        }

        if (!conditions.isEmpty()) {
            requeteTotal.where(constructeur.and(conditions.toArray(new Predicate[0])));
        }

        return gestionnaireEntites.createQuery(requeteTotal).getSingleResult();
    }

    @Override
    public void enregistrer(Salle salleAEnregistrer) {
        if (salleAEnregistrer.getId() == null) {
            gestionnaireEntites.persist(salleAEnregistrer);
        } else {
            gestionnaireEntites.merge(salleAEnregistrer);
        }
    }

    @Override
    public Salle chercherParId(Long identifiant) {
        return gestionnaireEntites.find(Salle.class, identifiant);
    }
}
