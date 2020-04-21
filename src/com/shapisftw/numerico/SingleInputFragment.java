package com.shapisftw.numerico;

import java.util.ArrayList;

import com.shapisftw.numerico.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class SingleInputFragment extends Fragment  {

	protected static ArrayList<Double> itemsSingleInput  = new ArrayList<Double>();
	protected static ArrayList<PolyInfo> polyInfo = new ArrayList<PolyInfo>();
	private ArrayList<String> displayItemsSingleInput = new ArrayList<String>();
	private ArrayAdapter<String> displayItemsAdapterSingleInput;
	private ListView listViewSingleInput;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_single_input,
				container, false);
		assignBtnListeners(rootView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		buildArrayLists();
		assignViews();
		requestFocus();
		//assignListViewListener();
		
	}
	
	private void assignViews() {
		listViewSingleInput = (ListView) getView().findViewById(
				R.id.listViewSingleInput);
		displayItemsAdapterSingleInput = new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1, displayItemsSingleInput);
		listViewSingleInput.setAdapter(displayItemsAdapterSingleInput);
	}
	
	private void assignBtnListeners(View rootView) {
		 Button btnAddSingleInput = (Button) rootView.findViewById(R.id.btnAddSingleInput);
		 btnAddSingleInput.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  addSingleInput();
		      }
		    });
		 Button btnResetSingleInput = (Button) rootView.findViewById(R.id.btnResetSingleInput);
		 btnResetSingleInput.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  resetSingleInput();
		      }
		    });
		 
	}
	
	private void assignListViewListener() {
		listViewSingleInput.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long rowId) {
				itemsSingleInput.remove(position);
				displayItemsAdapterSingleInput.notifyDataSetChanged();
				buildArrayLists();
				return true;
			}
		});
	}
	
	
	private void requestFocus() {
		EditText editTextAddNewSingleInput = (EditText) getView().findViewById(R.id.editTextSingleInput);
		editTextAddNewSingleInput.requestFocus();
	}

	
	private void addSingleInput() {
		EditText editTextAddNewSingleInput = (EditText) getView().findViewById(R.id.editTextSingleInput);
		try{
		double value = Double.parseDouble(editTextAddNewSingleInput.getText().toString());
		itemsSingleInput.add(value);
		buildArrayLists();
		displayItemsAdapterSingleInput.notifyDataSetChanged();
		editTextAddNewSingleInput.setText("");
		//Toast.makeText(getActivity(), getString(R.string.single_input_added) ,Toast.LENGTH_SHORT).show();
		}catch(Exception e) {
			editTextAddNewSingleInput.setText("");
			requestFocus();
		}
		listViewSingleInput.setSelection(itemsSingleInput.size());
		addToPolyInfo(itemsSingleInput);
		}
	
	private void addToPolyInfo(ArrayList<Double> i) {
		polyInfo.clear();
		PolyInfo j = new PolyInfo();
		j.setGrau(i.size()-1);
		j.setPoli(i);
		polyInfo.add(j);
	}
	
	private void resetSingleInput() {
		itemsSingleInput.clear();
		polyInfo.clear();
		buildArrayLists();
		displayItemsAdapterSingleInput.notifyDataSetChanged();
		requestFocus();
	}

	
	private void buildArrayLists()	{
		displayItemsSingleInput.clear();
		int k = 1;
		for (double i: itemsSingleInput) {
			displayItemsSingleInput.add(k +":\t\t" + i + " x^" + (k-1));
			k++;
		}
		
		
	}
	
	
	}
	

	
