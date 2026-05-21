package service;

import model.Emprunt;
import dao.EmpruntDAO;
import dao.LivreDAO;
import dao.MembreDAO;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmpruntService {
    private EmpruntDAO empruntDAO;
    private LivreDAO livreDAO;
    private MembreDAO membreDAO;
    
    // Constructeur principal
    public EmpruntService(EmpruntDAO empruntDAO) {
        this.empruntDAO = empruntDAO;
    }
    
    // Constructeur avec toutes les dépendances
    public EmpruntService(EmpruntDAO empruntDAO, LivreDAO livreDAO, MembreDAO membreDAO) {
        this.empruntDAO = empruntDAO;
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
    }
    
    // Pour l'injection des dépendances supplémentaires
    public void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }
    
    public void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }
    
    // Enregistrer un nouvel emprunt
    public boolean enregistrerEmprunt(Emprunt emprunt) {
        if (emprunt == null) {
            return false;
        }
        
        // Validation des données
        if (!validerEmprunt(emprunt)) {
            return false;
        }
        
        // Vérifier la disponibilité du livre
        if (livreDAO != null && isLivreEmprunte(emprunt.getIdLivre())) {
            return false;
        }
        
        // Vérifier que le membre peut emprunter
        if (membreDAO != null) {
            int nbEmprunts = empruntDAO.getNombreEmpruntsEnCours(emprunt.getIdMembre());
            if (nbEmprunts >= 3) { // Limite de 3 emprunts par membre
                return false;
            }
        }
        
        return empruntDAO.enregistrerEmprunt(emprunt);
    }
    
    // Récupérer tous les emprunts
    public List<Emprunt> getAllEmprunts() {
        return empruntDAO.getAllEmprunts();
    }
    
    // Récupérer les emprunts en cours
    public List<Emprunt> getEmpruntsEnCours() {
        return empruntDAO.getEmpruntsEnCours();
    }
    
    // Récupérer les emprunts en retard
    public List<Emprunt> getEmpruntsEnRetard() {
        return empruntDAO.getEmpruntsEnRetard();
    }
    
    // Récupérer les emprunts retournés
    public List<Emprunt> getEmpruntsRetournes() {
        return empruntDAO.getEmpruntsRetournes();
    }
    
    // Récupérer un emprunt par ID
    public Emprunt getEmpruntById(int id) {
        return empruntDAO.getEmpruntById(id);
    }
    
    // Enregistrer le retour d'un livre
    public boolean enregistrerRetour(Emprunt emprunt) {
        if (emprunt == null) {
            return false;
        }
        
        // Vérifier que l'emprunt est bien en cours
        Emprunt empruntExistant = empruntDAO.getEmpruntById(emprunt.getIdEmprunt());
        if (empruntExistant == null || !"en cours".equals(empruntExistant.getStatut())) {
            return false;
        }
        
        // Déterminer le statut final
        Date maintenant = Date.valueOf(LocalDate.now());
        emprunt.setDateRetourEffective(maintenant);
        
        if (maintenant.after(emprunt.getDateRetourPrevue())) {
            emprunt.setStatut("en retard");
        } else {
            emprunt.setStatut("retourné");
        }
        
        return empruntDAO.enregistrerRetour(emprunt);
    }
    
    // Compter les emprunts en cours d'un membre
    public int getNombreEmpruntsEnCours(int idMembre) {
        return empruntDAO.getNombreEmpruntsEnCours(idMembre);
    }
    
    // Vérifier si un livre est emprunté
    public boolean isLivreEmprunte(int idLivre) {
        return empruntDAO.isLivreEmprunte(idLivre);
    }
    
    // Vérifier si un membre a des emprunts en cours
    public boolean membreAEmpruntsEnCours(int idMembre) {
        return empruntDAO.membreAEmpruntsEnCours(idMembre);
    }
    
    // Vérifier si un emprunt peut être créé
    public boolean peutEmprunter(int idMembre, int idLivre) {
        // Vérifier la disponibilité du livre
        if (isLivreEmprunte(idLivre)) {
            return false;
        }
        
        // Vérifier la limite d'emprunts du membre
        int nbEmprunts = getNombreEmpruntsEnCours(idMembre);
        if (nbEmprunts >= 3) {
            return false;
        }
        
        return true;
    }
    
    // Calculer les jours de retard pour un emprunt
    public int calculerJoursRetard(Emprunt emprunt) {
        if (emprunt == null || emprunt.getDateRetourPrevue() == null) {
            return 0;
        }
        
        LocalDate retourPrevue = emprunt.getDateRetourPrevue().toLocalDate();
        LocalDate maintenant = LocalDate.now();
        
        if (maintenant.isAfter(retourPrevue) && "en cours".equals(emprunt.getStatut())) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(retourPrevue, maintenant);
        }
        
        return 0;
    }
    
    // Calculer l'amende pour un emprunt
    public double calculerAmende(Emprunt emprunt) {
        int joursRetard = calculerJoursRetard(emprunt);
        return joursRetard * 2.0; // 2 DH par jour de retard
    }
    
    // Mettre à jour automatiquement les statuts des emprunts en retard
    public void mettreAJourEmpruntsEnRetard() {
        List<Emprunt> empruntsEnCours = getEmpruntsEnCours();
        
        for (Emprunt emprunt : empruntsEnCours) {
            if (calculerJoursRetard(emprunt) > 0 && "en cours".equals(emprunt.getStatut())) {
                // L'emprunt est en retard mais toujours marqué comme "en cours"
                // On pourrait mettre à jour le statut ici si nécessaire
                System.out.println("Emprunt ID " + emprunt.getIdEmprunt() + " est en retard de " + 
                                 calculerJoursRetard(emprunt) + " jours");
            }
        }
    }
    
    // Récupérer l'historique des emprunts par membre
    public List<Emprunt> getHistoriqueEmpruntsParMembre(int idMembre) {
        // Utilisation de la méthode DAO si elle existe, sinon filtre manuel
        try {
            return empruntDAO.getHistoriqueEmpruntsParMembre(idMembre);
        } catch (Exception e) {
            // Fallback: filtre manuel
            List<Emprunt> tousEmprunts = getAllEmprunts();
            return tousEmprunts.stream()
                    .filter(emprunt -> emprunt.getIdMembre() == idMembre)
                    .collect(Collectors.toList());
        }
    }
    
    // Récupérer l'historique des emprunts par livre
    public List<Emprunt> getHistoriqueEmpruntsParLivre(int idLivre) {
        // Utilisation de la méthode DAO si elle existe, sinon filtre manuel
        try {
            return empruntDAO.getHistoriqueEmpruntsParLivre(idLivre);
        } catch (Exception e) {
            // Fallback: filtre manuel
            List<Emprunt> tousEmprunts = getAllEmprunts();
            return tousEmprunts.stream()
                    .filter(emprunt -> emprunt.getIdLivre() == idLivre)
                    .collect(Collectors.toList());
        }
    }
    
    // Valider les données d'un emprunt
    public boolean validerEmprunt(Emprunt emprunt) {
        if (emprunt == null) return false;
        
        if (emprunt.getIdLivre() <= 0) {
            return false;
        }
        
        if (emprunt.getIdMembre() <= 0) {
            return false;
        }
        
        if (emprunt.getDateEmprunt() == null) {
            return false;
        }
        
        if (emprunt.getDateRetourPrevue() == null) {
            return false;
        }
        
        if (emprunt.getDateRetourPrevue().before(emprunt.getDateEmprunt())) {
            return false;
        }
        
        if (emprunt.getStatut() == null || emprunt.getStatut().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    // Méthode pour obtenir les statistiques d'emprunts
    public String obtenirStatistiques() {
        int totalEmprunts = getAllEmprunts().size();
        int empruntsEnCours = getEmpruntsEnCours().size();
        int empruntsEnRetard = getEmpruntsEnRetard().size();
        int empruntsRetournes = getEmpruntsRetournes().size();
        
        return String.format(
            "Statistiques des emprunts:\n" +
            "- Total: %d\n" +
            "- En cours: %d\n" +
            "- En retard: %d\n" +
            "- Retournés: %d",
            totalEmprunts, empruntsEnCours, empruntsEnRetard, empruntsRetournes
        );
    }
}