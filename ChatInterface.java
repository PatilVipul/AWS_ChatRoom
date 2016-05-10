/**@author Vipul P. Patil, Rachana R. Mitkar
 * @description This interface is the remote interface which extends Remote. It contains all the
 * method definition which are to invoked remotely, but not their description.
 */

import java.rmi.*;
import java.util.Vector;

// Remote Interface
public interface ChatInterface extends Remote 
{	
	// method definition for creating a new and unique chat room
	public int createChatRoom(String roomName, String info) throws RemoteException;
	
	// method definition for deleting a specific chat room
	public int deleteChatRoom(String roomName)throws RemoteException;
	
	// method definition which returns the name of all the chat rooms which are created
	public Vector getAllRooms() throws RemoteException;  
	
	// method definition which returns the information about all the rooms which are created 
	public Vector getAllInfo() throws RemoteException;
	
	// method definition which connects the user with the specified chat room
	int signIn(String roomName, String userName) throws java.rmi.RemoteException;
	
	// method definition which sends message from one user to all other users
	void broadcast(String s,int i) throws java.rmi.RemoteException;
	
	// method definition which returns the message which is to be broadcasted 
	String getbroadcast() throws java.rmi.RemoteException;
}