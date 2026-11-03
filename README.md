# Photography Portfolio

A Spring Boot application for managing and storing photography portfolio with MongoDB as the non-relational database.

## Features

- **Photo Management**: Create, read, update, and delete photos
- **Azure Blob Storage Integration**: Images stored in Azure for scalability and performance
- **Rich Metadata**: Store detailed information including:
  - Title, description, and file information
  - Camera settings (model, lens, aperture, shutter speed, ISO, focal length)
  - Location data
  - Tags and categories
  - Editing software information
  - Capture and upload dates
  - Public/private visibility settings
- **File Upload**: Upload photos directly via API with validation
- **Advanced Search**: 
  - Search by title or description
  - Filter by tags, location, camera model
  - Filter by date range
  - Filter by edited/original status
- **RESTful API**: Complete REST endpoints for all operations

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Database**: MongoDB Atlas (Cloud, Non-relational)
- **Storage**: Azure Blob Storage (for images)
- **Build Tool**: Maven
- **Additional Libraries**:
  - Lombok (reducing boilerplate code)
  - Spring Data MongoDB
  - Spring Boot Validation
  - Azure Storage Blob SDK

## Prerequisites

Before running the application, ensure you have:

1. **Java 17** or higher installed
2. **Maven** 3.6+ installed
3. **MongoDB Atlas account** (cloud database)
4. **Azure Storage Account** (for blob storage)

### Setting up MongoDB Atlas

1. Create a free account at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a new cluster (free tier available)
3. Create a database user with password
4. Whitelist your IP address (or use 0.0.0.0/0 for development)
5. Get your connection string from the "Connect" button
6. Database name: `userportfoliophotos`

### Setting up Azure Blob Storage

See [AZURE_SETUP.md](./AZURE_SETUP.md) for detailed instructions.

Quick steps:
1. Create an Azure account at [Azure Portal](https://portal.azure.com)
2. Create a Storage Account
3. Get the connection string from Access Keys
4. Update `application.properties` with your credentials

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

2. **Configure the application:**
   
   Update `src/main/resources/application.properties` with your credentials:
   
   ```properties
   # MongoDB Atlas
   spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/userportfoliophotos?retryWrites=true&w=majority
   
   # Azure Blob Storage
   azure.storage.connection-string=YOUR_AZURE_CONNECTION_STRING
   azure.storage.container-name=photos
   azure.storage.account-name=YOUR_ACCOUNT_NAME
   ```
   
   See [AZURE_SETUP.md](./AZURE_SETUP.md) for detailed Azure configuration.

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
   curl http://localhost:8181/api/photos/health
   ```

   You should see: `Photography Portfolio API is running!`

## Configuration

The application can be configured in `src/main/resources/application.properties`:

```properties
# Application Name
spring.application.name=photography-portfolio

# Server Configuration
server.port=8181

# MongoDB Atlas Connection
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/userportfoliophotos?retryWrites=true&w=majority

# Azure Blob Storage Configuration
azure.storage.connection-string=YOUR_AZURE_STORAGE_CONNECTION_STRING
azure.storage.container-name=photos
azure.storage.account-name=YOUR_STORAGE_ACCOUNT_NAME

# File Upload Configuration
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

**Security Note:** Never commit credentials to version control. Use environment variables for production:

```properties
spring.data.mongodb.uri=${MONGODB_URI}
azure.storage.connection-string=${AZURE_STORAGE_CONNECTION_STRING}
azure.storage.container-name=${AZURE_STORAGE_CONTAINER_NAME:photos}
```

## API Endpoints

### Health Check
- `GET /api/photos/health` - Check if API is running

### CRUD Operations
- `POST /api/photos/upload` - Upload a photo file with metadata
- `POST /api/photos` - Create a new photo (metadata only)
- `GET /api/photos` - Get all photos
- `GET /api/photos/{id}` - Get photo by ID
- `PUT /api/photos/{id}` - Update photo
- `DELETE /api/photos/{id}` - Delete photo (removes from DB and Azure)

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

### Upload a Photo with File

```bash
curl -X POST http://localhost:8181/api/photos/upload \
  -F "file=@/path/to/your/photo.jpg" \
  -F "title=Sunset at the Beach" \
  -F "description=Beautiful sunset captured at Santa Monica Beach" \
  -F "captureDate=2024-08-15T18:30:00" \
  -F "location=Santa Monica Beach, California" \
  -F "tags=sunset" \
  -F "tags=beach" \
  -F "tags=landscape" \
  -F "isEdited=true" \
  -F "isPublic=true" \
  -F "cameraModel=Canon EOS R5" \
  -F "lens=RF 24-70mm F2.8" \
  -F "aperture=f/8" \
  -F "shutterSpeed=1/250" \
  -F "iso=100" \
  -F "focalLength=35mm"
```

The response will include a `blobUrl` field with the Azure Blob Storage URL where the image is hosted.

### Create a Photo (Metadata Only)

```bash
curl -X POST http://localhost:8181/api/photos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Sunset at the Beach",
    "description": "Beautiful sunset captured at Santa Monica Beach",
    "fileName": "sunset_beach.jpg",
    "blobUrl": "https://yourstorageaccount.blob.core.windows.net/photos/abc-123.jpg",
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
curl http://localhost:8181/api/photos
```

### Search Photos

```bash
curl http://localhost:8181/api/photos/search?q=sunset
```

### Get Photos by Tag

```bash
curl http://localhost:8181/api/photos/tag/landscape
```

### Get Recent Uploads

```bash
curl http://localhost:8181/api/photos/recent
```

## Data Model

### Photo Document

```json
{
  "id": "string",
  "title": "string (required)",
  "description": "string",
  "fileName": "string (required)",
  "blobUrl": "string (required) - Azure Blob Storage URL",
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

### Azure Blob Storage Issues

See [AZURE_SETUP.md](./AZURE_SETUP.md) for detailed troubleshooting.

Common issues:
- **Connection refused**: Check your connection string
- **Container not found**: App auto-creates it on startup
- **Images not loading**: Verify container has public blob access

### MongoDB Atlas Connection Issues

1. **Check connection string format:**
   ```
   mongodb+srv://username:password@cluster.mongodb.net/userportfoliophotos?retryWrites=true&w=majority
   ```

2. **Verify IP whitelist** in MongoDB Atlas
3. **Check username and password** are correct
4. **Ensure database name** is `userportfoliophotos`

### Application Won't Start

1. Check if port 8181 is already in use
2. Verify Java version: `java -version`
3. Verify Azure credentials in `application.properties`
4. Check MongoDB Atlas connection string
5. Check application logs in the console

## Architecture

### Storage Strategy
- **Images**: Stored in Azure Blob Storage (scalable, CDN-ready)
- **Metadata**: Stored in MongoDB Atlas (flexible schema, cloud-hosted)
- **Benefits**:
  - Reduced database size and costs
  - Better performance for image serving
  - Scalable storage for large files
  - Global content delivery via Azure CDN

### Data Flow
1. **Upload**: Client → API → Azure Blob Storage → MongoDB (stores blob URL)
2. **Retrieve**: Client → API → MongoDB → Returns blob URL → Client fetches from Azure
3. **Delete**: Client → API → Delete from Azure → Delete from MongoDB

## Future Enhancements

- [ ] Image thumbnail generation using Azure Functions
- [ ] User authentication and authorization with JWT
- [ ] Photo albums/collections
- [ ] Comments and likes
- [ ] Azure CDN integration for faster global delivery
- [ ] Image optimization and compression
- [ ] Export portfolio to PDF
- [ ] Frontend web interface (Vue.js)
- [ ] Batch upload functionality
- [ ] Image watermarking
- [ ] Advanced filtering and sorting

## License

This project is open source and available under the MIT License.

## Contact

For questions or support, please open an issue in the repository.
