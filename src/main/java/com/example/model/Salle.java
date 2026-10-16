package com.example.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "salles")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, unique = true)
    private String designation;

    @Column(name = "capacite", nullable = false)
    private int capaciteAccueil;

    @OneToMany(mappedBy = "salleConcernee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> listeReservations = new HashSet<>();

    public Salle() {
    }

    public Salle(String designation, int capaciteAccueil) {
        this.designation = designation;
        this.capaciteAccueil = capaciteAccueil;
    }

    public Salle(Long id, String designation, int capaciteAccueil) {
        this.id = id;
        this.designation = designation;
        this.capaciteAccueil = capaciteAccueil;
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getCapaciteAccueil() {
        return capaciteAccueil;
    }

    public void setCapaciteAccueil(int capaciteAccueil) {
        this.capaciteAccueil = capaciteAccueil;
    }

    public Set<Reservation> getListeReservations() {
        return listeReservations;
    }

    public void setListeReservations(Set<Reservation> listeReservations) {
        this.listeReservations = listeReservations;
    }

    public void ajouterReservation(Reservation nouvelleReservation) {
        if (this.listeReservations == null) {
            this.listeReservations = new HashSet<>();
        }
        this.listeReservations.add(nouvelleReservation);
        nouvelleReservation.setSalleConcernee(this);
    }

    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                ", capaciteAccueil=" + capaciteAccueil +
                '}';
    }
}
