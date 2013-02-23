package exo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.Buffer;
import java.text.DateFormat;
import java.util.Date;

public class MyRunnable implements Runnable {

	private Socket socketParClient;


	public MyRunnable(final Socket socketParClient){
		this.socketParClient= socketParClient;

	}
	@Override
	public void run() {

		final OutputStream oust;
		final InputStream inst;

		try{
			try {
				inst = socketParClient.getInputStream();
				oust = socketParClient.getOutputStream();

				final InputStreamReader inreader = new InputStreamReader(inst);
				final BufferedReader inbr = new BufferedReader(inreader);

				String s=inbr.readLine();
				String[] request = s.split(" ");


				if(!request[0].equals("GET") || /*!request[1].contains("/") ||*/ !request[2].startsWith("HTTP/", 0)){
					//System.out.print("Well Bad");
					throw new RuntimeException("Bad request");
				}

				while(true){
					s=inbr.readLine();
					if(s.isEmpty()){
						break;
					}	
				}

				File resource=new File(request[1]);

				if (resource.isDirectory()){
					System.out.println("Pas pour l'instant");
				} else {
					// It not a directory

					if(resource.canRead()){
						//System.out.println("It not a directory");
						final Writer writer = new OutputStreamWriter(oust);
						final PrintWriter pwr=new PrintWriter(writer,true);
						pwr.println("HTTP/1.0 200 OK\r\n"+"Content-Length: "+resource.length());
						pwr.flush();

						InputStream ips=new FileInputStream(resource);
						BufferedInputStream ipsr=new BufferedInputStream(ips);
						BufferedOutputStream opsr=new BufferedOutputStream(oust);

						final int lon=1024;
						byte[] data = new byte[lon];

						while (true){
							int n = ipsr.read(data);
							if (n == -1) {		 
                                break;
                        }
                        opsr.write(data, 0, n);
						}
						opsr.flush();
						opsr.close();
					}
				}


			}
			catch(RuntimeException e){
				socketParClient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}




		//try {
		//oust = socketParClient.getOutputStream();

		//final Writer writer = new OutputStreamWriter(oust);
		//final PrintWriter pwr=new PrintWriter(writer);

		//try{
		//	while(true){
		//		DateFormat df = DateFormat.getDateTimeInstance();
		//		Date now = new Date();
		//String dateTime = df.format(now);
		//	pwr.println(dateTime);
		//	pwr.flush();
		//	}
		//}
		//	finally{
		//	writer.close();
		//}
		//	}catch (Exception e) {
		// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
	}
}
