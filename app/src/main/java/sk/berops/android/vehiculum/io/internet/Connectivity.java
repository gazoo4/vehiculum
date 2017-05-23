package sk.berops.android.vehiculum.io.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import sk.berops.android.vehiculum.Vehiculum;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public class Connectivity {

	public static final int TIMEOUT_MS = 5000;

	public static boolean isOnline() {
		boolean result;

		try {
			Socket sock = new Socket();
			SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

			sock.connect(sockaddr, TIMEOUT_MS);
			sock.close();

			result = true;
		} catch (IOException e) {
			result = false;
		}

		return result;
	}
}
