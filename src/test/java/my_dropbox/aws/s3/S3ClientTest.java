package my_dropbox.aws.s3;

import java.net.URL;
import java.util.Objects;

import my_dropbox.App;
import org.junit.Test;

public class S3ClientTest {

	@Test
	public void testS3Client() {
		String bucketName = "cloud-example-001";

		S3Client s3Client = new S3ClientImpl();
		S3Bucket bucket = s3Client.createBucket(bucketName);

		bucket.getObjects();
		bucket.addObject("some-file.txt", getResource("some-file.txt"));
		bucket.destroy();
	}

	private static String getResource(String resourceName) {
		ClassLoader classLoader = App.class.getClassLoader();
		URL url = Objects.requireNonNull(classLoader.getResource(resourceName));
		return url.getFile();
	}
}
