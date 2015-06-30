package com.dev.mmx.domain.exception;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.HashMap;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The super class of all application component related exceptions thrown
 * from application component implementations.
 * <p>
 * Since this class extends <code>RuntimeException</code>, a method is not
 * required to explicitly catch an <code>CoreException</code> or any of
 * its subclasses.
 * <p>
 */
public
class CoreException
extends RuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String trace = null;
    CoreException causeCoreException;
    
    /**
     * Constructs a <code>CoreException</code> with no detail message.
     */
    public
    CoreException ()
    {
        super ();
    }

    /**
     * Constructs a <code>CoreException</code> with the specified detail
     * message.
     * <p>
     * @param s   the detail message.
     */
    public
    CoreException (String s)
    {
        super (s);
    }

    /**
     * Constructs an <code>CoreException</code> from an underlying
     * <code>java.rmi.RemoteException</code>
     * <p>
     * @param remoteEx   the underlying remote exception.
     */
    public
    CoreException (RemoteException remoteEx)
    {
        super (remoteEx);
    }
    
    /**
     * Constructs an <code>CoreException</code> from an underlying
     * <code>java.rmi.RemoteException</code>
     * <p>
     * @param remoteEx   the underlying remote exception.
     * @param message   the detail message (which is saved for later retrieval by the getMessage() method).
     */
    protected
    CoreException (String message,RemoteException remoteEx)
    {
    	super (message,remoteEx);
    }

    /**
     * Constructs an <code>CoreException</code> from an underlying <code>CoreException</code>
     * <p>
     * @param message
     * @param causeCoreException
     */
    public CoreException (String message, CoreException causeCoreException)
    {
        super(message);
        this.causeCoreException = causeCoreException;
    }


    /**
     * Returns core exception cause.
     * @return
     */
    public CoreException getCauseCoreException ()
    {
        return causeCoreException;
    }

    /**
     * Sets core exception cause.
     * @param causeCoreException
     */
    public void setCauseCoreException (CoreException causeCoreException)
    {
        this.causeCoreException = causeCoreException;
    }

    /**
     * Stores the stack trace of the core exception.
     */
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
        hm.put ("causeCoreException", causeCoreException);

	return hm;
    }

    /**
     * <b>For internal use only</b>.
     * <p>
     * Creates a <code>CoreException</code> from a field map.
     * <p>
     * @param map The fields from which the exception is to be created.
     * @return    The core exception created.
     */
    public
    static CoreException createFromFields (Map map)
    {
       CoreException coreEx;
       String message = (String) map.get ("message");
        String trace   = (String) map.get ("trace");
        CoreException causeCoreException   = (CoreException) map.get ("causeCoreException");

        if(causeCoreException != null) {
            coreEx = new CoreException (message, causeCoreException);
        }
        if (message != null) {
            coreEx = new CoreException (message);
        }
        else {
            coreEx = new CoreException ();
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
