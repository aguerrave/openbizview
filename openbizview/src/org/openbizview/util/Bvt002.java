/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openbizview.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Andres
 */
	@ManagedBean
	@ViewScoped
	public class Bvt002 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt002> lazyModel;  
		
		
		
		
	    /**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt002> getLazyModel() {
			return lazyModel;
		}



	@PostConstruct	
	public void init() {
		lazyModel  = new LazyDataModel<Bvt002>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt002> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
            	//Filtro
            	if (filters != null) {
               	 for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
               		 String filterProperty = it.next(); // table column name = field name
                     filterValue = filters.get(filterProperty);
               	 }
                }
            	try { 
            		//Consulta
					select(first, pageSize,sortField, filterValue);
					//Counter
					counter(filterValue);
					//Contador lazy
					lazyModel.setRowCount(rows);  //Necesario para crear la paginaci�n
				} catch (SQLException | NamingException | ClassNotFoundException e) {	
					e.printStackTrace();
				}             
				return list;  
            } 
            
            
            //Arregla bug de primefaces
            @Override
            /**
			 * Arregla el Issue 1544 de primefaces lazy loading porge generaba un error
			 * de divisor equal a cero, es solamente un override
			 */
            public void setRowIndex(int rowIndex) {
                /*
                 * The following is in ancestor (LazyDataModel):
                 * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
                 */
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                }
                else
                    super.setRowIndex(rowIndex % getPageSize());
            }
            
		};
	}
	
	private String coduser = "";
	private String desuser = "";
	private String cluser = "";
	private String cluser1 = "";
	private String b_codrol = "";
	private String desrol = "";
	private String cluserold = "";
	private String mail = "";
	private int columns;
	private String[][] arr;
	private Object filterValue = "";
	private List<Bvt002> list = new ArrayList<Bvt002>();
	private int validarOperacion = 0;
	private String instancia = "";
	private String instancia_insert = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	
	//Cambio de password
	StringMD md = new StringMD();
	private String randomKey;
	
	
	
	
	     /**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}



	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}



		/**
	 * @return the cluser1
	 */
	public String getCluser1() {
		return cluser1;
	}

	/**
	 * @param cluser1 the cluser1 to set
	 */
	public void setCluser1(String cluser1) {
		this.cluser1 = cluser1;
	}

		/**
	 * @return the coduser
	 */
	public String getCoduser() {
		return coduser;
	}
	
	/**
	 * @param coduser the coduser to set
	 */
	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}
	
	/**
	 * @return the desuser
	 */
	public String getDesuser() {
		return desuser;
	}
	
	/**
	 * @param desuser the desuser to set
	 */
	public void setDesuser(String desuser) {
		this.desuser = desuser;
	}
	
	/**
	 * @return the cluser
	 */
	public String getCluser() {
		return cluser;
	}
	
	/**
	 * @param cluser the cluser to set
	 */
	public void setCluser(String cluser) {
		this.cluser = cluser;
	}
	
	/**
	 * @return the b_codrol
	 */
	public String getb_codrol() {
		return b_codrol;
	}
	
	/**
	 * @param b_codrol the b_codrol to set
	 */
	public void setb_codrol(String b_codrol) {
		this.b_codrol = b_codrol;
	}
	
	
	
	/**
	 * @return the cluserold
	 */
	public String getCluserold() {
		return cluserold;
	}
	
	/**
	 * @param cluserold the cluserold to set
	 */
	public void setCluserold(String cluserold) {
		this.cluserold = cluserold;
	}
	


	/**
	 * @return the validarOperacion
	 */
	public int getValidarOperacion() {
		return validarOperacion;
	}

	/**
	 * @param validarOperacion the validarOperacion to set
	 */
	public void setValidarOperacion(int validarOperacion) {
		this.validarOperacion = validarOperacion;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	

	/**
	 * @return the desrol
	 */
	public String getDesrol() {
		return desrol;
	}



	/**
	 * @param desrol the desrol to set
	 */
	public void setDesrol(String desrol) {
		this.desrol = desrol;
	}
	
	
	

	/**
	 * @return the instancia
	 */
	public String getInstancia() {
		return instancia;
	}



	/**
	 * @param instancia the instancia to set
	 */
	public void setInstancia(String instancia) {
		this.instancia = instancia;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null;
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del men�
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;


	/**
     * Inserta Usuarios.
     * <p>
     * Parámetros del Mátodo: String coduser, String desuser, String clave, String b_codrol.
     **/
    public void insert() throws  NamingException {
    	//Valida que los campos no sean nulos
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO Bvt002 VALUES (?,?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, coduser.toUpperCase());
            pstmt.setString(2, desuser.toUpperCase());
            pstmt.setString(3, md.getStringMessageDigest(cluser.toUpperCase(), StringMD.SHA256));
            pstmt.setString(4, veccodrol[0].toUpperCase());
            pstmt.setString(5, login);
            pstmt.setString(6, login);
            pstmt.setString(7, mail.toLowerCase());
            pstmt.setInt(8, Integer.parseInt(instancia_insert));

            ////System.out.println(query);
            try {
                //Avisando
            	pstmt.executeUpdate();
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                limpiarValores();
                
             } catch (SQLException e)  {
            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            }
            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }	
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    public void delete() throws NamingException  {  
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] chkbox = request.getParameterValues("toDelete");
    	
    	if (chkbox==null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("del"), "");
    	} else {
	        try {
	       	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		
	        	
	        	String param = "'" + StringUtils.join(chkbox, "','") + "'";
	
	        	String query = "DELETE from Bvt002 WHERE coduser in (" + param + ")";
	        		        	
	            pstmt = con.prepareStatement(query);
	            ////System.out.println(query);
	
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");

	                limpiarValores();	
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
  

       /**
     * Actualiza Usuarios
     * <p> Parámetros del Método: String coduser, String desuser, String cluser, String p_codrol.
     **/
    public void update() throws  NamingException {
    
    		String[] veccodrol = b_codrol.split("\\ - ", -1);

        try {

        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();	
     		
     		if(instancia==""){
     			instancia = "0 - ";
     		}
     		String[] vecinst = instancia.split("\\ - ", -1);
     		
            String query = "UPDATE Bvt002";
             query += " SET DESUSER = '" + desuser.toUpperCase() + "'";
             query += " , B_CODROL = '" + veccodrol[0].toUpperCase() + "'";
             query += " , MAIL = '" + mail.toLowerCase() + "'";
             query += " , instancia = '" + vecinst[0] + "'";
             query += " WHERE CODUSER = '" + coduser.toUpperCase() + "'";
             
            //System.out.println(query);
            pstmt = con.prepareStatement(query);
            try {
                //Avisando
            	pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                desuser = "";
           		cluser = "";
           		b_codrol = "";
           		cluserold = "";
                mail = "";	
                instancia = "";
            } catch (SQLException e) {
                e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            }

            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();       
	}
	FacesContext.getCurrentInstance().addMessage(null, msj); 
   }
    
    public void guardar() throws NamingException, SQLException{   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
    
    /**
     * Actualiza Usuarios
     * <p> Parámetros del Método: String coduser, String desuser, String cluser, String p_codrol.
     **/
    public void updatea() throws  NamingException {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();	
            //Class.forName(getDriver());
            //con = DriverManager.getConnection(
            //        getUrl(), getUsuario(), getClave());
            String query = "UPDATE Bvt002";
             query += " SET CLUSER = ?";
             query += " WHERE CODUSER = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, md.getStringMessageDigest(cluser.toUpperCase(), StringMD.SHA256));
            pstmt.setString(2, login.toUpperCase());
            try {
            	if(!cluser.equals(cluser1)){
            		 msj = new FacesMessage(FacesMessage.SEVERITY_ERROR,  getMessage("bvt002Cl1Msj"), "");
            	} else {
                //Avisando
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("bvt002up"), "");
            	}
            } catch (SQLException e) {
                e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            }
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    /**
     * Leer Datos de paises
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, ClassNotFoundException, NamingException {
  		
  		Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();		
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		
 		String query = "";

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(A.CODUSER) , trim(A.DESUSER), trim(A.CLUSER), trim(A.B_CODROL), trim(B.DESROL), trim(a.mail), a.instancia||' - '||trim(c.descripcion)";
        	   query += " FROM Bvt002 A, BVT003 B, INSTANCIAS C " ;
        	   query += " WHERE A.B_CODROL=B.CODROL" ;
        	   query += " and A.instancia=c.instancia(+)" ;
        	   query += " AND A.CODUSER like '" + coduser.toUpperCase() + "%'";
        	   query += " AND A.instancia like '" + instancia_insert + "%'";
        	   query += " AND A.CODUSER||A.DESUSER like '%" + ((String) filterValue).toUpperCase() + "%'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
			   query += " SELECT trim(A.CODUSER) , trim(A.DESUSER), trim(A.CLUSER), trim(A.B_CODROL), trim(B.DESROL), trim(a.mail), a.instancia||' - '||trim(c.descripcion)";
			   query += " FROM Bvt002 A LEFT JOIN INSTANCIAS C ON a.INSTANCIA=c.INSTANCIA , BVT003 B" ;
			   query += " WHERE A.B_CODROL=B.CODROL " ;
			   query += " AND cast(A.instancia as text) like '" + instancia_insert + "%'";
			   query += " AND A.CODUSER||A.DESUSER like '%" + ((String) filterValue).toUpperCase() + "%'";
			   query += " AND A.CODUSER like '" + coduser.toUpperCase() + "%'";
			   query += " order by " + sortField ;
			   query += " LIMIT " + pageSize;
			   query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
			   query += "SELECT * FROM (SELECT ";
			   query += "			   ROW_NUMBER() OVER (ORDER BY A.CODUSER ASC) AS ROW_NUM, ";
			   query += "			   A.CODUSER , A.DESUSER, A.CLUSER, A.B_CODROL, B.DESROL, a.mail, a.instancia ";
			   query += "			   FROM Bvt002 A, BVT003 B ";
			   query += "			   WHERE A.B_CODROL=B.CODROL) TOT ";
			   query += "WHERE  ";
			   query += "TOT.CODUSER + TOT.DESUSER LIKE '%" + ((String) filterValue).toUpperCase() + "%')) ";
			   query += " AND A.instancia like '" + instancia_insert + "%'";
			   query += " AND TOT.CODUSER like '" + coduser.toUpperCase() + "%'";
			   query += "AND TOT.ROW_NUM <= " + pageSize;
			   query += "AND TOT.ROW_NUM > " + first; 
			   query += "ORDER BY " + sortField;        		
        }

        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
        while (r.next()){
        Bvt002 select = new Bvt002();
     	select.setCoduser(r.getString(1));
     	select.setDesuser(r.getString(2));
        select.setCluser(r.getString(3));
        select.setb_codrol(r.getString(4) + " - " + r.getString(5));
        select.setDesrol(r.getString(5));
        select.setMail(r.getString(6));
        select.setInstancia(r.getString(7));
        	//Agrega la lista
        	list.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
  	}
  	
 	/**
     * Leer registros en la tabla
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void counter(Object filterValue ) throws SQLException, NamingException {
     try {	
    	Context initContext = new InitialContext();     
   		DataSource ds = (DataSource) initContext.lookup(JNDI);
   		con = ds.getConnection();
   	   //Reconoce la base de datos de conecci�n para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conecci�n
   	      		
 		String query = "";
 		
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_bvt002('" + ((String) filterValue).toUpperCase() + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_bvt002('" + ((String) filterValue).toUpperCase() +  "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_bvt002('" + ((String) filterValue).toUpperCase() +  "')";
            break;
  		}

        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);

         r =  pstmt.executeQuery();
        
        
        while (r.next()){
        	rows = r.getInt(1);
        }
     } catch (SQLException e){
         e.printStackTrace();    
     }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();

  	}

        
    /**
     * Leer datos de Usuarios
     *<p> Par�metros del M�todo: String coduser, String desuser.
      * * Fila desde y hasta para paginaci�n, orden de la consulta.
     * @throws NamingException 
     * @throws IOException 
     **/

    @SuppressWarnings("null")
	public void  selectBvt002a(String usuario, String orden, String pool) throws NamingException  {

        //Pool de conecciones JNDI. Cambio de metodolog�a de conexi�n a bd. Julio 2010
        Context initContext = new InitialContext();
        DataSource ds = (DataSource) initContext.lookup(JNDI);
        try {
            Statement stmt = null;
            ResultSet rs;
            Connection con = ds.getConnection();
            
            
            String query = "SELECT CODUSER , DESUSER" +
           " FROM Bvt002" +
           " WHERE CODUSER = '" + usuario.toUpperCase() + "'" +
           " ORDER BY " + orden ;
            //System.out.println(query);
            try{
            rs = stmt.executeQuery(query);
            rows = 1;
 		    rs.last();
 		    rows = rs.getRow();
            //System.out.println(rows);

            ResultSetMetaData rsmd = rs.getMetaData();
        	columns = rsmd.getColumnCount();
 		    //System.out.println(columns);
        	arr = new String[rows][columns];

            int i = 0;
 		    rs.beforeFirst();
            while (rs.next()){
                for (int j = 0; j < columns; j++)
 				arr [i][j] = rs.getString(j+1);
 				i++;
               }
                    } catch (SQLException e) {
                    e.printStackTrace();
                }
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
     /**
      * Leer Datos de Usuarios
      *<p> Parametros del Metodo: String coduser, String desuser.
      * String pool
     * @throws IOException 
      **/
     public void selectLogin(String user, String pool) throws NamingException {

         //Pool de conecciones JNDI. Cambio de metodologia de conexion a Bd. Julio 2010
         Context initContext = new InitialContext();
         DataSource ds = (DataSource) initContext.lookup(JNDI);
         try {
             Statement stmt;
             ResultSet rs;
             Connection con = ds.getConnection();
           //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
      		DatabaseMetaData databaseMetaData = con.getMetaData();
      		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
             //Class.forName(getDriver());
             //con = DriverManager.getConnection(
             //        getUrl(), getUsuario(), getClave());
             stmt = con.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             String query = "";
             
             //System.out.println( productName );
             
             switch ( productName ) {
             case "Oracle":
            	 query = "SELECT trim(coduser), trim(cluser), trim(B_CODROL), trim(desuser), trim(mail), instancia";
                 query += " FROM BVT002";
                 query += " where coduser = '" + user.toUpperCase() + "'";
                  break;
             case "PostgreSQL":
            	 query = "SELECT trim(coduser), trim(cluser), trim(B_CODROL), trim(desuser), trim(mail), instancia";
                 query += " FROM BVT002";
                 query += " where coduser = '" + user.toUpperCase() + "'";
                  break;
             case "Microsoft SQL Server":
            	 query = "SELECT coduser, cluser, B_CODROL, desuser, mail, instancia";
                 query += " FROM BVT002";
                 query += " where coduser = '" + user.toUpperCase() + "'";
     	         break;            		   
       		}
       		
             
            // System.out.println(query);
             try {
                 rs = stmt.executeQuery(query);
                 rows = 1;
                 rs.last();
                 rows = rs.getRow();
                 //System.out.println(rows);

                 ResultSetMetaData rsmd = rs.getMetaData();
                 columns = rsmd.getColumnCount();
                 //System.out.println(columns);
                 arr = new String[rows][columns];

                 int i = 0;
                 rs.beforeFirst();
                 while (rs.next()) {
                     for (int j = 0; j < columns; j++) {
                         arr[i][j] = rs.getString(j + 1);
                     }
                     i++;
                 }
             } catch (SQLException e) {
             }
             stmt.close();
             con.close();

         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     /**
      * @return Retorna el arreglo
      **/
     public String[][] getArray(){
     	return arr;
     }

     /**
      * @return Retorna número de filas
      **/
     public int getRows(){
     	return rows;
     }
     /**
      * @return Retorna número de columnas
      **/
     public int getColumns(){
     	return columns;
     }

   	private void limpiarValores() {
 		// TODO Auto-generated method stub
   		coduser = "";
   		desuser = "";
   		cluser = "";
   		b_codrol = "";
   		cluserold = "";
   		mail = "";
   		instancia = "";
 	}
   	
   	
   	/**
     * Envía notificación a usuario al cambiar la contraseña
    **/
    private void ChangePassNotification2(String mail, String clave) {
    	try {
    		Context initContext = new InitialContext();     
        	Session session = (Session) initContext.lookup(JNDIMAIL);
    			// Crear el mensaje a enviar
    			MimeMessage mm = new MimeMessage(session);
    			// Establecer las direcciones a las que será enviado
    			// el mensaje (test2@gmail.com y test3@gmail.com en copia
    			// oculta)
    			// mm.setFrom(new
    			// InternetAddress("opennomina@dvconsultores.com"));
    			mm.addRecipient(Message.RecipientType.TO,
    					new InternetAddress(mail));
	        
    			// Establecer el contenido del mensaje
    			mm.setSubject(getMessage("mailUserUserChgPass"));
    			mm.setText(getMessage("mailNewUserMsj2"));
    			
    			// use this is if you want to send html based message
                mm.setContent(getMessage("mailNewUserMsj6") + " " + coduser.toUpperCase() + " / " + clave + "<br/><br/> " + getMessage("mailNewUserMsj2"), "text/html; charset=utf-8");

    			// Enviar el correo electrónico
    			Transport.send(mm);
    			//System.out.println("Correo enviado exitosamente");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
     }
   	
   	
   	//Recuperación de contraseñas
   	/**
     * Recuperación de contraseña por parte del usuario
     * @throws NamingException 
   	 * @throws ClassNotFoundException 
     * @throws IOException 
     **/ 
  	public void reqChgpass() throws NamingException, SQLException, ClassNotFoundException{
  			try {
  	        	Context initContext = new InitialContext();     
  	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
  	        	con = ds.getConnection();
  	        
  	        randomKey = "BIZ"+md.randomKey();
  	        	
  			String query = "UPDATE bvt002";
  			       query+= " set cluser = '" + md.getStringMessageDigest(randomKey.toUpperCase(), StringMD.SHA256) + "'";
  			       query+= " where coduser = '" +  coduser.toUpperCase() + "'";
  			pstmt = con.prepareStatement(query); 
  			//System.out.println(query);
  			Bvt002 seg = new Bvt002(); // Crea objeto para el login
  			int rows = 0;
  			// LLama al método que retorna el usuario y la contraseña
  			seg.selectLogin(coduser.toUpperCase(), JNDI);
  			rows = seg.getRows();
  			String[][] vltabla = seg.getArray();
  			//System.out.println("Mail: " + vltabla[0][4].toLowerCase());
        	//System.out.println("RandomKey: " + randomKey);
            //Valida que exista el usuario
  			if (rows == 0) {
  				msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("noreg"), "");

  			} else {
  			pstmt.executeUpdate();

            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("chgpass7"), "");
            	ChangePassNotification2(vltabla[0][4].toLowerCase(), randomKey);
            	
            cluser= "";
  			}
  			} catch (SQLException e) {
                e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
            }
  			
  			pstmt.close();
            con.close();
              
  		FacesContext.getCurrentInstance().addMessage(null, msj); 
  	}


}
