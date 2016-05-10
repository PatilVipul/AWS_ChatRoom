/**@author Vipul P. Patil, Rachana R. Mitkar
 * @description This Server class runs the server on the machine and performs basic
 * server operations for our Chat application using Remote Method Invocation.
 * These operations are: 1. Creating a chat room
 * 						 2. Deleting a specific chat room
 *  					 3. Displaying all the chat rooms which are created 
 */

import java.rmi.*; 
import java.util.Vector;
import java.io.*; 

public class Server
{
	// This main function initiates the running of server
	public static void main(String args[]) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
		int userChoice = 0; 
		
		// Check that there are no arguments while running the server
		if(args.length >= 1)
	    {
			System.out.println("Improper way to run the Server.");
	        System.out.println("Proper usage is: java Server");
	        System.exit(0);
	    }
		else
		{
			try
			{ 
				// Argument for methods of Naming class
				// localhost: where the RMI registry is located
				// 1099: default port for RMI registry
				// RMIChatRegistry: name of the registry
				String name = "rmi://54.187.133.31/RMIChatRegistry";
//				String name = "rmi://54.187.133.31:1099/RMIChatRegistry";
			
				// This method returns a reference, a stub, for the remote object
				ChatInterface reg = (ChatInterface) Naming.lookup(name);
			
				try
				{
					while(userChoice!=4)
					{
						// Server menu
						System.out.println("\n***** SERVER MENU *****"); 
						System.out.println("1.Create a chat room"); 
						System.out.println("2.Remove a chat room"); 
						System.out.println("3.Display all chat rooms"); 
						System.out.println("4.Quit"); 
						System.out.print("Please enter your choice: ");
						
						try
						{
							// Convert string argument to integer type
							userChoice = Integer.parseInt(in.readLine());
						}
						// if user enters anything other than a number
						catch(NumberFormatException e)
						{
							System.out.println("Invalid choice!!! Try again.");
							continue;
						}
						
						switch(userChoice)
						{
							case 1: // Creating a chat room
									System.out.print("Enter unique chat room name: "); 
        							String roomName = in.readLine();
        							System.out.print("Enter information about this room: "); 
        							String info = in.readLine();
        							if(roomName.length() != 0 && info.length() != 0 )
        							{
        								// passing room name and room information to remote method
        								int value = reg.createChatRoom(roomName, info);
        								if(value == 1)	// if room is successfully created
        								{
        									System.out.println("CREATED " + roomName);
        								}
        								else			// if room is not successfully created
        								{
        									System.out.println("Not created. Please enter a unique room name.");
        								}
        							}
        							else	// if user does not enter room name or information
        							{
        								System.out.println("Enter some name and information for the chat room.");
        							}
        							break; 
        							
							case 2: // Deleting a chat room
									// Asking user, which room is to be deleted
									System.out.print("Enter room name to be deleted: "); 
									roomName = in.readLine(); 
									if(roomName.length() != 0)
									{
										// passing that room name to the remote method
										int value = reg.deleteChatRoom(roomName);
										if(value == 1)	// if room is successfully deleted 
										{
											System.out.println("DELETED " + roomName);
										}
										else			// if room is not successfully deleted
										{
											System.out.println("Cannot delete " + roomName + " as it does not exist.");
										}
									}
									else	// if user does not specify any name of the room
									{
										System.out.println("Please enter some name for the chat room.");
									}
									break; 

							case 3: // Displaying all the created chat rooms
									// Store all the chat room names in chatRoomVector
        							Vector chatRoomVector= reg.getAllRooms(); 
        						 
        							if(chatRoomVector.size() != 0) // if there are any chat rooms
        							{
        								System.out.println("\nList of all chat rooms...");
        								for(int i=0; i<chatRoomVector.size(); i++)
        								{ 
        									// Displaying all the names one by one
        									System.out.println(i+1 + ". " + chatRoomVector.get(i));
        								} 
        							}
        							else	// if there is not even a single chat room created
        							{
        								System.out.println("No chat rooms have been created yet.");
        							}	
        							break; 
        			
							case 4: // Exiting the application from Server side
        							System.out.println("Thank you for using our application.");
//        							reg.reset();
        							break; 
        							
							default:System.out.println("Invalid choice!!! Try again."); 
        
						}	// end of switch case
						
					}	// end of while loop
					
				}	//end of inner try block
				
				// if user enters anything other than a number
				catch(NumberFormatException e)	
				{
					System.out.println("Invalid choice!!! Try again.");
					System.exit(0);
				}
				
			}// end of outer try block
			
			// if specified host is not found
			catch(UnknownHostException f)	
			{
				System.out.println("Chat Server Error: Host name not correct.");
				f.printStackTrace();
				System.exit(0);
			}
			
			catch(Exception e)
			{
				System.out.println("Oops.. Something went wrong. Start the server again.");
				e.printStackTrace();
				System.exit(0);
			} 
			
		}	// end of else block	
		
	}	// end of main method
	
}	// end of Server class