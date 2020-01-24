package com.test.vulnerableapp.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import javax.xml.bind.DatatypeConverter;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.test.vulnerableapp.model.DeliveryDetails;
import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.OrderedFlightInfo;
import com.test.vulnerableapp.model.PaymentDetails;
import com.test.vulnerableapp.model.ShippingDetails;
import org.springframework.web.multipart.MultipartFile;

public class AppUtil {
	private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);

	private static final String ALGO = "AES";
    private static final byte[] keyValue =
        new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
	/**
	 * create the salt with combination of random id and some salt string constants
	 * @return
	 */
	public static String getSalt(){
		String salt = AppConstants.SALT_PRE_STRING;
		return salt;
	}

	/**
	 * return the plain text password
	 * @param salt
	 * @param password
	 * @return
	 */
	public static String getEncryptedPassword(String password){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] encodedBytes = md.digest(password.getBytes());

			return DatatypeConverter.printHexBinary(encodedBytes).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
		}

	    return null;
	}

	/**
	 * return the plain text password
	 * @param salt
	 * @param password
	 * @return
	 */
	public static String getEncryptedKey(String key){
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		key = AppConstants.SALT_PRE_STRING + key;
		return bcrypt.encode(key);
	}

	/**
	 * Checks the login password is same or not
	 * @param salt
	 * @param raw_password
	 * @param db_password
	 * @return
	 */
	public static boolean isValidPassword(String raw_password, String db_password){

		String dbPasswordNew = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] encodedBytes = md.digest(raw_password.getBytes(StandardCharsets.UTF_8));
			dbPasswordNew = DatatypeConverter.printHexBinary(encodedBytes).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
		}
        return db_password.equals(dbPasswordNew);
    }

	/**
	 * Checks the key is same or not
	 * @param raw_key
	 * @param db_key
	 * @return
	 */
	public static boolean isValidKey(String key, String db_key){
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		key=AppConstants.SALT_PRE_STRING + key;
		return bcrypt.matches(key, db_key);
	}

	/**
	 * encode the String to its corresponding HTML
	 * @param string
	 * @return
	 */
	public static String encodeForHTML(String string){
		return ESAPI.encoder().encodeForHTML(string);
	}

	/**
	 * Check for valid Alphanumeric String (e.g John12)
	 * @param string
	 * @return
	 */
	public static boolean isValidInput(String context, String param, String regExp, int maxLength, boolean allowNull) throws ValidationException{
		boolean isValid = true;
		try {
			isValid = ESAPI.validator().isValidInput(context, param, regExp, maxLength, allowNull);
		} catch (IntrusionException e) {
			isValid=false;
		}
		return isValid;
	}

	/**
	 * Check for valid Integer value
	 * @param context
	 * @param param
	 * @param minValue
	 * @param maxValue
	 * @param allowNull
	 * @return
	 * @throws ValidationException
	 */
	public static boolean isValidInteger(String context, String param, int minValue, int maxValue, boolean allowNull) throws ValidationException{
		boolean isValid = true;
		try {
			isValid = ESAPI.validator().isValidInteger(context, param, minValue, maxValue, allowNull);
		} catch (IntrusionException e) {
			isValid=false;
		}
		return isValid;
	}

	/**
	 * Check for valid Double value
	 * @param context
	 * @param param
	 * @param minValue
	 * @param maxValue
	 * @param allowNull ^[1-9]\d*(\.\d{1,2})?$
	 * @return
	 * @throws ValidationException
	 */
	public static boolean isValidPrice(String value){
		return value.matches("^[1-9]\\d*(\\.\\d{1,2})?$");
	}



	 /**
	  * Validate the flight's quantity in the order
	 */
	 public static boolean isValidQuantity(String quantity){
		boolean isValid = true;
		/*if(quantity>0 && quantity<=50);*/
		try{
			isValid = ESAPI.validator().isValidInteger("Quantity", quantity.trim(), 1, 10, false);
		}catch(IntrusionException e){
			isValid=false;
		}
		return isValid;
	 }

	 /**
	  * Validate the flight's quantity in the order
	 */
	 public static boolean isValidQuantity(int quantity){
		return isValidQuantity(quantity+"");
	 }

	 /**
	  * returns the orderDetails object stored in session.
	  * @param session
	  * @return
	  */
	 public static OrderDetails getOrderDetailsFromSession(HttpSession session){
		 return (OrderDetails)session.getAttribute("orderDetails");
	 }

	 /**
	  * Verify the valid quantity of flights in order.
	  * @param orderDetails
	  * @return
	  */
	 public static boolean isOrderHasFlight(OrderDetails orderDetails){
         return orderDetails.getOrderedFlights().size() >= 1;
     }

	 /**
	  * Generate a 9 digit unique number used as order id
	  * @return
	  */
	 public static long getGeneratedOrderId(){
		  long timeSeed = System.nanoTime(); // to get the current date time value
	      double randSeed = Math.random() * 1000; // random number generation
	      long midSeed = (long) (timeSeed * randSeed); // mixing up the time and rand number.
	      String s = midSeed + "";
	      String subStr = s.substring(0, 9);
	      long finalOrderId = Long.parseLong(subStr);    // integer value
	      return finalOrderId;
	 }

	 /**
	  * Return credit card number as it is.
	  * @param args
	 */
	 public static String encrypt(String Data) throws Exception {
	    return Base64.getEncoder().encodeToString(Data.getBytes());
    }

	/**
	  * @param args
	*/
	public static String decrypt(String encryptedData) throws Exception {
		byte[] originalData = Base64.getDecoder().decode(encryptedData);
        return new String(originalData);
	}

	/**
	  * Change the credit card number as standard view like *****1234, show only last 4 digit.
	  * @param args
	*/
	public static String getCardNumberToView(String cardNumber){
		int length = cardNumber.length();
		int nonViewableChar = length-4;
		String viewableNumbers = cardNumber.substring(nonViewableChar);
		StringBuilder card = new StringBuilder();
		for(int i=1;i<=nonViewableChar;i++){
			card.append("*");
		}
		card.append(viewableNumbers);
		return card.toString();
	}

	/**
	 * Generate private key for encryption and decryption.
	 * @return
	 * @throws Exception
	 */
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    /**
     * Encoded all ordered flight information in HTML
     * @param listOfFlightInfo
     * @return
    */
    public static List<OrderedFlightInfo> getHTMLEncodedOrderedFlightsInfo(List<OrderedFlightInfo> listOfFlightInfo){
		List<OrderedFlightInfo> encodedList = new ArrayList<OrderedFlightInfo>();
    	//OrderedFlightInfo orderedFlight = null;
    	for(OrderedFlightInfo orderedFlightInfo : listOfFlightInfo){
    		//orderedFlightInfo.setColor(encodeForHTML(orderedFlightInfo.getColor()));
    		//orderedFlightInfo.setFlightName(encodeForHTML(orderedFlightInfo.getFlightName()));
    		encodedList.add(orderedFlightInfo);
    	}
    	return encodedList;
    }

    /**
     * Encoded Payment details information in HTML
     * @param listOfFlightInfo
     * @return
    */
    public static PaymentDetails getHTMLEncodedPaymentDetails(PaymentDetails paymentDetails){
    	paymentDetails.setCardNumber(encodeForHTML(paymentDetails.getCardNumber()));
    	//paymentDetails.setCardOwner(ESAPI.encoder().encodeForHTML(paymentDetails.getCardOwner()));
    	//paymentDetails.setCardType(ESAPI.encoder().encodeForHTML(paymentDetails.getCardType()));
    	//paymentDetails.setTotalAmount(ESAPI.encoder().encodeForHTML(paymentDetails.getTotalAmount()));
    	return paymentDetails;
    }

    /**
     * Encoded Delivery details information in HTML
     * @param listOfFlightInfo
     * @return
    */
    public static DeliveryDetails getHTMLEncodedDeliveryDetails(DeliveryDetails deliveryDetails){
    	deliveryDetails.setCity(encodeForHTML(deliveryDetails.getCity()));
    	deliveryDetails.setCountry(encodeForHTML(deliveryDetails.getCountry()));
    	deliveryDetails.setHouseNumber(encodeForHTML(deliveryDetails.getHouseNumber()));
    	deliveryDetails.setStreet(encodeForHTML(deliveryDetails.getStreet()));
    	return deliveryDetails;
    }

    /**
     * Encoded Shipping details information in HTML
     * @param listOfFlightInfo
     * @return
    */
    public static ShippingDetails getHTMLEncodedShippingDetails(ShippingDetails shippingDetails){
    	shippingDetails.setType(encodeForHTML(shippingDetails.getType()));
    	return shippingDetails;
    }

    /**
     * Validate the shipping type
     * @param shippingType
     * @return
     */
    public static boolean isValidShippingType(String shippingType){
    	if(shippingType.equalsIgnoreCase(AppConstants.SHIPPING_EXPRESS)){
    		return true;
    	}else if(shippingType.equalsIgnoreCase(AppConstants.SHIPPING_NORMAL)){
    		return true;
    	}else return shippingType.equalsIgnoreCase(AppConstants.SHIPPING_SUPER_EXPRESS);
    }

	 /**
	  * check if file size is valid or not
	  * file size is defined in property file
	  * @return
	  */
	 public static boolean isValidFileSize(long fileSize, long maxSize){
		 if(maxSize==0)return true;
         return fileSize <= maxSize;
     }

	 /**
	  * check if file extension is valid or not
	  * invalid file extensions are defined in property file
	  * @return
	  */
	 public static boolean isValidFileExtenstion(String fileName, String fileExtensions){
		if(fileExtensions.isEmpty())return false;
		StringTokenizer st=new StringTokenizer(fileExtensions, ",");
		String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
		while(st.hasMoreElements()){
			String validExt= st.nextToken().trim();
			if(fileExt.equalsIgnoreCase(validExt))
				return true;
		}
		return false;
	 }

	 public static String sanitizeInput(String input)
     {
         input = input.replaceAll("'", "''");
         return input;
     }

	 public static String filterInput(String input)
     {
		 input = input.toLowerCase();
		 while(input.contains("from") || input.contains("where") || input.contains("and") || input.contains("or") || input.contains("as"))
             input = input.replaceAll("from", "").replaceAll("where", "").replaceAll("and", "").replaceAll("or", "").replaceAll("as", "");

         return input;
     }

	public static void store(Path rootLocation, MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new ApplicationException(1111, "Failed to store empty file " + file.getOriginalFilename());
			}
			Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new ApplicationException(1111, "Failed to store file " + file.getOriginalFilename(), e);
		}
	}
}