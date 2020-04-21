package com.shapisftw.numerico;


import com.shapisftw.numerico.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SingleResultsFragment extends Fragment {
	
	private TextView textViewSingleVariableResults;
	private int temp1 = 1;
	private StringBuilder stringBuilder = new StringBuilder();
	private String stringTemp = "Inicialização de variável.";
	private String stringFinal = "Inicialização de variável.";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_single_results,
				container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		executeMath();
		writeSingleVariableResults();
	}
	
	
	private void executeMath() {
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
	}
	
	private void writeSingleVariableResults() {
		
		textViewSingleVariableResults = (TextView) getView().findViewById(R.id.textViewSingleVariable);
		textViewSingleVariableResults.setText(stringFinal);
				
	}
	

}
