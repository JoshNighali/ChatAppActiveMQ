package chat;
// importation des librairies
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.swing.JOptionPane;

import org.apache.activemq.ActiveMQConnectionFactory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// la classe de notre application
public class JMSChat extends Application {
	// css code
	String style_label =
	        "-fx-font-weight: bolder;"
	        + "-fx-text-fill: #FFFFFF;";

	String style_button =
			"-fx-background-color: #1C8AE5;"
			+ "-fx-text-fill: #FFFFFF;"
			+ "-fx-border-color: #1C8AE5;"
			+ " -fx-border-width: 0px;"
			+ "-fx-background-radius:10px;";
	
	String style_button_close =
			"-fx-background-color: #E5401C;"
			+ "-fx-text-fill: #FFFFFF;"
			+ "-fx-border-color: #E5401C;"
			+ " -fx-border-width: 0px;"
			+ "-fx-background-radius:20px;";
	
	String style_button_off =
			"-fx-background-color: #E5401C;"
			+ "-fx-text-fill: #FFFFFF;"
			+ "-fx-border-color: #E5401C;"
			+ "-fx-border-width: 0px;"
			+ "-fx-background-radius:30px;"
			+ "-fx-height:20px;";
	
	String style_hbox =
			"-fx-border-color: #ff0000;"
			+ " -fx-border-width: 2px;";
	
	String style_hbox2 =
			"-fx-border-color: #ff0000;"
			+ " -fx-border-width: 2px;"
			+ "-fx-background-color: #3BA41C;"
			+ "-fx-height:20px;";
	
	String style_connected =
			"-fx-border-color: #1F630B;"
			+ " -fx-border-width: 2px;"
			+ "-fx-background-color: #1F630B;";

	String style_stage =
			"-fx-background-image: url('images/cute-kitten-2560x1440-12770.jpg');"
			+ "-fx-background-repeat: no-repeat;"
			+ " -fx-background-size: 500 500;"
			+ "-fx-background-position: center center;";
	
	String style_inputs ="-fx-background-radius: 20px;";

	String style_message ="-fx-text-fill: #03F687;";
	
	String style_vbox_account ="-fx-background-color:#000000;"
								+ "-fx-height:200px;";
	
	String style_separator ="-fx-border-width:20px;";
	
	
	
	
	// la methode principale
	public static void main(String[] args) {
		Application.launch(JMSChat.class);
	}

	// la methode pour demarrer notre interface
	@Override
	public void start(Stage primaryStage) throws Exception {
		File file = new File("images/chat.png");
		Image image = new Image(file.toURI().toString());
		
		primaryStage.setTitle("Soft Chat | Version 1.0");
		primaryStage.getIcons().add(image);
		
		
		BorderPane borderPane = new BorderPane();
		HBox hBox1 = new HBox();
		hBox1.setPadding(new Insets(12));
		hBox1.setSpacing(12);
		hBox1.setStyle(style_vbox_account);
		
		
		
		Label labelUser = new Label("User ID :");
		labelUser.setStyle(style_label);
		
		
		
		
		
		 // définir la plage
        int maximum = 10000; 
        int minimum = 1; 
        int range = (maximum - minimum) + 1;     
     
        // Générer un nombre aléatoire entre une plage spécifique  
        int user_identification = (int)(Math.random() * range) + minimum;
        
        String username = "USER_"+user_identification;
        
        TextField textFieldCode = new TextField(username);
        
		textFieldCode.setStyle(style_inputs);
		textFieldCode.setPromptText(username);
		textFieldCode.setEditable(false);
		textFieldCode.setMouseTransparent(true);
		textFieldCode.setFocusTraversable(false);
		
		
		
		          
		Label labelHost = new Label("IP Server :");
		labelHost.setStyle(style_label);
		
		
		TextField textFieldHost = new TextField("localhost");
		textFieldHost.setStyle(style_inputs);
		textFieldHost.setPromptText("SERVER");
		textFieldHost.setEditable(false);
		textFieldHost.setMouseTransparent(true);
		textFieldHost.setFocusTraversable(false);
		
		
		Label labelPort = new Label("PORT SERVER :");
		labelPort.setStyle(style_label);
		
		TextField textFieldPort = new TextField("61616");
		textFieldPort.setStyle(style_inputs);
		textFieldPort.setEditable(false);
		textFieldPort.setMouseTransparent(true);
		textFieldPort.setFocusTraversable(false);
		textFieldPort.setPromptText("PORT");
		
		Button buttonConnecter = new Button("Connection");
		Button buttonDeconnecter = new Button("Fermer");
		Button buttonStatus = new Button();
		
		buttonConnecter.setStyle(style_button);
		buttonDeconnecter.setStyle(style_button_close);
		buttonStatus.setStyle(style_button_off);
		
		// Use a separator to better organize the layout.
	    Separator separator = new Separator();
	    separator.setPrefWidth(260);
	    separator.setStyle(style_separator);
	    
	    
		hBox1.getChildren().add(labelUser);
		hBox1.getChildren().add(textFieldCode);
		hBox1.getChildren().add(labelHost);
		hBox1.getChildren().add(textFieldHost);
		hBox1.getChildren().add(labelPort);
		hBox1.getChildren().add(textFieldPort);
		
		//hBox1.getChildren().add(buttonStatus);
		
		hBox1.getChildren().add(buttonConnecter);
		hBox1.getChildren().add(buttonDeconnecter);
		borderPane.setTop(hBox1);
		
		VBox vBox = new VBox();
		GridPane gridPane = new GridPane();
		HBox hBox2 = new HBox();
		vBox.getChildren().addAll(separator);
		vBox.getChildren().add(gridPane);
		vBox.getChildren().add(hBox2);
		
//		hBox2.setStyle(style_hbox2);
		
		vBox.setStyle("-fx-background-color : #000000;");
		borderPane.setCenter(vBox);
		
		Label labelTo = new Label("To :"); 
		labelTo.setStyle(style_label);
		TextField textFieldTo = new TextField("USER_");
		textFieldTo.setStyle(style_inputs);
		textFieldTo.setPrefWidth(300);
		
		Scene scene = new Scene(borderPane, 1000, 500);
		//scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		
buttonConnecter.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {				
				try {
					String host = textFieldHost.getText();	
					int port = Integer.parseInt(textFieldPort.getText());
					ConnectionFactory connectionFactory = new 
							ActiveMQConnectionFactory("tcp://"+host+":"+port);
				   Connection connection = connectionFactory.createConnection();
				   connection.start();
				   
				   String message_connection = "Your User ID is : "+username+". Please, don't forget it!!";

			    //hBox1.setDisable(true);  //On désactive le hBox1 après avoir connecté un USER
				  JOptionPane.showMessageDialog(null, message_connection);
				  hBox1.setStyle(style_connected);
				  buttonConnecter.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

buttonDeconnecter.setOnAction(new EventHandler<ActionEvent>() {

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		System.exit(0);
		
	}
	
});
		
	}
	
	
	
	
}