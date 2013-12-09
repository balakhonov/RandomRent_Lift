package balakhonov.util

import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.security.MessageDigest

object Util {

  def chopString(s: String, n: Int): String = {
    s.dropRight(s.length() - n) + " ..."
  }

  def toMd5(s: String): String = {
    new java.math.BigInteger(1, MessageDigest.getInstance("MD5").digest(s.getBytes)).toString(16)
  }

  def getExtension(fileName: String): String = {
    fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())
  }

  def resize(originalImage: BufferedImage, maxWidth: Int, maxHeight: Int): BufferedImage = {
    val height = originalImage.getHeight
    val width = originalImage.getWidth

    if (width <= maxWidth && height <= maxHeight)
      originalImage
    else {
      var newWidth = 0
      var newHeight = 0

      if (width > height) {
        val ratio: Double = width.toDouble / maxWidth.toDouble
        newHeight = (height / ratio).intValue
        newWidth = maxWidth
      } else {
        val ratio: Double = height.toDouble / maxHeight.toDouble
        newWidth = (width / ratio).intValue
        newHeight = maxHeight
      }

      val scaledBI = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
      val g = scaledBI.createGraphics
      g.setComposite(AlphaComposite.Src)
      g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
      g.dispose
      scaledBI
    }
  }

  val adTypes = Map[Int, String](0 -> "Все", 1 -> "Сниму", 2 -> "Сдам" /*, 3 -> "Куплю", 4 -> "Продам"*/ )
  val periods = Map[Int, String](0 -> "Все", 1 -> "Длительно", 2 -> "Посуточно")

  /**
   * Number of rooms
   */
  val roomTypes = Map[Int, String](1 -> "1", 2 -> "2", 3 -> "3", 4 -> "Больше");
}