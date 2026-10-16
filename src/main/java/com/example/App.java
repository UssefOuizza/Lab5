package com.example;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.service.SalleService;
import com.example.service.SalleServiceImpl;
import com.example.util.PaginationResult;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class App {

    public static void main(String[] args) {
        EntityManagerFactory fabriqueEmf = Persistence.createEntityManagerFactory("SallePU");
        EntityManager gestionnaireEm = fabriqueEmf.createEntityManager();

        // Peuplement de données initiales pour le test
        alimenterBaseTest(gestionnaireEm);

        SalleService serviceSalles = new SalleServiceImpl();
        // Injection manuelle pour l'environnement Java SE
        ((SalleServiceImpl) serviceSalles).setEntityManager(gestionnaireEm);

        // 1. Récupération des salles disponibles sur une plage horaire via JPQL
        LocalDateTime dateDebutRecherche = LocalDateTime.of(2024, 6, 1, 9, 0);
        LocalDateTime dateFinRecherche = LocalDateTime.of(2024, 6, 1, 11, 0);

        System.out.println("=== 1. TEST JPQL : Salles disponibles ===");
        List<Salle> listeSallesDisponibles = serviceSalles.obtenirSallesDisponibles(dateDebutRecherche, dateFinRecherche);
        System.out.println("Salles libres entre " + dateDebutRecherche + " et " + dateFinRecherche + " :");
        for (Salle salleCourante : listeSallesDisponibles) {
            System.out.println("- " + salleCourante.getDesignation() + " (Capacité : " + salleCourante.getCapaciteAccueil() + ")");
        }

        // 2. Recherche multi-critères dynamique via Criteria API
        System.out.println("\n=== 2. TEST CRITERIA API : Recherche multi-critères ===");
        String filtreNom = "Salle";
        Integer seuilCapacite = 30;
        List<Salle> listeSallesFiltrees = serviceSalles.rechercherSalles(filtreNom, seuilCapacite, dateDebutRecherche, dateFinRecherche, 1, 10);
        System.out.println("Salles contenant '" + filtreNom + "', capacité >= " + seuilCapacite + " et non réservées :");
        for (Salle salleFiltree : listeSallesFiltrees) {
            System.out.println("- " + salleFiltree.getDesignation() + " (Capacité : " + salleFiltree.getCapaciteAccueil() + ")");
        }

        // 3. Pagination des résultats côté serveur
        System.out.println("\n=== 3. TEST DE PAGINATION CÔTÉ SERVEUR ===");
        int pageCible = 1;
        int elementsParLot = 2;
        PaginationResult<Salle> lotResultat = serviceSalles.rechercherSallesPaginees(null, null, null, null, pageCible, elementsParLot);
        System.out.println("Page " + lotResultat.getNumeroPage() + "/" + lotResultat.getNombreTotalPages() +
                " (Total salles : " + lotResultat.getNombreTotalElements() + ") :");
        for (Salle itemSalle : lotResultat.getElements()) {
            System.out.println("- " + itemSalle.getDesignation() + " (Capacité : " + itemSalle.getCapaciteAccueil() + ")");
        }

        gestionnaireEm.close();
        fabriqueEmf.close();
    }

    private static void alimenterBaseTest(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Salle salleAlpha = new Salle("Salle A - Einstein", 100);
        Salle salleBeta = new Salle("Salle B - Turing", 25);
        Salle salleGamma = new Salle("Salle C - Lovelace", 50);
        Salle salleDelta = new Salle("Salle D - Curie", 15);

        em.persist(salleAlpha);
        em.persist(salleBeta);
        em.persist(salleGamma);
        em.persist(salleDelta);

        // Réservation sur Salle B qui chevauche 09:00 - 11:00
        Reservation resaChevauchante = new Reservation(
                salleBeta,
                LocalDateTime.of(2024, 6, 1, 9, 30),
                LocalDateTime.of(2024, 6, 1, 10, 30)
        );
        em.persist(resaChevauchante);

        // Réservation sur Salle D qui se situe plus tard dans la journée
        Reservation resaApresMidi = new Reservation(
                salleDelta,
                LocalDateTime.of(2024, 6, 1, 14, 0),
                LocalDateTime.of(2024, 6, 1, 16, 0)
        );
        em.persist(resaApresMidi);

        transaction.commit();
    }
}
