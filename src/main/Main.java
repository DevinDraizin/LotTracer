package main;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import screensframework.ScreensController;

import java.sql.SQLException;


/*
    The goal of this application is to perform different types
    of lot tracing and management functions. It manages existing
    products as well as supports the creation of new products. Each
    product can also be broken down into components that are also
    tracked by lot. Simple vendor and buyer management is also
    supported. Managing products/components/vendors/buyers etc
    allows for detailed tracking of purchase orders along with
    shipping statuses. Lastly, the application implements a global
    search to quickly get information about current and past orders.

*/

public class Main extends Application
{

    //ID and File names corresponding to all fxml screens
    public static String screen1ID = "main";
    public static String screen1File = "/main/LotTracer.fxml";
    public static String screen2ID = "NewOrder";
    public static String screen2File = "/NewOrder/newOrder.fxml";
    public static String screen3ID = "ShipItems";
    public static String screen3File = "/ShipItems/shipItems.fxml";
    public static String screen4ID = "ReturnItems";
    public static String screen4File = "/ReturnItems/returnItems.fxml";
    public static String screen5ID = "ShipItems";
    public static String screen5File = "/ShipItems/shipItems.fxml";
    public static String screen6ID = "Buyers";
    public static String screen6File = "/Buyers/buyers.fxml";
    public static String screen7ID = "Vendors";
    public static String screen7File = "/Vendors/vendors.fxml";
    public static String screen8ID = "Components";
    public static String screen8File = "/Components/components.fxml";
    public static String screen9ID = "Products";
    public static String screen9File = "/Products/products.fxml";
    public static String screen10ID = "LotNumbers";
    public static String screen10File = "/LotNumbers/lotNumbers.fxml";
    public static String screen11ID = "ProductBuilder";
    public static String screen11File = "/ProductBuilder/productBuilder.fxml";


    private void shutdown()
    {
        //Before we shut down the application we need to
        //terminate the database connection
        try {
            DAL.DBConnectionManager.closeConnection();
        } catch (SQLException e) {
            System.out.println("Failed to close database connection");
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage)
    {
        boolean checkConn;

        checkConn = DAL.DBConnectionManager.establishConnection();

        if(!checkConn)
        {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Database Error");
            error.setHeaderText("Database connection failed\nPlease make sure the database is online");
            error.showAndWait();

            return;
        }

        //Load all screens into the screen controller
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.screen1ID, Main.screen1File);
        mainContainer.loadScreen(Main.screen2ID, Main.screen2File);
        mainContainer.loadScreen(Main.screen3ID, Main.screen3File);
        mainContainer.loadScreen(Main.screen4ID, Main.screen4File);
        mainContainer.loadScreen(Main.screen5ID, Main.screen5File);
        mainContainer.loadScreen(Main.screen6ID, Main.screen6File);
        mainContainer.loadScreen(Main.screen7ID, Main.screen7File);
        mainContainer.loadScreen(Main.screen8ID, Main.screen8File);
        mainContainer.loadScreen(Main.screen9ID, Main.screen9File);
        mainContainer.loadScreen(Main.screen10ID, Main.screen10File);
        mainContainer.loadScreen(Main.screen11ID, Main.screen11File);

        mainContainer.setScreen(Main.screen1ID);

        Scene scene = new Scene(mainContainer);
        primaryStage.getIcons().add(new Image("images/headsUpIcon.png"));
        primaryStage.setTitle("Lot Tracer");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();


        //Call shutdown() before closing application
        primaryStage.setOnCloseRequest(event -> {
            event.consume();

            shutdown();

            primaryStage.close();
        });


    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
