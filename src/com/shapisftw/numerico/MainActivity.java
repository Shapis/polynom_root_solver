package com.shapisftw.numerico;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		assignViews();
		assignDrawer();
		assignListeners();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		actionBarDrawerToggle.syncState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_import_file:
			SingleInputFragment.polyInfo = readFile();
			populateLists(SingleInputFragment.polyInfo);
			return true;
		case R.id.action_export_file:
			writeFile();
			return true;
		}
		// call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns
		// true
		// then it has handled the app icon touch event

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			drawerLayout.closeDrawer(drawerListView);
			// display view for selected nav drawer item
			displayView(position);

		}
	}

	private void displayView(int position) {
		android.app.Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new SingleInputFragment();
			break;
		case 1:
			fragment = new SingleResultsFragment();
			break;
		}
		if (fragment != null) {

			android.app.FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// closes current keyboard when a new fragment is called
			InputMethodManager inputMethodManager = (InputMethodManager) this
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);

			// update selected item and title, then close the drawer
			drawerListView.setItemChecked(position, true);
			drawerListView.setSelection(position);
			setTitle(drawerListViewItems[position]);
			drawerLayout.closeDrawer(drawerListView);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}

	}

	private void assignViews() {
		// get list items from strings.xml
		drawerListViewItems = getResources().getStringArray(R.array.items);
		// get ListView defined in activity_main.xml
		drawerListView = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		drawerListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_listview_item, drawerListViewItems));

		// 2. App Icon
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	}

	private void assignDrawer() {
		// 2.1 create ActionBarDrawerToggle
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		);

		// 2.2 Set actionBarDrawerToggle as the DrawerListener
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		// 2.3 enable and show "up" arrow
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// just styling option
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
	}

	private void assignListeners() {
		drawerListView.setOnItemClickListener(new DrawerItemClickListener());

	}

	private ArrayList<PolyInfo> readFile() {
		ArrayList<PolyInfo> polyInfoArray = new ArrayList<PolyInfo>();

		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/Download");
		File file = new File(dir, "NumericoInput.txt");

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currentLine;

			while ((currentLine = br.readLine()) != null) {

				ArrayList<Double> myDoubles = new ArrayList<Double>();
				Matcher matcher = Pattern.compile(
						"[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?").matcher(
						currentLine);

				while (matcher.find()) {
					double k = Double.parseDouble(matcher.group());
					myDoubles.add(k);
				}

				PolyInfo temp = new PolyInfo();
				myDoubles.remove(0);
				temp.setGrau(myDoubles.size()-1);
				temp.setPoli(myDoubles);
				polyInfoArray.add(temp);

			}
			br.close();
		} catch (IOException e) {
			Toast toast = Toast.makeText(MainActivity.this,
					"Arquivo não encontrado!", Toast.LENGTH_SHORT);
			toast.show();
			return polyInfoArray;
		}

		Toast toast = Toast.makeText(MainActivity.this,
				"Arquivo importado com sucesso!", Toast.LENGTH_SHORT);
		toast.show();

		return polyInfoArray;
	}

	private void writeFile() {
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/Download");
		File file = new File(dir, "NumericoOutput.txt");

		int temp1 = 1;
		StringBuilder stringBuilder = new StringBuilder();
		String stringTemp = "Inicialização de variável.";
		String stringFinal = "Inicialização de variável.";
		
		DoMath doMath1 = new DoMath();
		
		
		for (PolyInfo i : doMath1.executarAlgoritmo(SingleInputFragment.polyInfo)) {
			int temp2 = 1;
			stringTemp = "RAIZES POLINOMIO " + temp1;
			stringBuilder.append(stringTemp);
			stringBuilder.append(System.getProperty("line.separator"));
			for (double j : i.getPoli()) {
				stringTemp = temp2 + ": " + j;
				stringBuilder.append(stringTemp);
				stringBuilder.append(System.getProperty("line.separator"));
				temp2++;
			}
			stringBuilder.append(System.getProperty("line.separator"));
			temp1++;
		}
		stringFinal = stringBuilder.toString();
		
		
		
		
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			
			BufferedWriter br = new BufferedWriter(new FileWriter(file));
			br.write(stringFinal);
			br.close();

		} catch (Exception e) {
			Toast toast = Toast.makeText(MainActivity.this,
					"Arquivo não foi salvo!", Toast.LENGTH_SHORT);
			toast.show();

		}
		Toast toast = Toast.makeText(MainActivity.this,
				"Arquivo salvo com sucesso!", Toast.LENGTH_SHORT);
		toast.show();
	}

	private void populateLists(ArrayList<PolyInfo> i) {
		PolyInfo j = i.get(0);
		SingleInputFragment.itemsSingleInput = j.getPoli();

	}

}
