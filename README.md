<div align="center">

# 🚒 SafetyNet Alerts

**API REST de coordination pour les services de secours**

[![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![JUnit5](https://img.shields.io/badge/JUnit-5-25A162?style=flat-square&logo=junit5&logoColor=white)](https://junit.org/junit5/)

[![Tests](https://img.shields.io/badge/tests-76%2F76%20passing-brightgreen?style=flat-square)](#tests--qualité)
[![Coverage](https://img.shields.io/badge/coverage-91%25-brightgreen?style=flat-square)](#tests--qualité)
</div>

<br/>

## 📋 Aperçu

SafetyNet Alerts est une API REST construite avec **Spring Boot** qui fournit aux services de secours des informations critiques en temps réel : habitants couverts par une caserne, antécédents médicaux, et coordonnées de contact.

La particularité du projet : **aucune base de données relationnelle**. L'ensemble des données est lu et écrit dans un fichier JSON unique, ce qui a orienté toute la conception de la couche d'accès aux données.

<br/>

## ✨ Points clés

| | |
|---|---|
| 🏛️ **Architecture en couches** | Controller → Service → Repository → Model/DTO, respect des principes SOLID |
| 💾 **Persistance JSON** | Lecture et écriture directe dans un fichier, sans BDD |
| 🛡️ **Gestion d'erreurs centralisée** | `@RestControllerAdvice` pour des réponses HTTP cohérentes en toute situation |
| 🧪 **76 tests unitaires** | Couches isolées avec Mockito, 91% de couverture JaCoCo |
| 📝 **Logging structuré** | Log4j2, niveaux différenciés par package |

<br/>

## 🔌 Endpoints

### Lecture

| Méthode | Route | Description |
|---|---|---|
| `GET` | `/firestation?stationNumber=` | Personnes couvertes par une caserne, décompte adultes/enfants |
| `GET` | `/childAlert?address=` | Enfants à une adresse et membres du foyer |
| `GET` | `/phoneAlert?firestation=` | Téléphones des foyers desservis par une caserne |
| `GET` | `/fire?address=` | Résidents d'une adresse, caserne associée, dossier médical |
| `GET` | `/flood/stations?stations=` | Foyers groupés par adresse, pour plusieurs casernes |
| `GET` | `/personInfo?lastName=` | Détails d'une personne par nom de famille |
| `GET` | `/communityEmail?city=` | Emails de tous les habitants d'une ville |

### Écriture

| Ressource | Méthodes |
|---|---|
| `/person` | `POST` `PUT` `DELETE` |
| `/firestation` | `POST` `PUT` `DELETE` |
| `/medicalRecord` | `POST` `PUT` `DELETE` |

<br/>

## 🏗️ Architecture

```
src/main/java/com/safetynet/
├── controller/      → endpoints REST
├── service/         → logique métier
├── repository/      → accès aux données en mémoire
├── model/           → entités
├── dto/             → objets de réponse sur mesure
├── config/          → chargement/sauvegarde du fichier JSON
└── util/            → utilitaires (calcul d'âge, etc.)
```

**Flux de lecture**
```
Controller → Service → Repository → liste en mémoire
```

**Flux d'écriture**
```
Controller → Service → Repository → JsonDataLoader.saveData() → data.json
```

Le fichier est chargé une fois au démarrage (`@PostConstruct`) dans un objet partagé entre toutes les couches. Toute modification d'une liste se répercute automatiquement sur cet objet, qui est ensuite réécrit dans le fichier après chaque opération.

<br/>

## 🛡️ Gestion des erreurs

Une classe `GlobalExceptionHandler` centralise la gestion des erreurs pour tous les controllers :

| Code | Cas | Exception |
|---|---|---|
| `404` | Route inexistante | `NoResourceFoundException` |
| `400` | Paramètre obligatoire manquant | `MissingServletRequestParameterException` |
| `500` | Erreur non anticipée | `Exception` |

<br/>

## 🧪 Tests & qualité

| Métrique | Résultat |
|---|---|
| Tests unitaires | **76 / 76** (100%) |
| Couverture des instructions | **91%** |
| Couverture des branches | **71%** |

Chaque couche est testée isolément grâce à Mockito :

```
RepositoryTest   → mock de JsonDataLoader
ServiceTest      → mock de Repository + JsonDataLoader
ControllerTest   → mock de Service (@MockitoBean) + MockMvc
```

```bash
mvn clean verify
```

Génère les rapports :
- `target/site/jacoco/index.html` — couverture de code
- `target/reports/surefire.html` — résultats des tests

<br/>

## 🚀 Installation

```bash
git clone https://github.com/djerbiano/SafetyNet_Api_SpringBoot.git
cd SafetyNet_Api_SpringBoot
mvn spring-boot:run
```

L'API est disponible sur `http://localhost:8080`.

<br/>

## 🛠️ Stack technique

**Backend** — Java 21 · Spring Boot 4 · Maven · Jackson · Log4j2 · Lombok

**Tests** — JUnit 5 · Mockito · MockMvc · JaCoCo · Surefire

<br/>

