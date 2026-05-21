<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion Bibliothèque - ${param.title != null ? param.title : 'Accueil'}</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body class="d-flex flex-column min-vh-100">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
                <i class="bi bi-book-half me-2"></i>Gestion Bibliothèque
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link ${param.active == 'livres' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/livres/liste">
                            <i class="bi bi-book me-1"></i>Livres
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.active == 'membres' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/membres/liste">
                            <i class="bi bi-people me-1"></i>Membres
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.active == 'emprunts' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/emprunts/liste">
                            <i class="bi bi-arrow-left-right me-1"></i>Emprunts
                        </a>
                    </li>
                </ul>
                <div class="navbar-nav">
                    <span class="navbar-text text-light">
                        <i class="bi bi-calendar3 me-1"></i>
                        <script>document.write(new Date().toLocaleDateString('fr-FR'))</script>
                    </span>
                </div>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <main class="flex-grow-1">