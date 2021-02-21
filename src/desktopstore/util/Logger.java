/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Wilson Carvajal
 */
public class Logger {
    
    public final static String M_ERROR = "Error";
	public final static String M_WARNING = "Warning";
	public final static String M_INFORMATION = "Information";


	public static String getDirPath()
	{
		return Util.LOGSDIR;
	}

	public static  String getFilePath()
	{
		return Util.LOGSDIR + "store.txt";
	}
	/**
	 * Checks if the directory where the files will be stored exists. If not, it
	 * is created.
	 */
        
	public static void createFile() {
		try {
			File x = new File(getFilePath());
			if (!x.exists()) {
                            new File(getDirPath()).mkdir();
                            x.createNewFile();
                            writeInFile(x, createLogLine("CREATION", "Log file create"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private static boolean fileExist() {
		boolean exist = false;
		File f = new File(getFilePath());
		if (f.exists()) {
			exist = true;
		}
		return exist;
	}

	public static boolean deleteLogFile() {
            boolean isDeleted = false;
            if (fileExist()) {
                    File f = new File(getFilePath());
                    isDeleted = f.delete();
            }
            return isDeleted;
	}

	/**
	 * Logs any kind of information, given a Tag and a message
         * @param logTag	 
         * @param className	 
         * @param method	 
	 * @param message The message that will be logged
	 */
	public static void log(String logTag, String className,
						   String method, String message) {
		File x = new File(getFilePath());
		writeInFile(x, createLogLine(logTag, className, method, message));
	}



	private static void writeInFile(File file, String out) {
		try {
			if (!fileExist()) {
				createFile();
			}

			if (fileExist()) {
				long length = file.length();
				if(length>20000000)
				{
					File f = new File(getDirPath()+ File.separator + (new Date()).getTime() + ".txt");
					file.renameTo(f);
					createFile();
					file = new File(getFilePath());
				}

				PrintWriter p = new PrintWriter(
						new FileOutputStream(file, true));
				p.println(out);
				p.close();
				evaluateFilesNumber();
			}
		} catch (NullPointerException nullException) {
			System.out.println("error to write in Log file error:"+nullException);
                } catch (FileNotFoundException ex) {
                
                }

	}

	public static String createLogLine(String logTag,String className, String method, String message) {
		StringBuilder line = new StringBuilder();
		line.append("[");
		line.append(logTag);
		line.append("];[");
		line.append(className);
		line.append("];[");
		line.append(method);
		line.append("];[");
		line.append(Calendar.getInstance().getTime());
		line.append("];");
		line.append(message);
		return line.toString();
	}

	private static String createLogLine(String tag, String message) {
		StringBuilder line = new StringBuilder();
		line.append("[");
		line.append(tag);
		line.append("];[");
		line.append(Calendar.getInstance().getTime());
		line.append("];");
		line.append(message);
		return line.toString();
	}

	/**
	 * Evalua que el numero de archivos Logs no supere 3, si lo supera borra el archivo mas antiguo
	 * @param ctx contexto actual de la aplicación
	 */
	private static void evaluateFilesNumber()
	{
		File folder = new File(getDirPath());
		if(folder.isDirectory())
		{
			File [] files = folder.listFiles();
			File fSelected = null;
			if(files.length>2)
			{
				for(File file : files)
				{
					if(!file.getName().equals("store.txt"))
					{
						if(fSelected == null)
						{
							fSelected = file;
						}
						else
						{
							if(fSelected.lastModified()>file.lastModified())
							{
								fSelected = file;
							}
						}
					}
				}
				if(fSelected!=null)
				{
					fSelected.delete();
				}
			}

		}
	}

	
}
