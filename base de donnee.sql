-- Création de la base de données
CREATE DATABASE IF NOT EXISTS gestion_bibliotheque;
USE gestion_bibliotheque;

-- Table des Membres
CREATE TABLE Membre (
    id_membre INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    date_inscription DATE NOT NULL,
    statut BOOLEAN DEFAULT TRUE
);

-- Table des Livres
CREATE TABLE Livre (
    id_livre INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    date_ajout DATE NOT NULL
);

-- Table des Emprunts
CREATE TABLE Emprunt (
    id_emprunt INT AUTO_INCREMENT PRIMARY KEY,
    id_livre INT NOT NULL,
    id_membre INT NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE NULL,
    statut ENUM('en cours', 'retourné', 'en retard') DEFAULT 'en cours',
    FOREIGN KEY (id_livre) REFERENCES Livre(id_livre) ON DELETE CASCADE,
    FOREIGN KEY (id_membre) REFERENCES Membre(id_membre) ON DELETE CASCADE
);

-- Index pour améliorer les performances
CREATE INDEX idx_emprunt_livre ON Emprunt(id_livre);
CREATE INDEX idx_emprunt_membre ON Emprunt(id_membre);
CREATE INDEX idx_emprunt_statut ON Emprunt(statut);
CREATE INDEX idx_livre_disponible ON Livre(disponible);
CREATE INDEX idx_membre_statut ON Membre(statut);

-- Insertion de données d'exemple
INSERT INTO Membre (nom, prenom, email, telephone, date_inscription, statut) VALUES
('Dupont', 'Jean', 'jean.dupont@email.com', '0123456789', '2024-01-15', TRUE),
('Martin', 'Marie', 'marie.martin@email.com', '0234567890', '2024-02-20', TRUE),
('Bernard', 'Pierre', 'pierre.bernard@email.com', '0345678901', '2024-03-10', FALSE);

INSERT INTO Livre (titre, auteur, isbn, disponible, date_ajout) VALUES
('Le Petit Prince', 'Antoine de Saint-Exupéry', '978-2-07-040000-0', TRUE, '2024-01-01'),
('1984', 'George Orwell', '978-2-07-040001-7', TRUE, '2024-01-02'),
('L''Étranger', 'Albert Camus', '978-2-07-040002-4', TRUE, '2024-01-03');

INSERT INTO Emprunt (id_livre, id_membre, date_emprunt, date_retour_prevue, date_retour_effective, statut) VALUES
(1, 1, '2024-10-01', '2024-10-15', NULL, 'en cours'),
(2, 2, '2024-10-05', '2024-10-19', '2024-10-18', 'retourné');