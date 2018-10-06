package my_dropbox.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import my_dropbox.aws.s3.S3Bucket;
import my_dropbox.aws.s3.S3Client;
import my_dropbox.aws.s3.S3ClientImpl;
import my_dropbox.services.exception.ResourceNotFoundException;
import my_dropbox.services.exception.UserNotLoggedInException;

public class FileServiceImpl implements FileService {

	private static final String STORAGE_BUCKET_NAME = "my-dropbox-storage";

	private S3Bucket s3Bucket;
	private UserService userService;

	public FileServiceImpl() {
		S3Client s3Client = new S3ClientImpl();
		s3Bucket = s3Client.createBucket(STORAGE_BUCKET_NAME);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void upload(String filename) {
		if(userService.isLoggedIn()) {
			String username = userService.getCurrentUser().getUsername();
			String key = username + "/" + getFileName(filename);
			s3Bucket.addObject(key, filename);
		} else {
			throw new UserNotLoggedInException();
		}
	}

	@Override
	public void download(String filename) {
		if (userService.isLoggedIn()) {
			String username = userService.getCurrentUser().getUsername();
			String key = username + "/" + filename;

			try (S3ObjectInputStream inputStream = s3Bucket.getObject(key).getObjectContent();
					FileOutputStream outputStream = new FileOutputStream(new File(filename))) {
				byte[] readBuffer = new byte[1024];
				int readLength = 0;
				while ((readLength = inputStream.read(readBuffer)) > 0) {
					outputStream.write(readBuffer, 0, readLength);
				}
			} catch (AmazonServiceException e) {
				throw new ResourceNotFoundException();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} else {
			throw new UserNotLoggedInException();
		}
	}

	@Override
	public List<S3ObjectSummary> getFileSummaries() {
		if (userService.isLoggedIn()) {
			try {
				String username = userService.getCurrentUser().getUsername();
				List<S3ObjectSummary> objectSummaries = s3Bucket.getObjects();
				return objectSummaries.stream().filter(summary -> summary.getKey().startsWith(username + "/")).collect(
						Collectors.toList());
			} catch (AmazonServiceException e) {
				System.err.println(e.getErrorMessage());
				return new ArrayList<>();
			}
		} else {
			throw new UserNotLoggedInException();
		}
	}

	private String getFileName(String filepath) {
		File f = new File(filepath);
		return f.getName();
	}
}
