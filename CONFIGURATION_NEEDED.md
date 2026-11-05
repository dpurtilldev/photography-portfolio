# Configuration Required

Before running the application, you need to provide your credentials in the `.env` file at the project root.

## ✅ Already Configured

- ✅ MongoDB database name: `userportfoliophotos`
- ✅ Server port: 8181
- ✅ File upload limits: 10MB
- ✅ `.env` file created (you need to fill in your credentials)

## ⚠️ NEEDS YOUR INPUT

### 1. MongoDB Connection String

Open the `.env` file and replace this line:

```
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/userportfoliophotos?retryWrites=true&w=majority
```

**Options:**

**Option A: MongoDB Atlas (Cloud - Recommended)**
- Get your connection string from MongoDB Atlas
- Format: `mongodb+srv://username:password@cluster.mongodb.net/userportfoliophotos?retryWrites=true&w=majority`

**Option B: Local MongoDB (for testing)**
- Install MongoDB locally
- Use: `mongodb://localhost:27017/photography_portfolio`

### 2. Azure Blob Storage Credentials

You need to update these values in the `.env` file:

```env
# Azure Blob Storage Configuration
AZURE_STORAGE_CONNECTION_STRING=DefaultEndpointsProtocol=https;AccountName=YOUR_ACCOUNT_NAME;AccountKey=YOUR_ACCOUNT_KEY;EndpointSuffix=core.windows.net
AZURE_STORAGE_CONTAINER_NAME=photos
AZURE_STORAGE_ACCOUNT_NAME=YOUR_ACCOUNT_NAME
```

### How to Get Azure Credentials

1. **Go to Azure Portal**: https://portal.azure.com
2. **Find your Storage Account** (the one you created)
3. **Click on "Access keys"** in the left sidebar (under Security + networking)
4. **Copy the Connection String** from key1 or key2

It should look like:
```
DefaultEndpointsProtocol=https;AccountName=mystorageaccount;AccountKey=abc123...==;EndpointSuffix=core.windows.net
```

5. **Get your Account Name**: It's the name you gave your storage account when creating it

### Example .env File

```env
# MongoDB Configuration
MONGODB_URI=mongodb+srv://myuser:mypassword@cluster0.mongodb.net/userportfoliophotos?retryWrites=true&w=majority

# Azure Configuration (use YOUR actual values)
AZURE_STORAGE_CONNECTION_STRING=DefaultEndpointsProtocol=https;AccountName=photographyportfolio2024;AccountKey=aBcD1234eFgH5678...==;EndpointSuffix=core.windows.net
AZURE_STORAGE_CONTAINER_NAME=photos
AZURE_STORAGE_ACCOUNT_NAME=photographyportfolio2024

# Server Configuration
SERVER_PORT=8181
```

## Container Name

The container name is set to `photos` by default. The application will:
- Automatically create the container if it doesn't exist
- Set it to public blob access (so images can be viewed)

You can change the container name if you prefer something else.

## Security Best Practices

### For Development
- ✅ Keep your credentials in the `.env` file
- ✅ The `.env` file is already in `.gitignore` (won't be committed to git)
- ⚠️ **NEVER** commit your `.env` file to version control

### For Production
The application already uses environment variables via the `.env` file approach, which is perfect for deployment platforms like:
- Heroku
- AWS Elastic Beanstalk
- Azure App Service
- Docker containers

Just set the environment variables on your deployment platform.

## Next Steps

1. ✅ Open the `.env` file in the project root
2. ✅ Add your MongoDB connection string (from MongoDB Atlas or use local MongoDB)
3. ✅ Add your Azure Storage connection string (from Azure Portal)
4. ✅ Add your Azure Storage account name
5. ✅ Optionally change `photos` to your preferred container name
6. ✅ Run `mvn clean install` to install the new dependencies
7. ✅ Run `mvn spring-boot:run` to start the application
8. ✅ Test with `curl http://localhost:8181/api/photos/health`

## Need Help?

See [AZURE_SETUP.md](./AZURE_SETUP.md) for detailed Azure setup instructions and troubleshooting.
