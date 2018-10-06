package my_dropbox.services;

import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectSummary;

public interface FileService {
	void upload(String filename);
	void download(String filename);
	List<S3ObjectSummary> getFileSummaries();
	void setUserService(UserService userService);
}
