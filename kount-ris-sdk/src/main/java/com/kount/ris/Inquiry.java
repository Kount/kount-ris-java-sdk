package com.kount.ris;

import java.util.Collection;
import java.util.Iterator;

import com.kount.ris.util.Address;
import com.kount.ris.util.CartItem;
import com.kount.ris.util.CurrencyType;
import com.kount.ris.util.InquiryMode;
import com.kount.ris.util.ShippingType;

/**
 * RIS initial inquiry class.
 * <p/>
 * Contains specific methods for setting various inquiry properties 
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class Inquiry extends Request {

	/**
	 * Class constructor. Sets the RIS mode to "Inquiry" ("Q"), sets currency to
	 * "USD", and sets the Java SDK identifier. The mode and currency can be
	 * adjusted by called setMode() and setCurrency methods respectively.
	 */
	public Inquiry() {
		super();
		setMode(InquiryMode.INITIAL_INQUIRY);
		setCurrency("USD");
		params.put("SDK_VERSION", "Sdk-Ris-Java-0632-20170321T1355");
	}

	/**
	 * Set cash amount of any feasible goods.
	 * 
	 * @param cash
	 *            long Cash amount of any feasible goods
	 * @return this
	 */
	public Inquiry setCash(long cash) {
		this.params.put("CASH", String.valueOf(cash));
		return this;
	}

	/**
	 * Set the date of birth in the format YYYY-MM-DD.
	 * 
	 * @param dob
	 *            Date of birth
	 * @return this
	 */
	public Inquiry setDateOfBirth(String dob) {
		this.params.put("DOB", dob);
		return this;
	}

	/**
	 * Set the gender. Either M(ale) of F(emale).
	 * </p>
	 * Acceptable values: 'M' or 'F'
	 * 
	 * @param gender
	 *            Gender
	 * @return this
	 */
	public Inquiry setGender(char gender) {
		this.params.put("GENDER", Character.toString(gender));
		return this;
	}

	/**
	 * Set a user defined field.
	 * 
	 * @param label
	 *            The name of the user defined field
	 * @param value
	 *            The value of the user defined field
	 * @return this
	 */
	public Inquiry setUserDefinedField(String label, String value) {
		this.params.put("UDF[" + label + "]", value);
		return this;
	}

	/**
	 * Set the request mode.
	 * </p>
	 * Acceptable values are: "Q", "P", "W", "J"
	 * 
	 * @param mode
	 *            Mode of the request
	 * @return this
	 */
	public Inquiry setMode(InquiryMode mode) {
		this.params.put("MODE", mode.toString());
		return this;
	}

	/**
	 * Set the currency.
	 * 
	 * @param currency
	 *            Type of currency
	 * @return this
	 * @deprecated Version 4.3.5. Use method
	 *             com.kount.ris.Inquiry.setCurrency(String currency) : Inquiry.
	 */
	public Inquiry setCurrency(CurrencyType currency) {
		this.logger.info("The method " + "com.kount.ris.Inquiry.setCurrency(CurrencyType) : Inquiry is "
				+ "deprecated. Use " + "com.kount.ris.Inquiry.setCurrency(String) : Inquiry instead.");
		this.params.put("CURR", currency.toString());
		return this;
	}

	/**
	 * Set the three character ISO-4217 currency code.
	 * 
	 * @param currency
	 *            Currency code, eg, USD, EUR...
	 * @return this
	 */
	public Inquiry setCurrency(String currency) {
		this.params.put("CURR", currency);
		return this;
	}

	/**
	 * Set the total amount in lowest possible denomination of currency.
	 * 
	 * @param total
	 *            Transaction amount in lowest possible denomination of given currency
	 * @return this
	 */
	public Inquiry setTotal(long total) {
		this.params.put("TOTL", String.valueOf(total));
		return this;
	}

	/**
	 * Set the email address of the client.
	 * 
	 * @param email
	 *            Email address of the client
	 * @return this
	 */
	public Inquiry setEmail(String email) {
		this.params.put("EMAL", email);
		return this;
	}

	/**
	 * Set the name of the client or company.
	 * 
	 * @param customerName
	 *            Name of the client or company
	 * @return this
	 */
	public Inquiry setCustomerName(String customerName) {
		this.params.put("NAME", customerName);
		return this;
	}

	/**
	 * Set the billing address.
	 * 
	 * @param address
	 *            The billing address
	 * @return this
	 */
	public Inquiry setBillingAddress(Address address) {
		this.params.put("B2A1", address.getAddress1());
		this.params.put("B2A2", address.getAddress2());
		this.params.put("B2CI", address.getCity());
		this.params.put("B2ST", address.getState());
		this.params.put("B2PC", address.getPostalCode());
		this.params.put("B2CC", address.getCountry());
		this.params.put("BPREMISE", address.getPremise());
		this.params.put("BSTREET", address.getStreet());
		return this;
	}

	/**
	 * Set the shipping address.
	 * 
	 * @param address
	 *            Shipping address
	 * @return this
	 */
	public Inquiry setShippingAddress(Address address) {
		this.params.put("S2A1", address.getAddress1());
		this.params.put("S2A2", address.getAddress2());
		this.params.put("S2CI", address.getCity());
		this.params.put("S2ST", address.getState());
		this.params.put("S2PC", address.getPostalCode());
		this.params.put("S2CC", address.getCountry());
		this.params.put("SPREMISE", address.getPremise());
		this.params.put("SSTREET", address.getStreet());
		return this;
	}

	/**
	 * Set the billing phone number.
	 * 
	 * @param billingPhoneNumber
	 *            Billing phone number
	 * @return this
	 */
	public Inquiry setBillingPhoneNumber(String billingPhoneNumber) {
		this.params.put("B2PN", billingPhoneNumber);
		return this;
	}

	/**
	 * Set the shipping phone number.
	 * 
	 * @param shippingPhoneNumber
	 *            Shipping phone number
	 * @return this
	 */
	public Inquiry setShippingPhoneNumber(String shippingPhoneNumber) {
		this.params.put("S2PN", shippingPhoneNumber);
		return this;
	}

	/**
	 * Set the shipping name.
	 * 
	 * @param shippingName
	 *            Shipping name
	 * @return this
	 */
	public Inquiry setShippingName(String shippingName) {
		this.params.put("S2NM", shippingName);
		return this;
	}

	/**
	 * Set the shipping email.
	 * 
	 * @param shippingEmail
	 *            Shipping email
	 * @return this
	 */
	public Inquiry setShippingEmail(String shippingEmail) {
		this.params.put("S2EM", shippingEmail);
		return this;
	}

	/**
	 * Set the unique ID or cookie set by merchant.
	 * 
	 * @param uniqueCustomerId
	 *            Customer-unique ID or cookie set by merchant.
	 * @return this
	 */
	public Inquiry setUniqueCustomerId(String uniqueCustomerId) {
		this.params.put("UNIQ", uniqueCustomerId);
		return this;
	}

	/**
	 * Set the IP address.
	 * 
	 * @param ipAddress
	 *            IP Address of the client
	 * @return this
	 */
	public Inquiry setIpAddress(String ipAddress) {
		this.params.put("IPAD", ipAddress);
		return this;
	}

	/**
	 * Set the user agent string of the client.
	 * 
	 * @param userAgent
	 *            Useragent string of the client
	 * @return this
	 */
	public Inquiry setUserAgent(String userAgent) {
		this.params.put("UAGT", userAgent);
		return this;
	}

	/**
	 * Set the timestamp (in seconds) since the UNIX epoch for when the UNIQ
	 * value was set.
	 * 
	 * @param timeStamp
	 *            The timestamp
	 * @return this
	 */
	public Inquiry setEpoch(long timeStamp) {
		this.params.put("EPOC", String.valueOf(timeStamp));
		return this;
	}

	/**
	 * Set shipment type.
	 * </p>
	 * Accepted values: "SD" - Same Day, "ND" - Next Day, "2D" - Second Day, "ST" - Standard
	 * 
	 * @param shippingType
	 *            Ship type
	 * @return this
	 */
	public Inquiry setShippingType(ShippingType shippingType) {
		this.params.put("SHTP", shippingType.toString());
		return this;
	}

	/**
	 * Set the anid.
	 * </p>
	 * Automatic Number Identification (ANI) submitted with order. If the ANI cannot be determined, 
	 * merchant must pass 0123456789 as the ANID. This field is only valid for MODE=P RIS submissions.
	 * 
	 * @param anid
	 *            Anid of the client
	 * @return this
	 */
	public Inquiry setAnid(String anid) {
		this.params.put("ANID", anid);
		return this;
	}

	/**
	 * Set the name of the company.
	 * 
	 * @param name
	 *            Name of the company
	 * @return this
	 */
	public Inquiry setName(String name) {
		this.params.put("NAME", name);
		return this;
	}

	/**
	 * Set the website.
	 * 
	 * @param site
	 *            the website
	 * @return this
	 */
	public Inquiry setWebsite(String site) {
		this.params.put("SITE", site);
		return this;
	}

	/**
	 * Set the shopping cart.
	 * 
	 * @param cart
	 *            Collection of CartItems in the shopping cart
	 * @return this
	 */
	public Inquiry setCart(Collection<CartItem> cart) {
		Iterator<CartItem> itr = cart.iterator();
		int index = 0;
		while (itr.hasNext()) {
			CartItem item = itr.next();
			this.params.put("PROD_TYPE[" + index + "]", item.getProductType());
			this.params.put("PROD_ITEM[" + index + "]", item.getItemName());
			this.params.put("PROD_DESC[" + index + "]", item.getDescription());
			this.params.put("PROD_QUANT[" + index + "]", new Integer(item.getQuantity()).toString());
			this.params.put("PROD_PRICE[" + index + "]", new Integer(item.getPrice()).toString());
			index += 1;
		}
		return this;
	}
}
