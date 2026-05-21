package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Membre {
    private int idMembre;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Date dateInscription;
    private boolean statut;

    // Constructeurs
    public Membre() {
        this.dateInscription = Date.valueOf(LocalDate.now());
        this.statut = true;
    }

    public Membre(String nom, String prenom, String email) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public Membre(int idMembre, String nom, String prenom, String email, String telephone, 
                  Date dateInscription, boolean statut) {
        this.idMembre = idMembre;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.dateInscription = dateInscription;
        this.statut = statut;
    }

    // Getters et Setters
    public int getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    // Méthodes métier
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public int getAncienneteMois() {
        if (dateInscription == null) return 0;
        
        LocalDate inscription = dateInscription.toLocalDate();
        LocalDate maintenant = LocalDate.now();
        
        return Period.between(inscription, maintenant).getMonths() + 
               Period.between(inscription, maintenant).getYears() * 12;
    }

    public boolean estNouveauMembre() {
        return getAncienneteMois() < 3; // Moins de 3 mois
    }

    public void activer() {
        this.statut = true;
    }

    public void desactiver() {
        this.statut = false;
    }

    public boolean peutEmprunter() {
        return statut;
    }

    // Equals et HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membre membre = (Membre) o;
        return idMembre == membre.idMembre && 
               Objects.equals(email, membre.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMembre, email);
    }

    // toString
    @Override
    public String toString() {
        return "Membre{" +
                "idMembre=" + idMembre +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", dateInscription=" + dateInscription +
                ", statut=" + statut +
                '}';
    }

    // Méthodes de validation
    public boolean isValid() {
        return nom != null && !nom.trim().isEmpty() &&
               prenom != null && !prenom.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               isValidEmail(email);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public String getStatutDisplay() {
        return statut ? "Actif" : "Inactif";
    }

    public String getCssClassStatut() {
        return statut ? "badge bg-success" : "badge bg-secondary";
    }

    public String getInitiales() {
        if (prenom == null || nom == null) return "??";
        return (prenom.charAt(0) + "" + nom.charAt(0)).toUpperCase();
    }
}