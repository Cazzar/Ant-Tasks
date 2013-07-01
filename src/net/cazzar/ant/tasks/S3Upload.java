/**
 * 
 */
package net.cazzar.ant.tasks;

import java.awt.event.KeyEvent;
import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

/**
 * @author Cayde
 * 
 */
public class S3Upload extends Task {
	String	accessKey;
	String	secretKey;
	String	bucketName;
	String	prefix;
	String	file;
	boolean	recursive;
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	@Override
	public void execute() throws BuildException {
		String destBucketAndPath;
		if (prefix == null || prefix.length() == 0) {
			destBucketAndPath = bucketName;
		}
		else {
			destBucketAndPath = bucketName + "/" + prefix;
		}
		
		BasicAWSCredentials creds = new BasicAWSCredentials(accessKey,
				secretKey);
		AmazonS3 s3 = new AmazonS3Client(creds);
		
		TransferManager transfer = new TransferManager(s3);
		File file = new File(this.file);
		PutObjectRequest req = new PutObjectRequest(destBucketAndPath,
				file.getName(), file);
		
		req.withCannedAcl(CannedAccessControlList.PublicRead);
		
		Upload upload = transfer.upload(req);
		
		while (!upload.isDone()) {
			log(String.format("Progress %s", upload.getProgress()
					.getPercentTransfered())
					+ "%");
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
