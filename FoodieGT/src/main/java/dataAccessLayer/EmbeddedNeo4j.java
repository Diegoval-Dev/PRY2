package dataAccessLayer;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.summary.ResultSummary;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EmbeddedNeo4j implements AutoCloseable{
	//Crea un driver
	private final Driver driver;
	
	/**
	 * Constructor del driver
	 * @param uri String de conexion
	 * @param user usuario
	 * @param password contrasenia
	 */
	public EmbeddedNeo4j( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
     * Imprime un saludo y crea un nodo Greeting en la base de datos.
     *
     * @param message el mensaje del saludo
     */
	public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Greeting) " +
                                                     "SET a.message = $message " +
                                                     "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
	
	public LinkedList<Restaurante> getRestaurants(){
		try(Session session = driver.session()){
			LinkedList<Restaurante> restaurants = session.readTransaction( new TransactionWork<LinkedList<Restaurante>>()
				{
				@Override
                public LinkedList<Restaurante> execute( Transaction tx )
                {
                    Result result = tx.run( "MATCH (restaurante:Restaurante) RETURN restaurante.nombre , restaurante.ubicacion, restaurante.precio, restaurante.tipo_comida, restaurante.ambiente, restaurante.tipo_servicio,restaurante.horario, restaurante.web, restaurante.img1, restaurante.img2, restaurante.img3");
                    LinkedList<Restaurante> myrestaurants = new LinkedList<Restaurante>();
                    List<Record> registros = result.list();
                    
                    for (int i = 0; i < registros.size(); i++) {
                   	 //myactors.add(registros.get(i).toString());
                    	
                    	String nombre = registros.get(i).get("restaurante.nombre").toString();
                    	
                    	String ubicacion = registros.get(i).get("restaurante.ubicacion").toString();
                    	
                    	String precio = registros.get(i).get("restaurante.nombre").toString();
                    	
                    	String tipoComida = registros.get(i).get("restaurante.tipo_comida").toString();
                    	
                    	String ambiente = registros.get(i).get("restaurante.ambiente").toString();
                    	
                    	String tipoServicio = registros.get(i).get("restaurante.tipo_servicio").toString();
     	
                    	String horario = registros.get(i).get("restaurante.horario").toString();
                    	      
                    	String web = registros.get(i).get("restaurante.web").toString();
                    	String img1 = registros.get(i).get("restaurante.img1").toString();
                    	String img2 = registros.get(i).get("restaurante.img2").toString();
                    	String img3 = registros.get(i).get("restaurante.img3").toString();
                    	
                    	
                    	Restaurante rs = new Restaurante(nombre, ubicacion, precio, tipoComida, ambiente, tipoServicio, horario, web, img1, img2, img3);
                   	 	myrestaurants.add(rs);
                    }
                    
                    return myrestaurants;
                	}	
				
				});
			return restaurants;
		}
	}
	
	public LinkedList<String> getMoviesByLocation(String location){
		return null;
	}
	
}
