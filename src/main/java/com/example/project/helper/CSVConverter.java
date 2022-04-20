package com.example.project.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.project.model.MoviesInfo;
import com.example.project.repository.MovieRepository;

@Component
@Configuration
public class CSVConverter {

	@Value("${csv.file.path}")
    String filePath;
	final Logger logger = LoggerFactory.getLogger(getClass());
	static final String handleMultipleValues = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
	MoviesInfo movie;
	@Autowired
	MovieRepository repository;
	@SuppressWarnings("resource")
	@Scheduled(fixedDelayString = "2000")
	public void saveMovie() {
		
		try {
			File file = new File(filePath);
			InputStream targetStream = new FileInputStream(file);
			List<MoviesInfo> moviesInfo = repository.findAll();
			long totalRecords = (int) moviesInfo.stream().count();
			List<MoviesInfo> movies = new ArrayList<>();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(targetStream));
			String line = "";
			long lines = 0;
			while ((line = bufferedReader.readLine()) != null) {
				lines++;
				if (lines > totalRecords + 1) {
					logger.info("New record found");
					String[] columns = line.split(handleMultipleValues);
					movie = setData(columns);
					movies.add(movie);
					logger.info("New record added");
				}
			}
			repository.saveAll(movies);
		} catch (Exception e) {
			String msg = e.getMessage();
			logger.error(String.format("Unable to save data %s", msg));
		}
	}
	private MoviesInfo setData(String[] columns) {
		movie = new MoviesInfo();
		movie.setId(columns[0]);
		try {
			movie.setTitle(columns[1]);
			movie.setPublishedYear(Integer.parseInt(columns[3]));
			movie.setBudget(columns[16]);
			movie.setUserReviews(Integer.parseInt(columns[20]));
			movie.setCountry(columns[7]);
			movie.setGenre(columns[5]);
			movie.setDuration(Integer.parseInt(columns[6]));
			movie.setDirector(columns[9].replaceAll("\\s", ""));
			
		}catch(Exception ex) {
			String id = movie.getId();
			logger.error(String.format("Unable to save data for id: %s", id));
		}
		return movie;
	}

}
