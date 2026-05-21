<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Détails du Livre"/>
    <jsp:param name="active" value="livres"/>
</jsp:include>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-10">
            <!-- En-tête avec boutons d'action -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/livres/liste">Livres</a>
                            </li>
                            <li class="breadcrumb-item active">Détails</li>
                        </ol>
                    </nav>
                    <h1 class="h2 mb-1">Détails du livre</h1>
                </div>
                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/livres/modifier?id=${livre.idLivre}" 
                       class="btn btn-warning">
                        <i class="bi bi-pencil me-1"></i>Modifier
                    </a>
                    <a href="${pageContext.request.contextPath}/livres/liste" class="btn btn-secondary">
                        <i class="bi bi-arrow-left me-1"></i>Retour
                    </a>
                </div>
            </div>

            <c:if test="${not empty livre}">
                <div class="row g-4">
                    <!-- Informations principales -->
                    <div class="col-lg-8">
                        <div class="card shadow-sm">
                            <div class="card-header bg-primary text-white">
                                <h5 class="card-title mb-0">
                                    <i class="bi bi-info-circle me-2"></i>Informations générales
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold text-muted">Titre</label>
                                        <p class="fs-5 text-primary">${livre.titre}</p>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold text-muted">Auteur</label>
                                        <p class="fs-6">${livre.auteur}</p>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold text-muted">ISBN</label>
                                        <p><code class="fs-6">${livre.isbn}</code></p>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold text-muted">Statut</label>
                                        <p>
                                            <span class="badge ${livre.disponible ? 'bg-success' : 'bg-warning'} fs-6">
                                                <i class="bi ${livre.disponible ? 'bi-check-circle' : 'bi-bookmark'} me-1"></i>
                                                ${livre.disponible ? 'Disponible' : 'Emprunté'}
                                            </span>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold text-muted">Date d'ajout</label>
                                        <p>
                                            <fmt:formatDate value="${livre.dateAjout}" pattern="dd/MM/yyyy"/>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold text-muted">ID du livre</label>
                                        <p><span class="badge bg-secondary">#${livre.idLivre}</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Actions rapides -->
                        <div class="card mt-4 border-info">
                            <div class="card-header bg-info text-white">
                                <h6 class="mb-0">
                                    <i class="bi bi-lightning me-2"></i>Actions rapides
                                </h6>
                            </div>
                            <div class="card-body">
                                <div class="d-grid gap-2 d-md-flex">
                                    <c:if test="${livre.disponible}">
                                        <a href="${pageContext.request.contextPath}/emprunts/nouveau?livreId=${livre.idLivre}" 
                                           class="btn btn-success me-2">
                                            <i class="bi bi-bookmark-plus me-1"></i>Emprunter ce livre
                                        </a>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/livres/modifier?id=${livre.idLivre}" 
                                       class="btn btn-warning me-2">
                                        <i class="bi bi-pencil me-1"></i>Modifier les informations
                                    </a>
                                    <c:if test="${livre.disponible}">
                                        <a href="${pageContext.request.contextPath}/livres/supprimer?id=${livre.idLivre}" 
                                           class="btn btn-danger"
                                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer le livre \\'${livre.titre}\\' ?')">
                                            <i class="bi bi-trash me-1"></i>Supprimer
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Statut et métadonnées -->
                    <div class="col-lg-4">
                        <!-- Carte statut -->
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-secondary text-white">
                                <h6 class="mb-0">
                                    <i class="bi bi-graph-up me-2"></i>Statut
                                </h6>
                            </div>
                            <div class="card-body text-center">
                                <div class="mb-3">
                                    <i class="bi ${livre.disponible ? 'bi-check-circle text-success' : 'bi-bookmark text-warning'} display-4"></i>
                                </div>
                                <h5 class="${livre.disponible ? 'text-success' : 'text-warning'}">
                                    ${livre.disponible ? 'Disponible' : 'Emprunté'}
                                </h5>
                                <p class="text-muted small mb-0">
                                    <c:choose>
                                        <c:when test="${livre.disponible}">
                                            Ce livre peut être emprunté immédiatement
                                        </c:when>
                                        <c:otherwise>
                                            Ce livre est actuellement emprunté
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </div>

                        <!-- Informations techniques -->
                        <div class="card shadow-sm">
                            <div class="card-header bg-light">
                                <h6 class="mb-0">
                                    <i class="bi bi-tools me-2"></i>Informations techniques
                                </h6>
                            </div>
                            <div class="card-body">
                                <div class="mb-2">
                                    <small class="text-muted">ID:</small>
                                    <div><strong>#${livre.idLivre}</strong></div>
                                </div>
                                <div class="mb-2">
                                    <small class="text-muted">Date d'ajout:</small>
                                    <div>
                                        <strong>
                                            <fmt:formatDate value="${livre.dateAjout}" pattern="dd/MM/yyyy"/>
                                        </strong>
                                    </div>
                                </div>
                                <div class="mb-2">
                                    <small class="text-muted">ISBN:</small>
                                    <div><code>${livre.isbn}</code></div>
                                </div>
                                <hr>
                                <div class="text-center">
                                    <small class="text-muted">
                                        Dernière mise à jour: 
                                        <fmt:formatDate value="${livre.dateAjout}" pattern="dd/MM/yyyy"/>
                                    </small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty livre}">
                <div class="card shadow-sm">
                    <div class="card-body text-center py-5">
                        <i class="bi bi-exclamation-triangle display-1 text-warning"></i>
                        <h3 class="text-warning mt-3">Livre non trouvé</h3>
                        <p class="text-muted">Le livre que vous recherchez n'existe pas ou a été supprimé.</p>
                        <a href="${pageContext.request.contextPath}/livres/liste" class="btn btn-primary">
                            <i class="bi bi-arrow-left me-1"></i>Retour à la liste
                        </a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="/layout/footer.jsp"/>