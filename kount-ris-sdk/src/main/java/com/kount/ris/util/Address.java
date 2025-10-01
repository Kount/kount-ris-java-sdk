package com.kount.ris.util;

/**
 * An class representing a street address.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class Address {

	/**
	 * Address, line 1.
	 */
	protected String address1;
	/**
	 * Address, line 2.
	 */
	protected String address2;
	/**
	 * City.
	 */
	protected String city;
	/**
	 * State.
	 */
	protected String state;
	/**
	 * Postal code.
	 */
	protected String postalCode;
	/**
	 * Country.
	 */
	protected String country;
	/**
	 * Premise.
	 */
	protected String premise;
	/**
	 * Street.
	 */
	protected String street;

	/**
	 * Default address constructor.
	 */
	public Address() {
	}

	/**
	 * Address constructor.
	 * 
	 * @param add1
	 *            Address 1
	 * @param add2
	 *            Address 2
	 * @param cty
	 *            City
	 * @param st
	 *            State
	 * @param pc
	 *            Postal code
	 * @param ctry
	 *            Country
	 */
	public Address(String add1, String add2, String cty, String st, String pc, String ctry) {
		this.address1 = add1;
		this.address2 = add2;
		this.city = cty;
		this.state = st;
		this.country = ctry;
		this.postalCode = pc;
	}

	/**
	 * Set address 1.
	 * 
	 * @param add1
	 *            Address 1
	 */
	public void setAddress1(String add1) {
		this.address1 = add1;
	}

	/**
	 * Set address 2.
	 * 
	 * @param add2
	 *            Address 2
	 */
	public void setAddress2(String add2) {
		this.address2 = add2;
	}

	/**
	 * Set city.
	 * 
	 * @param cty
	 *            City
	 */
	public void setCity(String cty) {
		this.city = cty;
	}

	/**
	 * Set country.
	 * 
	 * @param ctry
	 *            Country
	 */
	public void setCountry(String ctry) {
		this.country = ctry;
	}

	/**
	 * Set the state.
	 * 
	 * @param st
	 *            State
	 */
	public void setState(String st) {
		this.state = st;
	}

	/**
	 * Set the postal code.
	 * 
	 * @param pc
	 *            Postal code
	 */
	public void setPostalCode(String pc) {
		this.postalCode = pc;
	}

	/**
	 * Set the premise.
	 * 
	 * @param prem
	 *            Premise
	 */
	public void setPremise(String prem) {
		this.premise = prem;
	}

	/**
	 * Set the street.
	 * 
	 * @param str
	 *            Street
	 */
	public void setStreet(String str) {
		this.street = str;
	}

	/**
	 * Get address 1.
	 * 
	 * @return Address 1
	 */
	public String getAddress1() {
		return this.address1;
	}

	/**
	 * Get address 2.
	 * 
	 * @return Address 2
	 */
	public String getAddress2() {
		return this.address2;
	}

	/**
	 * Get city.
	 * 
	 * @return City
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Get state.
	 * 
	 * @return State
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * Get postal code.
	 * 
	 * @return Postal code
	 */
	public String getPostalCode() {
		return this.postalCode;
	}

	/**
	 * Get street.
	 * 
	 * @return Street
	 */
	public String getStreet() {
		return this.street;
	}

	/**
	 * Get premise.
	 * 
	 * @return Premise
	 */
	public String getPremise() {
		return this.premise;
	}

	/**
	 * Get country.
	 * 
	 * @return Country
	 */
	public String getCountry() {
		return this.country;
	}
}
