package com.example.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salleConcernee;

    @Column(name = "debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "fin", nullable = false)
    private LocalDateTime dateFin;

    public Reservation() {
    }

    public Reservation(Salle salleConcernee, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.salleConcernee = salleConcernee;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Reservation(Long id, Salle salleConcernee, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.id = id;
        this.salleConcernee = salleConcernee;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Salle getSalleConcernee() {
        return salleConcernee;
    }

    public void setSalleConcernee(Salle salleConcernee) {
        this.salleConcernee = salleConcernee;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", salleId=" + (salleConcernee != null ? salleConcernee.getId() : null) +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
