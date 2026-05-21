package dao;

import model.Membre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {
    
    // Inscrire un membre
    public boolean inscrireMembre(Membre membre) {
        String sql = "INSERT INTO Membre (nom, prenom, email, telephone, date_inscription, statut) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setString(4, membre.getTelephone());
            stmt.setDate(5, new java.sql.Date(membre.getDateInscription().getTime()));
            stmt.setBoolean(6, membre.isStatut());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Récupérer tous les membres
    public List<Membre> getAllMembres() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM Membre ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Membre membre = new Membre();
                membre.setIdMembre(rs.getInt("id_membre"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setTelephone(rs.getString("telephone"));
                membre.setDateInscription(rs.getDate("date_inscription"));
                membre.setStatut(rs.getBoolean("statut"));
                membres.add(membre);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return membres;
    }
    
    // Récupérer les membres actifs
    public List<Membre> getMembresActifs() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM Membre WHERE statut = true ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Membre membre = new Membre();
                membre.setIdMembre(rs.getInt("id_membre"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setTelephone(rs.getString("telephone"));
                membre.setDateInscription(rs.getDate("date_inscription"));
                membre.setStatut(rs.getBoolean("statut"));
                membres.add(membre);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return membres;
    }
    
    // Récupérer les membres inactifs
    public List<Membre> getMembresInactifs() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM Membre WHERE statut = false ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Membre membre = new Membre();
                membre.setIdMembre(rs.getInt("id_membre"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setTelephone(rs.getString("telephone"));
                membre.setDateInscription(rs.getDate("date_inscription"));
                membre.setStatut(rs.getBoolean("statut"));
                membres.add(membre);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return membres;
    }
    
    // Récupérer un membre par ID
    public Membre getMembreById(int id) {
        String sql = "SELECT * FROM Membre WHERE id_membre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Membre membre = new Membre();
                membre.setIdMembre(rs.getInt("id_membre"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setTelephone(rs.getString("telephone"));
                membre.setDateInscription(rs.getDate("date_inscription"));
                membre.setStatut(rs.getBoolean("statut"));
                return membre;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Modifier un membre
    public boolean modifierMembre(Membre membre) {
        String sql = "UPDATE Membre SET nom = ?, prenom = ?, email = ?, telephone = ?, statut = ? WHERE id_membre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setString(4, membre.getTelephone());
            stmt.setBoolean(5, membre.isStatut());
            stmt.setInt(6, membre.getIdMembre());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Supprimer un membre
    public boolean supprimerMembre(int id) {
        String sql = "DELETE FROM Membre WHERE id_membre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Vérifier si un email existe
    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM Membre WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Changer le statut d'un membre
    public boolean changerStatutMembre(int id, boolean statut) {
        String sql = "UPDATE Membre SET statut = ? WHERE id_membre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, statut);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}