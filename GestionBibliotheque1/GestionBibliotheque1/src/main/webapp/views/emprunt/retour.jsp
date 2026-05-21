<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Retour d'Emprunt"/>
    <jsp:param name="active" value="emprunts"/>
</jsp:include>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-arrow-return-left fs-4 me-2"></i>
                        <h4 class="card-title mb-0">Enregistrer un retour</h4>
                    </div>
                </div>
                <div class="card-body">
                    <c:if test="${not empty emprunt}">
                        <!-- Résumé de l'emprunt -->
                        <div class="alert alert-info">
                            <h6 class="alert-heading">
                                <i class="bi bi-info-circle me-2"></i>Résumé de l'emprunt
                            </h6>
                            <div class="row mt-3">
                                <div class="col-md-6">
                                    <strong>Livre:</strong> ${emprunt.titreLivre}<br>
                                    <strong>Auteur:</strong> ${emprunt.auteurLivre}<br>
                                    <strong>ISBN:</strong> ${emprunt.isbn}
                                </div>
                                <div class="col-md-6">
                                    <strong>Membre:</strong> ${emprunt.nomMembre}<br>
                                    <strong>Date emprunt:</strong> 
                                    <fmt:formatDate value="${emprunt.dateEmprunt}" pattern="dd/MM/yyyy"/><br>
                                    <strong>Retour prévu:</strong> 
                                    <fmt:formatDate value="${emprunt.dateRetourPrevue}" pattern="dd/MM/yyyy"/>
                                </div>
                            </div>
                        </div>

                        <!-- Détails du retour -->
                        <div class="card border-warning mb-4">
                            <div class="card-header bg-warning text-dark">
                                <h6 class="mb-0">
                                    <i class="bi bi-exclamation-triangle me-2"></i>Détails du retour
                                </h6>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="form-label">Date de retour effective</label>
                                        <p class="form-control-plaintext">
                                            <strong>
                                                <script>
                                                    document.write(new Date().toLocaleDateString('fr-FR'));
                                                </script>
                                            </strong>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <c:set var="joursRetard" value="${emprunt.joursRetard}"/>
                                        <c:set var="amende" value="${emprunt.calculerAmende()}"/>
                                        
                                        <label class="form-label">Statut du retour</label>
                                        <c:choose>
                                            <c:when test="${joursRetard > 0}">
                                                <p class="text-danger fw-bold">
                                                    <i class="bi bi-exclamation-triangle me-1"></i>
                                                    Retard de ${joursRetard} jour(s)
                                                </p>
                                                <p class="text-danger">
                                                    Amende à payer: <strong>${amende} DH</strong>
                                                </p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="text-success fw-bold">
                                                    <i class="bi bi-check-circle me-1"></i>
                                                    Retour dans les délais
                                                </p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Formulaire de retour -->
                        <form action="${pageContext.request.contextPath}/emprunts/retourner" method="post">
                            <input type="hidden" name="id_emprunt" value="${emprunt.idEmprunt}">
                            
                            <div class="mb-3">
                                <label for="notes" class="form-label">Notes (optionnel)</label>
                                <textarea class="form-control" id="notes" name="notes" rows="3" 
                                          placeholder="Éventuelles observations sur l'état du livre..."></textarea>
                            </div>

                            <div class="alert alert-warning">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="confirmation" required>
                                    <label class="form-check-label" for="confirmation">
                                        Je confirme que le livre "<strong>${emprunt.titreLivre}</strong>" 
                                        a été retourné par "<strong>${emprunt.nomMembre}</strong>"
                                    </label>
                                    <div class="invalid-feedback">
                                        Vous devez confirmer le retour du livre.
                                    </div>
                                </div>
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${pageContext.request.contextPath}/emprunts/liste" class="btn btn-secondary me-md-2">
                                    <i class="bi bi-arrow-left me-1"></i>Annuler
                                </a>
                                <button type="submit" class="btn btn-success">
                                    <i class="bi bi-check-circle me-1"></i>Confirmer le retour
                                </button>
                            </div>
                        </form>
                    </c:if>

                    <c:if test="${empty emprunt}">
                        <div class="alert alert-danger text-center">
                            <i class="bi bi-exclamation-triangle display-4"></i>
                            <h4 class="mt-3">Emprunt non trouvé</h4>
                            <p>L'emprunt que vous essayez de retourner n'existe pas ou a déjà été retourné.</p>
                            <a href="${pageContext.request.contextPath}/emprunts/liste" class="btn btn-primary">
                                <i class="bi bi-arrow-left me-1"></i>Retour à la liste
                            </a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Validation de confirmation
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const confirmation = document.getElementById('confirmation');
    
    form.addEventListener('submit', function(event) {
        if (!confirmation.checked) {
            event.preventDefault();
            confirmation.classList.add('is-invalid');
        } else {
            confirmation.classList.remove('is-invalid');
        }
    });
    
    confirmation.addEventListener('change', function() {
        if (this.checked) {
            this.classList.remove('is-invalid');
        }
    });
});
</script>

<jsp:include page="/layout/footer.jsp"/>