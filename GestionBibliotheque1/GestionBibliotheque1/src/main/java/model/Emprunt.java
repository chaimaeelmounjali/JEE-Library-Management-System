package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Emprunt {
    private int idEmprunt;
    private int idLivre;
    private int idMembre;
    private Date dateEmprunt;
    private Date dateRetourPrevue;
    private Date dateRetourEffective;
    private String statut;

    // Relations (pour l'affichage)
    private String titreLivre;
    private String nomMembre;
    private String auteurLivre;

    // Constructeurs
    public Emprunt() {
        this.dateEmprunt = Date.valueOf(LocalDate.now());
        this.dateRetourPrevue = Date.valueOf(LocalDate.now().plusWeeks(2)); // 2 semaines
        this.statut = "en cours";
    }

    public Emprunt(int idLivre, int idMembre) {
        this();
        this.idLivre = idLivre;
        this.idMembre = idMembre;
    }

    public Emprunt(int idEmprunt, int idLivre, int idMembre, Date dateEmprunt, 
                   Date dateRetourPrevue, Date dateRetourEffective, String statut) {
        this.idEmprunt = idEmprunt;
        this.idLivre = idLivre;
        this.idMembre = idMembre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.statut = statut;
    }

    // Getters et Setters
    public int getIdEmprunt() {
        return idEmprunt;
    }

    public void setIdEmprunt(int idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public int getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public Date getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(Date dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Date getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(Date dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    // Getters pour les relations
    public String getTitreLivre() {
        return titreLivre;
    }

    public void setTitreLivre(String titreLivre) {
        this.titreLivre = titreLivre;
    }

    public String getNomMembre() {
        return nomMembre;
    }

    public void setNomMembre(String nomMembre) {
        this.nomMembre = nomMembre;
    }

    public String getAuteurLivre() {
        return auteurLivre;
    }

    public void setAuteurLivre(String auteurLivre) {
        this.auteurLivre = auteurLivre;
    }

    // Méthodes métier
    public boolean estEnCours() {
        return "en cours".equals(statut);
    }

    public boolean estRetourne() {
        return "retourné".equals(statut);
    }

    public boolean estEnRetard() {
        return "en retard".equals(statut);
    }

    public boolean estEnRetardAutomatique() {
        if (estRetourne() || dateRetourPrevue == null) return false;
        
        LocalDate retourPrevue = dateRetourPrevue.toLocalDate();
        LocalDate maintenant = LocalDate.now();
        
        return maintenant.isAfter(retourPrevue) && estEnCours();
    }

    public int getJoursRetard() {
        if (!estEnRetardAutomatique() && !estEnRetard()) return 0;
        
        LocalDate retourPrevue = dateRetourPrevue.toLocalDate();
        LocalDate reference = estRetourne() ? 
            dateRetourEffective.toLocalDate() : LocalDate.now();
        
        return (int) ChronoUnit.DAYS.between(retourPrevue, reference);
    }

    public double calculerAmende() {
        int joursRetard = getJoursRetard();
        if (joursRetard <= 0) return 0.0;
        
        // 2 DH par jour de retard
        return joursRetard * 2.0;
    }

    public boolean peutEtreRetourne() {
        return estEnCours() || estEnRetard();
    }

    public void marquerCommeRetourne() {
        this.dateRetourEffective = Date.valueOf(LocalDate.now());
        // Correction de la logique de statut
        if (this.dateRetourEffective.after(this.dateRetourPrevue)) {
            this.statut = "en retard";
        } else {
            this.statut = "retourné";
        }
    }

    // Equals et HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprunt emprunt = (Emprunt) o;
        return idEmprunt == emprunt.idEmprunt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmprunt);
    }

    // toString
    @Override
    public String toString() {
        return "Emprunt{" +
                "idEmprunt=" + idEmprunt +
                ", idLivre=" + idLivre +
                ", idMembre=" + idMembre +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevue=" + dateRetourPrevue +
                ", dateRetourEffective=" + dateRetourEffective +
                ", statut='" + statut + '\'' +
                '}';
    }

    // Méthodes d'affichage
    public String getStatutDisplay() {
        switch (statut) {
            case "en cours":
                return estEnRetardAutomatique() ? "En retard" : "En cours";
            case "retourné":
                return "Retourné";
            case "en retard":
                return "Retourné (en retard)";
            default:
                return statut;
        }
    }

    public String getCssClassStatut() {
        switch (statut) {
            case "en cours":
                return estEnRetardAutomatique() ? "badge bg-danger" : "badge bg-primary";
            case "retourné":
                return "badge bg-success";
            case "en retard":
                return "badge bg-warning";
            default:
                return "badge bg-secondary";
        }
    }

    public String getCssClassLigne() {
        if (estEnRetardAutomatique() || estEnRetard()) {
            return "table-warning";
        }
        return "";
    }

    public boolean isRetourEnRetard() {
        return estEnRetard() || (estRetourne() && dateRetourEffective.after(dateRetourPrevue));
    }
}