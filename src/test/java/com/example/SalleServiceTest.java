package com.example;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.service.SalleService;
import com.example.service.SalleServiceImpl;
import com.example.util.PaginationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class SalleServiceTest {

    private EntityManagerFactory fabriqueEmf;
    private EntityManager gestionnaireEm;
    private SalleService serviceTest;

    @Before
    public void initialiserEnvironnement() {
        fabriqueEmf = Persistence.createEntityManagerFactory("SallePU");
        gestionnaireEm = fabriqueEmf.createEntityManager();

        serviceTest = new SalleServiceImpl();
        ((SalleServiceImpl) serviceTest).setEntityManager(gestionnaireEm);

        gestionnaireEm.getTransaction().begin();
        gestionnaireEm.createQuery("DELETE FROM Reservation").executeUpdate();
        gestionnaireEm.createQuery("DELETE FROM Salle").executeUpdate();

        Salle salleBleue = new Salle("Salle Bleue", 50);
        Salle salleRouge = new Salle("Salle Rouge", 20);
        Salle salleVerte = new Salle("Salle Verte", 100);

        gestionnaireEm.persist(salleBleue);
        gestionnaireEm.persist(salleRouge);
        gestionnaireEm.persist(salleVerte);

        Reservation reservationOccupee = new Reservation(
                salleBleue,
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2024, 6, 1, 12, 0)
        );
        gestionnaireEm.persist(reservationOccupee);

        gestionnaireEm.getTransaction().commit();
    }

    @After
    public void cloturerEnvironnement() {
        if (gestionnaireEm != null && gestionnaireEm.isOpen()) {
            gestionnaireEm.close();
        }
        if (fabriqueEmf != null && fabriqueEmf.isOpen()) {
            fabriqueEmf.close();
        }
    }

    @Test
    public void testObtenirSallesDisponiblesJPQL() {
        LocalDateTime instantDebut = LocalDateTime.of(2024, 6, 1, 10, 30);
        LocalDateTime instantFin = LocalDateTime.of(2024, 6, 1, 11, 30);

        List<Salle> disponibles = serviceTest.obtenirSallesDisponibles(instantDebut, instantFin);

        assertEquals(2, disponibles.size());
        for (Salle s : disponibles) {
            assertNotEquals("Salle Bleue", s.getDesignation());
        }
    }

    @Test
    public void testRechercheMultiCriteresCriteria() {
        LocalDateTime instantDebut = LocalDateTime.of(2024, 6, 1, 10, 30);
        LocalDateTime instantFin = LocalDateTime.of(2024, 6, 1, 11, 30);

        List<Salle> selection = serviceTest.rechercherSalles("Salle", 60, instantDebut, instantFin, 1, 10);
        assertEquals(1, selection.size());
        assertEquals("Salle Verte", selection.get(0).getDesignation());
        assertEquals(100, selection.get(0).getCapaciteAccueil());
    }

    @Test
    public void testPaginationDesSalles() {
        PaginationResult<Salle> premierePage = serviceTest.rechercherSallesPaginees(null, null, null, null, 1, 2);
        assertEquals(1, premierePage.getNumeroPage());
        assertEquals(2, premierePage.getTaillePage());
        assertEquals(3, premierePage.getNombreTotalElements());
        assertEquals(2, premierePage.getNombreTotalPages());
        assertEquals(2, premierePage.getElements().size());

        PaginationResult<Salle> deuxiemePage = serviceTest.rechercherSallesPaginees(null, null, null, null, 2, 2);
        assertEquals(1, deuxiemePage.getElements().size());
    }
}
