/*
 *	This file is part of dhcp4java, a DHCP API for the Java language.
 *	(c) 2006 Stephan Hadinger
 *
 *	This library is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU Lesser General Public
 *	License as published by the Free Software Foundation; either
 *	version 2.1 of the License, or (at your option) any later version.
 *
 *	This library is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *	Lesser General Public License for more details.
 *
 *	You should have received a copy of the GNU Lesser General Public
 *	License along with this library; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.dhcp4java.old;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.core.DatagramPacket;
import ch.bemar.dhcp.core.SocketManager;
import ch.bemar.dhcp.exception.DHCPServerInitException;
import ch.bemar.dhcp.util.PropertiesLoader;

/**
 * A simple generic DHCP Server.
 *
 * The DHCP Server provided is based on a multi-thread model. The main thread
 * listens at the socket, then dispatches work to a pool of threads running the
 * servlet.
 *
 * <p>
 * Configuration: the Server reads the following properties in
 * "/DHCPd.properties" at the root of the class path. You can however provide a
 * properties set when contructing the server. Default values are:
 *
 * <blockquote> <tt>serverAddress=127.0.0.1:67</tt> <i>[address:port]</i> <br>
 * <tt>serverThreads=2</tt> <i>[number of concurrent threads for servlets]</i>
 * </blockquote>
 *
 * <p>
 * Note: this class implements <tt>Runnable</tt> allowing it to be run in a
 * dedicated thread.
 *
 * <p>
 * Example:
 *
 * <pre>
 * public static void main(String[] args) {
 * 	try {
 * 		DHCPCoreServer server = DHCPCoreServer.initServer(new DHCPStaticServlet(), null);
 * 		new Thread(server).start();
 * 	} catch (DHCPServerInitException e) {
 * 		// die gracefully
 * 	}
 * }
 * </pre>
 *
 * @author Stephan Hadinger
 * @author Benjamin Mark
 * @version 1.00
 */
public class DHCPCoreServer  {

	private static final Logger logger = Logger.getLogger(DHCPCoreServer.class.getName().toLowerCase());
	private static final int BOUNDED_QUEUE_SIZE = 20;

	

	/** the servlet it must run */
	protected DHCPServlet servlet;
	/** working threads pool. */
	protected ThreadPoolExecutor threadPool;
	/** Consolidated parameters of the server. */
	protected Properties properties;
	/** Reference of user-provided parameters */
	protected Properties userProps;
	/** IP address and port for the server */

	/** The sockets for receiving and sending. */
	private SocketManager socketManager;

	/** do we need to stop the server? */
	private boolean stopped = false;

	/**
	 * Constructor
	 *
	 * <p>
	 * Constructor shall not be called directly. New servers are created through
	 * <tt>initServer()</tt> factory.
	 */
	private DHCPCoreServer(DHCPServlet servlet, Properties userProps, SocketManager manager) {
		this.servlet = servlet;
		this.userProps = userProps;
		this.socketManager = manager;
	}

	/**
	 * Creates and initializes a new DHCP Server.
	 *
	 * <p>
	 * It instanciates the object, then calls <tt>init()</tt> method.
	 *
	 * @param servlet   the <tt>DHCPServlet</tt> instance processing incoming
	 *                  requests, must not be <tt>null</tt>.
	 * @param userProps specific properties, overriding file and default properties,
	 *                  may be <tt>null</tt>.
	 * @return the new <tt>DHCPCoreServer</tt> instance (never null).
	 * @throws DHCPServerInitException unable to start the server.
	 */
	public static DHCPCoreServer initServer(DHCPServlet servlet, Properties userProps, SocketManager manager)
			throws DHCPServerInitException {
		if (servlet == null) {
			throw new IllegalArgumentException("servlet must not be null");
		}
		DHCPCoreServer server = new DHCPCoreServer(servlet, userProps, manager);
		server.init();
		return server;
	}

	/**
	 * Initialize the server context from the Properties, and open socket.
	 *
	 */
	protected void init() throws DHCPServerInitException {
		if (this.socketManager != null) {
			throw new IllegalStateException("Server already initialized");
		}

		try {
			// default built-in minimal properties
			this.properties = new Properties(DEF_PROPS);

			this.properties = PropertiesLoader.loadProperties(DhcpConstants.DEFAULT_PROPERTIES_FILE);

			// now integrate provided properties
			if (this.userProps != null) {
				this.properties.putAll(this.userProps);
			}

			// initialize Thread Pool
			int numThreads = Integer.valueOf(this.properties.getProperty(SERVER_THREADS));
			int maxThreads = Integer.valueOf(this.properties.getProperty(SERVER_THREADS_MAX));
			int keepaliveThreads = Integer.valueOf(this.properties.getProperty(SERVER_THREADS_KEEPALIVE));
			this.threadPool = new ThreadPoolExecutor(numThreads, maxThreads, keepaliveThreads, TimeUnit.MILLISECONDS,
					new ArrayBlockingQueue<Runnable>(BOUNDED_QUEUE_SIZE), new ServerThreadFactory());
			this.threadPool.prestartAllCoreThreads();

			// now intialize the servlet
			this.servlet.setServer(this);
			this.servlet.init(this.properties);

		} catch (Exception e) {

			this.socketManager = null;
			logger.log(Level.SEVERE, "Cannot open socket", e);
			throw new DHCPServerInitException("Unable to init server", e);
		}
	}

	

	/**
	 * Send back response packet to client.
	 *
	 * <p>
	 * This is a callback method used by servlet dispatchers to send back responses.
	 */
	protected void sendResponse(DatagramPacket responseDatagram) {
		if (responseDatagram == null) {
			return; // skipping
		}

		try {
			// sending back
			this.socketManager.send(responseDatagram);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException", e);
		}
	}

	/**
	 * This is the main loop for accepting new request and delegating work to
	 * servlets in different threads.
	 *
	 */
	public void run() {
		if (this.socketManager == null) {
			throw new IllegalStateException("Listening sockets are not open - terminating");
		}
		while (!this.stopped) {
			try {
				//this.dispatch(); // do the stuff
			} catch (Exception e) {
				logger.log(Level.WARNING, "Unexpected Exception", e);
			}
		}
	}

	/**
	 * This method stops the server and closes the socket.
	 *
	 */
	public void stopServer() {
		this.stopped = true;
		this.socketManager.close(); // this generates an exception when trying to receive
	}

	private static final Properties DEF_PROPS = new Properties();

	public static final String SERVER_ADDRESS = "serverAddress";
	private static final String SERVER_ADDRESS_DEFAULT = "127.0.0.1:67";
	public static final String SERVER_THREADS = "serverThreads";
	private static final String SERVER_THREADS_DEFAULT = "2";
	public static final String SERVER_THREADS_MAX = "serverThreadsMax";
	private static final String SERVER_THREADS_MAX_DEFAULT = "4";
	public static final String SERVER_THREADS_KEEPALIVE = "serverThreadsKeepalive";
	private static final String SERVER_THREADS_KEEPALIVE_DEFAULT = "10000";

	static {
		// initialize defProps
		DEF_PROPS.put(SERVER_ADDRESS, SERVER_ADDRESS_DEFAULT);
		DEF_PROPS.put(SERVER_THREADS, SERVER_THREADS_DEFAULT);
		DEF_PROPS.put(SERVER_THREADS_MAX, SERVER_THREADS_MAX_DEFAULT);
		DEF_PROPS.put(SERVER_THREADS_KEEPALIVE, SERVER_THREADS_KEEPALIVE_DEFAULT);
	}

	private static class ServerThreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);

		final AtomicInteger threadNumber = new AtomicInteger(1);
		final String namePrefix;

		ServerThreadFactory() {
			this.namePrefix = "DHCPCoreServer-" + poolNumber.getAndIncrement() + "-thread-";
		}

		public Thread newThread(Runnable runnable) {
			return new Thread(runnable, this.namePrefix + this.threadNumber.getAndIncrement());
		}
	}

	
}


