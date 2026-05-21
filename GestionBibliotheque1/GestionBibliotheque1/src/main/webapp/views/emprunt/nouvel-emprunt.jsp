<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Nouvel Emprunt"/>
    <jsp:param name="active" value="emprunts"/>
</jsp:include>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-10">
            <div class="card shadow-sm">
                <div class="card-header bg-warning text-dark">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-bookmark-plus fs-4 me-2"></i>
                        <h4 class="card-title mb-0">Nouvel emprunt</h4>
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

                    <form action="${pageContext.request.contextPath}/emprunts/nouveau" method="post" class="needs-validation" novalidate>
                        <div class="row g-4">
                            <!-- Sélection du membre -->
                            <div class="col-md-6">
                                <div class="card h-100">
                                    <div class="card-header bg-primary text-white">
                                        <h6 class="mb-0">
                                            <i class="bi bi-person me-2"></i>Sélection du membre
                                        </h6>
                                    </div>
                                    <div class="card-body">
                                        <div class="mb-3">
                                            <label for="id_membre" class="form-label">
                                                Membre <span class="text-danger">*</span>
                                            </label>
                                            <select class="form-select" id="id_membre" name="id_membre" required>
                                                <option value="">Choisir un membre...</option>
                                                <c:forEach var="membre" items="${membresActifs}">
                                                    <option value="${membre.idMembre}">
                                                        ${membre.prenom} ${membre.nom} - ${membre.email}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <div class="invalid-feedback">
                                                Veuillez sélectionner un membre.
                                            </div>
                                        </div>
                                        <div id="membre-info" class="alert alert-info d-none">
                                            <small>
                                                <strong>Informations du membre:</strong><br>
                                                <span id="membre-details"></span>
                                            </small>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Sélection du livre -->
                            <div class="col-md-6">
                                <div class="card h-100">
                                    <div class="card-header bg-success text-white">
                                        <h6 class="mb-0">
                                            <i class="bi bi-book me-2"></i>Sélection du livre
                                        </h6>
                                    </div>
                                    <div class="card-body">
                                        <div class="mb-3">
                                            <label for="id_livre" class="form-label">
                                                Livre <span class="text-danger">*</span>
                                            </label>
                                            <select class="form-select" id="id_livre" name="id_livre" required>
                                                <option value="">Choisir un livre...</option>
                                                <c:forEach var="livre" items="${livresDisponibles}">
                                                    <option value="${livre.idLivre}" data-auteur="${livre.auteur}" data-isbn="${livre.isbn}">
                                                        ${livre.titre} - ${livre.auteur}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <div class="invalid-feedback">
                                                Veuillez sélectionner un livre.
                                            </div>
                                        </div>
                                        <div id="livre-info" class="alert alert-info d-none">
                                            <small>
                                                <strong>Informations du livre:</strong><br>
                                                <span id="livre-details"></span>
                                            </small>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Informations de l'emprunt -->
                        <div class="row mt-4">
                            <div class="col-12">
                                <div class="card border-info">
                                    <div class="card-header bg-info text-white">
                                        <h6 class="mb-0">
                                            <i class="bi bi-calendar me-2"></i>Informations de l'emprunt
                                        </h6>
                                    </div>
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label class="form-label">Date d'emprunt</label>
                                                <p class="form-control-plaintext">
                                                    <strong>
                                                        <script>
                                                            document.write(new Date().toLocaleDateString('fr-FR'));
                                                        </script>
                                                    </strong>
                                                </p>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Date de retour prévue</label>
                                                <p class="form-control-plaintext">
                                                    <strong>
                                                        <script>
                                                            var date = new Date();
                                                            date.setDate(date.getDate() + 14);
                                                            document.write(date.toLocaleDateString('fr-FR'));
                                                        </script>
                                                    </strong>
                                                    <br>
                                                    <small class="text-muted">(14 jours à partir d'aujourd'hui)</small>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row mt-4">
                            <div class="col-12">
                                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                    <a href="${pageContext.request.contextPath}/emprunts/liste" class="btn btn-secondary me-md-2">
                                        <i class="bi bi-arrow-left me-1"></i>Annuler
                                    </a>
                                    <button type="submit" class="btn btn-warning">
                                        <i class="bi bi-check-circle me-1"></i>Enregistrer l'emprunt
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Règles d'emprunt -->
            <div class="card mt-4 border-info">
                <div class="card-header bg-info text-white">
                    <h6 class="mb-0">
                        <i class="bi bi-info-circle me-2"></i>Règles d'emprunt
                    </h6>
                </div>
                <div class="card-body">
                    <ul class="list-unstyled mb-0">
                        <li><i class="bi bi-check text-success me-2"></i>Seuls les membres actifs peuvent emprunter</li>
                        <li><i class="bi bi-check text-success me-2"></i>Un membre peut emprunter jusqu'à 3 livres simultanément</li>
                        <li><i class="bi bi-check text-success me-2"></i>La durée d'emprunt est de 14 jours</li>
                        <li><i class="bi bi-check text-success me-2"></i>Les retards sont facturés 2 DH par jour de retard</li>
                        <li><i class="bi bi-check text-success me-2"></i>Seuls les livres disponibles peuvent être empruntés</li>
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

    // Affichage des informations du membre sélectionné
    const membreSelect = document.getElementById('id_membre');
    const membreInfo = document.getElementById('membre-info');
    const membreDetails = document.getElementById('membre-details');

    membreSelect.addEventListener('change', function() {
        if (this.value) {
            const selectedOption = this.options[this.selectedIndex];
            const membreText = selectedOption.text;
            membreDetails.textContent = membreText;
            membreInfo.classList.remove('d-none');
        } else {
            membreInfo.classList.add('d-none');
        }
    });

    // Affichage des informations du livre sélectionné
    const livreSelect = document.getElementById('id_livre');
    const livreInfo = document.getElementById('livre-info');
    const livreDetails = document.getElementById('livre-details');

    livreSelect.addEventListener('change', function() {
        if (this.value) {
            const selectedOption = this.options[this.selectedIndex];
            const titre = selectedOption.text.split(' - ')[0];
            const auteur = selectedOption.getAttribute('data-auteur');
            const isbn = selectedOption.getAttribute('data-isbn');
            
            livreDetails.innerHTML = `
                <strong>Titre:</strong> ${titre}<br>
                <strong>Auteur:</strong> ${auteur}<br>
                <strong>ISBN:</strong> ${isbn}
            `;
            livreInfo.classList.remove('d-none');
        } else {
            livreInfo.classList.add('d-none');
        }
    });
})
</script>

<jsp:include page="/layout/footer.jsp"/>