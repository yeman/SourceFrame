package com.holly.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FTPUtil.class);

	private FTPClient ftpClient = new FTPClient();

	/**
	 * connect ftp server
	 * 
	 * @param hostname
	 * @param port
	 * @param username
	 * @param password
	 * @return connect status
	 * @throws IOException
	 */
	public boolean connect(String hostname, int port, String username,
			String password) throws IOException {
		ftpClient.connect(hostname, port);
		ftpClient.setControlEncoding("UTF-8");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password))
				return true;
		}
		disconnect();
		return false;
	}

	/**
	 * 
	 * @param remote
	 * @param local
	 * @return
	 * @throws IOException
	 */
	public DownloadStatus download(String remote, String local)
			throws IOException {
		// config auto mode
		ftpClient.enterLocalPassiveMode();
		// config binary transport mode
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

		DownloadStatus result;
		// check remote file is exist
		FTPFile[] files = ftpClient.listFiles(new String(remote
				.getBytes("UTF-8"), "iso-8859-1"));
		if (files.length != 1) {
			LOGGER.info("remote file is not exist");
			return DownloadStatus.Remote_File_Notexist;
		}

		long lRemoteSize = files[0].getSize();
		File f = new File(local);
		// if local file is exist
		if (f.exists()) {
			long localSize = f.length();
			// check local file size bigger than remote one
			if (localSize >= lRemoteSize) {
				LOGGER.info("local file size is more bigger than remote one,so stop it");
				return DownloadStatus.Local_Bigger_Remote;
			}
			// from break download && log status
			FileOutputStream out = new FileOutputStream(f, true);
			ftpClient.setRestartOffset(localSize);

			InputStream in = ftpClient.retrieveFileStream(new String(remote
					.getBytes("UTF-8"), "iso-8859-1"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize / 100;
			long process = localSize / step;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0)
						LOGGER.info("download status:" + process);
					// TODO update 文件下载进度，值放在process中
				}
			}
			in.close();
			out.close();
			boolean isDo = ftpClient.completePendingCommand();
			if (isDo) {
				result = DownloadStatus.Download_From_Break_Success;
			} else {
				result = DownloadStatus.Download_From_Break_Failed;
			}
		} else {
			OutputStream out = new FileOutputStream(f);
			InputStream in = ftpClient.retrieveFileStream(new String(remote
					.getBytes("UTF-8"), "iso-8859-1"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize / 100;
			long process = 0;
			long localSize = 0;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess >= process) {
					process = nowProcess;
					if (process % 10 == 0)
						LOGGER.info("download status:" + process);
					// TODO update 文件下载进度，值放在process中
				}
			}
			in.close();
			out.close();

			boolean upNewStatus = ftpClient.completePendingCommand();
			if (upNewStatus) {
				result = DownloadStatus.Download_New_Success;
			} else {
				result = DownloadStatus.Download_New_Failed;
			}
		}
		return result;
	}

	/**
	 * 
	 * @param local
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	public UploadStatus upload(String local, String remote) throws IOException {
		// config passive mode
		ftpClient.enterLocalPassiveMode();
		// config binary transport mode
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("GBK");

		UploadStatus result;

		String remoteFileName = remote;
		if (remote.contains("/"))
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);

		// 创建远程目录结构，创建失败返回
		if (createDirectory(remote, ftpClient) == UploadStatus.Create_Directory_Failed) {
			return UploadStatus.Create_Directory_Failed;
		}
		// check remote file is exist
		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName
				.getBytes("UTF-8"), "iso-8859-1"));
		if (files.length == 1) {
			long remoteSize = files[0].getSize();
			File f = new File(local);
			long localSize = f.length();
			// file is exist
			if (remoteSize == localSize) {
				return UploadStatus.File_Exist;
			} else if (remoteSize > localSize) {
				return UploadStatus.Remote_Bigger_Local;
			}
			// 尝试移动文件内读取指针，实现断点续传
			result = uploadFile(remoteFileName, f, ftpClient, remoteSize);
			// 如果断点续传失败，则删除服务器上文件，重新上传
			if (result == UploadStatus.Upload_From_Break_Failed) {
				if (!ftpClient.deleteFile(remoteFileName)) {
					return UploadStatus.Delete_Remote_Failed;
				}
				result = uploadFile(remoteFileName, f, ftpClient, 0);
			}
		} else {
			result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
		}
		return result;
	}

	/**
	 * dis connect from server
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		if (ftpClient.isConnected())
			ftpClient.disconnect();
	}

	/**
	 * 
	 * @param remote
	 * @param ftpClient
	 * @return
	 * @throws IOException
	 */
	public UploadStatus createDirectory(String remote, FTPClient ftpClient)
			throws IOException {
		UploadStatus status = UploadStatus.Create_Directory_Success;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		if (!directory.equalsIgnoreCase("/")
				&& !ftpClient.changeWorkingDirectory(new String(directory
						.getBytes("UTF-8"), "iso-8859-1"))) {
			//
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end)
						.getBytes("UTF-8"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						LOGGER.info("create directory failed");
						return UploadStatus.Create_Directory_Failed;
					}
				}
				start = end + 1;
				end = directory.indexOf("/", start);

				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}

	/**
	 * 
	 * @param remoteFile
	 * @param localFile
	 * @param ftpClient
	 * @param remoteSize
	 * @return
	 * @throws IOException
	 */
	public UploadStatus uploadFile(String remoteFile, File localFile,
			FTPClient ftpClient, long remoteSize) throws IOException {
		UploadStatus status;
		// 显示进度的上传
		long step = localFile.length() / 100;
		long process = 0;
		long localreadbytes = 0;

		RandomAccessFile raf = new RandomAccessFile(localFile, "r");

		OutputStream out = ftpClient.appendFileStream(new String(remoteFile
				.getBytes("UTF-8"), "iso-8859-1"));
		// 断点续传
		if (remoteSize > 0) {
			ftpClient.setRestartOffset(remoteSize);
			process = remoteSize / step;
			raf.seek(remoteSize);
			localreadbytes = remoteSize;
		}
		byte[] bytes = new byte[1024];
		int c;
		while ((c = raf.read(bytes)) != -1) {
			out.write(bytes, 0, c);
			localreadbytes += c;
			if (localreadbytes / step != process) {
				process = localreadbytes / step;
				LOGGER.info("上传进度：" + process);
				// TODO 汇报上传状态
			}
		}
		out.flush();
		raf.close();
		out.close();
		boolean result = ftpClient.completePendingCommand();
		if (remoteSize > 0) {
			status = result ? UploadStatus.Upload_From_Break_Success
					: UploadStatus.Upload_From_Break_Failed;
		} else {
			status = result ? UploadStatus.Upload_New_File_Success
					: UploadStatus.Upload_New_File_Failed;
		}
		return status;
	}

	public static void main(String[] args) {
		FTPUtil ftpUtil = new FTPUtil();
		try {
			// ftp://wymqw:sdhj78Md@edftp.hollybeacon.com/
			String hostname = "edftp.hollybeacon.com";
			int port = 21;
			String username = "wymqw";
			String password = "sdhj78Md";
			boolean flag = ftpUtil.connect(hostname, port, username, password);
			System.out.println("connect status: " + flag);
			String local = "C:/Users/Public/Desktop/Oracle9i客户端精简版.exe";
			String remote = "hollybeacon/Oracle9i客户端精简版.exe";
			// System.out.println(ftpUtil.upload(local, remote));
			System.out.println(ftpUtil.download(remote, local));
		} catch (IOException e) {
			System.out.println("连接FTP出错：" + e.getMessage());
			e.printStackTrace();
		}
	}

	public enum UploadStatus {
		Create_Directory_Failed, Create_Directory_Success, Upload_New_File_Success, Upload_New_File_Failed, File_Exist, Remote_Bigger_Local, Upload_From_Break_Success, Upload_From_Break_Failed, Delete_Remote_Failed
	}

	public enum DownloadStatus {
		Remote_File_Notexist, Download_New_Success, Download_New_Failed, Local_Bigger_Remote, Download_From_Break_Success, Download_From_Break_Failed
	}
}
