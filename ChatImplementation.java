/**@author Vipul P. Patil, Rachana R. Mitkar
 * @description This ChatImplementation class has implementation of all the methods which are 
 * defined in the ChatInterface.
 */

import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

/**  This class implements our RMI methods */
public class ChatImplementation extends UnicastRemoteObject implements ChatInterface
{
	private Vector roomsVector;
	private Vector informationVector; 
	String [] Users = new String[50];
	int userId = 0;
	public String message = "";

	// Default constructor which creates a new Vector for rooms name and their information
	public ChatImplementation() throws RemoteException 
	{
		super(); 
		roomsVector = new Vector();
		informationVector = new Vector(); 
	}

	// Method definition for creating a new chat room
	public int createChatRoom(String roomName, String information) throws RemoteException
	{
		if(!(roomsVector.contains(roomName)))	// There is no room with same name as roomName
		{
			// add this room to roomsVector
			roomsVector.add(roomName);
			
			// add this room's information to informationVector 
			informationVector.add(information);
			
			// display that the room has been created
			System.out.println("Created room: " + roomName);
			return 1;
		}
		else	// There is another room with similar name
		{
			// display that new chat room has not been created
			System.out.println("Not created: " + roomName);
			return -1;
		}
	}

	// Method definition foe deleting the existing room which has been created previously
	public int deleteChatRoom(String roomName) throws RemoteException 
	{
		// Find the index of room from the Vector
		int token = roomsVector.indexOf(roomName); 
		if(token != -1)	// This room has been created and found and can be deleted
		{
			// Remove roomName and its information from the Vectors
			roomsVector.remove(roomName);
			informationVector.remove(token);
			System.out.println("Deleted chat room: " + roomName);
			return 1;
		}
		else	// This room name does not exist and has not been created previously
		{
			System.out.println("Not deleted as no such chat room exists");
			return -1;
		}
	}

	// Method definition which returns the name of all the rooms which are created
	public Vector getAllRooms() 
	{
		return roomsVector;
	}

	// Method definition which returns the information of all the rooms which are already created
	public Vector getAllInfo() 
	{
		 return informationVector; 
	}	
	
	// Method definition which connects the specified userName to the chat room
	public int signIn(String roomName, String userName) throws java.rmi.RemoteException
	{		
		if(roomsVector.contains(roomName))	// if the room with roomName exists
		{
			if(Users[userId] == null)		// if the users arrays at userId is empty
			{
				Users[userId] = roomName.concat(" (" + userName + ")");
				userId++;
				return (userId-1);
			}
		}
		else		// if the room with roomName does not exist
		{
			return -1;
		}
		return -1;
	}
	
	// Method definition which would broadcast the message, msg, sent by the user 
	public void broadcast(String msg, int i) throws java.rmi.RemoteException
	{
		if(msg != null)
		{
			message = message + "\n" + Users[i] + ": " + msg;
		}
	}

	// Method definition which would return the message 
	public String getbroadcast() throws java.rmi.RemoteException
	{
		return message;
	}

	// main method
	public static void main(String[] args) 
	{	 
		// Check that there are no arguments while running the this file
		if(args.length >= 1)
		{
			System.out.println("Improper way to run the ChatImplementation");
	        System.out.println("Proper usage is: java ChatImplementation");
	        System.exit(0);
		}
		else
		{
			try 
			{
				// Creating an object of ChatImplemenation class
				ChatImplementation obj = new ChatImplementation();
			
				// Check the service for its location, if present or not
				RemoteRef location = obj.getRef();
				System.out.println(location.remoteToString());
		
				// Argument for methods of Naming class
				// localhost: where the RMI registry is located
				// 1099: default port for RMI registry
				// RMIChatRegistry: name of the registry
				String name = "rmi://54.187.133.31/RMIChatRegistry";
			
				// Rebinds the specified name to a new Remote object, obj
				Naming.rebind(name, obj);
			}
			
			catch(RemoteException re)
			{
				System.out.println("Remote Error. Please try again.");
			}
			
			catch(Exception e)
			{
				System.out.println("Oops.. Something went wrong. Please try again.");
			}
		}
	}
}