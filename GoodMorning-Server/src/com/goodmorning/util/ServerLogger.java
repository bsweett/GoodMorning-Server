package com.goodmorning.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerLogger {

	public static String ID = "com.goodmorning.util";
	/**
	 * Default logger.
	 */
	private static ServerLogger slog = new ServerLogger();

	/**
	 * Underlying Java logger, used by the default logger.
	 */
	private Logger logger;
	
	private static final String SPACE = " ";
	private static final String LF = "\n";
	private static final String ALL = "ALL";
	private static final String SEVERE = "SEVERE";
	private static final String OFF = "OFF";
	private static final String LOG_FILE_NAME = "server-log.txt";
	
	public static ServerLogger getDefault() {
		return slog;
	}
	
	private ServerLogger() {
		
		try {
			boolean append = true;
			FileHandler handler = new FileHandler(LOG_FILE_NAME, append);
			handler.setFormatter(new SimpleFormatter());
			String level = ServerLogger.ALL;	// TODO:: Read from config file
			
			switch(level) {
			case ServerLogger.ALL: 
				handler.setLevel(Level.ALL);
				break;
			case ServerLogger.SEVERE: 
				handler.setLevel(Level.SEVERE);
				break;
			case ServerLogger.OFF: 
				handler.setLevel(Level.OFF);
				break;
			default: 
				handler.setLevel(Level.ALL);
				break;
			}
			
			setLogger(Logger.getLogger(ID));
			getLogger().addHandler(handler);
		} catch (IOException e) {
			System.err.println("IOException thrown while logging: " + e.getLocalizedMessage());
		}
	}
	
	private void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	private Logger getLogger() {
		return logger;
	}
	
	/**
	 * Formats given parameters for logging.
	 * Used by other logging methods before logging happens.
	 * 
	 * @param object
	 * @param method
	 * @param message
	 * @param exception
	 * @return
	 */
	private String format(Object object, String method, String message, Exception exception) {
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
		StringBuffer buffer = new StringBuffer();
		
		if (object == null) {
			buffer.append(method + SPACE + message + SPACE);
		} else {
			buffer.append(object.getClass().getName() + SPACE + method + SPACE + message + SPACE);
		}
		if (exception != null) {
			 exception.printStackTrace(printWriter);
			 printWriter.close();
		     buffer.append(stringWriter.toString());
		}
		buffer.append(LF);
		return buffer.toString();
	}
	
	/**
	 * Logs given parameters as a warning.
	 * 
	 * @param object
	 * @param method
	 * @param message
	 * @param exception
	 */
	public void warn(Object object, String method, String message, Exception exception) {
		
		String displayMessage = format (object, method, message, exception);
		getLogger().log(Level.WARNING, displayMessage, object);
	}
	
	/**
	 * Logs given parameters as an info. 
	 * 
	 * @param object
	 * @param method
	 * @param message
	 * @param exception
	 */
	public void info(Object object, String method, String message, Exception exception) {
		
		String displayMessage = format (object, method, message, exception);
		getLogger().log(Level.INFO, displayMessage, object);
	}
	
	/**
	 * Logs given parameters as severe.
	 * 
	 * @param object
	 * @param method
	 * @param message
	 * @param exception
	 */
	public void severe(Object object, String method, String message, Exception exception) {
		
		String displayMessage = format (object, method, message, exception);
		getLogger().log(Level.SEVERE, displayMessage, object);
	}

	public void showMessage(String message) {
		
		String newMessage = message + SPACE + "check log file: " + LOG_FILE_NAME;
		Utility.showMessage(newMessage);
	}
}
