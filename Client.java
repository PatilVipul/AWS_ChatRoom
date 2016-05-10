/**@author Vipul P. Patil, Rachana R. Mitkar
 * @description This class runs on the client side and provides different 
 * functionalities for the users of Chat application using Remote Method Invocation.
 * The different functionalities are: 1. Displaying all the chat rooms which are created
 * 										 on the Server side
 * 						 			  2. Connecting to a specific chat room
 *  					 			  3. Chat with other users from different chat rooms 
 *  								  4. Disconnecting from the currently connected chat room
 */

import java.rmi.*; 
import java.util.Vector;
import java.io.*; 
public class Client
{
	// This main method initiates the client
	public static void main(String args[]) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));  
		int userChoice = 0; 
		int connectionId = -1; 
		boolean isConnected = false; 

		// Check that there are no arguments while running the client
		if(args.length >= 1)
	    {
			System.out.println("Improper way to run the client");
	        System.out.println("Proper usage is: java Client");
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
//				String name = "rmi://54.187.133.31:1099/RMIChatRegistry";	Amazon AWS
				
				// This method returns a reference, a stub, for the remote object
				ChatInterface reg = (ChatInterface) Naming.lookup(name);

				
				try
				{
					System.out.println("\nWELCOME TO CHAT ROOM APPLICATION");
					while(userChoice != 5)
					{
						// Client menu
						System.out.println("\n***** CLIENT MENU *****"); 
						System.out.println("1.Display all chat rooms"); 
						System.out.println("2.Connect to a chat room"); 
						System.out.println("3.Chat");
						System.out.println("4.Disconnect from current chat room");
						System.out.println("5.Quit"); 
						System.out.print("Please enter your choice: ");
						
						try
						{
							// Convert string argument to integer type
							userChoice = Integer.parseInt(in.readLine());
						}
						// if user enters anything other than a number
						catch(NumberFormatException e)
						{
							System.out.println("Invalid choice!!! Please try again.");
							continue;
						}
						
						switch(userChoice)
						{
							case 1: // Displaying all the chat rooms created along with their information
									
									// Store all the chat room names in roomVector
									Vector roomVector = reg.getAllRooms(); 
									
									// Store all the chat room information in infoVector
									Vector infoVector = reg.getAllInfo();
									
									if(roomVector.size() != 0 && infoVector.size() != 0) // if there are any chat rooms
									{
										System.out.println("\nList of all chat rooms created..."); 
										for(int i = 0; i < roomVector.size(); i++)
										{ 
											// display chat room name and information one by one
											System.out.println((i+1) + ". " + roomVector.get(i) + ": " + infoVector.get(i)); 
										} 
									}
									else		// if there is not even a single chat room created
									{
										System.out.println("\nNo chat rooms have been created yet.");
									}
									break; 
									
							case 2: // Connect to a specific chat room 		
									if(isConnected == false)	// if user is not connected to any other chat room
									{
										// Ask user which chat room he/she has to connect
										System.out.print("Enter chat room name: "); 
										String roomName = in.readLine();
										
										// Ask the person his/her name for identity
										System.out.print("Enter your name: ");
										String userName = in.readLine();
										
										if(roomName.length() != 0){		// if user has specified the room name
											// pass the room name and user name to the remote method
											connectionId=reg.signIn(roomName, userName);
											if(connectionId >= 0)	// is connection is successful
											{	
												isConnected=true;
												System.out.println("CONNECTED to chat room: " + roomName);
											}
											else if(connectionId < 0)	// if connection is not successful
											{
												System.out.println("NOT CONNECTED to the chat room. Some problem occurred.");
											}
										}
										else	// if user did not specify the name of the room
										{
											System.out.println("Please enter some name for chat room");
										}
									}
									else	// if user is already connected to some other room and still trying to connect to a new room
									{
										System.out.println("You are already connected to another chat room.");
										System.out.println("First disconnect from there.");
									}
									break; 
									
							case 3: // User can start chatting with other users
									String messageReceived = "";
									String messageSent = ""; 
									boolean q = false;
									while(q != true)
									{
										// if user is connected to any chat room then only he/she
										// will be allowed to chat with other users
										if(isConnected)	 
										{
											try
											{
												// Accept the message which user needs to send (broadcast)
												System.out.print("\nSend ('q' to exit): "); 
												messageSent = in.readLine();
												
												// if user enter 'q' or 'Q', that means he/she has left the chat
												if(messageSent.equals("q") || messageSent.equals("Q"))
												{
													q = true;
												}
												else if(!messageSent.equals(""))
												{
													// Send the message typed by the user to all 
													// other users through a remote method
													reg.broadcast(messageSent, connectionId);
												}
										
												// also display all the messages from the registry 
												// through another remote method invocation
												messageReceived = reg.getbroadcast();
												if (messageReceived.equals("q"))
												{
													q = true;
												}
												else if(!messageReceived.equals(""))
												{
													System.out.println("\nReceived..." + messageReceived);
												}
											} // end of try block
											catch(Exception e)
											{ 
												System.out.println("Something went wrong. Send again.");
												continue;
											}
											
										}//end of if block
										
										else // user did not connect to any chat room before chatting
										{
											System.out.println("Please connect to some chat room first.");
											break;
										}
									}
									break; 
									
							case 4:	// Disconnect from the current chat room
								
									if(isConnected == true)	// if user is connected to any chat room
									{
										isConnected = false;	// disconnect the user
										System.out.println("Disconnected from the current chat room.");
									}
									// if user did not connect to any chat room and still tries to disconnect
									else
									{
										System.out.println("Please connect to some chat room first.");
									}
									break;
									
							case 5: // Exiting the application from Client side
									System.out.println("Thank you for using our application."); 
									break; 
									
							default:System.out.println("Invalid choice!!! Please try again.");
							
						}	// end of switch case
						
					}	// end of while loop
					
				}	// end of inner try block
				
				catch(NumberFormatException e)	// if user enters anything other than a number
				{
					System.out.println("Invalid choice!!! Please start the client again."); 
				}
				
			}	// end of outer try block
			
			catch(Exception e) 
			{
				System.out.println("Oops.. Chat Client Error. Please start the client again.");
			}
			
		}	// end of else block
		
	}	// end of main method
	
}	// end of Client class