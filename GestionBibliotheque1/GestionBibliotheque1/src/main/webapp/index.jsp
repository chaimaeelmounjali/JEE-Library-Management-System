<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Tableau de Bord"/>
</jsp:include>

<div class="container mt-4">
    <!-- En-tête principal -->
    <div class="row align-items-center mb-5">
        <div class="col-lg-8">
            <h1 class="display-4 fw-bold text-primary mb-3">
                <i class="bi bi-book-half me-3"></i>Gestion Bibliothèque
            </h1>
            <p class="lead text-muted mb-4">
                Système complet de gestion de bibliothèque moderne et efficace. 
                Gérez votre catalogue de livres, vos membres et les emprunts en toute simplicité.
            </p>
            <div class="d-flex gap-3 flex-wrap">
                <span class="badge bg-success fs-6 p-3">
                    <i class="bi bi-check-circle me-2"></i>Interface intuitive
                </span>
                <span class="badge bg-info fs-6 p-3">
                    <i class="bi bi-lightning me-2"></i>Rapide et efficace
                </span>
                <span class="badge bg-warning fs-6 p-3">
                    <i class="bi bi-shield-check me-2"></i>Sécurisé
                </span>
            </div>
        </div>
        <div class="col-lg-4 text-center">
            <div class="card border-0 bg-light">
                <div class="card-body p-4">
                    <i class="bi bi-book text-primary display-1"></i>
                    <h5 class="card-title mt-3">Bienvenue</h5>
                    <p class="card-text text-muted">
                        <script>
                            document.write(new Date().toLocaleDateString('fr-FR', { 
                                weekday: 'long', 
                                year: 'numeric', 
                                month: 'long', 
                                day: 'numeric' 
                            }));
                        </script>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <!-- Messages -->
    <jsp:include page="/layout/messages.jsp"/>

    <!-- Cartes de fonctionnalités principales -->
    <div class="row g-4 mb-5">
        <div class="col-xl-4 col-md-6">
            <div class="card text-white bg-primary h-100 shadow-hover">
                <div class="card-body">
                    <div class="d-flex align-items-center mb-3">
                        <i class="bi bi-book display-6 me-3"></i>
                        <div>
                            <h3 class="card-title h2 mb-1">Livres</h3>
                            <p class="card-text opacity-75">Gestion du catalogue</p>
                        </div>
                    </div>
                    <p class="card-text mb-4">
                        Gérez l'ensemble de votre collection de livres : ajout, modification, 
                        consultation et recherche avancée.
                    </p>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/livres/liste" class="btn btn-light btn-lg">
                            <i class="bi bi-arrow-right me-2"></i>Accéder aux livres
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xl-4 col-md-6">
            <div class="card text-white bg-success h-100 shadow-hover">
                <div class="card-body">
                    <div class="d-flex align-items-center mb-3">
                        <i class="bi bi-people display-6 me-3"></i>
                        <div>
                            <h3 class="card-title h2 mb-1">Membres</h3>
                            <p class="card-text opacity-75">Gestion des adhérents</p>
                        </div>
                    </div>
                    <p class="card-text mb-4">
                        Inscrivez et gérez vos membres, suivez leurs emprunts et 
                        maintenez leurs informations à jour.
                    </p>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/membres/liste" class="btn btn-light btn-lg">
                            <i class="bi bi-arrow-right me-2"></i>Gérer les membres
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xl-4 col-md-6">
            <div class="card text-white bg-warning h-100 shadow-hover">
                <div class="card-body">
                    <div class="d-flex align-items-center mb-3">
                        <i class="bi bi-arrow-left-right display-6 me-3"></i>
                        <div>
                            <h3 class="card-title h2 mb-1">Emprunts</h3>
                            <p class="card-text opacity-75">Circulation des livres</p>
                        </div>
                    </div>
                    <p class="card-text mb-4">
                        Suivez les emprunts en cours, gérez les retours et consultez 
                        l'historique complet des transactions.
                    </p>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/emprunts/liste" class="btn btn-light btn-lg">
                            <i class="bi bi-arrow-right me-2"></i>Voir les emprunts
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Statistiques rapides -->
    <div class="row g-4 mb-5">
        <div class="col-12">
            <div class="card shadow-sm">
                <div class="card-header bg-light">
                    <h5 class="card-title mb-0">
                        <i class="bi bi-graph-up me-2"></i>Aperçu rapide
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row text-center">
                        <div class="col-md-3">
                            <div class="border-end py-3">
                                <h3 class="text-primary fw-bold" id="stats-livres">-</h3>
                                <p class="text-muted mb-0">Livres au catalogue</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="border-end py-3">
                                <h3 class="text-success fw-bold" id="stats-membres">-</h3>
                                <p class="text-muted mb-0">Membres actifs</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="border-end py-3">
                                <h3 class="text-warning fw-bold" id="stats-emprunts">-</h3>
                                <p class="text-muted mb-0">Emprunts en cours</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="py-3">
                                <h3 class="text-danger fw-bold" id="stats-retards">-</h3>
                                <p class="text-muted mb-0">Retards</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Actions rapides -->
    <div class="row g-4">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-light">
                    <h5 class="card-title mb-0">
                        <i class="bi bi-lightning me-2"></i>Actions rapides
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/livres/ajouter" 
                               class="btn btn-outline-primary w-100 h-100 py-3">
                                <div class="d-flex align-items-center justify-content-center">
                                    <i class="bi bi-plus-circle display-6 me-3"></i>
                                    <div class="text-start">
                                        <h6 class="mb-1">Ajouter un livre</h6>
                                        <small class="text-muted">Nouveau au catalogue</small>
                                    </div>
                                </div>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/membres/ajouter" 
                               class="btn btn-outline-success w-100 h-100 py-3">
                                <div class="d-flex align-items-center justify-content-center">
                                    <i class="bi bi-person-plus display-6 me-3"></i>
                                    <div class="text-start">
                                        <h6 class="mb-1">Inscrire un membre</h6>
                                        <small class="text-muted">Nouvel adhérent</small>
                                    </div>
                                </div>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/emprunts/nouveau" 
                               class="btn btn-outline-warning w-100 h-100 py-3">
                                <div class="d-flex align-items-center justify-content-center">
                                    <i class="bi bi-bookmark-plus display-6 me-3"></i>
                                    <div class="text-start">
                                        <h6 class="mb-1">Nouvel emprunt</h6>
                                        <small class="text-muted">Prêter un livre</small>
                                    </div>
                                </div>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/emprunts/liste?filtre=en-retard" 
                               class="btn btn-outline-danger w-100 h-100 py-3">
                                <div class="d-flex align-items-center justify-content-center">
                                    <i class="bi bi-exclamation-triangle display-6 me-3"></i>
                                    <div class="text-start">
                                        <h6 class="mb-1">Voir les retards</h6>
                                        <small class="text-muted">Emprunts en retard</small>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="card shadow-sm">
                <div class="card-header bg-light">
                    <h5 class="card-title mb-0">
                        <i class="bi bi-clock me-2"></i>Activité récente
                    </h5>
                </div>
                <div class="card-body">
                    <div class="list-group list-group-flush">
                        <div class="list-group-item d-flex align-items-center">
                            <i class="bi bi-book text-primary me-3"></i>
                            <div class="flex-grow-1">
                                <small class="text-muted">Aujourd'hui</small>
                                <div>Nouveaux livres ajoutés</div>
                            </div>
                        </div>
                        <div class="list-group-item d-flex align-items-center">
                            <i class="bi bi-arrow-left-right text-warning me-3"></i>
                            <div class="flex-grow-1">
                                <small class="text-muted">Hier</small>
                                <div>Emprunts enregistrés</div>
                            </div>
                        </div>
                        <div class="list-group-item d-flex align-items-center">
                            <i class="bi bi-person-plus text-success me-3"></i>
                            <div class="flex-grow-1">
                                <small class="text-muted">Cette semaine</small>
                                <div>Nouveaux membres inscrits</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
.shadow-hover {
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.shadow-hover:hover {
    transform: translateY(-5px);
    box-shadow: 0 1rem 3rem rgba(0, 0, 0, 0.175) !important;
}

.card {
    transition: transform 0.2s ease;
}

.card:hover {
    transform: translateY(-2px);
}
</style>

<script>
// Simulation de données pour les statistiques
document.addEventListener('DOMContentLoaded', function() {
    // Dans une application réelle, ces données viendraient d'une API
    setTimeout(() => {
        document.getElementById('stats-livres').textContent = '156';
        document.getElementById('stats-membres').textContent = '89';
        document.getElementById('stats-emprunts').textContent = '23';
        document.getElementById('stats-retards').textContent = '3';
    }, 1000);
});
</script>

<jsp:include page="/layout/footer.jsp"/>