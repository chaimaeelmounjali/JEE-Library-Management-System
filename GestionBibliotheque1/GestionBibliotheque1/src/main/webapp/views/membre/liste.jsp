<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Gestion des Membres"/>
    <jsp:param name="active" value="membres"/>
</jsp:include>

<div class="container mt-4">
    <!-- En-tête avec boutons d'action -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1 class="h2 mb-1">
                <i class="bi bi-people me-2 text-success"></i>Gestion des Membres
            </h1>
            <p class="text-muted">Gérez les membres inscrits à votre bibliothèque</p>
        </div>
        <a href="${pageContext.request.contextPath}/membres/ajouter" class="btn btn-success btn-lg">
            <i class="bi bi-person-plus me-2"></i>Inscrire un membre
        </a>
    </div>

    <!-- Messages -->
    <jsp:include page="/layout/messages.jsp"/>

    <!-- Cartes de statistiques -->
    <div class="row g-3 mb-4">
        <div class="col-xl-3 col-md-6">
            <div class="card bg-success text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Total Membres</h6>
                            <h2 class="mb-0">${nombreMembres}</h2>
                        </div>
                        <i class="bi bi-people fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-primary text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Membres Actifs</h6>
                            <h2 class="mb-0">
                                <c:set var="membresActifs" value="${membres.stream().filter(m -> m.statut).count()}"/>
                                ${membresActifs}
                            </h2>
                        </div>
                        <i class="bi bi-person-check fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-secondary text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Membres Inactifs</h6>
                            <h2 class="mb-0">
                                <c:set var="membresInactifs" value="${membres.stream().filter(m -> !m.statut).count()}"/>
                                ${membresInactifs}
                            </h2>
                        </div>
                        <i class="bi bi-person-x fs-1 opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-info text-white h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-2">Nouveaux (30j)</h6>
                            <h2 class="mb-0">
                                <c:set var="nouveauxMembres" value="0"/>
                                <!-- Logique pour calculer les nouveaux membres -->
                                ${nouveauxMembres}
                            </h2>
                        </div>
                        <i class="bi bi-person-plus fs-1 opacity-50"></i>
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
                   class="btn ${filtreActif == 'tous' ? 'btn-success' : 'btn-outline-success'}">
                    <i class="bi bi-grid-3x3-gap me-1"></i>Tous
                </a>
                <a href="?filtre=actifs" 
                   class="btn ${filtreActif == 'actifs' ? 'btn-primary' : 'btn-outline-primary'}">
                    <i class="bi bi-person-check me-1"></i>Actifs
                </a>
                <a href="?filtre=inactifs" 
                   class="btn ${filtreActif == 'inactifs' ? 'btn-secondary' : 'btn-outline-secondary'}">
                    <i class="bi bi-person-x me-1"></i>Inactifs
                </a>
            </div>
        </div>
    </div>

    <!-- Tableau des membres -->
    <div class="card shadow-sm">
        <div class="card-header bg-light">
            <div class="d-flex justify-content-between align-items-center">
                <h5 class="card-title mb-0">
                    <i class="bi bi-list-ul me-2"></i>Liste des Membres
                    <span class="badge bg-success ms-2">${nombreMembres}</span>
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
                <c:when test="${empty membres}">
                    <div class="text-center py-5">
                        <i class="bi bi-people display-1 text-muted"></i>
                        <h4 class="text-muted mt-3">Aucun membre trouvé</h4>
                        <p class="text-muted mb-4">
                            <c:choose>
                                <c:when test="${filtreActif != 'tous'}">
                                    Aucun membre ne correspond au filtre sélectionné.
                                </c:when>
                                <c:otherwise>
                                    Commencez par inscrire votre premier membre.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="${pageContext.request.contextPath}/membres/ajouter" class="btn btn-success">
                            <i class="bi bi-person-plus me-2"></i>Inscrire un membre
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover table-striped mb-0">
                            <thead class="table-dark">
                                <tr>
                                    <th width="80">ID</th>
                                    <th>Membre</th>
                                    <th>Contact</th>
                                    <th width="120">Date d'inscription</th>
                                    <th width="100">Ancienneté</th>
                                    <th width="100">Statut</th>
                                    <th width="180" class="text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="membre" items="${membres}">
                                    <tr>
                                        <td>
                                            <span class="badge bg-secondary">#${membre.idMembre}</span>
                                        </td>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <div class="avatar bg-success text-white rounded-circle me-3" 
                                                     style="width: 40px; height: 40px; display: flex; align-items: center; justify-content: center;">
                                                    ${membre.prenom.charAt(0)}${membre.nom.charAt(0)}
                                                </div>
                                                <div>
                                                    <strong class="text-success">${membre.prenom} ${membre.nom}</strong>
                                                    <br>
                                                    <small class="text-muted">ID: ${membre.idMembre}</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div>
                                                <i class="bi bi-envelope me-1 text-muted"></i>
                                                ${membre.email}
                                            </div>
                                            <small class="text-muted">
                                                <i class="bi bi-telephone me-1"></i>
                                                ${membre.telephone != null ? membre.telephone : 'Non renseigné'}
                                            </small>
                                        </td>
                                        <td>
                                            <small class="text-muted">
                                                <fmt:formatDate value="${membre.dateInscription}" pattern="dd/MM/yyyy"/>
                                            </small>
                                        </td>
                                        <td>
                                            <c:set var="anciennete" value="${membre.ancienneteMois}"/>
                                            <span class="badge ${anciennete < 3 ? 'bg-info' : 'bg-secondary'}">
                                                ${anciennete} mois
                                            </span>
                                        </td>
                                        <td>
                                            <span class="badge ${membre.statut ? 'bg-primary' : 'bg-secondary'}">
                                                <i class="bi ${membre.statut ? 'bi-person-check' : 'bi-person-x'} me-1"></i>
                                                ${membre.statut ? 'Actif' : 'Inactif'}
                                            </span>
                                        </td>
                                        <td>
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a href="${pageContext.request.contextPath}/membres/modifier?id=${membre.idMembre}" 
                                                   class="btn btn-outline-primary" 
                                                   data-bs-toggle="tooltip" title="Modifier">
                                                    <i class="bi bi-pencil"></i>
                                                </a>
                                                <c:if test="${membre.statut}">
                                                    <a href="${pageContext.request.contextPath}/membres/changer-statut?id=${membre.idMembre}&statut=false" 
                                                       class="btn btn-outline-warning" 
                                                       data-bs-toggle="tooltip" title="Désactiver"
                                                       onclick="return confirm('Êtes-vous sûr de vouloir désactiver le membre ${membre.prenom} ${membre.nom} ?')">
                                                        <i class="bi bi-person-dash"></i>
                                                    </a>
                                                </c:if>
                                                <c:if test="${!membre.statut}">
                                                    <a href="${pageContext.request.contextPath}/membres/changer-statut?id=${membre.idMembre}&statut=true" 
                                                       class="btn btn-outline-success" 
                                                       data-bs-toggle="tooltip" title="Activer"
                                                       onclick="return confirm('Êtes-vous sûr de vouloir activer le membre ${membre.prenom} ${membre.nom} ?')">
                                                        <i class="bi bi-person-check"></i>
                                                    </a>
                                                </c:if>
                                                <a href="${pageContext.request.contextPath}/membres/supprimer?id=${membre.idMembre}" 
                                                   class="btn btn-outline-danger" 
                                                   data-bs-toggle="tooltip" title="Supprimer"
                                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer définitivement le membre ${membre.prenom} ${membre.nom} ?')">
                                                    <i class="bi bi-trash"></i>
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
        <c:if test="${not empty membres}">
            <div class="card-footer bg-light">
                <div class="d-flex justify-content-between align-items-center">
                    <small class="text-muted">
                        Affichage de <strong>${nombreMembres}</strong> membre(s)
                        <c:if test="${filtreActif != 'tous'}">
                            (filtre: ${filtreActif})
                        </c:if>
                    </small>
                    <div>
                        <a href="${pageContext.request.contextPath}/membres/ajouter" class="btn btn-success btn-sm">
                            <i class="bi bi-person-plus me-1"></i>Nouveau membre
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