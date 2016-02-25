package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class Grid extends GridPane {

	public Grid(Pos position, double hGap, double vGap,
			double insetTop, double insetRight, double insetBottom, double insetLeft, String id) {
		this(position, hGap, vGap, insetTop, insetRight, insetBottom, insetLeft);
		setId(id);
		
	}
	
	public Grid(Pos position, double hGap, double vGap,
			double insetTop, double insetRight, double insetBottom, double insetLeft) {
		super();
    	setAlignment(position);
    	setHgap(hGap);
    	setVgap(vGap);
    	setPadding(new Insets(insetTop, insetRight, insetBottom, insetLeft));
	}
}
