<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Modifier un Livre"/>
    <jsp:param name="active" value="livres"/>
</jsp:include>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-warning text-dark">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-pencil-square fs-4 me-2"></i>
                        <h4 class="card-title mb-0">Modifier le livre</h4>
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
                        <form action="${pageContext.request.contextPath}/livres/modifier" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="id" value="${livre.idLivre}">
                            
                            <div class="row g-3">
                                <div class="col-md-12">
                                    <label for="titre" class="form-label">
                                        Titre du livre <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control form-control-lg" id="titre" name="titre" 
                                           value="${livre.titre}" required>
                                    <div class="invalid-feedback">
                                        Veuillez saisir le titre du livre.
                                    </div>
                                </div>

                                <div class="col-md-12">
                                    <label for="auteur" class="form-label">
                                        Auteur <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="auteur" name="auteur" 
                                           value="${livre.auteur}" required>
                                    <div class="invalid-feedback">
                                        Veuillez saisir le nom de l'auteur.
                                    </div>
                                </div>

                                <div class="col-md-12">
                                    <label for="isbn" class="form-label">
                                        ISBN <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="isbn" name="isbn" 
                                           value="${livre.isbn}" required>
                                    <div class="invalid-feedback">
                                        Veuillez saisir le code ISBN.
                                    </div>
                                </div>

                                <div class="col-md-12">
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" id="disponible" name="disponible" 
                                               ${livre.disponible ? 'checked' : ''}>
                                        <label class="form-check-label" for="disponible">
                                            Livre disponible à l'emprunt
                                        </label>
                                    </div>
                                    <div class="form-text">
                                        Décochez cette case si le livre est endommagé ou en réparation.
                                    </div>
                                </div>
                            </div>

                            <div class="row mt-4">
                                <div class="col-12">
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <a href="${pageContext.request.contextPath}/livres/liste" class="btn btn-secondary me-md-2">
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

            <!-- Informations du livre -->
            <div class="card mt-4 border-info">
                <div class="card-header bg-info text-white">
                    <h6 class="mb-0">
                        <i class="bi bi-info-circle me-2"></i>Informations du livre
                    </h6>
                </div>
                <div class="card-body">
                    <c:if test="${not empty livre}">
                        <div class="row">
                            <div class="col-md-6">
                                <strong>ID:</strong> ${livre.idLivre}<br>
                                <strong>Date d'ajout:</strong> 
                                <fmt:formatDate value="${livre.dateAjout}" pattern="dd/MM/yyyy"/>
                            </div>
                            <div class="col-md-6">
                                <strong>Statut actuel:</strong>
                                <span class="badge ${livre.disponible ? 'bg-success' : 'bg-warning'}">
                                    ${livre.disponible ? 'Disponible' : 'Non disponible'}
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