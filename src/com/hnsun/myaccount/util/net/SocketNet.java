package com.hnsun.myaccount.util.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.hnsun.myaccount.util.log.LogFactory;

/**
 * Socket的访问
 * @author hnsun
 * @date 2016/09/14
 */
public class SocketNet {

	public SocketNet connect(String dstAddress, int dstPort) {
		try {
			socket = new Socket(dstAddress, dstPort);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true) {
						try {
							InputStream stream = socket.getInputStream();
							BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
							String content = reader.readLine();
							LogFactory.log().i(content);
						} catch (IOException e) {
							LogFactory.log().e(e);
						}
					}
				}
			}).start();
		} catch (UnknownHostException e) {
			LogFactory.log().e(e);
		} catch (IOException e) {
			LogFactory.log().e(e);
		}
		return this;
	}
	
	public void send(String msg) {
		try {
			PrintStream stream = new PrintStream(socket.getOutputStream());
			stream.println(msg);
			stream.flush();
		} catch (IOException e) {
			LogFactory.log().e(e);
		}
	}
	
	private Socket socket;
}
