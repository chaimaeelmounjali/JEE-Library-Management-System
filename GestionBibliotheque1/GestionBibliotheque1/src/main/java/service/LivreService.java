package service;

import model.Livre;
import dao.LivreDAO;
import java.util.List;

public class LivreService {
    private LivreDAO livreDAO;
    
    public LivreService(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }
    
    // Ajouter un livre
    public boolean ajouterLivre(Livre livre) {
        if (livre == null || !livre.isValid()) {
            return false;
        }
        
        // Vérifier l'unicité de l'ISBN
        if (livreDAO.isbnExiste(livre.getIsbn())) {
            return false;
        }
        
        return livreDAO.ajouterLivre(livre);
    }
    
    // Récupérer tous les livres
    public List<Livre> getAllLivres() {
        return livreDAO.getAllLivres();
    }
    
    // Récupérer les livres disponibles
    public List<Livre> getLivresDisponibles() {
        return livreDAO.getLivresDisponibles();
    }
    
    // Récupérer les livres indisponibles
    public List<Livre> getLivresIndisponibles() {
        return livreDAO.getLivresIndisponibles();
    }
    
    // Récupérer un livre par ID
    public Livre getLivreById(int id) {
        return livreDAO.getLivreById(id);
    }
    
    // Modifier un livre
    public boolean modifierLivre(Livre livre) {
        if (livre == null || !livre.isValid()) {
            return false;
        }
        
        // Vérifier que l'ISBN n'existe pas pour un autre livre
        Livre livreExistant = livreDAO.getLivreById(livre.getIdLivre());
        if (livreExistant == null) {
            return false;
        }
        
        // Si l'ISBN a changé, vérifier qu'il n'existe pas déjà
        if (!livreExistant.getIsbn().equals(livre.getIsbn())) {
            if (livreDAO.isbnExiste(livre.getIsbn())) {
                return false;
            }
        }
        
        return livreDAO.modifierLivre(livre);
    }
    
    // Supprimer un livre
    public boolean supprimerLivre(int id) {
        // Vérifier si le livre peut être supprimé (non emprunté)
        if (!peutSupprimerLivre(id)) {
            return false;
        }
        
        return livreDAO.supprimerLivre(id);
    }
    
    // Vérifier si un ISBN existe
    public boolean isbnExiste(String isbn) {
        return livreDAO.isbnExiste(isbn);
    }
    
    // Marquer un livre comme disponible
    public boolean marquerLivreDisponible(int id) {
        return livreDAO.marquerLivreDisponible(id);
    }
    
    // Marquer un livre comme indisponible
    public boolean marquerLivreIndisponible(int id) {
        return livreDAO.marquerLivreIndisponible(id);
    }
    
    // Vérifier si un livre peut être supprimé (non emprunté)
    public boolean peutSupprimerLivre(int idLivre) {
        // Pour l'instant, on suppose qu'un livre ne peut pas être supprimé s'il est emprunté
        // Cette logique sera implémentée dans EmpruntService
        Livre livre = livreDAO.getLivreById(idLivre);
        return livre != null && livre.isDisponible();
    }
    
    // Rechercher des livres par terme
    public List<Livre> rechercherLivres(String terme) {
        if (terme == null || terme.trim().isEmpty()) {
            return getAllLivres();
        }
        
        // Implémentation basique de recherche - à améliorer si nécessaire
        List<Livre> tousLivres = getAllLivres();
        return tousLivres.stream()
                .filter(livre -> 
                    livre.getTitre().toLowerCase().contains(terme.toLowerCase()) ||
                    livre.getAuteur().toLowerCase().contains(terme.toLowerCase()) ||
                    livre.getIsbn().toLowerCase().contains(terme.toLowerCase()))
                .toList();
    }
    
    // Valider les données d'un livre
    public boolean validerLivre(Livre livre) {
        if (livre == null) return false;
        
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            return false;
        }
        
        if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty()) {
            return false;
        }
        
        if (livre.getIsbn() == null || livre.getIsbn().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
}