/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.opennms.netmgt.xml.eventconf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * The event correlation information
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Correlation implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The state determines if event is
     *  correlated
     */
    private java.lang.String _state = "off";

    /**
     * The correlation path
     */
    private java.lang.String _path = "suppressDuplicates";

    /**
     * A cancelling UEI for this event
     */
    private java.util.List<java.lang.String> _cueiList;

    /**
     * The minimum count for this event
     */
    private java.lang.String _cmin;

    /**
     * The maximum count for this event
     */
    private java.lang.String _cmax;

    /**
     * The correlation time for this event
     */
    private java.lang.String _ctime;


      //----------------/
     //- Constructors -/
    //----------------/

    public Correlation() {
        super();
        setState("off");
        setPath("suppressDuplicates");
        this._cueiList = new java.util.ArrayList<java.lang.String>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCuei
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCuei(
            final java.lang.String vCuei)
    throws java.lang.IndexOutOfBoundsException {
        this._cueiList.add(vCuei);
    }

    /**
     * 
     * 
     * @param index
     * @param vCuei
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCuei(
            final int index,
            final java.lang.String vCuei)
    throws java.lang.IndexOutOfBoundsException {
        this._cueiList.add(index, vCuei);
    }

    /**
     * Method enumerateCuei.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<java.lang.String> enumerateCuei(
    ) {
        return java.util.Collections.enumeration(this._cueiList);
    }

    /**
     * Overrides the java.lang.Object.equals method.
     * 
     * @param obj
     * @return true if the objects are equal.
     */
    @Override()
    public boolean equals(
            final java.lang.Object obj) {
        if ( this == obj )
            return true;
        
        if (obj instanceof Correlation) {
        
            Correlation temp = (Correlation)obj;
            if (this._state != null) {
                if (temp._state == null) return false;
                else if (!(this._state.equals(temp._state))) 
                    return false;
            }
            else if (temp._state != null)
                return false;
            if (this._path != null) {
                if (temp._path == null) return false;
                else if (!(this._path.equals(temp._path))) 
                    return false;
            }
            else if (temp._path != null)
                return false;
            if (this._cueiList != null) {
                if (temp._cueiList == null) return false;
                else if (!(this._cueiList.equals(temp._cueiList))) 
                    return false;
            }
            else if (temp._cueiList != null)
                return false;
            if (this._cmin != null) {
                if (temp._cmin == null) return false;
                else if (!(this._cmin.equals(temp._cmin))) 
                    return false;
            }
            else if (temp._cmin != null)
                return false;
            if (this._cmax != null) {
                if (temp._cmax == null) return false;
                else if (!(this._cmax.equals(temp._cmax))) 
                    return false;
            }
            else if (temp._cmax != null)
                return false;
            if (this._ctime != null) {
                if (temp._ctime == null) return false;
                else if (!(this._ctime.equals(temp._ctime))) 
                    return false;
            }
            else if (temp._ctime != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Returns the value of field 'cmax'. The field 'cmax' has the
     * following description: The maximum count for this event
     * 
     * @return the value of field 'Cmax'.
     */
    public java.lang.String getCmax(
    ) {
        return this._cmax;
    }

    /**
     * Returns the value of field 'cmin'. The field 'cmin' has the
     * following description: The minimum count for this event
     * 
     * @return the value of field 'Cmin'.
     */
    public java.lang.String getCmin(
    ) {
        return this._cmin;
    }

    /**
     * Returns the value of field 'ctime'. The field 'ctime' has
     * the following description: The correlation time for this
     * event
     * 
     * @return the value of field 'Ctime'.
     */
    public java.lang.String getCtime(
    ) {
        return this._ctime;
    }

    /**
     * Method getCuei.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getCuei(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._cueiList.size()) {
            throw new IndexOutOfBoundsException("getCuei: Index value '" + index + "' not in range [0.." + (this._cueiList.size() - 1) + "]");
        }
        
        return (java.lang.String) _cueiList.get(index);
    }

    /**
     * Method getCuei.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getCuei(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._cueiList.toArray(array);
    }

    /**
     * Method getCueiCollection.Returns a reference to '_cueiList'.
     * No type checking is performed on any modifications to the
     * Vector.
     * 
     * @return a reference to the Vector backing this class
     */
    public java.util.List<java.lang.String> getCueiCollection(
    ) {
        return this._cueiList;
    }

    /**
     * Method getCueiCount.
     * 
     * @return the size of this collection
     */
    public int getCueiCount(
    ) {
        return this._cueiList.size();
    }

    /**
     * Returns the value of field 'path'. The field 'path' has the
     * following description: The correlation path
     * 
     * @return the value of field 'Path'.
     */
    public java.lang.String getPath(
    ) {
        return this._path;
    }

    /**
     * Returns the value of field 'state'. The field 'state' has
     * the following description: The state determines if event is
     *  correlated
     * 
     * @return the value of field 'State'.
     */
    public java.lang.String getState(
    ) {
        return this._state;
    }

    /**
     * Overrides the java.lang.Object.hashCode method.
     * <p>
     * The following steps came from <b>Effective Java Programming
     * Language Guide</b> by Joshua Bloch, Chapter 3
     * 
     * @return a hash code value for the object.
     */
    public int hashCode(
    ) {
        int result = 17;
        
        long tmp;
        if (_state != null) {
           result = 37 * result + _state.hashCode();
        }
        if (_path != null) {
           result = 37 * result + _path.hashCode();
        }
        if (_cueiList != null) {
           result = 37 * result + _cueiList.hashCode();
        }
        if (_cmin != null) {
           result = 37 * result + _cmin.hashCode();
        }
        if (_cmax != null) {
           result = 37 * result + _cmax.hashCode();
        }
        if (_ctime != null) {
           result = 37 * result + _ctime.hashCode();
        }
        
        return result;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Method iterateCuei.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<java.lang.String> iterateCuei(
    ) {
        return this._cueiList.iterator();
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllCuei(
    ) {
        this._cueiList.clear();
    }

    /**
     * Method removeCuei.
     * 
     * @param vCuei
     * @return true if the object was removed from the collection.
     */
    public boolean removeCuei(
            final java.lang.String vCuei) {
        boolean removed = _cueiList.remove(vCuei);
        return removed;
    }

    /**
     * Method removeCueiAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeCueiAt(
            final int index) {
        java.lang.Object obj = this._cueiList.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * Sets the value of field 'cmax'. The field 'cmax' has the
     * following description: The maximum count for this event
     * 
     * @param cmax the value of field 'cmax'.
     */
    public void setCmax(
            final java.lang.String cmax) {
        this._cmax = cmax;
    }

    /**
     * Sets the value of field 'cmin'. The field 'cmin' has the
     * following description: The minimum count for this event
     * 
     * @param cmin the value of field 'cmin'.
     */
    public void setCmin(
            final java.lang.String cmin) {
        this._cmin = cmin;
    }

    /**
     * Sets the value of field 'ctime'. The field 'ctime' has the
     * following description: The correlation time for this event
     * 
     * @param ctime the value of field 'ctime'.
     */
    public void setCtime(
            final java.lang.String ctime) {
        this._ctime = ctime;
    }

    /**
     * 
     * 
     * @param index
     * @param vCuei
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCuei(
            final int index,
            final java.lang.String vCuei)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._cueiList.size()) {
            throw new IndexOutOfBoundsException("setCuei: Index value '" + index + "' not in range [0.." + (this._cueiList.size() - 1) + "]");
        }
        
        this._cueiList.set(index, vCuei);
    }

    /**
     * 
     * 
     * @param vCueiArray
     */
    public void setCuei(
            final java.lang.String[] vCueiArray) {
        //-- copy array
        _cueiList.clear();
        
        for (int i = 0; i < vCueiArray.length; i++) {
                this._cueiList.add(vCueiArray[i]);
        }
    }

    /**
     * Sets the value of '_cueiList' by copying the given Vector.
     * All elements will be checked for type safety.
     * 
     * @param vCueiList the Vector to copy.
     */
    public void setCuei(
            final java.util.List<java.lang.String> vCueiList) {
        // copy vector
        this._cueiList.clear();
        
        this._cueiList.addAll(vCueiList);
    }

    /**
     * Sets the value of '_cueiList' by setting it to the given
     * Vector. No type checking is performed.
     * @deprecated
     * 
     * @param cueiList the Vector to set.
     */
    public void setCueiCollection(
            final java.util.List<java.lang.String> cueiList) {
        this._cueiList = cueiList;
    }

    /**
     * Sets the value of field 'path'. The field 'path' has the
     * following description: The correlation path
     * 
     * @param path the value of field 'path'.
     */
    public void setPath(
            final java.lang.String path) {
        this._path = path;
    }

    /**
     * Sets the value of field 'state'. The field 'state' has the
     * following description: The state determines if event is
     *  correlated
     * 
     * @param state the value of field 'state'.
     */
    public void setState(
            final java.lang.String state) {
        this._state = state;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.opennms.netmgt.xml.eventconf.Correlation
     */
    public static org.opennms.netmgt.xml.eventconf.Correlation unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.opennms.netmgt.xml.eventconf.Correlation) Unmarshaller.unmarshal(org.opennms.netmgt.xml.eventconf.Correlation.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
