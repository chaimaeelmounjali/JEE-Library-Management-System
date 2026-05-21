package dao;

import model.Livre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    
    // Ajouter un livre
    public boolean ajouterLivre(Livre livre) {
        String sql = "INSERT INTO Livre (titre, auteur, isbn, disponible, date_ajout) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getIsbn());
            stmt.setBoolean(4, livre.isDisponible());
            stmt.setDate(5, new java.sql.Date(livre.getDateAjout().getTime()));
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Récupérer tous les livres
    public List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livre ORDER BY titre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Livre livre = mapResultSetToLivre(rs);
                livres.add(livre);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return livres;
    }
    
    // Récupérer les livres disponibles
    public List<Livre> getLivresDisponibles() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livre WHERE disponible = true ORDER BY titre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Livre livre = mapResultSetToLivre(rs);
                livres.add(livre);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return livres;
    }
    
    // Récupérer les livres indisponibles
    public List<Livre> getLivresIndisponibles() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livre WHERE disponible = false ORDER BY titre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Livre livre = mapResultSetToLivre(rs);
                livres.add(livre);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return livres;
    }
    
    // Récupérer un livre par ID
    public Livre getLivreById(int id) {
        String sql = "SELECT * FROM Livre WHERE id_livre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLivre(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Modifier un livre
    public boolean modifierLivre(Livre livre) {
        String sql = "UPDATE Livre SET titre = ?, auteur = ?, isbn = ?, disponible = ? WHERE id_livre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getIsbn());
            stmt.setBoolean(4, livre.isDisponible());
            stmt.setInt(5, livre.getIdLivre());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Supprimer un livre
    public boolean supprimerLivre(int id) {
        String sql = "DELETE FROM Livre WHERE id_livre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Vérifier si un ISBN existe
    public boolean isbnExiste(String isbn) {
        String sql = "SELECT COUNT(*) FROM Livre WHERE isbn = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Vérifier si un ISBN existe pour un autre livre
    public boolean isbnExistePourAutreLivre(String isbn, int idLivre) {
        String sql = "SELECT COUNT(*) FROM Livre WHERE isbn = ? AND id_livre != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            stmt.setInt(2, idLivre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Marquer un livre comme disponible
    public boolean marquerLivreDisponible(int id) {
        String sql = "UPDATE Livre SET disponible = true WHERE id_livre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Marquer un livre comme indisponible
    public boolean marquerLivreIndisponible(int id) {
        String sql = "UPDATE Livre SET disponible = false WHERE id_livre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Vérifier si un livre est emprunté (en utilisant la table Emprunt)
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
    
    // Méthode utilitaire pour mapper un ResultSet vers un objet Livre
    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        Livre livre = new Livre();
        livre.setIdLivre(rs.getInt("id_livre"));
        livre.setTitre(rs.getString("titre"));
        livre.setAuteur(rs.getString("auteur"));
        livre.setIsbn(rs.getString("isbn"));
        livre.setDisponible(rs.getBoolean("disponible"));
        livre.setDateAjout(rs.getDate("date_ajout"));
        return livre;
    }
}