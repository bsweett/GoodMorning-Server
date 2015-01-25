package com.goodmorning.enums;

import java.sql.Types;
import java.util.Properties;

import org.hibernate.type.EnumType;

public class HibernateVarCharEnum extends EnumType {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setParameterValues(Properties parameters) {
        parameters.setProperty(TYPE, "" + Types.VARCHAR);
        super.setParameterValues(parameters);
    }
}