package my_dropbox.aws.s3;

import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

public class S3ClientImpl implements S3Client {
	private AmazonS3 s3Client;

	public S3ClientImpl() {
		s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.AP_SOUTHEAST_1)
				.withCredentials(new ProfileCredentialsProvider("s3onlyaccess"))
				.build();
	}

	@Override
	public S3Bucket createBucket(String bucketName) {
		Bucket bucket = null;
		if (s3Client.doesBucketExistV2(bucketName)) {
			bucket = getBucket(bucketName);
		} else {
			try {
				bucket = s3Client.createBucket(bucketName);
			} catch (AmazonS3Exception e) {
				System.err.println(e.getErrorMessage());
			}
		}
		S3Bucket s3Bucket = new S3BucketImpl(s3Client);
		s3Bucket.copy(bucket);
		return s3Bucket;
	}

	private Bucket getBucket(String bucketName) {
		Bucket namedBucket = null;
		List<Bucket> buckets = s3Client.listBuckets();
		for (Bucket b : buckets) {
			if (b.getName().equals(bucketName)) {
				namedBucket = b;
				break;
			}
		}
		return namedBucket;
	}

}
