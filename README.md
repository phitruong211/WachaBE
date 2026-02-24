# Movie Module

This module provides CRUD endpoints for movies using Spring Boot and a layered architecture.

## Endpoints

- `POST /api/movies` - create a movie
- `GET /api/movies/{id}` - fetch a movie by id
- `GET /api/movies` - list all movies
- `DELETE /api/movies/{id}` - delete a movie

## Notes

- DTOs live in `com.example.movieapp.dto`.
- Entity is `com.example.movieapp.model.Movie`.
- MapStruct mapper is `com.example.movieapp.mapper.MovieMapper`.

