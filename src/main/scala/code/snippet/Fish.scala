package code.snippet

import scala.xml.NodeSeq

class Fish {
	def randomFishPic (content : NodeSeq) : NodeSeq ={
        <img src="{randomFishPicUri}" />  
	}
}