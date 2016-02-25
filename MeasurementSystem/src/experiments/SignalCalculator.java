package experiments;

import javafx.scene.chart.XYChart;

public class SignalCalculator {

	public static void main(String[] args) {
		calculateFFT();
	}
	
	public static void calculateFFT() {
		XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
		
		//calculate functions (dummy data)
	    double x_temp = 0;
	    for(int i=0; i<500; i++) {
	    	x_temp = i*0.1d;
	    	series.getData().add(new XYChart.Data<Number,Number>(x_temp,Math.sin(x_temp/2.5d)));
	    	//series.getData().add(new XYChart.Data<Number,Number>(x_temp,Math.cos(x_temp)+Math.sin(2*x_temp)));
	    }
	    
	    int N = series.getData().size();
	    XYChart.Series<Number,Number> outputSeries = new XYChart.Series<Number,Number>();
	    double sum = 0;
	    
	    for(int k=1; k<N; k++) {
		    for(int n=1; n<N; n++) {
		    	sum = sum + ((double) series.getData().get(n-1).getYValue()) *
		    			Math.exp(2.0d*Math.PI*(k-1)*(n-1)/N);
		    }
		    outputSeries.getData().add(new XYChart.Data<Number, Number>(k, sum));
		    System.out.println(sum);
		    sum = 0;
	    }
	}
	
}
