# City Records Backend

## Microservice
- This app displays the public meetings, awards and notices from NYC OPEN DATA API
- This was built following the principles of Test Driven Development. All code was written only after writing a corresponding failing test. All unit/controller tests are passing. 
- The backend has full crud functionality
- There are two microservices, users and records which each work individually when they run on port 8081 but don't run simultaneously. 

## React
- The entire app is built with just three components that are re-used for every type of data. This makes it easy to add many more data points and makes the code simpler to read and edit. 
- Every record can be saved to the backend. You can view the saved records at localhost:8080/records. It saves the document file url link and which category it belongs to. 

## Next Steps
- Saving more details for every record to the database such as title, date, agency. i.e adding more columns to the records table. 
- Creating a user login 
- Implement a one to many relationship between users and records to be able to save separate records for each user. 

