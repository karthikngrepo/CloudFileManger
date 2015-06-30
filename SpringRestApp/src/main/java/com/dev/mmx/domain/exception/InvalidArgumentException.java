package com.dev.mmx.domain.exception;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.HashMap;

import java.io.PrintWriter;
import java.io.StringWriter;

public class InvalidArgumentException extends RuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String trace = null;
    InvalidArgumentException causeInvalidArgumentException;
    
    public
    InvalidArgumentException ()
    {
        super ();
    }

    public
    InvalidArgumentException (String s)
    {
        super (s);
    }

    public
    InvalidArgumentException (RemoteException remoteEx)
    {
        super (remoteEx);
    }
    
    protected
    InvalidArgumentException (String message,RemoteException remoteEx)
    {
    	super (message,remoteEx);
    }

    public InvalidArgumentException (String message, InvalidArgumentException causeInvalidArgumentException)
    {
        super(message);
        this.causeInvalidArgumentException = causeInvalidArgumentException;
    }


    public InvalidArgumentException getCauseInvalidArgumentException ()
    {
        return causeInvalidArgumentException;
    }

    public void setCauseInvalidArgumentException (InvalidArgumentException causeInvalidArgumentException)
    {
        this.causeInvalidArgumentException = causeInvalidArgumentException;
    }

    public
    void setStackTrace (String trace)
    {
        this.trace = trace;
    }

    public
    String getMessage ()
    {
        return super.getMessage () +
                ((trace != null) ? ", " + trace : "");
    }

    public
    String toString ()
    {
        return super.toString () +
            ((trace != null) ? ", " + trace : "");
    }

    private
    static String _getStringWithStackTrace (Exception ex)
    {
        return ex.toString () + ", " + _getStackTraceOnly (ex);
    }

    private
    static String _getStackTraceOnly (Exception ex)
    {
        StringWriter sw = new StringWriter ();
        ex.printStackTrace (new PrintWriter (sw));
        String trace = "[STACK_TRACE]: " + sw.toString ();
        return trace;
    }

    /**
     * <b>For internal use only</b>.
     * <p>
     * Returns the fields for marshalling purposes.
     * <p>
     * @return The fields to be marshalled in a map.
     */
    public
    Map getFieldsForMarshalling ()
    {
        HashMap hm = new HashMap ();
        hm.put ("message", getMessage ());
        hm.put ("trace", trace);
        hm.put ("causeInvalidArgumentException", causeInvalidArgumentException);

	return hm;
    }

    /**
     * <b>For internal use only</b>.
     * <p>
     * Creates a <code>InvalidArgumentException</code> from a field map.
     * <p>
     * @param map The fields from which the exception is to be created.
     * @return    The invalid argument exception created.
     */
    public
    static InvalidArgumentException createFromFields (Map map)
    {
       InvalidArgumentException coreEx;
       String message = (String) map.get ("message");
        String trace   = (String) map.get ("trace");
        InvalidArgumentException causeInvalidArgumentException   = (InvalidArgumentException) map.get ("causeInvalidArgumentException");

        if(causeInvalidArgumentException != null) {
            coreEx = new InvalidArgumentException (message, causeInvalidArgumentException);
        }
        if (message != null) {
            coreEx = new InvalidArgumentException (message);
        }
        else {
            coreEx = new InvalidArgumentException ();
        }

        if (trace != null) {
            coreEx.setStackTrace (trace);
        }

        return coreEx;
    }
    public java.lang.String getTrace () { 
        return trace;
    } 
    public void setTrace ( java.lang.String trace) { 
    this.trace = trace;
    } 
}
