<img src="itemss/readmee.jpg" alt="App Banner" width="100%">


# Movie Discovery App

A modern, native Android application for browsing and discovering movies using Kotlin and Jetpack Compose.  
The application fetches movie data from The Movie Database (TMDB) API and provides an elegant, user-friendly interface for exploring popular movies, viewing details, and searching for titles.

---

## Team Members
| Name       |
|------------|
| Sama Ahmed Mohamed Abdelkareem |
| Mirna mahmoud mohamed mahmoud |
| Mouhab Mamdouh Zakaria Saleh |
| Abdelrahman Ahmed ElSayed ElQazaz |
| Lina Ashraf Sediq Hamzawy |
| Mariam Magdy Soliman Youssef |

---

## Features
- Browse a grid of popular movies fetched from TMDB API.
- View detailed information about each movie (title, synopsis, rating, release date).
- Search for movies by title.
- Modern UI/UX built with Material Design Components and Jetpack Compose.
- Secure API key storage in `gradle.properties`.
- Unit tests for core business logic.

---

## Technologies Used
- Kotlin
- Jetpack Compose
- Retrofit (Networking)
- Coil (Image Loading)
- Navigation Component
- Material Design 3
- Git & GitHub
- Unit Testing (JUnit)

---

## Project Plan

| Week  | Tasks | Deliverables |
|-------|-------|--------------|
| Week 1 | Project setup, GitHub repo creation, UI/UX wireframes, static UI for home screen | Project initialized, wireframes, static grid UI |
| Week 2 | API integration with Retrofit, dynamic movie list display | Networking layer, dynamic grid of movies |
| Week 3 | Navigation to details, movie detail screen, unit testing | Navigation working, detail screen ready, tests passing |
| Week 4 | Search functionality, security improvements, UI polish, documentation | Search feature, secure API key storage, final polish |

---
## 👥 Team Tasks Distribution

| Name                                | Member | Phase                | Responsibilities |
|-------------------------------------|--------|----------------------|------------------|
| Mouhab Mamdouh Zakaria              | 1      | Project Setup        | - Create Android project & configure Gradle.<br>- Setup package structure (data, domain, presentation).<br>- Configure Navigation component. |
| Sama Ahmed Mohamed Abdelkareem      | 2      | UI (Home Screen)     | - Build Home screen UI with Jetpack Compose.<br>- Show trending & popular movies.<br>- Connect with ViewModel & handle states. |
| Mirna Mahmoud Mohamed Mahmoud       | 3      | UI (Movie Details)   | - Build Movie Details screen (poster, overview, rating).<br>- Add “Add to Favorites” button.<br>- API integration for movie details. |
| Abdelrahman Ahmed ElSayed ElQazaz   | 4      | UI (Search & Filter) | - Implement Search screen (text input + results).<br>- Add filtering by genre/rating.<br>- Integrate with API for search. |
| Lina Ashraf Sediq Hamzawy           | 5      | Local Storage        | - Setup Room / DataStore.<br>- Implement Favorites screen.<br>- Save & retrieve favorites.<br>- Add offline cache support. |
| Mariam Magdy Soliman Youssef        | 6      | Testing & Docs       | - Write Unit & UI tests.<br>- Maintain README & documentation.<br>- Prepare final presentation.<br>- Polish UI/UX. |
---
## License
This project is licensed under the MIT License.

