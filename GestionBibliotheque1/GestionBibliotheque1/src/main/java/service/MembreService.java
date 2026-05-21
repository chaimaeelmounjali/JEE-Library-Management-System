package service;

import model.Membre;
import dao.MembreDAO;
import java.util.List;

public class MembreService {
    private MembreDAO membreDAO;
    
    public MembreService(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }
    
    // Inscrire un membre
    public boolean inscrireMembre(Membre membre) {
        if (membre == null || !membre.isValid()) {
            return false;
        }
        
        // Vérifier l'unicité de l'email
        if (membreDAO.emailExiste(membre.getEmail())) {
            return false;
        }
        
        return membreDAO.inscrireMembre(membre);
    }
    
    // Récupérer tous les membres
    public List<Membre> getAllMembres() {
        return membreDAO.getAllMembres();
    }
    
    // Récupérer les membres actifs
    public List<Membre> getMembresActifs() {
        return membreDAO.getMembresActifs();
    }
    
    // Récupérer les membres inactifs
    public List<Membre> getMembresInactifs() {
        return membreDAO.getMembresInactifs();
    }
    
    // Récupérer un membre par ID
    public Membre getMembreById(int id) {
        return membreDAO.getMembreById(id);
    }
    
    // Modifier un membre
    public boolean modifierMembre(Membre membre) {
        if (membre == null || !membre.isValid()) {
            return false;
        }
        
        // Vérifier que l'email n'existe pas pour un autre membre
        Membre membreExistant = membreDAO.getMembreById(membre.getIdMembre());
        if (membreExistant == null) {
            return false;
        }
        
        // Si l'email a changé, vérifier qu'il n'existe pas déjà
        if (!membreExistant.getEmail().equals(membre.getEmail())) {
            if (membreDAO.emailExiste(membre.getEmail())) {
                return false;
            }
        }
        
        return membreDAO.modifierMembre(membre);
    }
    
    // Supprimer un membre
    public boolean supprimerMembre(int id) {
        // Vérifier si le membre peut être supprimé (pas d'emprunts en cours)
        if (!peutSupprimerMembre(id)) {
            return false;
        }
        
        return membreDAO.supprimerMembre(id);
    }
    
    // Vérifier si un email existe
    public boolean emailExiste(String email) {
        return membreDAO.emailExiste(email);
    }
    
    // Changer le statut d'un membre
    public boolean changerStatutMembre(int id, boolean statut) {
        return membreDAO.changerStatutMembre(id, statut);
    }
    
    // Vérifier si un membre peut être supprimé
    public boolean peutSupprimerMembre(int idMembre) {
        // Cette méthode devrait vérifier si le membre a des emprunts en cours
        // Pour l'instant, on retourne true - à implémenter avec EmpruntService
        return true;
    }
    
    // Rechercher des membres par terme
    public List<Membre> rechercherMembres(String terme) {
        if (terme == null || terme.trim().isEmpty()) {
            return getAllMembres();
        }
        
        List<Membre> tousMembres = getAllMembres();
        return tousMembres.stream()
                .filter(membre -> 
                    membre.getNom().toLowerCase().contains(terme.toLowerCase()) ||
                    membre.getPrenom().toLowerCase().contains(terme.toLowerCase()) ||
                    membre.getEmail().toLowerCase().contains(terme.toLowerCase()) ||
                    (membre.getTelephone() != null && membre.getTelephone().contains(terme)))
                .toList();
    }
    
    // Valider les données d'un membre
    public boolean validerMembre(Membre membre) {
        if (membre == null) return false;
        
        if (membre.getNom() == null || membre.getNom().trim().isEmpty()) {
            return false;
        }
        
        if (membre.getPrenom() == null || membre.getPrenom().trim().isEmpty()) {
            return false;
        }
        
        if (membre.getEmail() == null || membre.getEmail().trim().isEmpty()) {
            return false;
        }
        
        // Validation basique de l'email
        if (!membre.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }
        
        return true;
    }
    
    // Vérifier si un membre peut emprunter
    public boolean peutEmprunter(int idMembre) {
        Membre membre = membreDAO.getMembreById(idMembre);
        return membre != null && membre.isStatut();
    }
}