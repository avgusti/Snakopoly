package my.monopoly.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import object.Board;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;

public class BoardLoader {
	public static Board loadFromSting(String base64String) {
		ByteArrayInputStream bais = new ByteArrayInputStream(
				base64String.getBytes());
		Base64InputStream base64InputStream = new Base64InputStream(bais,
				Base64.DEFAULT);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(base64InputStream);
			return (Board) ois.readObject();
		} catch (Exception e) {
			Log.e("Board", "can't load board", e);
		}
		return null;
	}

	public static String saveToString(Board board) {
		String base64 = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Base64OutputStream base64OutputStream = new Base64OutputStream(baos,
				Base64.DEFAULT);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(base64OutputStream);
			oos.writeObject(board);
			oos.close();
			base64OutputStream.close();
			baos.close();
			base64 = baos.toString();

		} catch (IOException e) {
			Log.e("Board", "can't save board", e);
		}
		return base64;

	}
}
