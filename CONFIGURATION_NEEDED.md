# Configuration Required

Before running the application, you need to provide the following credentials in `src/main/resources/application.properties`:

## ✅ Already Configured

- ✅ MongoDB database name: `userportfoliophotos`
- ✅ MongoDB Atlas connection string (already in application.properties)
- ✅ Server port: 8181
- ✅ File upload limits: 10MB

## ⚠️ NEEDS YOUR INPUT

### Azure Blob Storage Credentials

You need to replace these placeholders in `application.properties`:

```properties
# Azure Blob Storage Configuration
azure.storage.connection-string=YOUR_AZURE_STORAGE_CONNECTION_STRING
azure.storage.container-name=photos
azure.storage.account-name=YOUR_STORAGE_ACCOUNT_NAME
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

### Example Configuration

```properties
# Example (use YOUR actual values)
azure.storage.connection-string=DefaultEndpointsProtocol=https;AccountName=photographyportfolio2024;AccountKey=aBcD1234eFgH5678...==;EndpointSuffix=core.windows.net
azure.storage.container-name=photos
azure.storage.account-name=photographyportfolio2024
```

## Container Name

The container name is set to `photos` by default. The application will:
- Automatically create the container if it doesn't exist
- Set it to public blob access (so images can be viewed)

You can change the container name if you prefer something else.

## Security Best Practices

### For Development
- It's okay to put credentials directly in `application.properties` 
- **BUT** make sure `application.properties` is in `.gitignore` (it already is)

### For Production
Use environment variables instead:

```properties
azure.storage.connection-string=${AZURE_STORAGE_CONNECTION_STRING}
azure.storage.container-name=${AZURE_STORAGE_CONTAINER_NAME:photos}
azure.storage.account-name=${AZURE_STORAGE_ACCOUNT_NAME}
```

Then set these environment variables on your deployment platform.

## Next Steps

1. ✅ Open `src/main/resources/application.properties`
2. ✅ Replace `YOUR_AZURE_STORAGE_CONNECTION_STRING` with your actual connection string
3. ✅ Replace `YOUR_STORAGE_ACCOUNT_NAME` with your actual account name
4. ✅ Optionally change `photos` to your preferred container name
5. ✅ Run `mvn clean install`
6. ✅ Run `mvn spring-boot:run`
7. ✅ Test with `curl http://localhost:8181/api/photos/health`

## Need Help?

See [AZURE_SETUP.md](./AZURE_SETUP.md) for detailed Azure setup instructions and troubleshooting.
