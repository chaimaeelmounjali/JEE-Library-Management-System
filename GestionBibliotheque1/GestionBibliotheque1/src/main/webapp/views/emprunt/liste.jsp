<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Gestion des Emprunts"/>
    <jsp:param name="active" value="emprunts"/>
</jsp:include>

<div class="container mt-4">
    <!-- En-tête avec boutons d'action -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1 class="h2 mb-1">
                <i class="bi bi-arrow-left-right me-2 text-warning"></i>Gestion des Emprunts
            </h1>
            <p class="text-muted">Suivez et gérez les emprunts en cours et l'historique</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/emprunts/historique" class="btn btn-info me-2">
                <i class="bi bi-clock-history me-1"></i>Historique
            </a>
            <a href="${pageContext.request.contextPath}/emprunts/nouveau" class="btn btn-warning">
                <i class="bi bi-bookmark-plus me-1"></i>Nouvel emprunt
            </a>
        </div>
    </div>

    <!-- Messages -->
    <jsp:include page="/layout/messages.jsp"/>

    <!-- Cartes de statistiques -->
    <div class="row g-3 mb-4">
        <div class="col-xl-3 col-md-6">
            <div class="card bg-warning text-dark h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Total Emprunts</h6>
                            <h2 class="mb-0">${nombreEmprunts}</h2>
                        </div>
                        <i class="bi bi-arrow-left-right fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-primary text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">En Cours</h6>
                            <h2 class="mb-0">
                                <c:set var="empruntsEnCours" value="${emprunts.stream().filter(e -> e.estEnCours()).count()}"/>
                                ${empruntsEnCours}
                            </h2>
                        </div>
                        <i class="bi bi-bookmark fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-danger text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">En Retard</h6>
                            <h2 class="mb-0">
                                <c:set var="empruntsEnRetard" value="${emprunts.stream().filter(e -> e.estEnRetard() || e.estEnRetardAutomatique()).count()}"/>
                                ${empruntsEnRetard}
                            </h2>
                        </div>
                        <i class="bi bi-exclamation-triangle fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-success text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Retournés</h6>
                            <h2 class="mb-0">
                                <c:set var="empruntsRetournes" value="${emprunts.stream().filter(e -> e.estRetourne()).count()}"/>
                                ${empruntsRetournes}
                            </h2>
                        </div>
                        <i class="bi bi-check-circle fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Filtres -->
    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <div class="btn-group" role="group">
                <a href="?filtre=tous" 
                   class="btn ${filtreActif == 'tous' ? 'btn-warning' : 'btn-outline-warning'}">
                    <i class="bi bi-grid-3x3-gap me-1"></i>Tous
                </a>
                <a href="?filtre=en-cours" 
                   class="btn ${filtreActif == 'en-cours' ? 'btn-primary' : 'btn-outline-primary'}">
                    <i class="bi bi-bookmark me-1"></i>En cours
                </a>
                <a href="?filtre=en-retard" 
                   class="btn ${filtreActif == 'en-retard' ? 'btn-danger' : 'btn-outline-danger'}">
                    <i class="bi bi-exclamation-triangle me-1"></i>En retard
                </a>
                <a href="?filtre=retournes" 
                   class="btn ${filtreActif == 'retournes' ? 'btn-success' : 'btn-outline-success'}">
                    <i class="bi bi-check-circle me-1"></i>Retournés
                </a>
            </div>
        </div>
    </div>

    <!-- Tableau des emprunts -->
    <div class="card shadow-sm">
        <div class="card-header bg-light">
            <div class="d-flex justify-content-between align-items-center">
                <h5 class="card-title mb-0">
                    <i class="bi bi-list-ul me-2"></i>Liste des Emprunts
                    <span class="badge bg-warning ms-2">${nombreEmprunts}</span>
                </h5>
                <div class="text-muted small">
                    <c:if test="${filtreActif != 'tous'}">
                        Filtre: <strong>${filtreActif}</strong>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty emprunts}">
                    <div class="text-center py-5">
                        <i class="bi bi-arrow-left-right display-1 text-muted"></i>
                        <h4 class="text-muted mt-3">Aucun emprunt trouvé</h4>
                        <p class="text-muted mb-4">
                            <c:choose>
                                <c:when test="${filtreActif != 'tous'}">
                                    Aucun emprunt ne correspond au filtre sélectionné.
                                </c:when>
                                <c:otherwise>
                                    Commencez par créer votre premier emprunt.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="${pageContext.request.contextPath}/emprunts/nouveau" class="btn btn-warning">
                            <i class="bi bi-bookmark-plus me-2"></i>Nouvel emprunt
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover table-striped mb-0">
                            <thead class="table-dark">
                                <tr>
                                    <th width="80">ID</th>
                                    <th>Livre</th>
                                    <th>Membre</th>
                                    <th width="120">Date emprunt</th>
                                    <th width="120">Retour prévu</th>
                                    <th width="120">Retour effectif</th>
                                    <th width="120">Statut</th>
                                    <th width="150" class="text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="emprunt" items="${emprunts}">
                                    <tr class="${emprunt.estEnRetardAutomatique() ? 'table-warning' : ''}">
                                        <td>
                                            <span class="badge bg-secondary">#${emprunt.idEmprunt}</span>
                                        </td>
                                        <td>
                                            <div>
                                                <strong class="text-primary">${emprunt.titreLivre}</strong>
                                                <br>
                                                <small class="text-muted">${emprunt.auteurLivre}</small>
                                            </div>
                                        </td>
                                        <td>
                                            <div>
                                                <strong class="text-success">${emprunt.nomMembre}</strong>
                                                <br>
                                                <small class="text-muted">ID: ${emprunt.idMembre}</small>
                                            </div>
                                        </td>
                                        <td>
                                            <small class="text-muted">
                                                <fmt:formatDate value="${emprunt.dateEmprunt}" pattern="dd/MM/yyyy"/>
                                            </small>
                                        </td>
                                        <td>
                                            <small class="${emprunt.estEnRetardAutomatique() ? 'text-danger fw-bold' : 'text-muted'}">
                                                <fmt:formatDate value="${emprunt.dateRetourPrevue}" pattern="dd/MM/yyyy"/>
                                                <c:if test="${emprunt.estEnRetardAutomatique()}">
                                                    <br>
                                                    <span class="badge bg-danger">
                                                        +${emprunt.joursRetard} jour(s)
                                                    </span>
                                                </c:if>
                                            </small>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${emprunt.dateRetourEffective != null}">
                                                    <small class="text-muted">
                                                        <fmt:formatDate value="${emprunt.dateRetourEffective}" pattern="dd/MM/yyyy"/>
                                                    </small>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">-</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <span class="${emprunt.cssClassStatut}">
                                                ${emprunt.statutDisplay}
                                            </span>
                                            <c:if test="${emprunt.estEnRetardAutomatique()}">
                                                <br>
                                                <small class="text-danger">
                                                    Amende: ${emprunt.calculerAmende()} DH
                                                </small>
                                            </c:if>
                                        </td>
                                        <td>
                                            <div class="btn-group btn-group-sm" role="group">
                                                <c:if test="${emprunt.peutEtreRetourne()}">
                                                    <a href="${pageContext.request.contextPath}/emprunts/retourner?id=${emprunt.idEmprunt}" 
                                                       class="btn btn-outline-success" 
                                                       data-bs-toggle="tooltip" title="Enregistrer retour">
                                                        <i class="bi bi-arrow-return-left"></i>
                                                    </a>
                                                </c:if>
                                                <a href="${pageContext.request.contextPath}/emprunts/historique?membreId=${emprunt.idMembre}" 
                                                   class="btn btn-outline-info" 
                                                   data-bs-toggle="tooltip" title="Historique membre">
                                                    <i class="bi bi-person-lines-fill"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/emprunts/historique?livreId=${emprunt.idLivre}" 
                                                   class="btn btn-outline-secondary" 
                                                   data-bs-toggle="tooltip" title="Historique livre">
                                                    <i class="bi bi-book"></i>
                                                </a>
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
        <c:if test="${not empty emprunts}">
            <div class="card-footer bg-light">
                <div class="d-flex justify-content-between align-items-center">
                    <small class="text-muted">
                        Affichage de <strong>${nombreEmprunts}</strong> emprunt(s)
                        <c:if test="${filtreActif != 'tous'}">
                            (filtre: ${filtreActif})
                        </c:if>
                    </small>
                    <div>
                        <a href="${pageContext.request.contextPath}/emprunts/nouveau" class="btn btn-warning btn-sm">
                            <i class="bi bi-bookmark-plus me-1"></i>Nouvel emprunt
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