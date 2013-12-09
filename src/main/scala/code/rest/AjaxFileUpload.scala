package code.rest

import java.awt.AlphaComposite
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.Date

import balakhonov.util.Util._
import code.snippet.AddApartmentForm.uploadedImagesIds
import db.dao.ApartmentImageDao
import db.mapping.ApartmentImage
import javax.imageio.ImageIO
import net.liftweb.http.FileParamHolder
import net.liftweb.http.JsonResponse
import net.liftweb.http.OkResponse
import net.liftweb.http.StreamingResponse
import net.liftweb.http.rest.RestHelper
import net.liftweb.json.Extraction
import net.liftweb.json.JValue

object AjaxFileUpload extends RestHelper {

  val ROOT_IMAGES_DIR = "/home/yuri/RandomRent/images/"

  // Try to make directories if not exists
  val dir = new File(ROOT_IMAGES_DIR);
  if (!dir.exists()) {
    dir.mkdirs();
  }

  serve {
    case "upload" :: Nil Post req => {
      var imageList = List[ApartmentImage]()
      for (file <- req.uploadedFiles) imageList ::= saveImage(file);

      var list = uploadedImagesIds.get
      for (im <- imageList) list ::= im.getId

      uploadedImagesIds.set(list)

      var json2: JValue = Extraction.decompose(imageList)
      JsonResponse(json2)
    }

    case "ap-images" :: fileName :: Nil Get req => {
      var absolutePath = ROOT_IMAGES_DIR + fileName + ".jpg";

      var file = new File(absolutePath);
      if (file.exists()) {
        var fileInputStream = new FileInputStream(file);
        val data = Stream.continually(fileInputStream.read).takeWhile(-1 !=).map(_.toByte).toArray
        println("get img: " + file.getAbsolutePath() + " " + file.length + " " + data.length);

        val headers = ("Content-type" -> "image/jpeg") ::
          ("Content-length" -> data.length.toString) :: Nil

        StreamingResponse(new ByteArrayInputStream(data), () => {}, data.length, headers, Nil, 200)
      } else {
        OkResponse()
      }
    }
  }

  /**
   * Convert to thumbs and save images
   */
  private def saveImage(file: FileParamHolder): ApartmentImage = {
    var newFileName = toMd5(new Date().getTime + "")
    var image = new ApartmentImage(newFileName, 0)
    ApartmentImageDao.save(image)

    var is: InputStream = new ByteArrayInputStream(file.file)
    var bufferedImage: BufferedImage = ImageIO.read(is)

    //convert PNG to JPG
    if ("png".equalsIgnoreCase(getExtension(file.fileName))) {
      //create new blank for JPG
      var newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
      bufferedImage = newBufferedImage;
    }

    bufferedImage = resize(bufferedImage, 800, 600)
    ImageIO.write(bufferedImage, "jpg", new File(ROOT_IMAGES_DIR, newFileName + "-max.jpg"))

    bufferedImage = resize(bufferedImage, 200, 200)
    ImageIO.write(bufferedImage, "jpg", new File(ROOT_IMAGES_DIR, newFileName + "-thumb-200x200.jpg"))

    bufferedImage = resize(bufferedImage, 150, 150)
    ImageIO.write(bufferedImage, "jpg", new File(ROOT_IMAGES_DIR, newFileName + "-thumb-150x150.jpg"))

    // clear data and references
    is.close()
    is = null
    bufferedImage.getGraphics().dispose()
    bufferedImage.flush()
    bufferedImage = null

    image
  }
}