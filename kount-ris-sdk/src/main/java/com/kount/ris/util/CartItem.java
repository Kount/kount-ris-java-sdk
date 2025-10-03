package com.kount.ris.util;

/**
 * A class that represents a shopping cart item.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class CartItem {
	
	public static final String PRODUCT_TYPE = "PROD_TYPE";
	public static final String PRODUCT_ITEM = "PROD_ITEM";
	public static final String PRODUCT_DESCRIPTION = "PROD_DESC";
	public static final String PRODUCT_QUANTITY = "PROD_QUANT";
	public static final String PRODUCT_PRICE = "PROD_PRICE";
	
	/**
	 * Product type.
	 */
	private String productType;
	/**
	 * Item name.
	 */
	private String itemName;
	/**
	 * Description.
	 */
	private String description;
	/**
	 * Quantity.
	 */
	private int quantity;
	/**
	 * Price.
	 */
	private int price;

	/**
	 * Constructor for a cart item.
	 * 
	 * @param type
	 *            the product type
	 * @param name
	 *            the name of the item
	 * @param desc
	 *            description
	 * @param qty
	 *            quantity
	 * @param prc
	 *            the price of the item
	 */
	public CartItem(String type, String name, String desc, int qty, int prc) {
		this.productType = type;
		this.itemName = name;
		this.description = desc;
		this.quantity = qty;
		this.price = prc;
	}

	/**
	 * @return the product type.
	 */
	public String getProductType() {
		return this.productType;
	}

	/**
	 * @return the name of the item.
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * @return description of the item.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return quantity of the item.
	 */
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * @return price of the item.
	 */
	public int getPrice() {
		return this.price;
	}

	/**
	 * @return String representation of this shopping cart item.
	 */
	public String toString() {

        return "Product Type: " + this.productType + "\n" +
                "Item Name: " + this.itemName + "\n" +
                "Description: " + this.description + "\n" +
                "Quantity: " + this.quantity + "\n" +
                "Price: " + this.price + "\n" +
                "\n";
	}

}
