/**
 * 
 */
package com.pmk;

import java.io.Serializable;

/**
 * @author manteshkumar.p
 *
 */
public class Employee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 600976821724740453L;
	
	private String name;
	
	private String id;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name
	 * @param id
	 */
	public Employee(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

	/**
	 * 
	 */
	public Employee() {
		super();
	}
}
