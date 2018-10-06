package my_dropbox.aws.s3;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public interface S3Bucket {
	void addObject(String keyName, String filePath);
	S3Object getObject(String keyName);
	List<S3ObjectSummary> getObjects();
	void destroy();

	String getName();
	Date getCreationDate();
	Owner getOwner();
	void copy(Bucket bucket);
}
