package com.example.util;

import java.util.List;

public class PaginationResult<T> {
    private List<T> elements;
    private int numeroPage;
    private int taillePage;
    private int nombreTotalPages;
    private long nombreTotalElements;

    public PaginationResult() {
    }

    public PaginationResult(List<T> donnees, int indexPage, int taille, long total) {
        this.elements = donnees;
        this.numeroPage = indexPage;
        this.taillePage = taille;
        this.nombreTotalElements = total;
        this.nombreTotalPages = taille > 0 ? (int) Math.ceil((double) total / taille) : 0;
    }

    // Getters et setters

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> donnees) {
        this.elements = donnees;
    }

    public int getNumeroPage() {
        return numeroPage;
    }

    public void setNumeroPage(int indexPage) {
        this.numeroPage = indexPage;
    }

    public int getTaillePage() {
        return taillePage;
    }

    public void setTaillePage(int taille) {
        this.taillePage = taille;
    }

    public int getNombreTotalPages() {
        return nombreTotalPages;
    }

    public void setNombreTotalPages(int totalPages) {
        this.nombreTotalPages = totalPages;
    }

    public long getNombreTotalElements() {
        return nombreTotalElements;
    }

    public void setNombreTotalElements(long totalElements) {
        this.nombreTotalElements = totalElements;
    }

    @Override
    public String toString() {
        return "PaginationResult{" +
                "numeroPage=" + numeroPage +
                ", taillePage=" + taillePage +
                ", nombreTotalPages=" + nombreTotalPages +
                ", nombreTotalElements=" + nombreTotalElements +
                ", nbElementsRecuperes=" + (elements != null ? elements.size() : 0) +
                '}';
    }
}
