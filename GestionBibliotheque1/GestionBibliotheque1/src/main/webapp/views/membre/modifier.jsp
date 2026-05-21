<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Modifier un Membre"/>
    <jsp:param name="active" value="membres"/>
</jsp:include>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-warning text-dark">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-pencil-square fs-4 me-2"></i>
                        <h4 class="card-title mb-0">Modifier le membre</h4>
                    </div>
                </div>
                <div class="card-body">
                    <!-- Messages d'erreur -->
                    <c:if test="${not empty erreur}">
                        <div class="alert alert-danger d-flex align-items-center">
                            <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
                            <div>${erreur}</div>
                        </div>
                    </c:if>

                    <c:if test="${not empty membre}">
                        <form action="${pageContext.request.contextPath}/membres/modifier" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="id" value="${membre.idMembre}">
                            
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="prenom" class="form-label">
                                        Prénom <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="prenom" name="prenom" 
                                           value="${membre.prenom}" required>
                                    <div class="invalid-feedback">
                                        Veuillez saisir le prénom du membre.
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label for="nom" class="form-label">
                                        Nom <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="nom" name="nom" 
                                           value="${membre.nom}" required>
                                    <div class="invalid-feedback">
                                        Veuillez saisir le nom du membre.
                                    </div>
                                </div>

                                <div class="col-md-12">
                                    <label for="email" class="form-label">
                                        Email <span class="text-danger">*</span>
                                    </label>
                                    <input type="email" class="form-control" id="email" name="email" 
                                           value="${membre.email}" required>
                                    <div class="invalid-feedback">
                                        Veuillez saisir une adresse email valide.
                                    </div>
                                </div>

                                <div class="col-md-12">
                                    <label for="telephone" class="form-label">Téléphone</label>
                                    <input type="tel" class="form-control" id="telephone" name="telephone" 
                                           value="${membre.telephone}" placeholder="Ex: 06 12 34 56 78">
                                </div>
                            </div>

                            <div class="row mt-4">
                                <div class="col-12">
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <a href="${pageContext.request.contextPath}/membres/liste" class="btn btn-secondary me-md-2">
                                            <i class="bi bi-arrow-left me-1"></i>Annuler
                                        </a>
                                        <button type="submit" class="btn btn-warning">
                                            <i class="bi bi-check-circle me-1"></i>Mettre à jour
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>

            <!-- Informations du membre -->
            <div class="card mt-4 border-info">
                <div class="card-header bg-info text-white">
                    <h6 class="mb-0">
                        <i class="bi bi-info-circle me-2"></i>Informations du membre
                    </h6>
                </div>
                <div class="card-body">
                    <c:if test="${not empty membre}">
                        <div class="row">
                            <div class="col-md-6">
                                <strong>ID:</strong> ${membre.idMembre}<br>
                                <strong>Date d'inscription:</strong> 
                                <fmt:formatDate value="${membre.dateInscription}" pattern="dd/MM/yyyy"/><br>
                                <strong>Ancienneté:</strong> ${membre.ancienneteMois} mois
                            </div>
                            <div class="col-md-6">
                                <strong>Statut actuel:</strong>
                                <span class="badge ${membre.statut ? 'bg-primary' : 'bg-secondary'}">
                                    ${membre.statut ? 'Actif' : 'Inactif'}
                                </span><br>
                                <strong>Peut emprunter:</strong>
                                <span class="badge ${membre.statut ? 'bg-success' : 'bg-secondary'}">
                                    ${membre.statut ? 'Oui' : 'Non'}
                                </span>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Validation Bootstrap
document.addEventListener('DOMContentLoaded', function() {
    'use strict'
    
    var forms = document.querySelectorAll('.needs-validation')
    
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            }
            
            form.classList.add('was-validated')
        }, false)
    })
})
</script>

<jsp:include page="/layout/footer.jsp"/>