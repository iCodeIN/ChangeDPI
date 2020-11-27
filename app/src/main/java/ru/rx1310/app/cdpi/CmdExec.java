// ! rx1310 <rx1310@inbox.ru> | Copyright (c) rx1310, 2020 | MIT License

package ru.rx1310.app.cdpi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class CmdExec {

	public static class ResultData {
		
		private int resultCode;
		private String resultData;
		private String resultError;

		ResultData(int code, String info, String err) {
			resultCode = code;
			resultData = info == null ? "" : info;
			resultError = err == null ? "" : err;
		}
		
		public int getResultCode() {
			return resultCode;
		}
		
		public String getResultData() {
			return resultData;
		}
		
		public String getResultError() {
			return resultError;
		}
		
		public String getResult() {
			return toString();
		}
		
		public String toString() {
			return resultData + "\n" + resultError;
		}
		
	}
	
	public static ResultData execute(boolean su, String command) {
		return execPool(rmSlashN(command).split("\n"));
	}
	
	private static ResultData execPool(String[] commands) {
		
		Process exec = null;
		InputStream execIn = null;
		InputStream execErr = null;
		OutputStream execOs = null;

		ResultData resultData;
		
		try {
			
			exec = Runtime.getRuntime().exec("su");
			execIn = exec.getInputStream();
			execErr = exec.getErrorStream();
			execOs = exec.getOutputStream();
			
			DataOutputStream dos = new DataOutputStream(execOs);
			
			for (String com : commands)
			
				if (!com.isEmpty()) {
					dos.writeBytes(com + "\n");
					dos.flush();
				}
				
			dos.close();
			resultData = new ResultData(exec.waitFor(), inputStream2String(execIn, "utf-8"), inputStream2String(execErr, "utf-8"));
			
		} catch (Exception e) {
			resultData = new ResultData(-1, "", e.toString());
		} finally {
			
			try {
				
				if (execIn != null) execIn.close();
				if (execErr != null) execErr.close();
				if (execOs != null) execOs.close();
				if (exec != null) exec.destroy();
				
			} catch (Exception ignored) {}
			
		}
		
		return resultData;
		
	}

	public static String inputStream2String(InputStream in, String encoding)  throws  Exception  {
		
		StringBuilder out = new StringBuilder();
		InputStreamReader inread = new InputStreamReader(in, encoding);
		
		char[] b = new char[1024];
		int n;
		
		while ((n = inread.read(b)) !=  -1) {
			String s = new String(b, 0, n);
			out.append(s);
		}
		
		return out.toString();
		
	}

	/*public static String read(InputStream is) {
		
		try {
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			
			while ((length = is.read(buffer)) != -1) baos.write(buffer, 0, length);
			
			return baos.toString("UTF-8");
			
		} catch (Exception e) {
			e.printStackTrace(); return e.toString(); 
		}
		
	}*/

	public static String rmSlashN(String text) {
		
		while (text.contains("\n\n")) text = text.replace("\n\n", "\n");
		
		if (text.startsWith("\n"))
			text = text.substring(1);
			
		if (text.endsWith("\n"))
			text = text.substring(0, text.length() - 1);

		return text;
		
	}
	
}
