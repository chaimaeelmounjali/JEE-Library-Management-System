<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Recherche de Livres"/>
    <jsp:param name="active" value="livres"/>
</jsp:include>

<div class="container mt-4">
    <!-- En-tête -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1 class="h2 mb-1">
                <i class="bi bi-search me-2 text-primary"></i>Recherche de Livres
            </h1>
            <p class="text-muted">Trouvez rapidement les livres dans votre bibliothèque</p>
        </div>
        <a href="${pageContext.request.contextPath}/livres/liste" class="btn btn-outline-primary">
            <i class="bi bi-arrow-left me-1"></i>Retour au catalogue
        </a>
    </div>

    <!-- Formulaire de recherche -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-light">
            <h5 class="card-title mb-0">
                <i class="bi bi-search me-2"></i>Critères de recherche
            </h5>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/livres/rechercher" method="get" class="row g-3">
                <div class="col-md-8">
                    <label for="terme" class="form-label">Terme de recherche</label>
                    <input type="text" class="form-control" id="terme" name="terme" 
                           value="${termeRecherche}" placeholder="Rechercher par titre, auteur, ISBN...">
                </div>
                <div class="col-md-4">
                    <label for="statut" class="form-label">Statut</label>
                    <select class="form-select" id="statut" name="statut">
                        <option value="">Tous les statuts</option>
                        <option value="disponible" ${param.statut == 'disponible' ? 'selected' : ''}>Disponible</option>
                        <option value="emprunte" ${param.statut == 'emprunte' ? 'selected' : ''}>Emprunté</option>
                    </select>
                </div>
                <div class="col-12">
                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                        <button type="reset" class="btn btn-outline-secondary me-2">
                            <i class="bi bi-arrow-clockwise me-1"></i>Réinitialiser
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-search me-1"></i>Rechercher
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Résultats -->
    <c:if test="${not empty termeRecherche}">
        <div class="card shadow-sm">
            <div class="card-header bg-light">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">
                        <i class="bi bi-list-ul me-2"></i>Résultats de la recherche
                        <span class="badge bg-primary ms-2">${nombreResultats}</span>
                    </h5>
                    <small class="text-muted">
                        Recherche pour: "<strong>${termeRecherche}</strong>"
                    </small>
                </div>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty livres}">
                        <div class="text-center py-5">
                            <i class="bi bi-search display-1 text-muted"></i>
                            <h4 class="text-muted mt-3">Aucun résultat trouvé</h4>
                            <p class="text-muted">
                                Aucun livre ne correspond à votre recherche "<strong>${termeRecherche}</strong>".
                            </p>
                            <div class="mt-3">
                                <a href="${pageContext.request.contextPath}/livres/liste" class="btn btn-primary me-2">
                                    <i class="bi bi-eye me-1"></i>Voir tous les livres
                                </a>
                                <a href="${pageContext.request.contextPath}/livres/ajouter" class="btn btn-outline-primary">
                                    <i class="bi bi-plus-circle me-1"></i>Ajouter un livre
                                </a>
                            </div>
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
                            ${nombreResultats} livre(s) trouvé(s) pour "<strong>${termeRecherche}</strong>"
                        </small>
                        <a href="${pageContext.request.contextPath}/livres/liste" class="btn btn-outline-primary btn-sm">
                            <i class="bi bi-grid-3x3-gap me-1"></i>Voir tout le catalogue
                        </a>
                    </div>
                </div>
            </c:if>
        </div>
    </c:if>
</div>

<script>
    // Activation des tooltips
    document.addEventListener('DOMContentLoaded', function() {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl)
        })
    });
</script>

<jsp:include page="/layout/footer.jsp"/>