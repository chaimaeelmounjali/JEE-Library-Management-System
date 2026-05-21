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

import model.Livre;
import service.LivreService;
import dao.LivreDAO;
import dao.DatabaseConnection;



@WebServlet("/livres/*")
public class LivreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LivreService livreService;

    @Override
    public void init() throws ServletException {
    	LivreDAO livreDAO = new LivreDAO();
        this.livreService = new LivreService(livreDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = extractAction(request);
        
        try {
            switch (action) {
                case "liste":
                    afficherListeLivres(request, response);
                    break;
                case "ajouter":
                    afficherFormulaireAjout(request, response);
                    break;
                case "modifier":
                    afficherFormulaireModification(request, response);
                    break;
                case "supprimer":
                    supprimerLivre(request, response);
                    break;
                case "consulter":
                    consulterLivre(request, response);
                    break;
                case "rechercher":
                    rechercherLivres(request, response);
                    break;
                default:
                    afficherListeLivres(request, response);
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
                    ajouterLivre(request, response);
                    break;
                case "modifier":
                    modifierLivre(request, response);
                    break;
                case "rechercher":
                    rechercherLivres(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/livres/liste");
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

    private void afficherListeLivres(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String filtre = request.getParameter("filtre");
        List<Livre> livres;
        
        if ("disponibles".equals(filtre)) {
            livres = livreService.getLivresDisponibles();
            request.setAttribute("filtreActif", "disponibles");
        } else if ("indisponibles".equals(filtre)) {
            livres = livreService.getLivresIndisponibles();
            request.setAttribute("filtreActif", "indisponibles");
        } else {
            livres = livreService.getAllLivres();
            request.setAttribute("filtreActif", "tous");
        }
        
        request.setAttribute("livres", livres);
        request.setAttribute("nombreLivres", livres.size());
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/liste.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/ajouter.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireModification(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Livre livre = livreService.getLivreById(id);
            
            if (livre != null) {
                request.setAttribute("livre", livre);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/modifier.jsp");
                dispatcher.forward(request, response);
            } else {
                request.getSession().setAttribute("erreur", "Livre non trouvé avec l'ID: " + id);
                response.sendRedirect(request.getContextPath() + "/livres/liste");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("erreur", "ID de livre invalide");
            response.sendRedirect(request.getContextPath() + "/livres/liste");
        }
    }

    private void consulterLivre(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Livre livre = livreService.getLivreById(id);
            
            if (livre != null) {
                request.setAttribute("livre", livre);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/consulter.jsp");
                dispatcher.forward(request, response);
            } else {
                request.getSession().setAttribute("erreur", "Livre non trouvé avec l'ID: " + id);
                response.sendRedirect(request.getContextPath() + "/livres/liste");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("erreur", "ID de livre invalide");
            response.sendRedirect(request.getContextPath() + "/livres/liste");
        }
    }

    private void rechercherLivres(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String terme = request.getParameter("terme");
            
            if (terme != null && !terme.trim().isEmpty()) {
                List<Livre> livres = livreService.rechercherLivres(terme.trim());
                request.setAttribute("livres", livres);
                request.setAttribute("termeRecherche", terme.trim());
                request.setAttribute("nombreResultats", livres.size());
                request.setAttribute("filtreActif", "recherche");
            } else {
                // Si pas de terme, afficher tous les livres
                afficherListeLivres(request, response);
                return;
            }
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/liste.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            handleError(request, response, "Erreur lors de la recherche: " + e.getMessage(), e);
        }
    }

    private void ajouterLivre(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Récupération des paramètres
            String titre = request.getParameter("titre");
            String auteur = request.getParameter("auteur");
            String isbn = request.getParameter("isbn");
            
            // Validation des champs obligatoires
            if (titre == null || titre.trim().isEmpty() || 
                auteur == null || auteur.trim().isEmpty() || 
                isbn == null || isbn.trim().isEmpty()) {
                
                request.setAttribute("erreur", "Tous les champs obligatoires doivent être remplis");
                request.setAttribute("titre", titre);
                request.setAttribute("auteur", auteur);
                request.setAttribute("isbn", isbn);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/ajouter.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Création du livre
            Livre livre = new Livre();
            livre.setTitre(titre.trim());
            livre.setAuteur(auteur.trim());
            livre.setIsbn(isbn.trim());
            livre.setDisponible(true);
            livre.setDateAjout(Date.valueOf(LocalDate.now()));
            
            // Validation via le service
            if (!livreService.validerLivre(livre)) {
                request.setAttribute("erreur", "Données du livre invalides");
                request.setAttribute("titre", titre);
                request.setAttribute("auteur", auteur);
                request.setAttribute("isbn", isbn);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/ajouter.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Sauvegarde du livre
            boolean succes = livreService.ajouterLivre(livre);
            
            if (succes) {
                request.getSession().setAttribute("succes", "Livre ajouté avec succès!");
                response.sendRedirect(request.getContextPath() + "/livres/liste");
            } else {
                request.setAttribute("erreur", "Erreur lors de l'ajout du livre - ISBN peut-être déjà existant");
                request.setAttribute("titre", titre);
                request.setAttribute("auteur", auteur);
                request.setAttribute("isbn", isbn);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/ajouter.jsp");
                dispatcher.forward(request, response);
            }
            
        } catch (Exception e) {
            handleError(request, response, "Erreur lors de l'ajout du livre: " + e.getMessage(), e);
        }
    }

    private void modifierLivre(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String titre = request.getParameter("titre");
            String auteur = request.getParameter("auteur");
            String isbn = request.getParameter("isbn");
            boolean disponible = "on".equals(request.getParameter("disponible"));
            
            // Validation
            if (titre == null || titre.trim().isEmpty() || 
                auteur == null || auteur.trim().isEmpty() || 
                isbn == null || isbn.trim().isEmpty()) {
                
                request.setAttribute("erreur", "Tous les champs obligatoires doivent être remplis");
                Livre livre = new Livre();
                livre.setIdLivre(id);
                livre.setTitre(titre);
                livre.setAuteur(auteur);
                livre.setIsbn(isbn);
                livre.setDisponible(disponible);
                request.setAttribute("livre", livre);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/modifier.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Vérification que le livre existe
            Livre livreExistant = livreService.getLivreById(id);
            if (livreExistant == null) {
                request.getSession().setAttribute("erreur", "Livre non trouvé");
                response.sendRedirect(request.getContextPath() + "/livres/liste");
                return;
            }
            
            // Mise à jour du livre
            Livre livre = new Livre();
            livre.setIdLivre(id);
            livre.setTitre(titre.trim());
            livre.setAuteur(auteur.trim());
            livre.setIsbn(isbn.trim());
            livre.setDisponible(disponible);
            livre.setDateAjout(livreExistant.getDateAjout());
            
            // Validation via le service
            if (!livreService.validerLivre(livre)) {
                request.setAttribute("erreur", "Données du livre invalides");
                request.setAttribute("livre", livre);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/modifier.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Sauvegarde
            boolean succes = livreService.modifierLivre(livre);
            
            if (succes) {
                request.getSession().setAttribute("succes", "Livre modifié avec succès!");
                response.sendRedirect(request.getContextPath() + "/livres/liste");
            } else {
                request.setAttribute("erreur", "Erreur lors de la modification - ISBN peut-être déjà utilisé par un autre livre");
                request.setAttribute("livre", livre);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livre/modifier.jsp");
                dispatcher.forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("erreur", "ID de livre invalide");
            response.sendRedirect(request.getContextPath() + "/livres/liste");
        } catch (Exception e) {
            handleError(request, response, "Erreur lors de la modification du livre: " + e.getMessage(), e);
        }
    }

    private void supprimerLivre(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            // Vérification si le livre peut être supprimé
            if (!livreService.peutSupprimerLivre(id)) {
                request.getSession().setAttribute("erreur", "Impossible de supprimer le livre: il est actuellement emprunté ou non disponible");
                response.sendRedirect(request.getContextPath() + "/livres/liste");
                return;
            }
            
            boolean succes = livreService.supprimerLivre(id);
            
            if (succes) {
                request.getSession().setAttribute("succes", "Livre supprimé avec succès!");
            } else {
                request.getSession().setAttribute("erreur", "Erreur lors de la suppression du livre");
            }
            
            response.sendRedirect(request.getContextPath() + "/livres/liste");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("erreur", "ID de livre invalide");
            response.sendRedirect(request.getContextPath() + "/livres/liste");
        } catch (Exception e) {
            handleError(request, response, "Erreur lors de la suppression du livre: " + e.getMessage(), e);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, 
                           String message, Exception e) throws ServletException, IOException {
        
        if (e != null) {
            e.printStackTrace();
            System.err.println("Erreur LivreServlet: " + message);
        }
        
        request.getSession().setAttribute("erreur", message);
        response.sendRedirect(request.getContextPath() + "/livres/liste");
    }
}