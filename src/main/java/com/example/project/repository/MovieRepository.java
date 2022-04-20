package com.example.project.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.project.model.MoviesInfo;

@Repository
public class MovieRepository {

	final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public List<MoviesInfo> saveAll(List<MoviesInfo> movieList) {
		dynamoDBMapper.batchSave(movieList);
		return movieList;
	}

	public List<MoviesInfo> findAll() {
		return dynamoDBMapper.scan(MoviesInfo.class, new DynamoDBScanExpression());
	}

	public List<String> findMoviesWithMoreUserReviews(String userReviews) {
		logger.info("Fetching movies with more user reviews from database");
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":val1", new AttributeValue().withN(userReviews));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("userReviews > :val1")
				.withExpressionAttributeValues(eav);

		List<MoviesInfo> scanResult = dynamoDBMapper.scan(MoviesInfo.class, scanExpression);

		HashMap<String, Integer> res = new HashMap<>();
		for (MoviesInfo m : scanResult) {
			res.put(m.getTitle(), m.getUserReviews());
		}

		return res.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
	}

	public List<String> findMoviesByDirectorAndYear(String director, String publishedFrom, String publishedTo) {
		logger.info("Fetching movies with given director and year range from database");
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":val1", new AttributeValue().withS(director));
		eav.put(":val2", new AttributeValue().withN(publishedFrom));
		eav.put(":val3", new AttributeValue().withN(publishedTo));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("director = :val1 and publishedYear >= :val2 and publishedYear<= :val3")
				.withExpressionAttributeValues(eav);

		List<MoviesInfo> scanResult = dynamoDBMapper.scan(MoviesInfo.class, scanExpression);
		List<String> res = new ArrayList<>();
		for (MoviesInfo m : scanResult) {
			res.add(m.getTitle());
		}
		return res;
	}

	public String findMovieWithHighBudget(String publishedYear, String country) {
		logger.info("Fetching movies with highest budget given publishedYear and country from database");
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":val1", new AttributeValue().withS(country));
		eav.put(":val2", new AttributeValue().withN(publishedYear));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("country = :val1 and publishedYear = :val2").withExpressionAttributeValues(eav);

		List<MoviesInfo> scanResult = dynamoDBMapper.scan(MoviesInfo.class, scanExpression);

		HashMap<String, Integer> res = new HashMap<>();
		for (MoviesInfo m : scanResult) {
			int budget = Integer.parseInt(m.getBudget().replaceAll("[^0-9]", ""));
			res.put(m.getTitle(), budget);
		}
		return Collections.max(res.entrySet(), Map.Entry.comparingByValue()).getKey();
	}

}
