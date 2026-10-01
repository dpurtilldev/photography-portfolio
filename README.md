# Photography Portfolio

A Spring Boot application for managing and storing photography portfolio with MongoDB as the non-relational database.

## Features

- **Photo Management**: Create, read, update, and delete photos
- **Rich Metadata**: Store detailed information including:
  - Title, description, and file information
  - Camera settings (model, lens, aperture, shutter speed, ISO, focal length)
  - Location data
  - Tags and categories
  - Editing software information
  - Capture and upload dates
  - Public/private visibility settings
- **Advanced Search**: 
  - Search by title or description
  - Filter by tags, location, camera model
  - Filter by date range
  - Filter by edited/original status
- **RESTful API**: Complete REST endpoints for all operations

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Database**: MongoDB (Non-relational)
- **Build Tool**: Maven
- **Additional Libraries**:
  - Lombok (reducing boilerplate code)
  - Spring Data MongoDB
  - Spring Boot Validation

## Prerequisites

Before running the application, ensure you have:

1. **Java 17** or higher installed
2. **Maven** 3.6+ installed
3. **MongoDB** installed and running on `localhost:27017`

### Installing MongoDB

**Windows:**
```bash
# Download and install from: https://www.mongodb.com/try/download/community
# Or use Chocolatey:
choco install mongodb
```

**macOS:**
```bash
brew tap mongodb/brew
brew install mongodb-community
```

**Linux:**
```bash
# Ubuntu/Debian
sudo apt-get install mongodb

# Or follow official guide: https://docs.mongodb.com/manual/installation/
```

### Starting MongoDB

**Windows:**
```bash
# Start MongoDB service
net start MongoDB

# Or run manually:
mongod --dbpath "C:\data\db"
```

**macOS/Linux:**
```bash
# Start MongoDB service
brew services start mongodb-community

# Or run manually:
mongod --config /usr/local/etc/mongod.conf
```

## Project Structure

```
photography-portfolio/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/portfolio/photographyportfolio/
│       │       ├── PhotographyPortfolioApplication.java
│       │       ├── controller/
│       │       │   └── PhotoController.java
│       │       ├── model/
│       │       │   └── Photo.java
│       │       ├── repository/
│       │       │   └── PhotoRepository.java
│       │       └── service/
│       │           └── PhotoService.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

## Installation and Setup

1. **Clone or navigate to the project directory:**
   ```bash
   cd "c:\Users\david\Desktop\Photography Porfolio"
   ```

2. **Ensure MongoDB is running** (see prerequisites above)

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file:
   ```bash
   java -jar target/photography-portfolio-1.0.0.jar
   ```

5. **Verify the application is running:**
   ```bash
   curl http://localhost:8080/api/photos/health
   ```

   You should see: `Photography Portfolio API is running!`

## Configuration

The application can be configured in `src/main/resources/application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=photography_portfolio

# Server Port
server.port=8080

# File Upload Settings
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Using MongoDB Atlas (Cloud)

To use MongoDB Atlas instead of local MongoDB, update the connection string:

```properties
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/photography_portfolio
```

## API Endpoints

### Health Check
- `GET /api/photos/health` - Check if API is running

### CRUD Operations
- `POST /api/photos` - Create a new photo
- `GET /api/photos` - Get all photos
- `GET /api/photos/{id}` - Get photo by ID
- `PUT /api/photos/{id}` - Update photo
- `DELETE /api/photos/{id}` - Delete photo

### Search and Filter
- `GET /api/photos/search?q={searchTerm}` - Search by title or description
- `GET /api/photos/search/title?q={title}` - Search by title
- `GET /api/photos/tag/{tag}` - Get photos by tag
- `POST /api/photos/tags` - Get photos by multiple tags (send array in body)
- `GET /api/photos/location/{location}` - Get photos by location
- `GET /api/photos/public` - Get only public photos
- `GET /api/photos/edited?isEdited=true` - Get edited/original photos
- `GET /api/photos/camera/{model}` - Get photos by camera model
- `GET /api/photos/date-range?start={start}&end={end}` - Get photos by date range
- `GET /api/photos/recent` - Get 10 most recent uploads

## Example API Usage

### Create a Photo

```bash
curl -X POST http://localhost:8080/api/photos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Sunset at the Beach",
    "description": "Beautiful sunset captured at Santa Monica Beach",
    "fileName": "sunset_beach.jpg",
    "filePath": "/photos/2024/sunset_beach.jpg",
    "fileType": "image/jpeg",
    "fileSize": 2048576,
    "captureDate": "2024-08-15T18:30:00",
    "location": "Santa Monica Beach, California",
    "tags": ["sunset", "beach", "landscape"],
    "isEdited": true,
    "editingSoftware": "Adobe Lightroom",
    "isPublic": true,
    "cameraSettings": {
      "cameraModel": "Canon EOS R5",
      "lens": "RF 24-70mm F2.8",
      "aperture": "f/8",
      "shutterSpeed": "1/250",
      "iso": 100,
      "focalLength": "35mm"
    }
  }'
```

### Get All Photos

```bash
curl http://localhost:8080/api/photos
```

### Search Photos

```bash
curl http://localhost:8080/api/photos/search?q=sunset
```

### Get Photos by Tag

```bash
curl http://localhost:8080/api/photos/tag/landscape
```

### Get Recent Uploads

```bash
curl http://localhost:8080/api/photos/recent
```

## Data Model

### Photo Document

```json
{
  "id": "string",
  "title": "string (required)",
  "description": "string",
  "fileName": "string (required)",
  "filePath": "string (required)",
  "fileType": "string",
  "fileSize": "number",
  "captureDate": "datetime (required)",
  "uploadDate": "datetime (auto-generated)",
  "lastEditedDate": "datetime",
  "location": "string",
  "tags": ["string"],
  "isEdited": "boolean",
  "editingSoftware": "string",
  "thumbnailPath": "string",
  "isPublic": "boolean",
  "cameraSettings": {
    "cameraModel": "string",
    "lens": "string",
    "aperture": "string",
    "shutterSpeed": "string",
    "iso": "number",
    "focalLength": "string"
  }
}
```

## Development

### Running in Development Mode

With Spring Boot DevTools included, the application will automatically restart when you make changes to the code.

```bash
mvn spring-boot:run
```

### Building for Production

```bash
mvn clean package -DskipTests
java -jar target/photography-portfolio-1.0.0.jar
```

## Testing

Run the tests with:

```bash
mvn test
```

## Troubleshooting

### MongoDB Connection Issues

1. **Check if MongoDB is running:**
   ```bash
   # Windows
   Get-Service MongoDB
   
   # macOS/Linux
   ps aux | grep mongod
   ```

2. **Verify MongoDB port:**
   ```bash
   netstat -an | findstr 27017
   ```

3. **Check MongoDB logs:**
   - Windows: `C:\Program Files\MongoDB\Server\{version}\log\mongod.log`
   - macOS: `/usr/local/var/log/mongodb/mongo.log`
   - Linux: `/var/log/mongodb/mongod.log`

### Application Won't Start

1. Check if port 8080 is already in use
2. Verify Java version: `java -version`
3. Check application logs in the console

## Future Enhancements

- [ ] File upload functionality for actual photo files
- [ ] Image thumbnail generation
- [ ] User authentication and authorization
- [ ] Photo albums/collections
- [ ] Comments and likes
- [ ] Image optimization and compression
- [ ] Export portfolio to PDF
- [ ] Frontend web interface

## License

This project is open source and available under the MIT License.

## Contact

For questions or support, please open an issue in the repository.
