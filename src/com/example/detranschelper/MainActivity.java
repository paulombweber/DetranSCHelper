package com.example.detranschelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button consultaVeiculo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		consultaVeiculo = (Button) findViewById(R.id.btnConsultaVeiculo);
		consultaVeiculo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
				String urlString = "http://consultas.detrannet.sc.gov.br/servicos/consultaveiculo.asp";
				Properties parameters = new Properties();
				parameters.setProperty("placa", "LZP8024");
				parameters.setProperty("renavam", "699148057");
				urlString += addProperties(parameters);

				try {
					HttpURLConnection conn = doRequest(urlString);
					String answer = getAnswer(conn);
					Toast.makeText(MainActivity.this, answer,
							Toast.LENGTH_SHORT).show();
					Toast.makeText(
							MainActivity.this,
							"Resultado: " + conn.getResponseCode() + "/"
									+ conn.getResponseMessage(),
							Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private String getAnswer(HttpURLConnection conn) {
		StringBuffer newData = new StringBuffer(10000);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			try {
				String s = "";
				while (null != ((s = br.readLine()))) {
					newData.append(s);
				}
			} finally {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newData.toString();
	}

	private HttpURLConnection doRequest(String urlString)
			throws MalformedURLException, IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Request-Method", "GET");
		conn.setDoInput(true);
		conn.setDoOutput(false);
		conn.connect();
		return conn;
	}

	private String addProperties(Properties prop) {
		String parametersURL = "";
		Iterator<Object> i = prop.keySet().iterator();
		int counter = 0;
		while (i.hasNext()) {
			String name = (String) i.next();
			String value = prop.getProperty(name);
			// adiciona com um conector (? ou &) o primeiro é ?, depois são &
			parametersURL += (++counter == 1 ? "?" : "&") + name + "=" + value;
		}
		return parametersURL;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
