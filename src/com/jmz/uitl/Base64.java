package com.jmz.uitl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/**
 * 加密
 * @author Administrator
 *
 */
public class Base64 {
	public static byte[] decode(String s) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {

			decode(s, bos);

		} catch (IOException e) {

			throw new RuntimeException();

		}

		byte[] decodedBytes = bos.toByteArray();

		try {

			bos.close();

			bos = null;

		} catch (IOException ex) {

			System.err.println("Error while decoding BASE64: " + ex.toString());

		}

		return decodedBytes;

	}

	private static void decode(String s, OutputStream os) throws IOException {

		int i = 0;

		int len = s.length();

		while (true) {

			while (i < len && s.charAt(i) <= ' ')

				i++;

			if (i == len)

				break;

			int tri = (decode(s.charAt(i)) << 18)

			+ (decode(s.charAt(i + 1)) << 12)

			+ (decode(s.charAt(i + 2)) << 6)

			+ (decode(s.charAt(i + 3)));

			os.write((tri >> 16) & 255);

			if (s.charAt(i + 2) == '=')

				break;

			os.write((tri >> 8) & 255);

			if (s.charAt(i + 3) == '=')

				break;

			os.write(tri & 255);

			i += 4;

		}

	}

	private static int decode(char c) {

		if (c >= 'A' && c <= 'Z')

			return ((int) c) - 65;

		else if (c >= 'a' && c <= 'z')

			return ((int) c) - 97 + 26;

		else if (c >= '0' && c <= '9')

			return ((int) c) - 48 + 26 + 26;

		else

			switch (c) {

			case '+':

				return 62;

			case '/':

				return 63;

			case '=':

				return 0;

			default:

				throw new RuntimeException("unexpected code: " + c);

			}

	}
}