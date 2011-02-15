package com.codecommit.antixml

import org.specs._

object ValidationSpecs extends Specification {
  import XML._
  "A validating XML parser" should {
    "complain about a wrong system identifier" in {
      fromString("""<!DOCTYPE test SYSTEM "urn:nothing:to:see:here"><test/>""") must throwA[java.net.MalformedURLException]
    }
  }
  "A non-validating XML parser" should {
    implicit val factory = newNonvalidatingFactory
    "not complain about a wrong system identifier" in {
      fromString("""<!DOCTYPE test SYSTEM "urn:nothing:to:see:here"><test/>""") mustEqual NodeSeq(elem("test"))
    }
  }
  def elem(name: String, children: Node*) = Elem(None, name, Map(), NodeSeq(children: _*))
}
