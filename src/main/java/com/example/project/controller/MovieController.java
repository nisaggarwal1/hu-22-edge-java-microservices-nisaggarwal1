package com.example.project.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.MoviesInfo;
import com.example.project.service.MovieService;

@RestController
@RequestMapping("/movie")
public class MovieController {
	final Logger logger = LoggerFactory.getLogger(getClass());
	String tokenValue = "Secret";
	String headerKey = "X-TIME-TO-EXECUTE";
	@Autowired
	MovieService movieService;

	@GetMapping
	public ResponseEntity<List<MoviesInfo>> getAllMovies(@RequestHeader("X-Auth-Token") String token) {
		logger.info("Get all movies info");
		long beginTime = System.currentTimeMillis();
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			if (!token.equals(tokenValue)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			List<MoviesInfo> movies = movieService.getAllMovies();
			if (movies.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			long responseTime = System.currentTimeMillis() - beginTime;
			responseHeaders.set(headerKey, String.valueOf(responseTime));
			return ResponseEntity.ok().headers(responseHeaders).body(movies);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/custom")
	public ResponseEntity<List<String>> getTitleByCustomQuery(@RequestHeader("X-Auth-Token") String token,
			@RequestParam Map<String, String> customQuery) {
		logger.info("Get Titles directed by  given director in the given year range");
		long beginTime = System.currentTimeMillis();
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			if (!token.equals(tokenValue)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			String director = customQuery.get("director");
			String publishedYearFrom = customQuery.get("fromYear");
			String publishedYearTo = customQuery.get("toYear");
			List<String> movieTitle = movieService.findMovieByCustomParameters(director, publishedYearFrom,
					publishedYearTo);
			if (movieTitle.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			long responseTime = System.currentTimeMillis() - beginTime;
			responseHeaders.set(headerKey, String.valueOf(responseTime));
			return ResponseEntity.ok().headers(responseHeaders).body(movieTitle);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/titlesWithUserReview")
	public ResponseEntity<List<String>> getTitleWithMoreUserReview(@RequestHeader("X-Auth-Token") String token,
			@RequestParam Map<String, String> customQuery) {
		logger.info("Generate titles which more user reviews than given user review filter and sort by descending ");
		long beginTime = System.currentTimeMillis();
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			if (!token.equals(tokenValue)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			String userReviews = customQuery.get("userReviews");
			List<String> movieTitle = movieService.findMovieWithMoreUserReviews(userReviews);
			if (movieTitle.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			long responseTime = System.currentTimeMillis() - beginTime;
			responseHeaders.set(headerKey, String.valueOf(responseTime));
			return ResponseEntity.ok().headers(responseHeaders).body(movieTitle);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/titlesWithHighBudget")
	public ResponseEntity<String> getTitleWithHighBudget(@RequestHeader("X-Auth-Token") String token,
			@RequestParam Map<String, String> customQuery) {
		logger.info("Generate titles with highest budget of given country and year");
		long beginTime = System.currentTimeMillis();
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			if (!token.equals(tokenValue)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			String publishedYear = customQuery.get("year");
			String country = customQuery.get("country");

			String movieTitle = movieService.findMovieWithHighBudget(publishedYear, country);
			if (movieTitle.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			long responseTime = System.currentTimeMillis() - beginTime;
			responseHeaders.set(headerKey, String.valueOf(responseTime));
			return ResponseEntity.ok().headers(responseHeaders).body(movieTitle);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
