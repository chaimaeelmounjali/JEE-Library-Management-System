package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class Livre {
    private int idLivre;
    private String titre;
    private String auteur;
    private String isbn;
    private boolean disponible;
    private Date dateAjout;

    // Constructeurs
    public Livre() {
        this.dateAjout = Date.valueOf(LocalDate.now());
        this.disponible = true;
    }

    public Livre(String titre, String auteur, String isbn) {
        this();
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
    }

    public Livre(int idLivre, String titre, String auteur, String isbn, boolean disponible, Date dateAjout) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.disponible = disponible;
        this.dateAjout = dateAjout;
    }

    // Getters et Setters
    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    // Méthodes métier
    public boolean peutEtreEmprunte() {
        return disponible;
    }

    public void emprunter() {
        if (disponible) {
            this.disponible = false;
        } else {
            throw new IllegalStateException("Le livre n'est pas disponible pour l'emprunt");
        }
    }

    public void retourner() {
        this.disponible = true;
    }

    // Equals et HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livre livre = (Livre) o;
        return idLivre == livre.idLivre && 
               Objects.equals(isbn, livre.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLivre, isbn);
    }

    // toString
    @Override
    public String toString() {
        return "Livre{" +
                "idLivre=" + idLivre +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", isbn='" + isbn + '\'' +
                ", disponible=" + disponible +
                ", dateAjout=" + dateAjout +
                '}';
    }

    // Méthodes de validation
    public boolean isValid() {
        return titre != null && !titre.trim().isEmpty() &&
               auteur != null && !auteur.trim().isEmpty() &&
               isbn != null && !isbn.trim().isEmpty();
    }

    public String getStatutDisplay() {
        return disponible ? "Disponible" : "Emprunté";
    }

    public String getCssClassStatut() {
        return disponible ? "badge bg-success" : "badge bg-warning";
    }
}