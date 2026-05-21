package dao;

import model.Emprunt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {
    
    // Enregistrer un nouvel emprunt
    public boolean enregistrerEmprunt(Emprunt emprunt) {
        String sql = "INSERT INTO Emprunt (id_livre, id_membre, date_emprunt, date_retour_prevue, date_retour_effective, statut) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, emprunt.getIdLivre());
            stmt.setInt(2, emprunt.getIdMembre());
            stmt.setDate(3, new java.sql.Date(emprunt.getDateEmprunt().getTime()));
            stmt.setDate(4, new java.sql.Date(emprunt.getDateRetourPrevue().getTime()));
            
            if (emprunt.getDateRetourEffective() != null) {
                stmt.setDate(5, new java.sql.Date(emprunt.getDateRetourEffective().getTime()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            
            stmt.setString(6, emprunt.getStatut());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Récupérer tous les emprunts
    public List<Emprunt> getAllEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre, " +
                    "CONCAT(m.prenom, ' ', m.nom) as nom_membre " +
                    "FROM Emprunt e " +
                    "JOIN Livre l ON e.id_livre = l.id_livre " +
                    "JOIN Membre m ON e.id_membre = m.id_membre " +
                    "ORDER BY e.date_emprunt DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprunt emprunt = mapResultSetToEmprunt(rs);
                emprunts.add(emprunt);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    // Récupérer les emprunts en cours
    public List<Emprunt> getEmpruntsEnCours() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre, " +
                    "CONCAT(m.prenom, ' ', m.nom) as nom_membre " +
                    "FROM Emprunt e " +
                    "JOIN Livre l ON e.id_livre = l.id_livre " +
                    "JOIN Membre m ON e.id_membre = m.id_membre " +
                    "WHERE e.statut = 'en cours' " +
                    "ORDER BY e.date_retour_prevue";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprunt emprunt = mapResultSetToEmprunt(rs);
                emprunts.add(emprunt);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    // Récupérer les emprunts en retard
    public List<Emprunt> getEmpruntsEnRetard() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre, " +
                    "CONCAT(m.prenom, ' ', m.nom) as nom_membre " +
                    "FROM Emprunt e " +
                    "JOIN Livre l ON e.id_livre = l.id_livre " +
                    "JOIN Membre m ON e.id_membre = m.id_membre " +
                    "WHERE e.statut = 'en cours' AND e.date_retour_prevue < CURDATE() " +
                    "ORDER BY e.date_retour_prevue";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprunt emprunt = mapResultSetToEmprunt(rs);
                emprunts.add(emprunt);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    // Récupérer les emprunts retournés
    public List<Emprunt> getEmpruntsRetournes() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre, " +
                    "CONCAT(m.prenom, ' ', m.nom) as nom_membre " +
                    "FROM Emprunt e " +
                    "JOIN Livre l ON e.id_livre = l.id_livre " +
                    "JOIN Membre m ON e.id_membre = m.id_membre " +
                    "WHERE e.statut IN ('retourné', 'en retard') " +
                    "ORDER BY e.date_retour_effective DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprunt emprunt = mapResultSetToEmprunt(rs);
                emprunts.add(emprunt);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    // Récupérer un emprunt par ID
    public Emprunt getEmpruntById(int id) {
        String sql = "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre, " +
                    "CONCAT(m.prenom, ' ', m.nom) as nom_membre " +
                    "FROM Emprunt e " +
                    "JOIN Livre l ON e.id_livre = l.id_livre " +
                    "JOIN Membre m ON e.id_membre = m.id_membre " +
                    "WHERE e.id_emprunt = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEmprunt(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Enregistrer le retour d'un livre
    public boolean enregistrerRetour(Emprunt emprunt) {
        String sql = "UPDATE Emprunt SET date_retour_effective = ?, statut = ? WHERE id_emprunt = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(emprunt.getDateRetourEffective().getTime()));
            stmt.setString(2, emprunt.getStatut());
            stmt.setInt(3, emprunt.getIdEmprunt());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Compter les emprunts en cours d'un membre
    public int getNombreEmpruntsEnCours(int idMembre) {
        String sql = "SELECT COUNT(*) FROM Emprunt WHERE id_membre = ? AND statut = 'en cours'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMembre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    // Vérifier si un livre est actuellement emprunté
    public boolean isLivreEmprunte(int idLivre) {
        String sql = "SELECT COUNT(*) FROM Emprunt WHERE id_livre = ? AND statut = 'en cours'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLivre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Vérifier si un membre a des emprunts en cours
    public boolean membreAEmpruntsEnCours(int idMembre) {
        String sql = "SELECT COUNT(*) FROM Emprunt WHERE id_membre = ? AND statut = 'en cours'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMembre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Méthode utilitaire pour mapper un ResultSet vers un objet Emprunt
    private Emprunt mapResultSetToEmprunt(ResultSet rs) throws SQLException {
        Emprunt emprunt = new Emprunt();
        emprunt.setIdEmprunt(rs.getInt("id_emprunt"));
        emprunt.setIdLivre(rs.getInt("id_livre"));
        emprunt.setIdMembre(rs.getInt("id_membre"));
        emprunt.setDateEmprunt(rs.getDate("date_emprunt"));
        emprunt.setDateRetourPrevue(rs.getDate("date_retour_prevue"));
        emprunt.setDateRetourEffective(rs.getDate("date_retour_effective"));
        emprunt.setStatut(rs.getString("statut"));
        emprunt.setTitreLivre(rs.getString("titre_livre"));
        emprunt.setAuteurLivre(rs.getString("auteur_livre"));
        emprunt.setNomMembre(rs.getString("nom_membre"));
        return emprunt;
    }
    
    // Récupérer l'historique des emprunts par membre
    public List<Emprunt> getHistoriqueEmpruntsParMembre(int idMembre) {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre, " +
                    "CONCAT(m.prenom, ' ', m.nom) as nom_membre " +
                    "FROM Emprunt e " +
                    "JOIN Livre l ON e.id_livre = l.id_livre " +
                    "JOIN Membre m ON e.id_membre = m.id_membre " +
                    "WHERE e.id_membre = ? " +
                    "ORDER BY e.date_emprunt DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMembre);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Emprunt emprunt = mapResultSetToEmprunt(rs);
                emprunts.add(emprunt);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    // Récupérer l'historique des emprunts par livre
    public List<Emprunt> getHistoriqueEmpruntsParLivre(int idLivre) {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre, " +
                    "CONCAT(m.prenom, ' ', m.nom) as nom_membre " +
                    "FROM Emprunt e " +
                    "JOIN Livre l ON e.id_livre = l.id_livre " +
                    "JOIN Membre m ON e.id_membre = m.id_membre " +
                    "WHERE e.id_livre = ? " +
                    "ORDER BY e.date_emprunt DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLivre);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Emprunt emprunt = mapResultSetToEmprunt(rs);
                emprunts.add(emprunt);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emprunts;
    }
}