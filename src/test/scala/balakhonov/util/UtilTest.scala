package balakhonov.util

import org.junit.Test
import java.io.File
import javax.imageio.ImageIO

class UtilTest {

  @Test
  def resizeTest() = {
    val avsolutePath = "/home/yuri/RandomRent/images/" + "variant1-2.jpg";
    val extension = avsolutePath.substring(avsolutePath.lastIndexOf(".") + 1, avsolutePath.length())
    var img = new File(avsolutePath)

    if (img.exists()) {
      val originalImage = ImageIO.read(img)
      var bi = Util.resize(originalImage, 800, 600);
      ImageIO.write(bi, extension, new File("/home/yuri/RandomRent/images/test-new." + extension))
    }

    println("none")
  }
}