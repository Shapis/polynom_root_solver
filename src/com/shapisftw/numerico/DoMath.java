package com.shapisftw.numerico;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class DoMath {

	public  ArrayList<PolyInfo> executarAlgoritmo(
			ArrayList<PolyInfo> polyInfoIncognitas) {

		ArrayList<PolyInfo> polyInfoResultados = new ArrayList<PolyInfo>();
		ArrayList<Double> guardarResultados = new ArrayList<Double>();
		double lowerBound = -10+1E-8;
		double upperBound = 10;
		double epsilon = 1e-11; 

		for (PolyInfo i : polyInfoIncognitas) {
			PolyInfo tempPolyInfo = new PolyInfo();

			
			metodoNewtonRaphson(lowerBound, upperBound, epsilon, i.getPoli());
			metodoBissecao(lowerBound, upperBound, epsilon, i.getPoli());
			metodoSecante(lowerBound, upperBound, epsilon, i.getPoli());
			guardarResultados = determinarRaizes(lowerBound, upperBound, epsilon, i.getPoli(), i.getGrau());
			tempPolyInfo.setPoli(guardarResultados);
			tempPolyInfo.setGrau(i.getGrau());
			polyInfoResultados.add(tempPolyInfo);
			
		}
		
		
		return polyInfoResultados;
		
	}

	
	private  double metodoBissecao(double lowerBound, double upperBound,
			double epsilon, ArrayList<Double> i) {
		double m = 0;
		int k = 0;

		while ((upperBound - lowerBound) > epsilon) {
			m = ((lowerBound + upperBound) / 2);
			if (resultadoPoli(i, m) * resultadoPoli(i, lowerBound) < 0) {
				upperBound = m;
			} else if (resultadoPoli(i, m) * resultadoPoli(i, upperBound) < 0) {
				lowerBound = m;
			}

			if (k >= 100) {
				break;
			}
			k++;
		}

		
		
		if (m>=lowerBound&&m<=upperBound){
			if (resultadoPoli(i,m)<=epsilon*10){
				return m;
			}else {
				return upperBound + 1000000;
			}
			
		
		}
		else {
		return upperBound+1000000;
		}
	}
	
	
	private  double metodoNewtonRaphson(double lowerBound, double upperBound,
			double epsilon, ArrayList<Double> i){
		double m = ((lowerBound + upperBound) / 2);
		int k = 0;
		while(Math.sqrt(Math.pow(resultadoPoli(i, m), 2)) > epsilon) {
		m = m - ((resultadoPoli(i, m)) / (resultadoPoli(derivarPoli(i), m)));
		if (k >= 50) {
			break;
		}
		k++;
		}		
		
				
		if (m>=lowerBound&&m<=upperBound){
			if (resultadoPoli(i,m)<=epsilon*10){
				return m;
			}else {
				return upperBound + 1000000;
			}
			
		
		}
		else {
		return upperBound+1000000;
		}
	}
	
	private  double metodoSecante(double lowerBound, double upperBound,
			double epsilon, ArrayList<Double> i){
		double upperCheck = upperBound;
		double lowerCheck = lowerBound;
		double temp = 0;
		double diff = 1;
		int k = 0;
		    while ((Math.sqrt(Math.pow(diff, 2))>epsilon)) {
		      double d = resultadoPoli(i, upperBound)-resultadoPoli(i, lowerBound);
		       temp = upperBound-resultadoPoli(i, upperBound)*(upperBound-lowerBound)/d;
		      lowerBound  = upperBound;
		      upperBound = temp;
		      diff = upperBound-lowerBound;

		      if (k >= 100) {
		    	 break;
		      }
		      k++;
		    }
		    
		    if (upperBound>=lowerCheck&&upperBound<=upperCheck){
		    	if (resultadoPoli(i,upperBound) <= epsilon*10){
		    		return upperBound;
		    	}
		    	else {
		    		return upperCheck + 1000000;
		    	}
		    }else {
		    	return upperBound +1000000;
		    }
		    
		}
		
		
		
	
	private  double determinarRaizNoIntervalo(double lowerBound, double upperBound,
			double epsilon, ArrayList<Double> i){
		double newton = metodoNewtonRaphson(lowerBound,upperBound, epsilon, i);
		double bissecao = metodoBissecao(lowerBound, upperBound, epsilon, i);
		double secante = metodoSecante(lowerBound, upperBound, epsilon, i);
		return saneCheck(newton, bissecao, secante, i, lowerBound, upperBound, epsilon);
	}
	
	private  double saneCheck(double newton,double  bissecao,double secante, ArrayList<Double> i, double lowerBound, double upperBound, double epsilon) {
		double precision = 1e-5;
		
		if (resultadoPoli(i,newton)>=epsilon*10) {
			newton = upperBound + 1000000;
		}
		if (resultadoPoli(i,secante)>=epsilon*10) {
			secante = upperBound + 1000000;
		}
		if (resultadoPoli(i,bissecao)>=epsilon*10) {
			bissecao = upperBound + 1000000;
		}
		
		if (Math.abs(newton-secante)<=precision&&Math.abs(newton-bissecao)<=precision){
			double temp = (secante+newton+bissecao) / 3;
			
			if (temp<=upperBound&&temp>=lowerBound) {
				return temp;
			}
		}
		
		if (Math.abs(newton-secante)<=precision){
			double temp = (newton + secante) /2;
			if (temp<=upperBound&&temp>=lowerBound) {
				return temp;
			}
		}
		
		if (Math.abs(newton-bissecao)<=precision) {
			double temp = (newton + bissecao) /2;
			if (temp<=upperBound&&temp>=lowerBound) {
				return temp;
			}
		}
		
		if (Math.abs(bissecao-secante)<=precision)
		{
			double temp = (bissecao + secante) /2;
			if (temp<=upperBound&&temp>=lowerBound) {
				return temp;
			}
		}
		
		if ((bissecao!=0)&&(bissecao<=upperBound)&&(bissecao>=lowerBound)){
	return bissecao;
		}
		
		if ((newton<=upperBound)&&(newton>=lowerBound)){
			return newton;
		}
		
		if ((secante<=upperBound)&&(secante>=lowerBound)){
			return secante;
		}
		
		
		return 5000000;
	}
	
	
	private  ArrayList<Double> determinarRaizes(double lowerBound, double upperBound,
			double epsilon, ArrayList<Double> i, double grau){
		
		ArrayList<Double> tempArrayList = new ArrayList<Double>();
		ArrayList<Double> meusPontos = new ArrayList<Double>();
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		ArrayList<Double> addList = new ArrayList<Double>();
		ArrayList<Double> rootListTemp = new ArrayList<Double>();
		ArrayList<Double> rootList = new ArrayList<Double>();
		int n = 0;
		grau = grau+2;
		
		for (int count = 0 ; count < grau ; count++) {
		
		tempArrayList.clear();
		tempArrayList.addAll(i);
		addList.clear();
		meusPontos.add(lowerBound-(epsilon*10));
		meusPontos.add(upperBound+(epsilon*10));
		meusPontos = ordenarArray(meusPontos);
		
		for (int count1 = 1; count1 < grau-n ; count1++) {
		tempArrayList = derivarPoli(tempArrayList);	
		}
		
		
		
		
		for (Double abc: meusPontos) {
			System.out.println(abc);
			System.out.println();
		}
		
		if (meusPontos.size() >= 2){
			removeList.clear();
			for (int count4 = 0; count4 < meusPontos.size()-1 ; count4++){
				if((resultadoPoli(tempArrayList,meusPontos.get(count4))*resultadoPoli(tempArrayList,meusPontos.get(count4+1))) > 0){
					removeList.add(count4);
				}
			}
			
			for (int count3 = 0; count3 < removeList.size() ; count3++) {
				int a;
				a = removeList.get(removeList.size()-1-count3);
				meusPontos.remove(a);
			}
			
			removeList.clear();
			
			for(int count2 = 0; count2 < meusPontos.size() ; count2++) {
				if (Math.abs(resultadoPoli(tempArrayList,meusPontos.get(count2))) < epsilon){
					removeList.add(count2);
				}
			}
			for (int count3 = 0; count3 < removeList.size() ; count3++) {
				int a;
				a = removeList.get(removeList.size()-1-count3);
				meusPontos.remove(a);
			}
			
			
			if(meusPontos.size() >=2 ) {
				for (int count5 = 0; count5 < meusPontos.size()-1; count5++) {
					System.out.println(count);
				addList.add(determinarRaizNoIntervalo(meusPontos.get(count5), meusPontos.get(count5+1), epsilon, tempArrayList));
				}
				
				for (Double temp: addList) {
					meusPontos.add(temp);
				}
			}
			
		}
		n++;
		meusPontos = ordenarArray(meusPontos);
		rootListTemp.addAll(addList);
		rootListTemp = ordenarArray(rootListTemp);
		}
		
		for (Double temp: rootListTemp) {
			if (Math.abs(resultadoPoli(i, temp)) < epsilon*10000) {
				rootList.add(temp);
			}
		}
		
		rootList = ordenarArray(rootList);
		removeList.clear();
		for (int count7 = 0 ; count7 < rootList.size()-1 ; count7++) {
			
			if (Math.abs(rootList.get(count7)-rootList.get(count7+1)) < epsilon*1E+7){
				removeList.add(count7+1);
			}
		}
		
		for (int count8 = 0; count8 < removeList.size() ; count8++) {
			int a;
			a = removeList.get(removeList.size()-1-count8);
			rootList.remove(a);
		}
		
		rootList = ordenarArray(rootList);
		return rootList ; // pode fazer também retorno do addList, que seria as raizes da última iteração
	}
	
	private  ArrayList<Double> ordenarArray(ArrayList<Double> i) {
		HashSet hs = new HashSet();
		hs.addAll(i);
		i.clear();
		i.addAll(hs);
		Collections.sort(i);
		return i;
	}
	
	private  ArrayList<Double> derivarPoli(ArrayList<Double> i){
		ArrayList<Double> derivado = new ArrayList<Double>();
		int k = 0;
		for(double j: i) {
			derivado.add(j*k);
			k++;
		}
		derivado.remove(0);
		return derivado;
	}

	private  double resultadoPoli(ArrayList<Double> i, double meuX) {
		double temp = 0;
		int n = 0;

		for (double j : i) {
			temp = temp + (Math.pow(meuX, n) * j);
			n++;
		}
		return temp;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
