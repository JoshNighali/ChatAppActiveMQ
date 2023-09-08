package chat;

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

public class JMSChat extends Application {
	
	// Variables Globales de l'Application sont declarées ici !
	private MessageProducer messageProducer; 
	private Session session; //Utilise la même session pour les messages Consumer et Producer 
	private String codeUser;

	public static void main(String[] args) {
		Application.launch(JMSChat.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		
		// css code
				String style_label =
				        "-fx-font-weight: bolder;"
				        + "-fx-text-fill: #FFFFFF;";

				String style_button =
						"-fx-background-color: #0C7042;"
						+ "-fx-text-fill: #FFFFFF;"
						+ "-fx-border-color: #ff0000;"
						+ " -fx-border-width: 0px;"
						+ "-fx-background-radius:20px;";
				
				String style_hbox =
						"-fx-border-color: #ff0000;"
						+ " -fx-border-width: 2px;";

				String style_stage =
						"-fx-background-image: url('images/1080634_ONIORX0.jpg');"
						+ "-fx-background-repeat: no-repeat;"
						+ " -fx-background-size: 500 500;"
						+ "-fx-background-position: center center;";
				
				String style_inputs ="-fx-background-radius: 20px;";

				String style_message ="-fx-text-fill: #03F687;";
				
				String style_vbox_account ="-fx-background-color:#000000;"
											+ "-fx-height:200px;";
				
		primaryStage.setTitle("JMS Chat Application");
		BorderPane borderPane = new BorderPane();
		HBox hBox1 = new HBox();
		hBox1.setPadding(new Insets(12));
		hBox1.setSpacing(12);
		hBox1.setStyle(style_vbox_account);
		
		
		
		Label labelUser = new Label("USER CODE :");
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
		
		
		
		          
		Label labelHost = new Label("IP SERVER :");
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
		
		Button buttonConnecter = new Button("CONNECTION");
		buttonConnecter.setStyle(style_button);
		
		
		hBox1.getChildren().add(labelUser);
		hBox1.getChildren().add(textFieldCode);
		hBox1.getChildren().add(labelHost);
		hBox1.getChildren().add(textFieldHost);
		hBox1.getChildren().add(labelPort);
		hBox1.getChildren().add(textFieldPort);
		hBox1.getChildren().add(buttonConnecter);
		borderPane.setTop(hBox1);
		
		VBox vBox = new VBox();
		GridPane gridPane = new GridPane();
		HBox hBox2 = new HBox();
		vBox.getChildren().add(gridPane);
		vBox.getChildren().add(hBox2);
		vBox.setStyle("-fx-background-color : #000000;");
		borderPane.setCenter(vBox);
		
		Label labelTo = new Label("To :"); 
		labelTo.setStyle(style_label);
		TextField textFieldTo = new TextField("USER_");
		textFieldTo.setStyle(style_inputs);
		textFieldTo.setPrefWidth(300);
		          
		Label labelMessage = new Label("Message :");
	          labelMessage.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.ITALIC, 15));
		TextArea textAreaMessage = new TextArea();
		         textAreaMessage.setPrefWidth(300);
		         textAreaMessage.setPrefRowCount(3);
		Button buttonSendMessage = new Button("Send Text Message");
	           buttonSendMessage.setStyle(style_button);

		Label labelImage = new Label("Image :");
              labelImage.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.ITALIC, 15));
		File f1=new File("images"); //Charger le contenu du dossier images
		
		ObservableList<String> observableListImages  = FXCollections.observableArrayList(f1.list());
		ComboBox<String> comboBoxImages = new ComboBox<String>(observableListImages);
		comboBoxImages.getSelectionModel().select(7); //la première image de la liste
		Button buttonSendImage = new Button("Send Image File");
               buttonSendImage.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BLACK, FontPosture.REGULAR, 12));

		gridPane.setPadding(new Insets(12));
		gridPane.setVgap(12); gridPane.setHgap(12);
		
		gridPane.add(labelTo, 0, 0); gridPane.add(textFieldTo, 1, 0);
		gridPane.add(labelMessage, 0, 1); 
		gridPane.add(textAreaMessage, 1, 1);
		gridPane.add(buttonSendMessage, 2, 1); 
		gridPane.add(labelImage, 0, 2);
		gridPane.add(comboBoxImages, 1, 2); 
		gridPane.add(buttonSendImage, 2, 2);
		
		ObservableList<String> observableListMessages  = FXCollections.observableArrayList();
		ListView<String> listViewMessages = new ListView<>(observableListMessages);
		                 listViewMessages.setPrefWidth(700); listViewMessages.setPrefHeight(300);
		 //Charger le fichier image choisie par défaut
		File f2=new File("images/"+comboBoxImages.getSelectionModel().getSelectedItem());
		Image image = new Image(f2.toURI().toString()); //Obtenir l'URI de l'image choisie par défaut
		ImageView imageView = new ImageView(image);
		          imageView.setFitWidth(400); imageView.setFitHeight(256);
		hBox2.getChildren().add(listViewMessages);
		hBox2.getChildren().add(imageView);
		hBox2.setPadding(new Insets(15));
		hBox2.setSpacing(15);

		Scene scene = new Scene(borderPane, 1000, 500);
		//scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//Sélectioner l'image et l'afficher dans la zone imageView
		comboBoxImages.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//Charger le fichier image sélectionée
				File f3=new File("images/"+newValue);
				Image image = new Image(f3.toURI().toString()); //Obtenir l'URI de l'image sélectionée
				imageView.setImage(image);
			}
		});
		
		//Récupérer le message saisi et l'envoyer au destinataire
		buttonSendMessage.setOnAction(e->{
			try {
				TextMessage textMessage = session.createTextMessage();
				textMessage.setText(textAreaMessage.getText());
				//textMessage.setStringProperty("user", codeUser);
				textMessage.setStringProperty("user", textFieldTo.getText());
				messageProducer.send(textMessage);
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		});
		
		//Envoi d'un fichier image à sélectionner dans la liste!
		buttonSendImage.setOnAction(e->{
			try {
				StreamMessage streamMessage = session.createStreamMessage();
				streamMessage.setStringProperty("user", textFieldTo.getText());
				File f4=new File("images/"+comboBoxImages.getSelectionModel().getSelectedItem());
				FileInputStream fis = new FileInputStream(f4);
				byte[] data = new byte[(int)f4.length()];
				fis.read(data); //lire les données images en octets
				streamMessage.writeString(comboBoxImages.getSelectionModel().getSelectedItem());
				streamMessage.writeInt(data.length);
				streamMessage.writeBytes(data);
				messageProducer.send(streamMessage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		buttonConnecter.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {				
				try {
					codeUser = textFieldCode.getText();	
					String host = textFieldHost.getText();	
					int port = Integer.parseInt(textFieldPort.getText());
					ConnectionFactory connectionFactory = new 
							ActiveMQConnectionFactory("tcp://"+host+":"+port);
				   Connection connection = connectionFactory.createConnection();
				   connection.start();
				   session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				   Destination destination = session.createTopic("biu.chat");
				   //MessageConsumer messageConsumer = session.createConsumer(destination);
				   MessageConsumer messageConsumer = session.createConsumer(destination, "user='"+codeUser+"'");
				   messageProducer = session.createProducer(destination); //Envoi et écoute sur la même file d'attente
				   messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); //messages non persistants au broker
				   
				   messageConsumer.setMessageListener(message->{
					 try {
						  if(message instanceof TextMessage) {
							TextMessage textMessage = (TextMessage)message;
							//System.out.println(textMessage.getText());
							observableListMessages.add(textMessage.getText());
						  }
						  else if(message instanceof StreamMessage) {
							 //Lire l'image envoyée
							  StreamMessage streamMessage = (StreamMessage)message;
							  String imageName = streamMessage.readString();
							  observableListMessages.add("Réception d'une image : "+imageName);
							  int size = streamMessage.readInt();
							  byte[] data = new byte[size];
							  streamMessage.readBytes(data);
							  ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
							  Image image = new Image(byteArrayInputStream);
							  imageView.setImage(image);
						  }
					} catch (Exception e) {
						e.printStackTrace();
					}
				   });
			    hBox1.setDisable(true);  //On désactive le hBox1 après avoir connecté un USER
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}