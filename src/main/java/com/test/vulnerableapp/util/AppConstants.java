package com.test.vulnerableapp.util;

public class AppConstants {
	
	public static final String APP_NAME = "CloudFileUpload";
	// salt strings
	public static final String SALT_PRE_STRING = "$2u!*t&p$l#k@3#$";
	public static final String SALT_POST_STRING ="$#45a_g4~em&#w$";
	public static final String ULPOAD_FILE_PREFIX="cfp-";
	
	
	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_DELETED = "DELETED";
	public static final int YES =1;
	public static final int NOT =0;
	
	// Role id
	public static final int ADMIN_ROLE_ID =1001;
	public static final int CUSTOMER_ROLE_ID =1002;
	
	// permissions
	public static final int READ_PERMISSION=1;
	public static final int WRITE_PERMISSION=2;
	public static final int READ_WRITE_PERMISSION=3;
	
	// Credit Card types
	public static final String CARD_MASTER = "Mastercard";
	public static final String CARD_VISA = "Visa";
	public static final String CARD_AMEX = "Amex";
	
	// Shipping Types
	public static final String SHIPPING_NORMAL = "Normal";
	public static final String SHIPPING_EXPRESS = "Express";
	public static final String SHIPPING_SUPER_EXPRESS = "Super Express";
	
	// Application URLs
	public static final String URL_HOME = "/home";
	public static final String URL_LOGIN = "/login";
	public static final String URL_OLDLOGIN = "/oldlogin";
	public static final String URL_REGISTRATION = "/registration";
	public static final String URL_USER_HOME = "/order";
	public static final String URL_PAYMENT = "/payment";
	public static final String URL_DELIVERY = "/delivery";
	public static final String URL_SHIPPING = "/shipping";
	public static final String URL_USER_FEEDBACK = "/feedback";
	public static final String URL_CONFIRMATION = "/confirmation";
	public static final String URL_CONTACT_US = "/contact_us";
	public static final String URL_ABOUT_US = "/about_us";
	public static final String URL_PROFILE = "/profile";
	public static final String URL_CART = "/cart";
	public static final String URL_NEW_ACCOUNT = "/newaccount";
	public static final String URL_PASSWORD_RECOVERY = "/passwordRecovery";
	
	public static final String URL_ADMIN_LOGIN = "/admin/login";
	public static final String URL_ADMIN_USER = "/admin/user";
	public static final String URL_ADMIN_USERS = "/admin/users";
	public static final String URL_ADMIN_ORDERS = "/admin/orders";
	public static final String URL_ADMIN_FEEDBAKS = "/admin/feedbacks";
	public static final String URL_ADMIN_SETTINGS = "/admin/settings";
	public static final String URL_ADMIN_DBACCES = "/admin/dbaccess";
	
	public static final String URL_ERROR = "/error";
	
	
	// messages
	public static final String MSG_REQUIRED_USERNAME = "Username is required!";
	public static final String MSG_REQUIRED_PASSWORD = "Password is required!";
	public static final String MSG_BAD_LOGIN_INPUT = "Bad login credentials!";
	public static final String MSG_LOGOUT_SUCCESS = "Logout successfully.";
	public static final String MSG_FORM_CONTAINS_EROOR = "The form contains error.";
	public static final String MSG_USER_SAVED="The User information is stored successfully.";
	public static final String MSG_USER_UPDATED="The User information is updated successfully.";
	public static final String MSG_USER_DELETED="The User is deleted successfully.";
	public static final String MSG_USER_NOT_EXIST="User does not exist.";
	public static final String MSG_SELECT_DROPDOWN_USER="Please select the User from dropdown list.";
	public static final String MSG_INVALID_USER="Please login to access application.";
	
	public static final String MSG_INVALID_PRODUCT="Please select the valid Product, Color, Size and Quantity(between 0 to 50).";
	public static final String MSG_INVALID_PAYMENT="Please enter the payment information.";
	public static final String MSG_INVALID_DELIVERY_INFO="Please enter the delivery details.";
	public static final String MSG_INVALID_SHIPPING_INFO="Please select the shipping details.";
	
	public static final String MSG_FLIGHT_ADDED="The flight has been added to your cart";
	
	public static final String MSG_UNAUTHORIZED_READ_FILE="User is not authorized to read this file.";
	public static final String MSG_UNAUTHORIZED_WRITE_FILE="User is not authorized to edit this file.";
	public static final String MSG_UNAUTHORIZED_DELETE_FILE="User is not authorized to delete this file.";
	
	public static final String FEEDBACK_SAVED="The Feedback is submitted successfully.";
	
	
	// Error Codes
	public static final int INVALID_LOGIN_CODE = 9001;
	public static final int INVALID_READ_AUTH_CODE = 9002;
	public static final int INVALID_FILE_CODE = 9003;
	public static final Object MSG_ACCOUNT_LOCKED = "Your account is locked. Wait 30 seconds and try again.";
	
}
