package server;import java.io.*;import common.ChatIF;import ocsf.server.*;/** * This class overrides some of the methods in the abstract superclass in order * to give more functionality to the server. *  * @author Dr Timothy C. Lethbridge * @author Dr Robert Lagani&egrave;re * @author Fran&ccedil;ois B&eacute;langer * @author Paul Holden * @version July 2000 */public class EchoServer extends AbstractServer {	// Class variables *************************************************	/**	 * The default port to listen on.	 */	final public static int DEFAULT_PORT = 5555;	ChatIF serverUI;	// Constructors ****************************************************	/**	 * Constructs an instance of the echo server.	 * 	 * @param port	 *            The port number to connect on.	 * @param ui	 *            instance qui a cree le serveur	 */	public EchoServer(int port, ChatIF ui) {		super(port);		serverUI = ui;	}	// Instance methods ************************************************	/**	 * This method handles any messages received from the client.	 * 	 * @param msg	 *            The message received from the client.	 * @param client	 *            The connection from which the message originated.	 */	public void handleMessageFromClient(Object msg, ConnectionToClient client) {		if (((String) msg).startsWith(CommandServer.login)) {			String id = ((String) msg).substring(7);			if (client.getInfo("login") == null) {				client.setInfo("login", id);			} else {				try {					client.sendToClient("You have already a login");				} catch (IOException e) {					// TODO Auto-generated catch block					e.printStackTrace();				}			}		} else if (((String) msg).startsWith(CommandServer.logoff)) {			try {				client.close();			} catch (IOException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}		} else if (client.getInfo("login") != null) {			System.out.println("Message received: " + msg + " from "					+ client.getInfo("login"));			this.sendToAllClients(msg);		} else {			try {				client.sendToClient("You do not have a login");			} catch (IOException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}		}	}	public void handleMessageFromServerUI(String message) {		if (message.startsWith(CommandServer.quit)) {			System.exit(0);		} else if (message.startsWith(CommandServer.stop)) {			this.stopListening();		} else if (message.startsWith(CommandServer.start)				&& !this.isListening()) {			try {				this.listen();			} catch (IOException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}		} else if (message.startsWith(CommandServer.start)				&& this.isListening()) {			this.serverUI.display("Server is already listening");		} else if (message.startsWith(CommandServer.close)) {			try {				this.close();			} catch (IOException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}		} else if (message.startsWith(CommandServer.setPort)) {			try {				this.close();			} catch (IOException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}			this.setPort(Integer.parseInt(message.substring(9)));			this.serverUI.display("The port has changed. The new port is : "					+ this.getPort());		} else if (message.startsWith(CommandServer.getPort)) {			serverUI.display(String.valueOf(this.getPort()));		} else {			this.sendToAllClients("Server MSG>" + message);		}	}	/**	 * This method overrides the one in the superclass. Called when the server	 * starts listening for connections.	 */	protected void serverStarted() {		System.out.println("Server listening for connections on port "				+ getPort());	}	/**	 * This method overrides the one in the superclass. Called when the server	 * stops listening for connections.	 */	protected void serverStopped() {		System.out.println("Server has stopped listening for connections.");	}	/**	 * Hook method called each time a new client connection is accepted. The	 * default implementation does nothing.	 * 	 * @param client	 *            the connection connected to the client.	 */	protected void clientConnected(ConnectionToClient client) {		System.out.println("Un client vient de se connecter");	}	/**	 * Hook method called each time a client disconnects. The default	 * implementation does nothing. The method may be overridden by subclasses	 * but should remains synchronized.	 * 	 * @param client	 *            the connection with the client.	 */	synchronized protected void clientDisconnected(ConnectionToClient client) {		System.out.println("Un client vient de se deconnecter normalement");	}	/**	 * Hook method called each time an exception is thrown in a	 * ConnectionToClient thread. The method may be overridden by subclasses but	 * should remains synchronized.	 * 	 * @param client	 *            the client that raised the exception.	 * @param Throwable	 *            the exception thrown.	 */	synchronized protected void clientException(ConnectionToClient client,			Throwable exception) {		System.out.println("Un client s'est deconnecte sans dire au revoir !");	}}// Class methods ***************************************************/** * This method is responsible for the creation of the server instance (there is * no UI in this phase). *  * @param args *            [0] The port number to listen on. Defaults to 5555 if no argument *            is entered. */// public static void main(String[] args) {// int port = 0; // Port to listen on//// try {// port = Integer.parseInt(args[0]); // Get port from command line// } catch (Throwable t) {// port = DEFAULT_PORT; // Set port to 5555// }//// EchoServer sv = new EchoServer(port);//// try {// sv.listen(); // Start listening for connections// } catch (Exception ex) {// System.out.println("ERROR - Could not listen for clients!");// }// }// End of EchoServer class