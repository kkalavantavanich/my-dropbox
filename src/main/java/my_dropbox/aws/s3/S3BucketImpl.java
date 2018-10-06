package my_dropbox.aws.s3;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;

public class S3BucketImpl implements S3Bucket {

	private String name;
	private Date creationDate;
	private Owner owner;
	private AmazonS3 s3Client;

	public S3BucketImpl(AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}

	@Override
	public void addObject(String keyName, String filePath) {
		TransferManagerBuilder builder = TransferManagerBuilder.standard().withS3Client(s3Client);
		TransferManager transferManager = builder.build();

		File file = new File(filePath);
		try {
			Upload upload = transferManager.upload(name, keyName, file);
			UploadResult result = upload.waitForUploadResult();
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public S3Object getObject(String keyName) {
		return s3Client.getObject(name, keyName);
	}

	@Override
	public List<S3ObjectSummary> getObjects() {
		List<S3ObjectSummary> objects = null;
		try {
			ListObjectsV2Result result = s3Client.listObjectsV2(name);
			objects = result.getObjectSummaries();
		} catch (AmazonServiceException ex) {
			System.err.println(ex.getErrorMessage());
		}
		return objects;
	}

	@Override
	public void destroy() {
		try {
			removeObjectsFromBucket();
			removeVersionsFromBucket();
			deleteEmptyBucket();
		} catch (AmazonServiceException ex) {
			System.err.println(ex.getErrorMessage());
		}
	}

	private void removeObjectsFromBucket() {
		ObjectListing objectListing = s3Client.listObjects(name);
		while (true) {
			for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
				s3Client.deleteObject(name, summary.getKey());
			}
			if (objectListing.isTruncated()) {
				objectListing = s3Client.listNextBatchOfObjects(objectListing);
			} else {
				break;
			}
		}
	}

	private void removeVersionsFromBucket() {
		VersionListing versionListing = s3Client.listVersions(
				new ListVersionsRequest().withBucketName(name));
		while (true) {
			for (S3VersionSummary summary : versionListing.getVersionSummaries()) {
				s3Client.deleteVersion(name, summary.getKey(), summary.getVersionId());
			}

			if (versionListing.isTruncated()) {
				versionListing = s3Client.listNextBatchOfVersions(
						versionListing);
			} else {
				break;
			}
		}
	}

	private void deleteEmptyBucket() {
		s3Client.deleteBucket(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public Owner getOwner() {
		return owner;
	}

	@Override
	public void copy(Bucket bucket) {
		name = bucket.getName();
		creationDate = bucket.getCreationDate();
		owner = bucket.getOwner();
	}
}
