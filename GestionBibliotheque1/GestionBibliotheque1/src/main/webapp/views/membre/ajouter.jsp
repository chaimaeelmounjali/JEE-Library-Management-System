<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Inscrire un Membre"/>
    <jsp:param name="active" value="membres"/>
</jsp:include>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-person-plus fs-4 me-2"></i>
                        <h4 class="card-title mb-0">Inscrire un nouveau membre</h4>
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

                    <form action="${pageContext.request.contextPath}/membres/ajouter" method="post" class="needs-validation" novalidate>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="prenom" class="form-label">
                                    Prénom <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="prenom" name="prenom" 
                                       value="${param.prenom}" required placeholder="Ex: Jean">
                                <div class="invalid-feedback">
                                    Veuillez saisir le prénom du membre.
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label for="nom" class="form-label">
                                    Nom <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="nom" name="nom" 
                                       value="${param.nom}" required placeholder="Ex: Dupont">
                                <div class="invalid-feedback">
                                    Veuillez saisir le nom du membre.
                                </div>
                            </div>

                            <div class="col-md-12">
                                <label for="email" class="form-label">
                                    Email <span class="text-danger">*</span>
                                </label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="${param.email}" required placeholder="Ex: jean.dupont@email.com">
                                <div class="invalid-feedback">
                                    Veuillez saisir une adresse email valide.
                                </div>
                                <div class="form-text">
                                    L'email doit être unique pour chaque membre.
                                </div>
                            </div>

                            <div class="col-md-12">
                                <label for="telephone" class="form-label">Téléphone</label>
                                <input type="tel" class="form-control" id="telephone" name="telephone" 
                                       value="${param.telephone}" placeholder="Ex: 06 12 34 56 78">
                                <div class="form-text">
                                    Numéro de téléphone facultatif.
                                </div>
                            </div>
                        </div>

                        <div class="row mt-4">
                            <div class="col-12">
                                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                    <a href="${pageContext.request.contextPath}/membres/liste" class="btn btn-secondary me-md-2">
                                        <i class="bi bi-arrow-left me-1"></i>Retour à la liste
                                    </a>
                                    <button type="submit" class="btn btn-success">
                                        <i class="bi bi-check-circle me-1"></i>Inscrire le membre
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Aide -->
            <div class="card mt-4 border-info">
                <div class="card-header bg-info text-white">
                    <h6 class="mb-0">
                        <i class="bi bi-info-circle me-2"></i>Informations importantes
                    </h6>
                </div>
                <div class="card-body">
                    <ul class="list-unstyled mb-0">
                        <li><i class="bi bi-check text-success me-2"></i>Tous les champs marqués d'un <span class="text-danger">*</span> sont obligatoires</li>
                        <li><i class="bi bi-check text-success me-2"></i>L'email doit être unique dans le système</li>
                        <li><i class="bi bi-check text-success me-2"></i>Le membre sera automatiquement activé après inscription</li>
                        <li><i class="bi bi-check text-success me-2"></i>Les membres actifs peuvent emprunter jusqu'à 3 livres</li>
                    </ul>
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