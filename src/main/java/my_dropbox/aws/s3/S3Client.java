package my_dropbox.aws.s3;

public interface S3Client {
	S3Bucket createBucket(String bucketName);
}
