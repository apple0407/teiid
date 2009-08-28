/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package com.metamatrix.admin.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.teiid.adminapi.AdminObject;

import com.metamatrix.admin.AdminPlugin;
import com.metamatrix.common.util.PropertiesUtils;
import com.metamatrix.core.util.StringUtil;

/**
 * Simple objects for the Admin API
 */
public abstract class MMAdminObject implements AdminObject, Serializable {
	
    /**SerialVersion used in serialization*/
    public static final long serialVersionUID = -8280437282118346149L;

    /**
     * The fully-qualified name of the Admin Object.  Will never be null.
     */
    protected String identifier;
    
    /**
     * The parts of the fully-qualified name of the Admin Object. 
     */
    protected String[] identifierParts;
    
    /**
     * The base name of this Admin Object
     */
    protected String name;
    
    
   
	private java.util.Properties props = new java.util.Properties();
	private java.util.Date created;
	private String createdBy = "<default>"; //$NON-NLS-1$;
	private java.util.Date lastUpdated;
	private String lastUpdatedBy = "<default>"; //$NON-NLS-1$;
    /** indicates if component is in operational configuration */
    protected boolean deployed;

    /** indicated if component exists in registry */
    protected boolean registered;
    
    /** indicated if component is enabled */
    protected boolean enabled;
    
    
    
    /**
     * Constructor.
     * @param identifierParts Parts of the fully-qualified identifier of the MetaMatrix Object 
     * @since 4.3
     */
    public MMAdminObject(String ... identifierParts) {
       if (identifierParts == null) {
            throw new IllegalArgumentException(AdminPlugin.Util.getString("AbstractAdminObject.0")); //$NON-NLS-1$
        }
        if (identifierParts.length == 0) {
            throw new IllegalArgumentException(AdminPlugin.Util.getString("AbstractAdminObject.1")); //$NON-NLS-1$
        }
        
        setIdentifier(identifierParts);
    }
    
    
    
    
    /**
     * Get the Name for this Admin Object
     * @return name
     * @since 4.3
     */
    public String getName() {
        return name;
    }
    
    /**
     * Build the Identifer, as a list of its parts
     * @param identifier
     *  
     * @return the Identifer, as a list of its parts
     * @since 4.3
     */
    protected static List buildIdentifierList(String identifier) {
        List result = null;
        if (identifier.indexOf(DELIMITER_CHAR) != -1) {
            result = StringUtil.split(identifier, DELIMITER);
        } else {
            result = new ArrayList(1);
            result.add(identifier);
        }
        return result;
    }
    
    /**
     * Build the Identifer, as an array of its parts
     * @param identifier
     *  
     * @return the Identifer, as an array of its parts
     * @since 4.3
     */
    public static String[] buildIdentifierArray(String identifier) {
        List list = buildIdentifierList(identifier);
        String[] array = new String[list.size()];
        return (String[]) list.toArray(array);
    }
    
    
    
    /**
     * Get the Name from a fully qualified Identifier string
     * @param identifier 
     * @return String of the Name
     * @since 4.3
     */
    public static String getNameFromIdentifier(String identifier) {
        List list = buildIdentifierList(identifier);
        int nameComponentCount = list.size();
        if (nameComponentCount > 0) {
            return (String) list.get(nameComponentCount - 1);
        }
        return null;
    }
    
    /**
     * Get the Parent Name 
     *  
     * @param identifier
     * @return the Parent Name 
     * @since 4.3
     */
    public static String getParentName(String identifier) {
        List list = buildIdentifierList(identifier);
        int nameComponentCount = list.size();
        if( nameComponentCount > 1 ) {
            return (String) list.get(nameComponentCount - 2);
        } 
        return identifier;
        
    }
    
    
    /**
     * Create a fully-qualified identifier from an array of identifier parts. 
     * @param identifierParts
     * @return fully-qualified identifier
     */
    public static String buildIdentifier(String[] identifierParts) {
        StringBuffer results = new StringBuffer();
        int length = identifierParts.length;
        for (int i=0; i<length-1; i++) {
            results.append(identifierParts[i]).append(AdminObject.DELIMITER_CHAR);
        }
        results.append(identifierParts[length-1]);

        return(results.toString());
    } 
    
	/**
	  * Get all the properties for this Object.
	  * 
	  * @return <code>Properties</code> object.
	  */
	public Properties getProperties() {
	 	return props;
	}
	
    /**
     * Get Properties as a String 
     *  
     * @return the Properties as a String 
     * @since 4.3
     */
	public String getPropertiesAsString() {
        String results = ""; //$NON-NLS-1$;
        if (props != null) {
            results = PropertiesUtils.prettyPrint(props);
        }
        return results;
    }
	 
	/**
     * Set the fully-qualified identifier
     * @param identifierParts
     */
    public void setIdentifier(String[] identifierParts) {
        this.identifier = buildIdentifier(identifierParts);
        this.identifierParts = identifierParts;
        this.name = identifierParts[identifierParts.length -1];
    }
    
    
	 
    /**
     * Get the fully-qualified identifier
     * @return the fully-qualified identifier
     */
    public String getIdentifier() {
        return identifier;
    } 
    
    /**
     * Get the fully-qualified identifier as an array of its parts
     * @return the fully-qualified identifier
     */
    public String[] getIdentifierArray() {
        return identifierParts;
    } 
    
    
	
	/**
     * Get MetaMatrix Object as a String
     *  
     * @see java.lang.Object#toString()
     * @since 4.3
     */
	public abstract String toString();
	/**
	 * Get the Creation Date 
	 * 
	 * @return <code>java.util.Date</code> object was created 
	 */
	public java.util.Date getCreatedDate() {
		return created;
	}

	/**
	 * Name of user that created the object 
	 * 
	 * @return <code>String</code> name of user
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Get the Last Revision Date
	 * 
	 * @return <code>java.util.Date</code> object was modified
	 */
	public java.util.Date getLastChangedDate() {
		return lastUpdated;
	}

	/**
	 * Get the name of the Revisor
	 * 
	 * @return <code>String<code> name of the Revisor
	 */
	public String getLastChangedBy() {
		return lastUpdatedBy;
	}
	
	/**
	 * Returns a property value for the given property name
	 * 
	 * @param name of the property value to obtain
	 * @return <code>String<code> property value
	 */
	public String getPropertyValue(String name) {
		return props.getProperty(name);
	}
    
    /** 
     * @return Returns the created.
     * @since 4.3
     */
    public java.util.Date getCreated() {
        return this.created;
    }

    /** 
     * @return Returns the lastUpdated.
     * @since 4.3
     */
    public java.util.Date getLastUpdated() {
        return this.lastUpdated;
    }
    /** 
     * @return Returns the lastUpdatedBy.
     * @since 4.3
     */
    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }
    /** 
     * @return Returns the props.
     * @since 4.3
     */
    public java.util.Properties getProps() {
        return this.props;
    }
    /** 
     * @return Returns the registered.
     * @since 4.3
     */
    public boolean isRegistered() {
        return this.registered;
    }
    
    /** 
     * @param registered The registered to set.
     * @since 4.3
     */
    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
    
    /** 
     * @return Returns the enabled.
     * @since 4.3
     */
    public boolean isEnabled() {
        return this.enabled;
    }
    /** 
     * @param enabled The enabled to set.
     * @since 4.3
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    /** 
     * @param created The created to set.
     * @since 4.3
     */
    public void setCreated(java.util.Date created) {
        this.created = created;
    }
    /** 
     * @param createdBy The createdBy to set.
     * @since 4.3
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /** 
     * @param lastUpdated The lastUpdated to set.
     * @since 4.3
     */
    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    /** 
     * @param lastUpdatedBy The lastUpdatedBy to set.
     * @since 4.3
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** 
     * @param props The props to set.
     * @since 4.3
     */
    public void setProperties(java.util.Properties props) {
        this.props = props;
    }
}
