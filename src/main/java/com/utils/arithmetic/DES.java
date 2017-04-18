package com.utils.arithmetic;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密解密算法
 * 
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
 * 24小时内即可被破解。虽然如此，在某些简单应用中，还是可以使用DES加密算法。
 * 
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 * 
 * @author bigbird
 *
 */
public class DES {

	/** 加密算法,可用 DES,DESede,Blowfish. */
	private final static String ALGORITHM = "DES";
	/** 加密、解密默认key. */
	private final static String DefaultKey = "national";

	/**
	 * 用指定的key对数据进行DES加密
	 * 
	 * @param src
	 *            待加密的数据
	 * @param key
	 *            DES加密的key
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String encrypt(String src, String key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException,
			IllegalBlockSizeException, BadPaddingException {
		return byte2hex(encrypt(src.getBytes(), key.getBytes()));
	}

	/**
	 * 用指定的key对数据进行DES加密
	 * 
	 * @param src
	 *            待加密的数据
	 * @param key
	 *            DES加密的key
	 * @return byte[]
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] encrypt(byte[] src, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException,
			IllegalBlockSizeException, BadPaddingException {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 用指定的key对数据进行DES解密
	 * 
	 * @param src
	 *            待解密的数据
	 * @param key
	 *            DES解密的key
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String decrypt(String src, String key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		return new String(decrypt(hex2byte(src.getBytes()), key.getBytes()));
	}

	/**
	 * 用指定的key对数据进行DES解密
	 * 
	 * @param src
	 *            待解密的数据
	 * @param key
	 *            DES解密的key
	 * @return byte[]
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 将字节数组转换为十六进制字节数组
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("Length is not even.");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	/**
	 * 利用默认密匙对数据进行DES加密.
	 * 
	 * @param data
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public final static String encrypt(String data) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, IllegalBlockSizeException,
			BadPaddingException {
		return byte2hex(encrypt(data.getBytes(), DefaultKey.getBytes()));
	}

	/**
	 * 利用默认密匙对用DES加密过的数据进行解密.
	 * 
	 * @param data
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public final static String decrypt(String data) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		return new String(decrypt(hex2byte(data.getBytes()),
				DefaultKey.getBytes()));
	}

	/************************************ 主方法用于测试 *************************************/
	public static void main(String[] args) throws Exception {
		// 待加密内容
		String str = "1";
		// 密码，长度要是8的倍数
		String password = "c_jDlog_";
		String encryResult = DES.encrypt(str, password);
		System.out.println("加密后：" + encryResult);
		// 直接将如上内容解密
		String decryResult = DES.decrypt(encryResult, password);
		System.out.println("解密后：" + decryResult);
	}
}
