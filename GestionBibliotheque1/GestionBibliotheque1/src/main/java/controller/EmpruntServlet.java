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

import model.Emprunt;
import model.Livre;
import model.Membre;
import service.EmpruntService;
import service.LivreService;
import service.MembreService;
import dao.EmpruntDAO;
import dao.LivreDAO;
import dao.MembreDAO;


@WebServlet("/emprunts/*")
public class EmpruntServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EmpruntService empruntService;
    private LivreService livreService;
    private MembreService membreService;

    @Override
    public void init() throws ServletException {
        try {
            // Initialisation correcte des DAOs
            EmpruntDAO empruntDAO = new EmpruntDAO();
            LivreDAO livreDAO = new LivreDAO();
            MembreDAO membreDAO = new MembreDAO();
            
            // Initialisation des services
            this.empruntService = new EmpruntService(empruntDAO);
            this.livreService = new LivreService(livreDAO);
            this.membreService = new MembreService(membreDAO);
            
            // Injection des dépendances supplémentaires
            this.empruntService.setLivreDAO(livreDAO);
            this.empruntService.setMembreDAO(membreDAO);
            
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'initialisation des services", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = extractAction(request);
        
        try {
            switch (action) {
                case "liste":
                    afficherListeEmprunts(request, response);
                    break;
                case "nouveau":
                    afficherFormulaireNouvelEmprunt(request, response);
                    break;
                case "retourner":
                    afficherFormulaireRetour(request, response);
                    break;
                case "historique":
                    afficherHistoriqueEmprunts(request, response);
                    break;
                default:
                    afficherListeEmprunts(request, response);
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
                case "nouveau":
                    enregistrerNouvelEmprunt(request, response);
                    break;
                case "retourner":
                    enregistrerRetour(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/emprunts/liste");
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

    private void afficherListeEmprunts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String filtre = request.getParameter("filtre");
        List<Emprunt> emprunts;
        
        if ("en-cours".equals(filtre)) {
            emprunts = empruntService.getEmpruntsEnCours();
            request.setAttribute("filtreActif", "en-cours");
        } else if ("retournes".equals(filtre)) {
            emprunts = empruntService.getEmpruntsRetournes();
            request.setAttribute("filtreActif", "retournes");
        } else if ("en-retard".equals(filtre)) {
            emprunts = empruntService.getEmpruntsEnRetard();
            request.setAttribute("filtreActif", "en-retard");
        } else {
            emprunts = empruntService.getAllEmprunts();
            request.setAttribute("filtreActif", "tous");
        }
        
        request.setAttribute("emprunts", emprunts);
        request.setAttribute("nombreEmprunts", emprunts.size());
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/emprunt/liste.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireNouvelEmprunt(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Récupérer les livres disponibles et membres actifs
            List<Livre> livresDisponibles = livreService.getLivresDisponibles();
            List<Membre> membresActifs = membreService.getMembresActifs();
            
            request.setAttribute("livresDisponibles", livresDisponibles);
            request.setAttribute("membresActifs", membresActifs);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/emprunt/nouvel-emprunt.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            handleError(request, response, "Erreur lors du chargement du formulaire: " + e.getMessage(), e);
        }
    }

    private void afficherFormulaireRetour(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Emprunt emprunt = empruntService.getEmpruntById(id);
            
            if (emprunt != null && "en cours".equals(emprunt.getStatut())) {
                request.setAttribute("emprunt", emprunt);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/emprunt/retour.jsp");
                dispatcher.forward(request, response);
            } else {
                request.getSession().setAttribute("erreur", "Emprunt non trouvé ou déjà retourné");
                response.sendRedirect(request.getContextPath() + "/emprunts/liste");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("erreur", "ID d'emprunt invalide");
            response.sendRedirect(request.getContextPath() + "/emprunts/liste");
        } catch (Exception e) {
            handleError(request, response, "Erreur lors du chargement du formulaire de retour: " + e.getMessage(), e);
        }
    }

    private void afficherHistoriqueEmprunts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String membreIdParam = request.getParameter("membreId");
            String livreIdParam = request.getParameter("livreId");
            
            List<Emprunt> historique;
            
            if (membreIdParam != null && !membreIdParam.isEmpty()) {
                int membreId = Integer.parseInt(membreIdParam);
                historique = empruntService.getHistoriqueEmpruntsParMembre(membreId);
                request.setAttribute("filtre", "membre");
            } else if (livreIdParam != null && !livreIdParam.isEmpty()) {
                int livreId = Integer.parseInt(livreIdParam);
                historique = empruntService.getHistoriqueEmpruntsParLivre(livreId);
                request.setAttribute("filtre", "livre");
            } else {
                historique = empruntService.getAllEmprunts();
                request.setAttribute("filtre", "tous");
            }
            
            request.setAttribute("historique", historique);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/emprunt/historique.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("erreur", "ID de membre ou livre invalide");
            response.sendRedirect(request.getContextPath() + "/emprunts/historique");
        } catch (Exception e) {
            handleError(request, response, "Erreur lors du chargement de l'historique: " + e.getMessage(), e);
        }
    }

    private void enregistrerNouvelEmprunt(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int idLivre = Integer.parseInt(request.getParameter("id_livre"));
            int idMembre = Integer.parseInt(request.getParameter("id_membre"));
            
            // Validation des données
            Livre livre = livreService.getLivreById(idLivre);
            Membre membre = membreService.getMembreById(idMembre);
            
            if (livre == null) {
                request.getSession().setAttribute("erreur", "Livre non trouvé");
                response.sendRedirect(request.getContextPath() + "/emprunts/nouveau");
                return;
            }
            
            if (membre == null) {
                request.getSession().setAttribute("erreur", "Membre non trouvé");
                response.sendRedirect(request.getContextPath() + "/emprunts/nouveau");
                return;
            }
            
            if (!livre.isDisponible()) {
                request.getSession().setAttribute("erreur", "Le livre n'est pas disponible");
                response.sendRedirect(request.getContextPath() + "/emprunts/nouveau");
                return;
            }
            
            if (!membre.isStatut()) {
                request.getSession().setAttribute("erreur", "Le membre n'est pas actif");
                response.sendRedirect(request.getContextPath() + "/emprunts/nouveau");
                return;
            }
            
            // Vérifier limite d'emprunts
            int nbEmpruntsEnCours = empruntService.getNombreEmpruntsEnCours(idMembre);
            if (nbEmpruntsEnCours >= 3) {
                request.getSession().setAttribute("erreur", 
                    "Le membre a atteint la limite d'emprunts (3 maximum)");
                response.sendRedirect(request.getContextPath() + "/emprunts/nouveau");
                return;
            }
            
            // Création de l'emprunt
            Emprunt emprunt = new Emprunt();
            emprunt.setIdLivre(idLivre);
            emprunt.setIdMembre(idMembre);
            emprunt.setDateEmprunt(Date.valueOf(LocalDate.now()));
            emprunt.setDateRetourPrevue(Date.valueOf(LocalDate.now().plusWeeks(2)));
            emprunt.setDateRetourEffective(null);
            emprunt.setStatut("en cours");
            
            // Enregistrement
            boolean succes = empruntService.enregistrerEmprunt(emprunt);
            
            if (succes) {
                // Mettre à jour la disponibilité du livre
                livreService.marquerLivreIndisponible(idLivre);
                
                request.getSession().setAttribute("succes", "Emprunt enregistré avec succès!");
                response.sendRedirect(request.getContextPath() + "/emprunts/liste");
            } else {
                request.getSession().setAttribute("erreur", "Erreur lors de l'enregistrement de l'emprunt");
                response.sendRedirect(request.getContextPath() + "/emprunts/nouveau");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("erreur", "ID de livre ou membre invalide");
            response.sendRedirect(request.getContextPath() + "/emprunts/nouveau");
        } catch (Exception e) {
            request.getSession().setAttribute("erreur", "Erreur lors de l'enregistrement: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/emprunts/nouveau");
        }
    }

    private void enregistrerRetour(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int idEmprunt = Integer.parseInt(request.getParameter("id_emprunt"));
            Date dateRetourEffective = Date.valueOf(LocalDate.now());
            
            Emprunt emprunt = empruntService.getEmpruntById(idEmprunt);
            
            if (emprunt == null || !"en cours".equals(emprunt.getStatut())) {
                request.getSession().setAttribute("erreur", "Emprunt non trouvé ou déjà retourné");
                response.sendRedirect(request.getContextPath() + "/emprunts/liste");
                return;
            }
            
            // Mettre à jour l'emprunt
            emprunt.setDateRetourEffective(dateRetourEffective);
            
            // Déterminer le statut final
            if (dateRetourEffective.after(emprunt.getDateRetourPrevue())) {
                emprunt.setStatut("en retard");
            } else {
                emprunt.setStatut("retourné");
            }
            
            boolean succes = empruntService.enregistrerRetour(emprunt);
            
            if (succes) {
                // Rendre le livre disponible
                livreService.marquerLivreDisponible(emprunt.getIdLivre());
                
                String message = "Retour enregistré avec succès!";
                if ("en retard".equals(emprunt.getStatut())) {
                    message += " (Livre retourné en retard)";
                }
                
                request.getSession().setAttribute("succes", message);
                response.sendRedirect(request.getContextPath() + "/emprunts/liste");
            } else {
                request.getSession().setAttribute("erreur", "Erreur lors de l'enregistrement du retour");
                response.sendRedirect(request.getContextPath() + "/emprunts/liste");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("erreur", "ID d'emprunt invalide");
            response.sendRedirect(request.getContextPath() + "/emprunts/liste");
        } catch (Exception e) {
            request.getSession().setAttribute("erreur", "Erreur lors du retour: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/emprunts/liste");
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, 
                           String message, Exception e) throws ServletException, IOException {
        
        if (e != null) {
            e.printStackTrace();
            System.err.println("Erreur EmpruntServlet: " + message);
            System.err.println("Cause: " + e.getMessage());
        }
        
        // Stocker le message d'erreur dans la session
        request.getSession().setAttribute("erreur", message);
        
        // Rediriger vers la liste des emprunts
        response.sendRedirect(request.getContextPath() + "/emprunts/liste");
    }
}