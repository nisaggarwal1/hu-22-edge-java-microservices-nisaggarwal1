package com.example.project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.MoviesInfo;
import com.example.project.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	MovieRepository repository;

	public List<MoviesInfo> getAllMovies() {
		return repository.findAll();
	}

	public List<String> findMovieByCustomParameters(String director, String from, String to) {
		return repository.findMoviesByDirectorAndYear(director, from, to);
	}

	public List<String> findMovieWithMoreUserReviews(String userReviews) {
		return repository.findMoviesWithMoreUserReviews(userReviews);
	}

	public String findMovieWithHighBudget(String publishedYear, String country) {
		return repository.findMovieWithHighBudget(publishedYear, country);
	}
}
