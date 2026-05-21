package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_bibliotheque";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456789";
    
    // Chargement du driver MySQL
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }
    
    // Constructeur privé
    private DatabaseConnection() {}
    
    /**
     * Retourne une nouvelle connexion à la base de données
     */
    public static Connection getConnection() {
        try {
            // Configuration des propriétés de connexion
            Properties properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", "UTF-8");
            properties.setProperty("serverTimezone", "UTC");
            properties.setProperty("useSSL", "false");
            properties.setProperty("allowPublicKeyRetrieval", "true");
            
            // Optimisations de performance
            properties.setProperty("cachePrepStmts", "true");
            properties.setProperty("prepStmtCacheSize", "250");
            properties.setProperty("prepStmtCacheSqlLimit", "2048");
            
            Connection connection = DriverManager.getConnection(URL, properties);
            System.out.println("✅ Connexion à la base de données établie avec succès");
            return connection;
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données: " + e.getMessage());
            throw new RuntimeException("Erreur de connexion à la base de données: " + e.getMessage(), e);
        }
    }
    
    /**
     * Ferme une connexion proprement
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("✅ Connexion à la base de données fermée");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test la connexion et retourne true si elle est opérationnelle
     */
    public static boolean testConnection() {
        try (Connection testConn = getConnection()) {
            return testConn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}