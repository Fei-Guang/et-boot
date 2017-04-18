package com.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {

	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 创建期望宽高的缓存图，保持原有图像宽高比
	 * 
	 * @param image
	 *            原有缓存图
	 * @param scale_width
	 *            缩放后期望得到的宽
	 * @param scale_height
	 *            缩放后期望得到的高
	 * @return
	 */
	public static BufferedImage createScaleBufferedImage(BufferedImage image,
			int scale_width, int scale_height) {

		float default_scale = scale_height * 1.0f / scale_width + 0.05f;

		// 真实高宽比以及高、宽值
		float width = image.getWidth();
		float height = image.getHeight();
		float scaleHW = height / width;

		// 缩放后高宽值
		int iconWidth = 0;
		int iconHeight = 0;

		// 判断缩放以高为基准还是以宽为基准，该处为了使图片尽量符合期望尺寸，会对小于默认尺寸的图片进行放大
		if (scaleHW <= default_scale) {
			iconWidth = scale_width;
			iconHeight = (int) (scaleHW * iconWidth);
		} else {
			iconHeight = scale_height;
			iconWidth = (int) (1.0f * iconHeight / scaleHW);
		}

		BufferedImage img = new BufferedImage(iconWidth, iconHeight,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
		g2d.drawImage(image, 0, 0, img.getWidth(), img.getHeight(), 0, 0,
				image.getWidth(), image.getHeight(), null);
		g2d.dispose();
		return img;
	}
	
	public static boolean writeImage(String destFilePath, Image srcImage,
			String format) {
		return writeImage(new File(destFilePath), srcImage, format);
	}

	/**
	 * 将图像写入指定文件
	 * 
	 * @param destFile
	 *            目标文件
	 * @param srcImage
	 *            源图像
	 * @param format
	 *            文件格式
	 */
	public static boolean writeImage(File destFile, Image srcImage,
			String format) {
		try {
			File dir = destFile.getParentFile();
			if (dir != null) {
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						return false;
					}
				}
			}
			OutputStream outputStream = new FileOutputStream(destFile);
			// 该处理代码是为了保持png格式的图片底色正确
			BufferedImage image = setImageCanvas(srcImage, null, format);
			ImageIO.write(image, format, outputStream);
			outputStream.flush();
			outputStream.close();
			return true;
		} catch (Exception e) {
			logger.error("WriteImage:", e);
			return false;
		}
	}
	
	/**
	 * 设置图像背景色，该方法能保持底色为透明的png格式的图片任然为透明底色
	 * 
	 * @param outputImage
	 *            要处理的图片
	 * @param canvasColor
	 *            画布颜色
	 * @param format
	 *            图片格式
	 */
	public static BufferedImage setImageCanvas(Image outputImage,
			Color canvasColor, String format) {
		int outputWidth = outputImage.getWidth(null);
		if (outputWidth < 1)
			throw new IllegalArgumentException("output image width "
					+ outputWidth + " is out of range");
		int outputHeight = outputImage.getHeight(null);
		if (outputHeight < 1)
			throw new IllegalArgumentException("output image height "
					+ outputHeight + " is out of range");
		// Get a buffered image from the image.
		BufferedImage image = new BufferedImage(outputWidth, outputHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		if (canvasColor == null) {
			if (format.equalsIgnoreCase("png")) {
				// png格式的图片，画布颜色应该为透明
				image = g2d.getDeviceConfiguration().createCompatibleImage(
						outputWidth, outputHeight, Transparency.TRANSLUCENT);
				g2d.dispose();
				g2d = image.createGraphics();
			} else {
				// 非png格式的图片，画布颜色默认为白色
				g2d.setColor(Color.white);
				g2d.fillRect(0, 0, outputWidth, outputHeight);
			}
		} else {
			g2d.setColor(canvasColor);
			g2d.fillRect(0, 0, outputWidth, outputHeight);
		}
		g2d.setStroke(new BasicStroke(1));// 设置画笔宽度
		g2d.drawImage(outputImage, 0, 0, null);
		// 释放对象
		g2d.dispose();
		return image;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param srcFile源文件
	 * @param outFile输出文件
	 * @param x坐标
	 * @param y坐标
	 * @param width宽度
	 * @param height高度
	 * @return
	 */
	public static boolean cutPic(String srcFile, String outFile, int x, int y,
			int width, int height) {
		FileInputStream is = null;
		ImageInputStream iis = null;
		try {
			// 如果源图片不存在
			if (!new File(srcFile).exists()) {
				return false;
			}
			// 读取图片文件
			is = new FileInputStream(srcFile);
			// 获取文件格式
			String ext = srcFile.substring(srcFile.lastIndexOf(".") + 1);
			// ImageReader声称能够解码指定格式
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(ext);
			ImageReader reader = it.next();
			// 获取图片流
			iis = ImageIO.createImageInputStream(is);
			// 输入源中的图像将只按顺序读取
			reader.setInput(iis, true);
			// 描述如何对流进行解码
			ImageReadParam param = reader.getDefaultReadParam();
			// 图片裁剪区域
			Rectangle rect = new Rectangle(x, y, width, height);
			// 提供一个 BufferedImage，将其用作解码像素数据的目标
			param.setSourceRegion(rect);
			// 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象
			BufferedImage bi = reader.read(0, param);
			// 保存新图片
			File tempOutFile = new File(outFile);
			if (!tempOutFile.exists()) {
				tempOutFile.mkdirs();
			}
			ImageIO.write(bi, ext, tempOutFile);
			return true;
		} catch (Exception e) {
			logger.error("CutPic:", e);
			return false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (iis != null) {
					iis.close();
				}
			} catch (IOException e) {
				logger.error("CutPic:", e);
				return false;
			}
		}
	}
}
