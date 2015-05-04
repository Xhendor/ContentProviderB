package curso.uabc.com.contentproviderb;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private Button btnInsertar;
	private Button btnConsultar;
	private Button btnEliminar;
	private Button btnLlamadas;
	private TextView txtResultados;
    private String _ID="_id";
    private String COL_NOMBRE="nombre";
    private String COL_TELEFONO="telefono";
    private String COL_EMAIL="email";
    private static final Uri CONTENT_URI = Uri.parse(
            "content://curso.uabc.com.test/clientes");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Referencias a los controles
        txtResultados = (TextView)findViewById(R.id.TxtResultados);
        btnConsultar = (Button)findViewById(R.id.BtnConsultar);
        btnInsertar = (Button)findViewById(R.id.BtnInsertar);
        btnEliminar = (Button)findViewById(R.id.BtnEliminar);
        btnLlamadas = (Button)findViewById(R.id.BtnLlamadas);
        
btnConsultar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Columnas de la tabla a recuperar
		        String[] projection = new String[] {
		        		_ID,
                        COL_NOMBRE,
                        COL_TELEFONO,
                        COL_EMAIL };

		        Uri clientesUri =  CONTENT_URI;

		        ContentResolver cr = getContentResolver();
		        
		        //Hacemos la consulta
		        Cursor cur = cr.query(clientesUri,
		                                 projection, //Columnas a devolver
		                                 null,       //Condici�n de la query
		                                 null,       //Argumentos variables de la query
		                                 null);      //Orden de los resultados
		        
		        if (cur.moveToFirst()) 
		        {
		            String nombre; 
		            String telefono;
		            String email;
		            
		            int colNombre = cur.getColumnIndex(COL_NOMBRE);
		            int colTelefono = cur.getColumnIndex(COL_TELEFONO);
		            int colEmail = cur.getColumnIndex(COL_EMAIL);
		        
		            txtResultados.setText("");
		            
		            do 
		            {
		            	
		                nombre = cur.getString(colNombre);
		                telefono = cur.getString(colTelefono);
		                email = cur.getString(colEmail);
		                
		                txtResultados.append(nombre + " - " + telefono + " - " + email + "\n");
		                
		            } while (cur.moveToNext());
		        }
			}
		});
        
        btnInsertar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ContentValues values = new ContentValues();

				values.put(COL_NOMBRE, "ClienteN");
				values.put(COL_TELEFONO, "999111222");
				values.put(COL_EMAIL, "nuevo@email.com");

				ContentResolver cr = getContentResolver();
				
				cr.insert(CONTENT_URI, values);
			}
		});
        
        btnEliminar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ContentResolver cr = getContentResolver();
				
				cr.delete(CONTENT_URI, COL_NOMBRE + " = 'ClienteN'", null);
			}
		});
        
        btnLlamadas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

		        String[] projection = new String[] {
		                Calls.TYPE,
		                Calls.NUMBER };

		        Uri llamadasUri =  Calls.CONTENT_URI;

		        ContentResolver cr = getContentResolver();
		        
		        Cursor cur = cr.query(llamadasUri,
		                                 projection, //Columnas a devolver
		                                 null,       //Condici�n de la query
		                                 null,       //Argumentos variables de la query
		                                 null);      //Orden de los resultados
		        
		        if (cur.moveToFirst()) 
		        {
		            int tipo;
		            String tipoLlamada = "";
		            String telefono;
		            
		            int colTipo = cur.getColumnIndex(Calls.TYPE); 
		            int colTelefono = cur.getColumnIndex(Calls.NUMBER);
		        
		            txtResultados.setText("");
		            
		            do 
		            {
		            	
		                tipo = cur.getInt(colTipo);
		                telefono = cur.getString(colTelefono);
		                
		                if(tipo == Calls.INCOMING_TYPE)
		                	tipoLlamada = "ENTRADA";
		                else if(tipo == Calls.OUTGOING_TYPE)
		                	tipoLlamada = "SALIDA";
		                else if(tipo == Calls.MISSED_TYPE)
		                	tipoLlamada = "PERDIDA";
		                
		                txtResultados.append(tipoLlamada + " - " + telefono + "\n");
		                
		            } while (cur.moveToNext());
		        }
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

}
