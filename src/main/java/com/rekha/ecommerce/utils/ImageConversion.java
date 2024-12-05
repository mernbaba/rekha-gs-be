package com.rekha.ecommerce.utils;

import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

public class ImageConversion {

	public static Blob byteToBlobConversion(byte[] image) throws SQLException {

		try {
			Blob blobImage = new SerialBlob(image);
			return blobImage;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;

		}
	}

	public static byte[] blobToByteConversion(Blob image) throws SQLException {
		try {
			byte[] byteImage = image.getBytes(1, (int) image.length());
			return byteImage;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;

		}

	}

}
