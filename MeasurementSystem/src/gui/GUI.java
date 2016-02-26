package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import connection.TwoWaySerialUSBCommunication;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
 
public class GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    private Grid graphGrid;
	private Grid graphDisplayGrid;
	private Grid channelOneInputGrid;
	private Grid channelTwoInputGrid;
	private Grid voltageSourceInputGrid;
	private Grid generatorInputGrid;
	private Grid updateGrid;
	private LineChart<Number, Number> graph;
	private XYChart.Series<Number, Number> channelOneData;
	private XYChart.Series<Number, Number> channelTwoData;
	private MenuBar menuBar;
	private TwoWaySerialUSBCommunication communicationChannel;
	
	public GUI() {
		this.communicationChannel = new TwoWaySerialUSBCommunication();
	}
	
	@Override
    public void init() {
    	initGraph();
    	initGraphGrid();
    	initGraphDisplayInputGrid();
    	initChannelOneInputGrid();
    	initChannelTwoInputGrid();
    	initVoltageSourceInputGrid();
    	initGeneratorInputGrid();
    	initUpdateGrid();
    	initMenuBar();
    }
	
	@Override
    public void start(Stage primaryStage) {

        Grid masterGrid = new Grid(Pos.TOP_LEFT, 0, 0, 0, 0, 0, 0, "mastergrid");
        Grid subGrid = new Grid(Pos.TOP_LEFT, 0, 0,	0, 0, 0, 0);
        Grid subsubGrid = new Grid(Pos.TOP_LEFT, 0, 0, 0, 0, 0, 0);
        
        masterGrid.add(getMenuBar(), 0, 0);
        masterGrid.add(subGrid, 0, 1);
        
        subGrid.add(getGraphGrid(), 0, 0);
        subGrid.add(getGraphDisplayInputGrid(), 0, 1);
        subGrid.add(getUpdateGrid(), 1, 1);
        
        subsubGrid.add(getChannelInputGrid(), 0, 0);
        subsubGrid.add(getGeneratorInputGrid(), 0, 1);
        subsubGrid.add(getVoltageSourceInputGrid(), 0, 2);
        subGrid.add(subsubGrid, 1, 0);
        
        Scene scene = new Scene(masterGrid, 782, 590);
        scene.getStylesheets().add(
        		GUI.class.getResource("Style.css").toExternalForm());
        
        primaryStage.setTitle("Measurement System v0.1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void initGraph() {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		
		xAxis.setMinorTickVisible(false);
		xAxis.setTickMarkVisible(false);
		yAxis.setMinorTickVisible(false);
		yAxis.setTickMarkVisible(false);
		
		final LineChart<Number,Number> lineChart = 
	            new LineChart<Number,Number>(xAxis,yAxis);
		lineChart.setCreateSymbols(false); //hide dots
		lineChart.setHorizontalGridLinesVisible(true);
		lineChart.setLegendVisible(false);
		lineChart.setHorizontalZeroLineVisible(true);
		
		this.channelOneData = new XYChart.Series<Number,Number>();
		getChannelOneData().setName("CH1");
		this.channelTwoData = new XYChart.Series<Number,Number>();
		getChannelTwoData().setName("CH2");
	    
	    lineChart.setAnimated(false);
	    
	    lineChart.getData().add(getChannelOneData());
	    lineChart.getData().add(getChannelTwoData());
	    
	    this.graph = lineChart;
	}
    
    public void setDataFromList(double[][] listChannelOne, double[][] listChannelTwo) {
	    int N = Math.min(listChannelOne[0].length, listChannelTwo[0].length);
	    XYChart.Series<Number,Number> series1 = new XYChart.Series<Number,Number>();
	    XYChart.Series<Number,Number> series2 = new XYChart.Series<Number,Number>();
	    
	    for(int i=0; i<N; i++) {
	    	series1.getData().add(new XYChart.Data<Number,Number>(listChannelOne[0][i],listChannelOne[1][i]));
	    	System.out.println(listChannelOne[0][i] + ", " + listChannelOne[1][i]);
	    	series2.getData().add(new XYChart.Data<Number,Number>(listChannelTwo[0][i],listChannelTwo[1][i]));
	    }
	    
	    this.channelOneData = series1;
	    this.channelTwoData = series2;
    }

	private void initGraphGrid() {
		Label channelOne = new Label("CH1");
		Label chOneFrequencyText = new Label("Frequency: ");
		Label chOneFrequencyNumber = new Label(12.3 + " kHz");
		Label chOnePeriodText = new Label("Period: ");
		Label chOnePeriodNumber = new Label(81.3 + " µs");
		Label chOneVoltText = new Label("Volt/div: ");
		Label chOneVoltNumber = new Label(500 + " mV");
		Label chOneTimeText = new Label("Time/div: ");
		Label chOneTimeNumber = new Label(50 + " µs");
		
		Label channelTwo = new Label("CH2");
		Label chTwoFrequencyText = new Label("Frequency: ");
		Label chTwoFrequencyNumber = new Label(7.89 + " kHz");
		Label chTwoPeriodText = new Label("Period: ");
		Label chTwoPeriodNumber = new Label(1.27 + " ms");
		Label chTwoVoltText = new Label("Volt/div: ");
		Label chTwoVoltNumber = new Label(50 + " mV");
		Label chTwoTimeText = new Label("Time/div: ");
		Label chTwoTimeNumber = new Label(50 + " µs");
		
		Label phaseText = new Label("Phase: ");
		Label phaseNumber = new Label(45 +"°");
		
		Grid graphInfoGrid = new Grid(Pos.TOP_LEFT, 10, 0, -18, 0, 0, 0);
		
		graphInfoGrid.add(channelOne, 2, 0);
		graphInfoGrid.add(chOneFrequencyText, 2, 1);
		graphInfoGrid.add(chOneFrequencyNumber, 3, 1);
		graphInfoGrid.add(chOnePeriodText, 2, 2);
		graphInfoGrid.add(chOnePeriodNumber, 3, 2);
		graphInfoGrid.add(chOneVoltText, 2, 3);
		graphInfoGrid.add(chOneVoltNumber, 3, 3);
		graphInfoGrid.add(chOneTimeText, 2, 4);
		graphInfoGrid.add(chOneTimeNumber, 3, 4);
		
		graphInfoGrid.add(channelTwo, 8, 0);
		graphInfoGrid.add(chTwoFrequencyText, 8, 1);
		graphInfoGrid.add(chTwoFrequencyNumber, 9, 1);
		graphInfoGrid.add(chTwoPeriodText, 8, 2);
		graphInfoGrid.add(chTwoPeriodNumber, 9, 2);
		graphInfoGrid.add(chTwoVoltText, 8, 3);
		graphInfoGrid.add(chTwoVoltNumber, 9, 3);
		graphInfoGrid.add(chTwoTimeText, 8, 4);
		graphInfoGrid.add(chTwoTimeNumber, 9, 4);
		
		graphInfoGrid.add(phaseText, 14, 1);
		graphInfoGrid.add(phaseNumber, 15, 1);
		
		
    	//Create the grid
    	Grid graphGrid = new Grid(Pos.TOP_LEFT, 0, 0, 0, 0, 0, 0);
    	
    	//All elements to the grid
    	graphGrid.add(getGraph(), 0, 0);
    	graphGrid.add(graphInfoGrid, 0, 1);
    	
    	this.graphGrid = graphGrid;
    }
    
    private void initGraphDisplayInputGrid() {
    	//Create the check boxes
    	CheckBox cbCH1 = new CheckBox("CH1");
        cbCH1.setSelected(true);
        cbCH1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cbCH1.isSelected()) {
                	getChannelOneInputGrid().setDisable(false);
                	getGraph().getData().add(getChannelOneData());
                	System.out.println("cbCH1 checked");
                }
                else {
                	getChannelOneInputGrid().setDisable(true);
                	getGraph().getData().removeAll(getChannelOneData());
                	System.out.println("cbCH1 unchecked");
                }
            }
        });

        CheckBox cbCH2 = new CheckBox("CH2");
        cbCH2.setSelected(true);
        cbCH2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cbCH2.isSelected()) {
                	getChannelTwoInputGrid().setDisable(false);
                	getGraph().getData().add(getChannelTwoData());
                	System.out.println("cbCH2 checked");
                }
                else {
                	getChannelTwoInputGrid().setDisable(true);
                	getGraph().getData().removeAll(getChannelTwoData());
                	System.out.println("cbCH2 unchecked");
                }
            }
        });
        
        //Create the radio button group
        ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Signal plot");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        RadioButton rb2 = new RadioButton("FFT plot");
        rb2.setToggleGroup(group);
         
        RadioButton rb3 = new RadioButton("XY plot");
        rb3.setToggleGroup(group);
        
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle old_toggle, Toggle new_toggle) {
            		if (group.getSelectedToggle() != null) {
		                if(group.getSelectedToggle().equals(rb1)) {
		                	System.out.println("Signal plot");
		                }
		                if(group.getSelectedToggle().equals(rb2)) {
		                	System.out.println("FFT plot");
		                }
		                if(group.getSelectedToggle().equals(rb3)) {
		                	System.out.println("XY plot");
		                }
            		}
            }
          });
        
        CheckBox cbUseGenerator = new CheckBox("Use generator");
        cbUseGenerator.setSelected(false);
        cbUseGenerator.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cbUseGenerator.isSelected()) {
                	getGeneratorInputGrid().setDisable(false);
                	System.out.println("Using generator");
                }
                else {
                	getGeneratorInputGrid().setDisable(true);
                	System.out.println("Generator disabled");
                }
            }
        });
        
        CheckBox cbUseSource = new CheckBox("Use voltage sources");
        cbUseSource.setSelected(false);
        cbUseSource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cbUseSource.isSelected()) {
                	getVoltageSourceInputGrid().setDisable(false);
                	System.out.println("Using voltage sources");
                }
                else {
                	getVoltageSourceInputGrid().setDisable(true);
                	System.out.println("Voltage sources disabled");
                }
            }
        });

        //Create the grid
    	Grid graphDisplayGrid = new Grid(Pos.TOP_LEFT, 25, 5,
    			20, 0, 0, 20);
    	
    	graphDisplayGrid.add(cbCH1, 0, 0);
    	graphDisplayGrid.add(cbCH2, 0, 1);
    	
    	graphDisplayGrid.add(rb1, 1, 0);
    	graphDisplayGrid.add(rb2, 1, 1);
    	graphDisplayGrid.add(rb3, 1, 2);
    	
    	graphDisplayGrid.add(cbUseGenerator, 9, 0);
    	graphDisplayGrid.add(cbUseSource, 9, 1);
    	
    	
        this.graphDisplayGrid = graphDisplayGrid;
        
    }
    
    private void initChannelOneInputGrid() {
    	//Create input elements to change time/division
    	Label labelTime = new Label("CH1 SEC/DIV");
    	
    	Button btnTimeMinus = new Button();
        btnTimeMinus.setText("-");
        btnTimeMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH1 SEC/DIV -");
            }
        });
        
        Button btnTimePlus = new Button();
        btnTimePlus.setText("+");
        btnTimePlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH1 SEC/DIV +");
            }
        });
        
        //Create input elements to change Volt/division
    	Label labelVolt = new Label("CH1 VOLTS/DIV");
    	
    	Button btnVoltMinus = new Button();
        btnVoltMinus.setText("-");
        btnVoltMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH1 VOLTS/DIV -");
            }
        });
        
        Button btnVoltPlus = new Button();
        btnVoltPlus.setText("+");
        btnVoltPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH1 VOLTS/DIV +");
            }
        });
        
        //Create input elements to move the signal vertically
    	Label labelVertical = new Label("CH1 Move vertically");
    	
    	Button btnVerticalMinus = new Button();
        btnVerticalMinus.setText("-");
        btnVerticalMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH1 Vertical movement -");
            }
        });
        
        Button btnVerticalPlus = new Button();
        btnVerticalPlus.setText("+");
        btnVerticalPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH1 Vertical movement +");
            }
        });
        
        //Create input elements to move the signal horizontally
    	Label labelHorizontal = new Label("CH1 Move horizontally");
    	
    	Button btnHorizontalMinus = new Button();
        btnHorizontalMinus.setText("-");
        btnHorizontalMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH1 Horizontal movement -");
            }
        });
        
        Button btnHorizontalPlus = new Button();
        btnHorizontalPlus.setText("+");
        btnHorizontalPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH1 Horizontal movement +");
            }
        });
        
        //Create the grid
    	Grid channelOneInputGrid = new Grid(Pos.TOP_LEFT, 0, 5,
    			15, 15, 15, 5);
    	
    	//Add all elements to the grid
    	channelOneInputGrid.add(labelVolt, 0, 0);
    	HBox volt = new HBox(10);
    	volt.getChildren().addAll(btnVoltMinus, btnVoltPlus);
    	channelOneInputGrid.add(volt, 0, 1);
    	
    	channelOneInputGrid.add(labelTime, 0, 3);
    	HBox time = new HBox(10);
    	time.getChildren().addAll(btnTimeMinus, btnTimePlus);
    	channelOneInputGrid.add(time, 0, 4);
    	
    	channelOneInputGrid.add(labelVertical, 0, 6);
    	HBox vertical = new HBox(10);
    	vertical.getChildren().addAll(btnVerticalMinus, btnVerticalPlus);
    	channelOneInputGrid.add(vertical, 0, 7);
    	
    	channelOneInputGrid.add(labelHorizontal, 0, 9);
    	HBox horizontal = new HBox(10);
    	horizontal.getChildren().addAll(btnHorizontalMinus, btnHorizontalPlus);
    	channelOneInputGrid.add(horizontal, 0, 10);
    	
    	this.channelOneInputGrid = channelOneInputGrid;
    }

	private void initChannelTwoInputGrid() {
		//Create input elements to change time/division
    	Label labelTime = new Label("CH2 SEC/DIV");
    	
    	Button btnTimeMinus = new Button();
        btnTimeMinus.setText("-");
        btnTimeMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH2 SEC/DIV -");
            }
        });
        
        Button btnTimePlus = new Button();
        btnTimePlus.setText("+");
        btnTimePlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH2 SEC/DIV +");
            }
        });
        
        //Create input elements to change Volt/division
    	Label labelVolt = new Label("CH2 VOLTS/DIV");
    	
    	Button btnVoltMinus = new Button();
        btnVoltMinus.setText("-");
        btnVoltMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH2 VOLTS/DIV -");
            }
        });
        
        Button btnVoltPlus = new Button();
        btnVoltPlus.setText("+");
        btnVoltPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH2 VOLTS/DIV +");
            }
        });
        
        //Create input elements to move the signal vertically
    	Label labelVertical = new Label("CH2 Move vertically");
    	
    	Button btnVerticalMinus = new Button();
        btnVerticalMinus.setText("-");
        btnVerticalMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH2 Vertical movement -");
            }
        });
        
        Button btnVerticalPlus = new Button();
        btnVerticalPlus.setText("+");
        btnVerticalPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH2 Vertical movement +");
            }
        });
        
        //Create input elements to move the signal horizontally
    	Label labelHorizontal = new Label("CH2 Move horizontally");
    	
    	Button btnHorizontalMinus = new Button();
        btnHorizontalMinus.setText("-");
        btnHorizontalMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH2 Horizontal movement -");
            }
        });
        
        Button btnHorizontalPlus = new Button();
        btnHorizontalPlus.setText("+");
        btnHorizontalPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CH2 Horizontal movement +");
            }
        });
        
        //Create the grid
    	Grid channelTwoInputGrid = new Grid(Pos.TOP_LEFT, 0, 5,
    			15, 15, 15, 5);
    	
    	//Add all elements to the grid
    	channelTwoInputGrid.add(labelVolt, 0, 0);
    	HBox volt = new HBox(10);
    	volt.getChildren().addAll(btnVoltMinus, btnVoltPlus);
    	channelTwoInputGrid.add(volt, 0, 1);
    	
    	channelTwoInputGrid.add(labelTime, 0, 3);
    	HBox time = new HBox(10);
    	time.getChildren().addAll(btnTimeMinus, btnTimePlus);
    	channelTwoInputGrid.add(time, 0, 4);
    	
    	channelTwoInputGrid.add(labelVertical, 0, 6);
    	HBox vertical = new HBox(10);
    	vertical.getChildren().addAll(btnVerticalMinus, btnVerticalPlus);
    	channelTwoInputGrid.add(vertical, 0, 7);
    	
    	channelTwoInputGrid.add(labelHorizontal, 0, 9);
    	HBox horizontal = new HBox(10);
    	horizontal.getChildren().addAll(btnHorizontalMinus, btnHorizontalPlus);
    	channelTwoInputGrid.add(horizontal, 0, 10);
    	
    	this.channelTwoInputGrid = channelTwoInputGrid;
	}
	
    private void initVoltageSourceInputGrid() {
    	Label voltageSourceText = new Label("Voltage sources");
    	
    	ObservableList<String> voltageSources = FXCollections.observableArrayList(
        	        "V- = 0 V and V+ = 0 V",
        	        "V- = -5 V and V+ = +5 V",
        	        "V- = -10 V and V+ = +10 V",
        	        "V- = -12 V and V+ = +12 V",
        	        "V- = -15 V and V+ = +15 V"
        	    );
        ComboBox cbVoltageSource = new ComboBox(voltageSources);
        cbVoltageSource.setValue(voltageSources.get(0));
    	
    	//Create the grid
    	Grid voltageSourceInputGrid = new Grid(Pos.TOP_LEFT, 0, 5,
    			15, 0, 0, 0);
    	
    	voltageSourceInputGrid.add(voltageSourceText, 0, 0);
    	voltageSourceInputGrid.add(cbVoltageSource, 0, 1);
    	
    	voltageSourceInputGrid.setDisable(true);
    	
    	this.voltageSourceInputGrid = voltageSourceInputGrid;
    }
    
    private void initGeneratorInputGrid() {
    	Label generatorText = new Label("Generator");
    	
    	Label waveTypeText = new Label("Wave type");
    	ObservableList<String> waveTypes = FXCollections.observableArrayList(
        	        "Sine",
        	        "Square"
        	    );
        ComboBox cbWaveType = new ComboBox(waveTypes);
        cbWaveType.setValue(waveTypes.get(0));
    	
        Label voltageText = new Label("Pkp voltage");
    	ObservableList<String> voltages = FXCollections.observableArrayList(
        	        "0 V",
        	        "1 V",
        	        "2 V",
        	        "3 V",
        	        "5 V"
        	    );
        ComboBox cbVoltage = new ComboBox(voltages);
        cbVoltage.setValue(voltages.get(0));
        
        Label frequencyText = new Label("Frequency");
    	ObservableList<String> frequencies = FXCollections.observableArrayList(
        	        "10 Hz",
        	        "100 Hz",
        	        "1 kHz",
        	        "10 kHz",
        	        "100 kHz"
        	    );
        ComboBox cbFrequency = new ComboBox(frequencies);
        cbFrequency.setValue(frequencies.get(0));
        
    	//Create the grid
    	Grid generatorInputGrid = new Grid(Pos.TOP_LEFT, 10, 5,
    			10, 0, 0, 0);
    	
    	//Add all elements to the grid
    	generatorInputGrid.add(generatorText, 0, 0);
    	
    	generatorInputGrid.add(waveTypeText, 0, 1);
    	generatorInputGrid.add(cbWaveType, 0, 2);
    	
    	generatorInputGrid.add(voltageText, 1, 1);
    	generatorInputGrid.add(cbVoltage, 1, 2);
    	
    	generatorInputGrid.add(frequencyText, 2, 1);
    	generatorInputGrid.add(cbFrequency, 2, 2);
    	
    	generatorInputGrid.setDisable(true);
    	
    	this.generatorInputGrid = generatorInputGrid;
    }
    
    private void initUpdateGrid() {
    	Button btnUpdate = new Button();
        btnUpdate.setText("Update plot and outputs");
        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Updating plot and outputs");
                getGraph().getData().removeAll(getChannelOneData());
                getGraph().getData().removeAll(getChannelTwoData());
                setDataFromList(
        				getCommunicationChannel().getReceivedDataChannelOne(),
        				getCommunicationChannel().getReceivedDataChannelTwo());
                getGraph().getData().add(getChannelOneData());
                getGraph().getData().add(getChannelTwoData());
            }
        });
    	
    	Grid updateGrid = new Grid(Pos.TOP_LEFT, 0, 0,
    			0, 0, 0, 50);
    	
    	updateGrid.add(btnUpdate, 0, 0);
    	
    	this.updateGrid = updateGrid;
    }
    
    private void initMenuBar() {
    	MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuHelp = new Menu("Help");
        
        MenuItem load = new MenuItem("Load...");
        load.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //TODO
            	System.out.println("The 'Load' option has not been implemented yet.");
            }
        });
        
        MenuItem save = new MenuItem("Save as...");
        save.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //TODO
            	System.out.println("The 'Save as' option has not been implemented yet.");
            }
        });
        
        MenuItem export = new MenuItem("Export as image...");
        export.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //TODO
            	saveAsPng(getGraphGrid());
            }
        });
        
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        
        MenuItem manual = new MenuItem("Manual");
        manual.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //TODO
            	System.out.println("The 'Manual' option has not been implemented yet.");
            }
        });
        
        MenuItem about = new MenuItem("About");
        about.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //TODO
            	System.out.println("The 'About' option has not been implemented yet.");
            }
        });
        
        menuFile.getItems().addAll(load, save, export, new SeparatorMenuItem(), exit);
        menuHelp.getItems().addAll(manual, about);
        menuBar.getMenus().addAll(menuFile, menuEdit, menuHelp);
        
        this.menuBar = menuBar;
    }
    
    @FXML
	private void saveAsPng(Grid gridToExport) {
	    WritableImage image = gridToExport.snapshot(new SnapshotParameters(), null);

	    // TODO: probably use a file chooser here
	    File file = new File("testImage.png");

	    try {
	        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	    } catch (IOException e) {
	        // TODO: handle exception here
	    }
	}
    
    private Grid getChannelInputGrid() {
		//Create the grid
		Grid channelInputGrid = new Grid(Pos.TOP_LEFT, 0, 0,
				0, 0, 0, 0);
		
	    //Set all elements in the grid
		channelInputGrid.add(getChannelOneInputGrid(), 0, 0);
		channelInputGrid.add(getChannelTwoInputGrid(), 1, 0);
	    
	    return channelInputGrid;
	}
    
	private Grid getChannelOneInputGrid() {
    	return this.channelOneInputGrid;
    }
    
	private Grid getChannelTwoInputGrid() {
		return this.channelTwoInputGrid;
	}
	
	private Grid getVoltageSourceInputGrid() {
		return this.voltageSourceInputGrid;
	}
	
	private Grid getGeneratorInputGrid() {
		return this.generatorInputGrid;
	}
	
	private LineChart<Number, Number> getGraph() {
		return this.graph;
	}
	
	private MenuBar getMenuBar() {
		return this.menuBar;
	}
	
	private Grid getGraphDisplayInputGrid() {
		return this.graphDisplayGrid;
	}
	
	private Grid getGraphGrid() {
		return this.graphGrid;
	}
	
	private Grid getUpdateGrid() {
		return this.updateGrid;
	}
	
	private XYChart.Series<Number, Number> getChannelOneData() {
		return this.channelOneData;
	}
	
	private XYChart.Series<Number, Number> getChannelTwoData() {
		return this.channelTwoData;
	}

	private TwoWaySerialUSBCommunication getCommunicationChannel() {
		return this.communicationChannel;
	}
}