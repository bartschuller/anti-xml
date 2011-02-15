package com.codecommit.antixml

import org.xml.sax._

import java.io.{InputStream, StringReader}
import javax.xml.parsers.SAXParserFactory

// TODO named arguments for configuration
object XML {
  def fromString(str: String)(implicit factory: SAXParserFactory): NodeSeq =
    fromInputSource(new InputSource(new StringReader(str)))(factory)

  def fromInputStream(is: InputStream)(implicit factory: SAXParserFactory): NodeSeq =
    fromInputSource(new InputSource(is))(factory)

  def fromInputSource(source: InputSource)(implicit factory: SAXParserFactory): NodeSeq = {
    val parser = factory.newSAXParser
    val handler = new NodeSeqSAXHandler
    parser.parse(source, handler)

    handler.result
  }

  implicit val validatingFactory = newValidatingFactory
  def newValidatingFactory = {
    val factory = SAXParserFactory.newInstance
    factory.setValidating(true)
    factory.setNamespaceAware(true)
    factory
  }
  def newNonvalidatingFactory = {
    val factory = SAXParserFactory.newInstance
    factory.setValidating(false)
    factory.setNamespaceAware(true)
    try {
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false)
    } catch {
      case sex: SAXNotRecognizedException => Unit
      case ex: Exception => ex.printStackTrace
    }
    factory
  }
}
