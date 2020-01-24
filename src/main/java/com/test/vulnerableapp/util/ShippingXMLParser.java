package com.test.vulnerableapp.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.test.vulnerableapp.model.ShippingPrice;

public class ShippingXMLParser {
	private static final Logger logger = LoggerFactory.getLogger(ShippingXMLParser.class);
	private Document dom = null;
	private String xmlFile="";
	private String path="";
	
	public ShippingXMLParser(){
		
	}
	
	public ShippingXMLParser(String xmlFile, String path) throws Exception {
		this.xmlFile = xmlFile;
		this.path = path;
		parseXmlFile(getXmlFile());
	}
 
 public ShippingPrice getShippingRecordByCity(String cityName){
	 List<ShippingPrice> listOfSippingPrice = parseDocument(true, cityName);
	 if(listOfSippingPrice.size()==0)return new ShippingPrice();
	 return listOfSippingPrice.get(0);
 }
 
 public ShippingPrice getPriceByCityAndShippingName(String cityName, String shippingName){
	 if(!AppUtil.isValidShippingType(shippingName))return new ShippingPrice();
	 List<ShippingPrice> listOfSippingPrice = parseDocument(true, cityName, true, shippingName);
	 if(listOfSippingPrice.size()==0)return new ShippingPrice();
	 return listOfSippingPrice.get(0);
 }
 
 public List<ShippingPrice> getAllShippingInfo(){
	 List<ShippingPrice> listOfSippingPrice = parseDocument();
	 if(listOfSippingPrice.size()==0)return null;
	 return listOfSippingPrice;
 }
 
 /**
  * Instantiate the object to parse the document
  * @param file
  */
 private void parseXmlFile(String file) throws Exception{
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature("http://xml.org/sax/features/external-general-entities",true);
			dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",true);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//parse using builder to get DOM representation of the XML file
			URL url = this.getClass().getClassLoader().getResource(xmlFile);
			Path uploadDirPath = Paths.get(url.toURI().resolve(".").getPath());
			String xml_path = uploadDirPath.toString() + '/' + xmlFile;
					File xml_file = new File(xml_path);
			dom = db.parse(xml_file);

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			throw pce;
		}catch(SAXException se) {
			se.printStackTrace();
			throw se;
		}catch(IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw e;
		}
 }
 
 	/**
 	 * It will return the list of all cities records with shipping details
 	 * @return
 	 */
 	private List<ShippingPrice> parseDocument(){
 		return parseDocument(false, "", false, "");
 	}
 	
 	/**
 	 * It will return the shipping details for the particular city.
 	 * @param isFilterByCity
 	 * @param filterCityName
 	 * @return
 	 */
 	private List<ShippingPrice> parseDocument(boolean isFilterByCity, String filterCityName){
 		return parseDocument(true, filterCityName, false, "");
 	}

 	/**
 	 * It will return the list of all cities records with shipping details
 	 * We can filter records by city and shipping name
 	 * @param isFilterByCity
 	 * @param filterCityName
 	 * @param isFilterByShipping
 	 * @param filterShipping
 	 * @return
 	 */
	private List<ShippingPrice> parseDocument(boolean isFilterByCity, String filterCityName, boolean isFilterByShipping, String filterShipping){
		//get the root element
		Element docEle = dom.getDocumentElement();
		List<ShippingPrice> listPrice = new ArrayList<ShippingPrice>();
		String cityName;
		ShippingPrice shippingPrice = new ShippingPrice();

		//get a nodelist of elements
		NodeList nl = docEle.getElementsByTagName("city");
		for(int i = 0 ; i < nl.getLength();i++) {
			//get the City element
			Element el = (Element)nl.item(i);
			cityName = getCityName(el);
			
			if(isFilterByCity && !cityName.equalsIgnoreCase(filterCityName.trim()))continue;
			
			shippingPrice = getShippingRecord(el, cityName, isFilterByShipping, filterShipping);
			listPrice.add(shippingPrice);
		}
		return listPrice;
	}
	
	/**
	 * It will return the City Name by parsing the xml document.
	 * @param empEl
	 * @return
	 */
	private String getCityName(Element empEl){
		String cityName = getTextValue(empEl,"name");
		return cityName;
	}
	
	/**
	 * I take an city element and read the values in, create
	 * an ShippingPrice object and return it
	 */
	private ShippingPrice getShippingRecord(Element empEl, String cityName, boolean isFilterByShipping, String filterShipping) {
		ShippingPrice shipping = new ShippingPrice();
		//for each <city> element get text or int values of
		//city name ,shipping name and shipping price
		shipping.setName(cityName);
		shipping.setShippingType(getShippingNameAndValue(empEl,"shippingType", isFilterByShipping, filterShipping));
		return shipping;
	}

	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <city><name>Delhi</name></city> xml snippet if
	 * the Element points to city node and tagName is 'shippingType' I will return City Name 'Delhi
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue().trim();
		}
		return textVal;
	}

	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <city><shippingType>John</shippingType></city> xml snippet if
	 * the Element points to city node and tagName is 'shippingType' I will return ShippingName and it's price
	 * e.g Express, 20.2
	 */
	private Map<String, String> getShippingNameAndValue(Element ele, String tagName, boolean isFilterByShipping, String filterShipping) {
		String shippingName = null;
		String price = null;
		Map<String, String> shippingInfo = new HashMap<String, String>();
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			for(int i=0; i<nl.getLength();i++){
				Element el = (Element)nl.item(i);
				shippingName = el.getFirstChild().getNodeValue().trim();
				
				if(isFilterByShipping && !shippingName.equalsIgnoreCase(filterShipping.trim()))continue;
				
				price = el.getAttribute("price").trim();
				shippingInfo.put(shippingName, price);
			}
		}
		return shippingInfo;
	}
	
	public Document getDom() {
		return dom;
	}

	public void setDom(Document dom) {
		this.dom = dom;
	}

	public String getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(String xmlFile) {
		this.xmlFile = xmlFile;
	}

/*

	*//**
	 * Calls getTextValue and returns a int value
	 *//*
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}*/
}