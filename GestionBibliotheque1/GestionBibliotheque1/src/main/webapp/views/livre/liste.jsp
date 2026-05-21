<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Gestion des Livres"/>
    <jsp:param name="active" value="livres"/>
</jsp:include>

<div class="container mt-4">
    <!-- En-tête avec boutons d'action -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1 class="h2 mb-1">
                <i class="bi bi-book me-2 text-primary"></i>Gestion des Livres
            </h1>
            <p class="text-muted">Gérez le catalogue complet de votre bibliothèque</p>
        </div>
        <a href="${pageContext.request.contextPath}/livres/ajouter" class="btn btn-primary btn-lg">
            <i class="bi bi-plus-circle me-2"></i>Ajouter un livre
        </a>
    </div>

    <!-- Messages -->
    <jsp:include page="/layout/messages.jsp"/>

    <!-- Cartes de statistiques -->
    <div class="row g-3 mb-4">
        <div class="col-xl-3 col-md-6">
            <div class="card bg-primary text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Total Livres</h6>
                            <h2 class="mb-0">${nombreLivres}</h2>
                        </div>
                        <i class="bi bi-book fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-success text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Disponibles</h6>
                            <h2 class="mb-0">
                                <c:set var="disponibles" value="${livres.stream().filter(l -> l.disponible).count()}"/>
                                ${disponibles}
                            </h2>
                        </div>
                        <i class="bi bi-check-circle fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-warning text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Empruntés</h6>
                            <h2 class="mb-0">
                                <c:set var="empruntes" value="${livres.stream().filter(l -> !l.disponible).count()}"/>
                                ${empruntes}
                            </h2>
                        </div>
                        <i class="bi bi-bookmark fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-info text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Nouveaux (7j)</h6>
                            <h2 class="mb-0">
                                <c:set var="nouveaux" value="0"/>
                                <!-- Logique pour calculer les nouveaux livres -->
                                ${nouveaux}
                            </h2>
                        </div>
                        <i class="bi bi-star fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Filtres et recherche -->
    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <div class="row g-3 align-items-center">
                <div class="col-md-6">
                    <div class="btn-group" role="group">
                        <a href="?filtre=tous" 
                           class="btn ${filtreActif == 'tous' ? 'btn-primary' : 'btn-outline-primary'}">
                            <i class="bi bi-grid-3x3-gap me-1"></i>Tous
                        </a>
                        <a href="?filtre=disponibles" 
                           class="btn ${filtreActif == 'disponibles' ? 'btn-success' : 'btn-outline-success'}">
                            <i class="bi bi-check-circle me-1"></i>Disponibles
                        </a>
                        <a href="?filtre=indisponibles" 
                           class="btn ${filtreActif == 'indisponibles' ? 'btn-warning' : 'btn-outline-warning'}">
                            <i class="bi bi-bookmark me-1"></i>Empruntés
                        </a>
                    </div>
                </div>
                <div class="col-md-6">
                    <form action="${pageContext.request.contextPath}/livres/rechercher" method="get" class="d-flex">
                        <div class="input-group">
                            <input type="text" name="terme" class="form-control" 
                                   placeholder="Rechercher un livre par titre, auteur ou ISBN..." 
                                   value="${termeRecherche}">
                            <button type="submit" class="btn btn-outline-secondary">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Tableau des livres -->
    <div class="card shadow-sm">
        <div class="card-header bg-light">
            <div class="d-flex justify-content-between align-items-center">
                <h5 class="card-title mb-0">
                    <i class="bi bi-list-ul me-2"></i>Catalogue des Livres
                    <span class="badge bg-primary ms-2">${nombreLivres}</span>
                </h5>
                <div class="text-muted small">
                    <c:if test="${not empty termeRecherche}">
                        Résultats pour: "<strong>${termeRecherche}</strong>"
                    </c:if>
                </div>
            </div>
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty livres}">
                    <div class="text-center py-5">
                        <i class="bi bi-book display-1 text-muted"></i>
                        <h4 class="text-muted mt-3">Aucun livre trouvé</h4>
                        <p class="text-muted mb-4">
                            <c:choose>
                                <c:when test="${not empty termeRecherche}">
                                    Aucun résultat pour votre recherche.
                                </c:when>
                                <c:otherwise>
                                    Commencez par ajouter votre premier livre à la bibliothèque.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="${pageContext.request.contextPath}/livres/ajouter" class="btn btn-primary">
                            <i class="bi bi-plus-circle me-2"></i>Ajouter un livre
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover table-striped mb-0">
                            <thead class="table-dark">
                                <tr>
                                    <th width="80">ID</th>
                                    <th>Titre</th>
                                    <th>Auteur</th>
                                    <th>ISBN</th>
                                    <th width="120">Statut</th>
                                    <th width="120">Date d'ajout</th>
                                    <th width="150" class="text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="livre" items="${livres}">
                                    <tr>
                                        <td>
                                            <span class="badge bg-secondary">#${livre.idLivre}</span>
                                        </td>
                                        <td>
                                            <div>
                                                <strong class="text-primary">${livre.titre}</strong>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="text-dark">${livre.auteur}</span>
                                        </td>
                                        <td>
                                            <code class="text-muted">${livre.isbn}</code>
                                        </td>
                                        <td>
                                            <span class="badge ${livre.disponible ? 'bg-success' : 'bg-warning'}">
                                                <i class="bi ${livre.disponible ? 'bi-check-circle' : 'bi-bookmark'} me-1"></i>
                                                ${livre.disponible ? 'Disponible' : 'Emprunté'}
                                            </span>
                                        </td>
                                        <td>
                                            <small class="text-muted">
                                                <fmt:formatDate value="${livre.dateAjout}" pattern="dd/MM/yyyy"/>
                                            </small>
                                        </td>
                                        <td>
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a href="${pageContext.request.contextPath}/livres/consulter?id=${livre.idLivre}" 
                                                   class="btn btn-outline-info" 
                                                   data-bs-toggle="tooltip" title="Consulter">
                                                    <i class="bi bi-eye"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/livres/modifier?id=${livre.idLivre}" 
                                                   class="btn btn-outline-primary" 
                                                   data-bs-toggle="tooltip" title="Modifier">
                                                    <i class="bi bi-pencil"></i>
                                                </a>
                                                <c:if test="${livre.disponible}">
                                                    <a href="${pageContext.request.contextPath}/livres/supprimer?id=${livre.idLivre}" 
                                                       class="btn btn-outline-danger" 
                                                       data-bs-toggle="tooltip" title="Supprimer"
                                                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer le livre \\'${livre.titre}\\' ?')">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <c:if test="${not empty livres}">
            <div class="card-footer bg-light">
                <div class="d-flex justify-content-between align-items-center">
                    <small class="text-muted">
                        Affichage de <strong>${nombreLivres}</strong> livre(s)
                    </small>
                    <div>
                        <a href="${pageContext.request.contextPath}/livres/ajouter" class="btn btn-primary btn-sm">
                            <i class="bi bi-plus-circle me-1"></i>Nouveau livre
                        </a>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script>
    // Activation des tooltips Bootstrap
    document.addEventListener('DOMContentLoaded', function() {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl)
        })
    });
</script>

<jsp:include page="/layout/footer.jsp"/>