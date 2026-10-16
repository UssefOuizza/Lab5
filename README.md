# Lab 5

![Screenshot 1](Lab5/Screenshot%201.png)
Génération automatique du schéma de la base de données (tables `salles` et `reservations` avec leurs contraintes) par Hibernate au démarrage de l'application.

---

![Screenshot 2](Lab5/Screenshot%202.png)
Insertion des salles de test dans la base de données H2 via `EntityManager.persist()` au sein d'une transaction locale.

---

![Screenshot 3](Lab5/Screenshot%203.png)
Exécution de la requête JPQL récupérant les salles disponibles sur le créneau horaire 09:00 - 11:00 en excluant celles déjà réservées.

---

![Screenshot 4](Lab5/Screenshot%204.png)
Recherche multi-critères dynamique avec l'API Criteria filtrant les salles selon le nom, une capacité minimale et l'absence de conflit de réservation.

---

![Screenshot 5](Lab5/Screenshot%205.png)
Pagination des résultats côté serveur limitant l'affichage à deux éléments par page avec calcul automatique du nombre total de pages.
