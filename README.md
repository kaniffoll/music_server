# Music-service backend

---
## About
- **EN**:  
  Spring Boot REST application (backend) for a snippet-based music service KMP application.

- **RU**:  
  Spring Boot REST приложение (backend) для музыкального сервиса с сниппетами для KMP-приложения.

---

## Endpoints

### 1. `album/`
- **GET** `{albumId}/track/{trackNumber}/stream`
    - **EN**: Get audio stream of a track
    - **RU**: Получить аудио поток трека

- **GET** `{albumId}/track/{trackNumber}/meta_data`
    - **EN**: Get track metadata
    - **RU**: Получить данные трека

- **GET** `{albumId}/cover`
    - **EN**: Get album cover image
    - **RU**: Получить обложку альбома

- **GET** `{albumId}/tracks`
    - **EN**: Get all tracks from the album
    - **RU**: Получить все треки альбома

---

### 2. `auth/`
- **POST** `/refresh`
    - **EN**: Get a new access token using a refresh token
    - **RU**: Получить новый access токен на основе refresh токена

- **POST** `/register`
    - **EN**: Register a new user
    - **RU**: Зарегистрироваться

- **POST** `/login`
    - **EN**: Log in
    - **RU**: Войти

---

### 3. `edit/`
- **POST** `/track`
    - **EN**: Upload a track
    - **RU**: Загрузить трек

- **DELETE** `/track/{id}`
    - **EN**: Delete a track by ID
    - **RU**: Удалить трек по ID

- **DELETE** `/album/{id}`
    - **EN**: Delete an album by ID
    - **RU**: Удалить альбом по ID

- **POST** `/album/{title}`
    - **EN**: Create a new album
    - **RU**: Создать альбом

---

### 4. `find/`
- **GET** `/track/{id}/stream`
    - **EN**: Get track stream by ID
    - **RU**: Найти трек по ID (поток)

- **GET** `/snippet/{id}`
    - **EN**: Get snippet by id
    - **RU**: Получить сниппет по id

- **GET** `/track/{title}`
    - **EN**: Find tracks by title
    - **RU**: Найти треки по названию

- **GET** `/album/{title}`
    - **EN**: Find albums by title
    - **RU**: Найти альбомы по названию

- **GET** `/snippet/batch`
    - **EN**: Get snippet batch
    - **RU**: Получить выборку сниппетов

---

### 5. `user/`
- **DELETE** `{id}/remove_genre`
    - **EN**: Remove a genre from user's favorites
    - **RU**: Удалить жанр из любимых у пользователя

- **POST** `{id}/add_genres`
    - **EN**: Add genres to user
    - **RU**: Добавить жанры пользователю

- **GET** `{id}/user_genres`
    - **EN**: Get user's favorite genres
    - **RU**: Получить жанры пользователя

- **GET** `all_genres`
    - **EN**: Get all available genres
    - **RU**: Получить все доступные жанры
  
- **POST** `{userId}/track/{trackId}`
    - **EN**: Add track in favorites for user
    - **RU**: Добавить трек в избранное для пользователя

- **GET** `{userId}/track`
    - **EN**: Get user's favorite tracks 
    - **RU**: Получить любимые треки пользователя