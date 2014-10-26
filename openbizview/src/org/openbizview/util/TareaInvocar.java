package org.openbizview.util;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;



//La clase que tiene la tarea debe de implementar de la clase Job de Quartz
//ya que el programador de la tarea va a buscar una clase que implemente de ella
//y buscara el metodo execute para ejecutar la tarea
@ManagedBean
@ViewScoped
public class TareaInvocar extends Bd implements Job {

	PntGenerica consulta = new PntGenerica();
	
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;



	java.util.Date fechadia = new java.util.Date();

 /**
  * Método que se ejecuta según la tarea programada, según tres opciones:
  * 1- Envío de reporte y corre
  * 2- Envío de reporte a una ruta específica sin envío de corre
  * 3- Envío de reporte a una lista de personas en partícular (Ejemplo: recibos de pago o notificaciones particulares)	
  */
  //Metodo que se ejecutara cada cierto tiempo que lo programemos despues
  public void execute(JobExecutionContext jec) throws JobExecutionException {
    //Aca pueden poner la tarea o el job que desean automatizar
    //Por ejemplo enviar correo, revisar ciertos datos, etc
    SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
   
    //System.out.println( "Tarea invocada a la hora: " + formato.format(new Date()));
    JobKey key = jec.getJobDetail().getKey();
    String job = key.toString().replace("unico.", "");
    //System.out.println("Trigger orden: " + job);
    
    java.sql.Date sqlDate = new java.sql.Date(fechadia.getTime());
    int rowsrep;
   	String[][] vltablarep;
   	int rowsmail;
   	String[][] vltablamail;
	
	//Envía reporte
    //Selecciona nombre del reporte y id del grupo según hora programada
  
	try {
		consulta.selectPntGenerica("select trim(codrep), trim(rutarep), trim(rutatemp) from t_programacion where hora=trim('" + formato.format(new Date()) + "') and job = trim('" + job + "') order by job", JNDI);
	
	//System.out.println("select nombrereporte, idgrupo, trim(rutareporte), trim(rutatemp) from mailtarea where hora='" + formato.format(new Date()) + "'");
	   
    rowsrep = consulta.getRows();
	vltablarep = consulta.getArray();
	
	//Imprime reporte
	if (rowsrep>0){//Si la consulta es mayor a cero devuelve registros envía el correo
	 new RunReport().outReporteRecibo(vltablarep[0][0].toString(), "pdf", vltablarep[0][1].toString(), vltablarep[0][2].toString(), vltablarep[0][0].toString(), sqlDate);

	}
	//Consulta lista de correos
	consulta.selectPntGenerica("select trim(a.mail), trim(b.rutatemp), trim(b.codrep), trim(b.asunto), trim(b.contenido)" +
			" from maillista a, t_programacion b" +
			" where a.idgrupo=b.idgrupo  " +
			//" and hora=trim('" + formato.format(new Date()) + "') and job =trim('" + job + "') order by a.mail", JNDI);
			//Modificación del 24/08/2014, si la conección es muy lenta ó el reporte es lago la tarea se ejecuta minutos después
	        " and job =trim('" + job + "') order by a.mail", JNDI);
	
	/*
	System.out.println("select trim(a.mail), trim(b.rutatemp), trim(b.codrep), trim(b.asunto), trim(b.contenido)" +
			" from maillista a, t_programacion b" +
			" where a.idgrupo=b.idgrupo  " +
			" and job =trim('" + job + "') order by a.mail");
	*/		
	
	rowsmail = consulta.getRows();
	vltablamail = consulta.getArray();
	
	if (rowsmail>0){//Si la consulta es mayor a cero devuelve registros envía el correo
		for(int i=0;i<rowsmail;i++){
		 new Sendmail().mailthread(vltablamail[i][0], vltablamail[i][1], vltablamail[i][2], vltablamail[i][3], vltablamail[i][4]);
		}
	}
	
	
	} catch (NamingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
  }
  
  

}
