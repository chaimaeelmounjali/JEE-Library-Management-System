/**
 * 
 */
// Scripts personnalisés pour l'application de gestion de bibliothèque

document.addEventListener('DOMContentLoaded', function() {
    // Initialisation des tooltips Bootstrap
    initTooltips();
    
    // Initialisation des popovers Bootstrap
    initPopovers();
    
    // Gestion des confirmations de suppression
    initDeleteConfirmations();
    
    // Auto-dismiss des alertes après 5 secondes
    autoDismissAlerts();
    
    // Amélioration des formulaires
    enhanceForms();
});

function initTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

function initPopovers() {
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    const popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

function initDeleteConfirmations() {
    const deleteButtons = document.querySelectorAll('a[onclick*="confirm"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const message = this.getAttribute('data-confirm-message') || 
                           "Êtes-vous sûr de vouloir effectuer cette action ?";
            
            if (!confirm(message)) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    });
}

function autoDismissAlerts() {
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
}

function enhanceForms() {
    // Ajout de classes pour la validation en temps réel
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateField(this);
            });
            
            input.addEventListener('input', function() {
                if (this.classList.contains('is-invalid')) {
                    validateField(this);
                }
            });
        });
    });
}

function validateField(field) {
    if (field.checkValidity()) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
    } else {
        field.classList.remove('is-valid');
        field.classList.add('is-invalid');
    }
}

// Fonctions utilitaires
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('fr-FR', options);
}

function showLoading(button) {
    const originalText = button.innerHTML;
    button.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Chargement...';
    button.disabled = true;
    
    return function() {
        button.innerHTML = originalText;
        button.disabled = false;
    };
}

// Gestion des modales
function showModal(modalId) {
    const modal = new bootstrap.Modal(document.getElementById(modalId));
    modal.show();
}

// Recherche en temps réel (si implémentée)
function initLiveSearch() {
    const searchInput = document.getElementById('live-search');
    if (searchInput) {
        let timeout = null;
        
        searchInput.addEventListener('input', function() {
            clearTimeout(timeout);
            timeout = setTimeout(() => {
                performLiveSearch(this.value);
            }, 300);
        });
    }
}

function performLiveSearch(query) {
    // Implémentation de la recherche en temps réel
    console.log('Recherche en temps réel:', query);
}

// Export des fonctions pour une utilisation globale
window.App = {
    formatDate,
    showLoading,
    showModal,
    initLiveSearch
};