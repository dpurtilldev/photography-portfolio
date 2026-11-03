# Azure Blob Storage Setup Guide

## Prerequisites
- Azure account with an active subscription
- Azure Storage Account created

## Azure Portal Setup

### 1. Create Storage Account (if not already created)
1. Go to [Azure Portal](https://portal.azure.com)
2. Navigate to **Storage Accounts**
3. Click **+ Create**
4. Fill in the details:
   - **Subscription**: Your subscription
   - **Resource Group**: Create new or use existing
   - **Storage Account Name**: Choose a unique name (e.g., `photographyportfolio`)
   - **Region**: Choose closest to your users
   - **Performance**: Standard
   - **Redundancy**: LRS (Locally Redundant Storage) for development, GRS for production
5. Click **Review + Create**, then **Create**

### 2. Get Connection String
1. Go to your Storage Account
2. In the left menu, under **Security + networking**, click **Access keys**
3. Click **Show keys**
4. Copy the **Connection string** under **key1**

### 3. Create Container (Optional - app will auto-create)
1. In your Storage Account, go to **Containers** (under Data storage)
2. Click **+ Container**
3. Name it `photos`
4. Set **Public access level** to **Blob (anonymous read access for blobs only)**
5. Click **Create**

## Application Configuration

### Update `application.properties`

Replace the placeholder values in `src/main/resources/application.properties`:

```properties
# Azure Blob Storage Configuration
azure.storage.connection-string=DefaultEndpointsProtocol=https;AccountName=YOUR_ACCOUNT_NAME;AccountKey=YOUR_ACCOUNT_KEY;EndpointSuffix=core.windows.net
azure.storage.container-name=photos
azure.storage.account-name=YOUR_ACCOUNT_NAME
```

**Example Connection String:**
```
DefaultEndpointsProtocol=https;AccountName=photographyportfolio;AccountKey=abcd1234...==;EndpointSuffix=core.windows.net
```

### Environment Variables (Recommended for Production)

Instead of hardcoding in `application.properties`, use environment variables:

```properties
azure.storage.connection-string=${AZURE_STORAGE_CONNECTION_STRING}
azure.storage.container-name=${AZURE_STORAGE_CONTAINER_NAME:photos}
azure.storage.account-name=${AZURE_STORAGE_ACCOUNT_NAME}
```

Then set environment variables:
- `AZURE_STORAGE_CONNECTION_STRING`
- `AZURE_STORAGE_CONTAINER_NAME`
- `AZURE_STORAGE_ACCOUNT_NAME`

## What Changed

### 1. **Dependencies Added**
- `azure-storage-blob` (v12.25.1) in `pom.xml`

### 2. **New Service Created**
- `AzureBlobStorageService.java` - Handles all Azure Blob Storage operations:
  - Upload files
  - Delete files
  - Download files
  - Check file existence

### 3. **Model Updated**
- `Photo.java`: Changed `filePath` to `blobUrl`
  - Now stores Azure Blob URL instead of local file path

### 4. **PhotoService Updated**
- Integrated `AzureBlobStorageService`
- `deletePhoto()` now deletes blob from Azure before removing DB record

### 5. **PhotoController Updated**
- Upload endpoint now uses Azure Blob Storage
- Removed local file system upload logic
- Removed `/api/photos/images/{filename}` endpoint (images now served directly from Azure)

### 6. **Database Name Updated**
- Changed from `photography_portfolio` to `userportfoliophotos`

## How It Works

### Upload Flow
1. User uploads image via POST `/api/photos/upload`
2. Controller validates the image file
3. `AzureBlobStorageService` uploads to Azure Blob Storage
4. Azure returns the public blob URL
5. Photo metadata (including blob URL) saved to MongoDB
6. Frontend can use the blob URL directly to display images

### Delete Flow
1. User deletes photo via DELETE `/api/photos/{id}`
2. Service retrieves photo from MongoDB
3. Service deletes blob from Azure using blob URL
4. Service deletes photo record from MongoDB

## Benefits

✅ **Scalability**: Azure handles storage and bandwidth
✅ **Cost-Effective**: Pay only for storage and data transfer used
✅ **CDN Integration**: Can enable Azure CDN for faster global delivery
✅ **No Local Storage**: Server doesn't store large files
✅ **Public URLs**: Images can be accessed directly from Azure
✅ **Reliability**: Azure provides high availability and redundancy

## Testing

1. Update `application.properties` with your Azure credentials
2. Run `mvn clean install` to download dependencies
3. Start the application
4. Upload a photo via POST `/api/photos/upload`
5. Check the response - `blobUrl` field should contain Azure URL
6. Open the blob URL in a browser to verify the image is accessible

## Security Considerations

🔒 **Connection String**: Never commit connection strings to version control
🔒 **Access Keys**: Rotate access keys periodically
🔒 **Container Access**: Use Blob-level public access (not Container-level)
🔒 **CORS**: Configure CORS in Azure if frontend is on different domain
🔒 **SAS Tokens**: Consider using SAS tokens for temporary access instead of public blobs

## Troubleshooting

### "Connection refused" or "Connection timed out"
- Check your connection string is correct
- Verify network connectivity to Azure

### "Container not found"
- The app auto-creates the container on startup
- Check the container name matches in `application.properties`

### "Blob already exists"
- Each uploaded file gets a unique UUID name, this shouldn't happen
- Check for concurrent uploads

### Images not loading in frontend
- Verify the blob URL is correct in the database
- Check container has public blob access enabled
- Verify CORS settings in Azure if frontend is on different domain

## CORS Configuration (If Needed)

If your frontend is on a different domain, configure CORS in Azure:

1. Go to your Storage Account
2. Navigate to **Resource sharing (CORS)**
3. Under **Blob service**, add a rule:
   - **Allowed origins**: `http://localhost:5173` (or your frontend domain)
   - **Allowed methods**: GET, HEAD
   - **Allowed headers**: *
   - **Exposed headers**: *
   - **Max age**: 3600
4. Click **Save**

## Cost Estimation

Azure Blob Storage pricing (approximate):
- **Storage**: ~$0.018 per GB per month (Hot tier)
- **Operations**: Very cheap (fractions of a cent per 10,000 operations)
- **Data Transfer**: First 100GB free, then ~$0.087 per GB

For a portfolio with 1000 photos (~10GB):
- Storage: ~$0.18/month
- Operations: negligible
- **Total**: < $1/month
