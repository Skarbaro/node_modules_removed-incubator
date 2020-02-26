import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClass {
    public String getInfo(){
        return "This text of class.";
    }
}

public class NewServlet extends HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<h1> HELLO, " + name + " " + surname + " </h1>");
        pw.println("</html>");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/testJsp.jsp");
        dispatcher.forward(request, response);
    }
}

public class TestASClient {
	public static void main(String[] args) throws InterruptedException {	
		try(Socket socket = new Socket("localhost", 3345);	
				BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
				DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
				DataInputStream ois = new DataInputStream(socket.getInputStream());	)
		{
			System.out.println("Client connected to socket.");
			System.out.println();
			System.out.println("Client writing channel = oos & reading channel = ois initialized.");	
				while(!socket.isOutputShutdown()){					
					if(br.ready()){					
			System.out.println("Client start writing in channel...");
			Thread.sleep(1000);
			String clientCommand = br.readLine();			
			oos.writeUTF(clientCommand);
			oos.flush();
			System.out.println("Clien sent message " + clientCommand + " to server.");
			Thread.sleep(1000);		
			if(clientCommand.equalsIgnoreCase("quit")){			
				System.out.println("Client kill connections");
				Thread.sleep(2000);				
				if(ois.available()!=0)		{	
					System.out.println("reading...");
					String in = ois.readUTF();
					System.out.println(in);
							}			
				break;				
			}		
			System.out.println("Client wrote & start waiting for data from server...");			
			Thread.sleep(2000);		
			if(ois.available()!=0)		{									
			System.out.println("reading...");
			String in = ois.readUTF();
			System.out.println(in);
					}			
				}
			}

			System.out.println("Closing connections & channels on clentSide - DONE.");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}