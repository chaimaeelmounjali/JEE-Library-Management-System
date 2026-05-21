<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Ajouter un Livre"/>
    <jsp:param name="active" value="livres"/>
</jsp:include>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-plus-circle fs-4 me-2"></i>
                        <h4 class="card-title mb-0">Ajouter un nouveau livre</h4>
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

                    <form action="${pageContext.request.contextPath}/livres/ajouter" method="post" class="needs-validation" novalidate>
                        <div class="row g-3">
                            <div class="col-md-12">
                                <label for="titre" class="form-label">
                                    Titre du livre <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control form-control-lg" id="titre" name="titre" 
                                       value="${param.titre}" required placeholder="Ex: Le Petit Prince">
                                <div class="invalid-feedback">
                                    Veuillez saisir le titre du livre.
                                </div>
                                <div class="form-text">
                                    Saisissez le titre complet du livre.
                                </div>
                            </div>

                            <div class="col-md-12">
                                <label for="auteur" class="form-label">
                                    Auteur <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="auteur" name="auteur" 
                                       value="${param.auteur}" required placeholder="Ex: Antoine de Saint-Exupéry">
                                <div class="invalid-feedback">
                                    Veuillez saisir le nom de l'auteur.
                                </div>
                                <div class="form-text">
                                    Saisissez le nom complet de l'auteur.
                                </div>
                            </div>

                            <div class="col-md-12">
                                <label for="isbn" class="form-label">
                                    ISBN <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="isbn" name="isbn" 
                                       value="${param.isbn}" required placeholder="Ex: 978-2-07-040000-0">
                                <div class="invalid-feedback">
                                    Veuillez saisir le code ISBN.
                                </div>
                                <div class="form-text">
                                    Saisissez le code ISBN unique du livre (10 ou 13 chiffres).
                                </div>
                            </div>
                        </div>

                        <div class="row mt-4">
                            <div class="col-12">
                                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                    <a href="${pageContext.request.contextPath}/livres/liste" class="btn btn-secondary me-md-2">
                                        <i class="bi bi-arrow-left me-1"></i>Retour à la liste
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-check-circle me-1"></i>Ajouter le livre
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
                        <li><i class="bi bi-check text-success me-2"></i>L'ISBN doit être unique dans le système</li>
                        <li><i class="bi bi-check text-success me-2"></i>Le livre sera automatiquement marqué comme disponible</li>
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