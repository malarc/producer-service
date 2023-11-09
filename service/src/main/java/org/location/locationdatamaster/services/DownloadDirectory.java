package org.location.locationdatamaster.services;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class DownloadDirectory {

    public static String destFilePath = "C:\\temp";
    public static String storageConnectionString="DefaultEndpointsProtocol=https;AccountName=locationmasterdata;AccountKey=TRo2mejwbL11SzFMhE4l07S9g9ch8uD8drDnn60l4pUjB+o9d+iJ5qM1b7JQf8CGmEKMjX8pKBHM+ASteJv1lQ==;EndpointSuffix=core.windows.net";
    private static AzureFileService azureFileService;
    private static BlobServiceAsyncClient blobServiceAsyncClient;

    public static void main(String[] args)
            throws InvalidKeyException, URISyntaxException, StorageException, IOException {
       // System.setProperty("server.port", "4000");

         CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
        CloudBlobClient serviceClient = account.createCloudBlobClient();
        CloudBlobContainer container = serviceClient.getContainerReference("locationmasterdatacontainer");
        List fileList = new ArrayList<String>();

        for (ListBlobItem blobItem : container.listBlobs()) {

            System.out.println("blobItem-----"+blobItem.getUri());
            String fullPath = blobItem.getUri().toString();
            int index = fullPath.lastIndexOf("/");
            String fileName = fullPath.substring(index + 1);
            System.out.println("fileName-----"+fileName);
            fileList.add(fileName);
            //azureFileService.downLoadFiles(blobItem.getStorageUri());
            // If the item is a blob, a virtual directory.
            if (blobItem instanceof CloudBlobDirectory) {

                CloudBlobDirectory blobDir = (CloudBlobDirectory) blobItem;
                System.out.println("CloudBlobDirectory.......");
                downloadDirectory(blobDir);
            }
        }
        System.out.println("fileList......."+fileList);
    }
    public static void downloadDirectory(CloudBlobDirectory blobDir)
            throws IOException, StorageException, URISyntaxException {

        System.out.println("downloadBlob..........");
        Files.createDirectories(Paths.get(destFilePath + blobDir.getPrefix()));

        for (ListBlobItem blobInDir : blobDir.listBlobs()) {

            if (blobInDir instanceof CloudBlockBlob) {
                CloudBlockBlob blob = (CloudBlockBlob) blobInDir;
                blob.downloadToFile(destFilePath + blob.getName());
            } else {
                downloadDirectory((CloudBlobDirectory) blobInDir);
            }
        }

    }

}
