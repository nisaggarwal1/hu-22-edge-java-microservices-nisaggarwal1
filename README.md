# hu-22-edge-java-microservices-nisaggarwal1
Fetch book details from csv file and save in DynamoDb and then use API to get those details from database

Pre-Requisites:
Setup dynamodb locally 

**Steps to run:**
1. Execute command **java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb** to run dynamodb locally 
2. Create table with attributes using command **aws dynamodb create-table --attribute-definitions AttributeName=id,AttributeType=S --table-name MoviesTable --key-schema AttributeName=id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 --region us-east-1 --output json --endpoint-url http://localhost:8000**
3. Run springboot server

**API Details:**
1. To view all movie details - **localhost:8081/movie**
2. Titles directed by  given director in the given year range e.g : generate titles report for director D.W. Griffith and year range 2010 to 2020! - **localhost:8081/movie/custom?director=Charles Tait&fromYear=1900&toYear=2000**
3. Generate report of English titles which have user reviews more than given user review filter and sort the report with user reviews by descending ! - **localhost:8081/movie/titlesWithUserReview?userReviews=7**
4. Generate highest budget titles for the given year and country filters! - **localhost:8081/movie/titlesWithHighBudget?year=1907&country=Australia**

**Additional points:**
1. Check time taken in a “X-TIME-TO-EXECUTE” header on the response.
2. API returns a 401 status code, unless a “X-Auth-Token” header is present on the request.!
3. Schedular is created to sync new additions in CSV file to database perodically (Set to 2 seconds).
