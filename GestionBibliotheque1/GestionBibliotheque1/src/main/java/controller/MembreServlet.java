package controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import model.Membre;
import service.MembreService;
import dao.MembreDAO;
import dao.DatabaseConnection;

@WebServlet("/membres/*")
public class MembreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MembreService membreService;

    @Override
    public void init() throws ServletException {
    	MembreDAO membreDAO = new MembreDAO();
        this.membreService = new MembreService(membreDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = extractAction(request);
        
        try {
            switch (action) {
                case "liste":
                    afficherListeMembres(request, response);
                    break;
                case "ajouter":
                    afficherFormulaireAjout(request, response);
                    break;
                case "modifier":
                    afficherFormulaireModification(request, response);
                    break;
                case "supprimer":
                    supprimerMembre(request, response);
                    break;
                case "changer-statut":
                    changerStatutMembre(request, response);
                    break;
                default:
                    afficherListeMembres(request, response);
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, "Erreur lors du traitement: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = extractAction(request);
        
        try {
            switch (action) {
                case "ajouter":
                    inscrireMembre(request, response);
                    break;
                case "modifier":
                    modifierMembre(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/membres/liste");
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, "Erreur lors du traitement: " + e.getMessage(), e);
        }
    }

    // === MÉTHODES PRIVÉES ===

    private String extractAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "liste";
        }
        return pathInfo.substring(1);
    }

    private void afficherListeMembres(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String filtre = request.getParameter("filtre");
        List<Membre> membres;
        
        if ("actifs".equals(filtre)) {
            membres = membreService.getMembresActifs();
            request.setAttribute("filtreActif", "actifs");
        } else if ("inactifs".equals(filtre)) {
            membres = membreService.getMembresInactifs();
            request.setAttribute("filtreActif", "inactifs");
        } else {
            membres = membreService.getAllMembres();
            request.setAttribute("filtreActif", "tous");
        }
        
        request.setAttribute("membres", membres);
        request.setAttribute("nombreMembres", membres.size());
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/liste.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/ajouter.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireModification(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Membre membre = membreService.getMembreById(id);
            
            if (membre != null) {
                request.setAttribute("membre", membre);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/modifier.jsp");
                dispatcher.forward(request, response);
            } else {
                handleError(request, response, "Membre non trouvé avec l'ID: " + id, null);
            }
        } catch (NumberFormatException e) {
            handleError(request, response, "ID de membre invalide", e);
        }
    }

    private void inscrireMembre(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Récupération des paramètres
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String email = request.getParameter("email");
            String telephone = request.getParameter("telephone");
            
            // Validation des champs obligatoires
            if (nom == null || nom.trim().isEmpty() || 
                prenom == null || prenom.trim().isEmpty() || 
                email == null || email.trim().isEmpty()) {
                
                request.setAttribute("erreur", "Nom, prénom et email sont obligatoires");
                request.setAttribute("nom", nom);
                request.setAttribute("prenom", prenom);
                request.setAttribute("email", email);
                request.setAttribute("telephone", telephone);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/ajouter.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Validation format email
            if (!isValidEmail(email)) {
                request.setAttribute("erreur", "Format d'email invalide");
                request.setAttribute("nom", nom);
                request.setAttribute("prenom", prenom);
                request.setAttribute("email", email);
                request.setAttribute("telephone", telephone);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/ajouter.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Vérification unicité email
            if (membreService.emailExiste(email.trim())) {
                request.setAttribute("erreur", "Un membre avec cet email existe déjà");
                request.setAttribute("nom", nom);
                request.setAttribute("prenom", prenom);
                request.setAttribute("email", email);
                request.setAttribute("telephone", telephone);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/ajouter.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Création du membre
            Membre membre = new Membre();
            membre.setNom(nom.trim());
            membre.setPrenom(prenom.trim());
            membre.setEmail(email.trim().toLowerCase());
            membre.setTelephone(telephone != null ? telephone.trim() : null);
            membre.setDateInscription(Date.valueOf(LocalDate.now()));
            membre.setStatut(true); // Actif par défaut
            
            // Sauvegarde
            boolean succes = membreService.inscrireMembre(membre);
            
            if (succes) {
                request.getSession().setAttribute("succes", "Membre inscrit avec succès!");
                response.sendRedirect(request.getContextPath() + "/membres/liste");
            } else {
                handleError(request, response, "Erreur lors de l'inscription du membre", null);
            }
            
        } catch (Exception e) {
            handleError(request, response, "Erreur lors de l'inscription: " + e.getMessage(), e);
        }
    }

    private void modifierMembre(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String email = request.getParameter("email");
            String telephone = request.getParameter("telephone");
            
            // Validation
            if (nom == null || nom.trim().isEmpty() || 
                prenom == null || prenom.trim().isEmpty() || 
                email == null || email.trim().isEmpty()) {
                
                request.setAttribute("erreur", "Nom, prénom et email sont obligatoires");
                Membre membre = new Membre();
                membre.setIdMembre(id);
                membre.setNom(nom);
                membre.setPrenom(prenom);
                membre.setEmail(email);
                membre.setTelephone(telephone);
                request.setAttribute("membre", membre);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/modifier.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Validation email
            if (!isValidEmail(email)) {
                request.setAttribute("erreur", "Format d'email invalide");
                Membre membre = new Membre();
                membre.setIdMembre(id);
                membre.setNom(nom);
                membre.setPrenom(prenom);
                membre.setEmail(email);
                membre.setTelephone(telephone);
                request.setAttribute("membre", membre);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/modifier.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Vérification unicité email (excluant le membre actuel)
            Membre membreExistant = membreService.getMembreById(id);
            if (membreExistant == null) {
                handleError(request, response, "Membre non trouvé", null);
                return;
            }
            
            if (!membreExistant.getEmail().equals(email.trim()) && membreService.emailExiste(email.trim())) {
                request.setAttribute("erreur", "Un autre membre avec cet email existe déjà");
                Membre membre = new Membre();
                membre.setIdMembre(id);
                membre.setNom(nom);
                membre.setPrenom(prenom);
                membre.setEmail(email);
                membre.setTelephone(telephone);
                request.setAttribute("membre", membre);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/membre/modifier.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Mise à jour
            Membre membre = new Membre();
            membre.setIdMembre(id);
            membre.setNom(nom.trim());
            membre.setPrenom(prenom.trim());
            membre.setEmail(email.trim().toLowerCase());
            membre.setTelephone(telephone != null ? telephone.trim() : null);
            membre.setDateInscription(membreExistant.getDateInscription());
            membre.setStatut(membreExistant.isStatut());
            
            boolean succes = membreService.modifierMembre(membre);
            
            if (succes) {
                request.getSession().setAttribute("succes", "Membre modifié avec succès!");
                response.sendRedirect(request.getContextPath() + "/membres/liste");
            } else {
                handleError(request, response, "Erreur lors de la modification du membre", null);
            }
            
        } catch (NumberFormatException e) {
            handleError(request, response, "ID de membre invalide", e);
        } catch (Exception e) {
            handleError(request, response, "Erreur lors de la modification: " + e.getMessage(), e);
        }
    }

    private void supprimerMembre(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            // Vérification si le membre peut être supprimé (sans emprunts en cours)
            if (!membreService.peutSupprimerMembre(id)) {
                request.getSession().setAttribute("erreur", 
                    "Impossible de supprimer le membre: il a des emprunts en cours");
                response.sendRedirect(request.getContextPath() + "/membres/liste");
                return;
            }
            
            boolean succes = membreService.supprimerMembre(id);
            
            if (succes) {
                request.getSession().setAttribute("succes", "Membre supprimé avec succès!");
            } else {
                request.getSession().setAttribute("erreur", "Erreur lors de la suppression du membre");
            }
            
            response.sendRedirect(request.getContextPath() + "/membres/liste");
            
        } catch (NumberFormatException e) {
            handleError(request, response, "ID de membre invalide", e);
        } catch (Exception e) {
            handleError(request, response, "Erreur lors de la suppression: " + e.getMessage(), e);
        }
    }

    private void changerStatutMembre(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean nouveauStatut = Boolean.parseBoolean(request.getParameter("statut"));
            
            boolean succes = membreService.changerStatutMembre(id, nouveauStatut);
            
            if (succes) {
                String message = nouveauStatut ? "Membre activé avec succès!" : "Membre désactivé avec succès!";
                request.getSession().setAttribute("succes", message);
            } else {
                request.getSession().setAttribute("erreur", "Erreur lors du changement de statut");
            }
            
            response.sendRedirect(request.getContextPath() + "/membres/liste");
            
        } catch (NumberFormatException e) {
            handleError(request, response, "ID de membre invalide", e);
        } catch (Exception e) {
            handleError(request, response, "Erreur lors du changement de statut: " + e.getMessage(), e);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, 
                           String message, Exception e) throws ServletException, IOException {
        
        if (e != null) {
            e.printStackTrace();
        }
        
        request.setAttribute("erreurMessage", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/erreur.jsp");
        dispatcher.forward(request, response);
    }
}