package com.kount.ris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kount.ris.util.CartItem;
import com.kount.ris.util.RisValidationException;
import com.kount.ris.util.ValidationError;

/**
 * RIS input data validator class.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class RisValidator {

	/**
	 * Logger.
	 */
	protected Log logger = LogFactory.getLog(RisValidator.class);

	/**
	 * Default constructor for a RIS validator.
	 */
	public RisValidator() {
	}

	/**
	 * Client side validate the data to be passed to RIS.
	 * 
	 * @param params
	 *            Map of data parameters
	 * @throws RisValidationException
	 *             Ris validation exception
	 * @return List of errors encountered as com.kount.ris.util.ValidationError objects
	 */
	public List<ValidationError> validate(Map<String, String> params) throws RisValidationException {
		List<ValidationError> errors = new ArrayList<>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(this.getClass().getResourceAsStream("/validate.xml"));
			NodeList nodes = doc.getElementsByTagName("param");
			Map<String, List<String>> arrayParams = this.fetchArrayParams(params);

			for (int i = 0; i < nodes.getLength(); i++) {
				Element node = (Element) nodes.item(i);
				String name = node.getAttribute("name");

				// check required field
				Element required = (Element) node.getElementsByTagName("required").item(0);
				if (null != required) {
					NodeList modes = required.getElementsByTagName("mode");

					// check for specific modes
					String currMode = (String) params.get("MODE");
					if (null == params.get(name) && null == arrayParams.get(name)
							&& (0 == modes.getLength() || this.contains(modes, currMode))) {
						this.logger.error("Required field " + name + " missing for mode " + currMode);
						ValidationError requiredErr = new ValidationError(name, currMode);
						errors.add(requiredErr);
					}
				}

				// check max length
				if (params.containsKey(name)) {
					this.validateHelper(params, node, name, errors);
				} else if (arrayParams.containsKey(name)) {
					List<String> keys = arrayParams.get(name);
					for (String key : keys) {
						this.validateHelper(params, node, key, errors);
					}
				}
			}
		} catch (Exception e) {
			this.logger.error("Validation process failed", e);
			throw new RisValidationException("Validation process failed", e);
		}
		return errors;
	}

	/**
	 * Helper method for validate(). Check whether a named value is contained in
	 * the validate XML tree.
	 * 
	 * @param list
	 *            Node that need to be validated
	 * @param value
	 *            String value to check for
	 * @return True if the value is found, false otherwise
	 */
	private boolean contains(NodeList list, String value) {
		for (int i = 0; i < list.getLength(); i++) {
			if (value.equals(list.item(i).getFirstChild().getNodeValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * A helper method that validates the actual data parameters.
	 * 
	 * @param params
	 *            Data parameters to pass to RIS
	 * @param node
	 *            XML element containing the field to validate
	 * @param name
	 *            Name of the data parameter to validate
	 * @param errors
	 *            List of errors
	 */
	private void validateHelper(Map<String, String> params, Element node, String name, List<ValidationError> errors) {
		Node maxLength = node.getElementsByTagName("max_length").item(0);
		if (null != maxLength) {
			int max = Integer.parseInt(maxLength.getFirstChild().getNodeValue());
			String value = params.get(name);
			int length = null != value ? value.length() : 0;
			if (length > max) {
				this.logger.error("Field " + name + " has length " + length + " which is longer than the maximum of "
						+ max);
				ValidationError lengthErr = new ValidationError(name, value, max);
				errors.add(lengthErr);
			}
		}

		// check regular expression
		Node regex = node.getElementsByTagName("reg_ex").item(0);
		if (null != regex) {
			String value = (String) params.get(name);
			String pattern = regex.getFirstChild().getNodeValue();
			if (null == value || !value.matches(pattern)) {
				this.logger.error("Field " + name + " has value " + value + " which does not match the pattern "
						+ pattern);
				ValidationError lengthErr = new ValidationError(name, value, pattern);
				errors.add(lengthErr);
			}
		}
	}

	/**
	 * A helper method to fetch the cart data and place it in a map.
	 * 
	 * @param params
	 *            Map of input data
	 * @return Output map
	 */
	private Map<String, List<String>> fetchArrayParams(Map<String, String> params) {
		Map<String, List<String>> arrayParams = new HashMap<>();
		List<String> prodType = new ArrayList<>();
		List<String> prodName = new ArrayList<>();
		List<String> prodDesc = new ArrayList<>();
		List<String> prodQty = new ArrayList<>();
		List<String> prodPrice = new ArrayList<>();

		for (String key : params.keySet()) {
			if (key.startsWith(CartItem.PRODUCT_TYPE)) {
				prodType.add(key);
			} else if (key.startsWith(CartItem.PRODUCT_ITEM)) {
				prodName.add(key);
			} else if (key.startsWith(CartItem.PRODUCT_DESCRIPTION)) {
				prodDesc.add(key);
			} else if (key.startsWith(CartItem.PRODUCT_QUANTITY)) {
				prodQty.add(key);
			} else if (key.startsWith(CartItem.PRODUCT_PRICE)) {
				prodPrice.add(key);
			}
		}

		arrayParams.put(CartItem.PRODUCT_TYPE, prodType);
		arrayParams.put(CartItem.PRODUCT_ITEM, prodName);
		arrayParams.put(CartItem.PRODUCT_DESCRIPTION, prodDesc);
		arrayParams.put(CartItem.PRODUCT_QUANTITY, prodQty);
		arrayParams.put(CartItem.PRODUCT_PRICE, prodPrice);

		return arrayParams;
	}
}
