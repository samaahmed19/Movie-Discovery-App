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
## Team Tasks Distribution

| Phase                | Member | GitHub Username | Responsibilities |
|----------------------|--------|-----------------|------------------|
| Project Setup        | 1      | Mouhab Mamdouh Zakaria | - Create Android project & configure Gradle.<br>- Setup package structure (data, domain, presentation).<br>- Configure Navigation component. |
| UI (Home Screen)     | 2      | Sama Ahmed Mohamed Abdelkareem| - Build Home screen UI with Jetpack Compose.<br>- Show trending & popular movies.<br>- Connect with ViewModel & handle states. |
| UI (Movie Details)   | 3      | Mirna mahmoud mohamed mahmoud | - Build Movie Details screen (poster, overview, rating).<br>- Add “Add to Favorites” button.<br>- API integration for movie details. |
| UI (Search & Filter) | 4      |Abdelrahman Ahmed ElSayed ElQazaz| - Implement Search screen (text input + results).<br>- Add filtering by genre/rating.<br>- Integrate with API for search. |
| Local Storage        | 5      | Lina Ashraf Sediq Hamzawy | - Setup Room / DataStore.<br>- Implement Favorites screen.<br>- Save & retrieve favorites.<br>- Add offline cache support. |
| Testing & Docs       | 6      | Mariam Magdy Soliman Youssef | - Write Unit & UI tests.<br>- Maintain README & documentation.<br>- Prepare final presentation.<br>- Polish UI/UX. |
---
## License
This project is licensed under the MIT License.

