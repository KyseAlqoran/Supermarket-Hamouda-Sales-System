package d.demo6;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerScene extends ScrollPane {
    private VBox cartPanel;
    private boolean cartVisible = false;
    private ButtonScene buttonScene;
    private MainWindow mainWindow;
    private Stage mainStage;
    private ObservableList<CartItem> cartItems;

    public CustomerScene(MainWindow mainWindow, Stage mainStage, Branch currentBranch, int personID) {
        this.mainStage = mainStage;
        this.mainWindow = mainWindow;
        cartItems = FXCollections.observableArrayList();
        cartPanel = createCartPanel();
        cartPanel.setVisible(false);
        cartPanel.setTranslateX(350);
        cartPanel.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!cartVisible) cartPanel.setTranslateX(newVal.doubleValue());
        });
        buttonScene = new ButtonScene(cartPanel, cartItems, currentBranch, personID);
        this.getStylesheets().add("data:text/css," +
                ".scroll-pane .scroll-bar:vertical {-fx-pref-width: 15px;}" +
                ".scroll-pane .thumb {-fx-background-color: #808080;-fx-background-insets: 2;-fx-background-radius: 3;}");

        StackPane mainContainer = new StackPane();
        BorderPane mainBorderPane = createMainContent();


        mainContainer.getChildren().addAll(mainBorderPane, cartPanel);
        this.setContent(mainContainer);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);

        this.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            mainContainer.setPrefWidth(newVal.getWidth());
            mainContainer.setPrefHeight(newVal.getHeight());
        });
    }

    private BorderPane createMainContent() {
        BorderPane mainBorderPane = new BorderPane();

        HBox topBar = new HBox();
        topBar.setPrefHeight(60);
        topBar.setStyle("-fx-background-color: #3c5688;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(20);

        ImageView logoutImage = new ImageView("logout.png");
        logoutImage.setPreserveRatio(true);
        logoutImage.setFitHeight(35);
        logoutImage.setFitWidth(35);

        Button logoutButton = new Button("", logoutImage);
        logoutButton.setPrefSize(50, 40);
        logoutButton.setMinSize(50, 40);
        logoutButton.setMaxSize(50, 40);
        logoutButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 4;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 1);"
        );

        logoutButton.setOnMouseEntered(e -> {
            logoutButton.setStyle(
                    "-fx-background-color: #c0392b;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 4;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);"
            );
        });

        logoutButton.setOnMouseExited(e -> {
            logoutButton.setStyle(
                    "-fx-background-color: #e74c3c;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 4;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 1);"
            );
        });

        logoutButton.setOnAction(e -> {
            try {
                mainWindow.root.setCenter(new LoginFX(mainWindow, mainStage).loginScene());
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error during logout: " + ex.getMessage());
            }
        });

        logoutButton.setMouseTransparent(false);
        logoutButton.setDisable(false);

        HBox labelContainer = new HBox(10);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        Label hamodaSupermarketLabel = new Label("Hamoda Supermarket");
        hamodaSupermarketLabel.setTextFill(Color.WHITE);
        hamodaSupermarketLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        ImageView superMarketImage = new ImageView("shopping-cart.png");
        superMarketImage.setFitHeight(35);
        superMarketImage.setFitWidth(35);

        labelContainer.getChildren().addAll(superMarketImage, hamodaSupermarketLabel);

        HBox searchHBox = new HBox();
        searchHBox.setAlignment(Pos.CENTER);
        TextField searchProductTextField = new TextField();
        searchProductTextField.setPromptText("Search products");
        searchProductTextField.setStyle("-fx-text-fill: gray;-fx-background-radius: 15;-fx-border-radius: 15;-fx-padding: 8 12 8 12;-fx-border-color: #ccc;-fx-border-width: 1;-fx-font-size: 14px;");
        searchProductTextField.setPrefWidth(400);
        searchProductTextField.setPrefHeight(40);
        searchHBox.getChildren().add(searchProductTextField);
        searchProductTextField.setOnKeyReleased(event -> {
            String filter = searchProductTextField.getText().toLowerCase();
            buttonScene.updateProducts(filter);
        });

        HBox viewHBox = new HBox(20);
        viewHBox.setAlignment(Pos.CENTER_RIGHT);
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setPrefHeight(40);
        viewCartButton.setPrefWidth(120);
        viewCartButton.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #e6e6e6);-fx-background-radius: 10;-fx-border-radius: 10;-fx-border-color: #cccccc;-fx-border-width: 1.5;-fx-font-weight: bold;-fx-font-size: 15px;-fx-text-fill: black;");
        viewCartButton.setOnAction(e -> toggleCartPanel());
        viewHBox.getChildren().addAll(viewCartButton);

        topBar.getChildren().addAll(logoutButton, labelContainer, searchHBox, viewHBox);

        HBox.setHgrow(labelContainer, javafx.scene.layout.Priority.NEVER);
        HBox.setHgrow(searchHBox, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(viewHBox, javafx.scene.layout.Priority.NEVER);

        HBox sortBox = new HBox(10);
        sortBox.setAlignment(Pos.CENTER_RIGHT);
        sortBox.setPadding(new Insets(0, 50, 0, 0));
        Button nameButton = new Button("Name");
        nameButton.setPrefHeight(30);
        nameButton.setPrefWidth(75);
        Button priceButton = new Button("Price");
        priceButton.setPrefHeight(30);
        priceButton.setPrefWidth(75);


        String buttonStyle = "-fx-background-color: #3c5688;" + "-fx-text-fill: white;" + "-fx-font-weight: bold;"
                + "-fx-background-radius: 10;" + "-fx-border-radius: 10;" + "-fx-border-color: transparent;";
        nameButton.setStyle(buttonStyle);
        priceButton.setStyle(buttonStyle);
        sortBox.getChildren().addAll(nameButton, priceButton);

        nameButton.setOnAction(e -> {
            ObservableList<Product> sortedList = executeQuery("SELECT * FROM Product ORDER BY prodName ASC;");
            buttonScene.sortProducts(sortedList);
        });
        priceButton.setOnAction(e -> {
            ObservableList<Product> sortedList = executeQuery("SELECT * FROM Product ORDER BY price ASC;");
            buttonScene.sortProducts(sortedList);
        });

        VBox mainVBox = new VBox(20);
        mainVBox.getChildren().addAll(topBar, sortBox, buttonScene);

        mainBorderPane.setTop(mainVBox);
        return mainBorderPane;
    }

    private VBox createCartPanel() {
        VBox cartPanel = new VBox();
        cartPanel.setPrefWidth(350);
        cartPanel.setMaxWidth(350);
        cartPanel.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 2; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, -5, 0);");
        StackPane.setAlignment(cartPanel, Pos.CENTER_RIGHT);

        VBox cartContent = shoppingCartVBox();
        HBox closeButtonBox = new HBox();
        closeButtonBox.setAlignment(Pos.CENTER_RIGHT);
        closeButtonBox.setPadding(new Insets(10));
        Button closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; -fx-font-size: 16px; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> toggleCartPanel());
        closeButtonBox.getChildren().add(closeButton);
        cartPanel.getChildren().addAll(closeButtonBox, cartContent);

        return cartPanel;
    }

    private void toggleCartPanel() {
        cartPanel.setVisible(true);
        double targetX = cartVisible ? cartPanel.getWidth() : 0;

        TranslateTransition transition = new TranslateTransition(Duration.millis(300), cartPanel);
        transition.setToX(cartVisible ? targetX : 0);
        transition.setOnFinished(e -> {
            if (cartVisible) cartPanel.setVisible(false);
            cartVisible = !cartVisible;
        });
        transition.play();
    }

    public VBox shoppingCartVBox() {
        VBox mainVBox = new VBox(30);

        VBox leftVBox = new VBox(15);
        leftVBox.setAlignment(Pos.TOP_LEFT);
        leftVBox.setPadding(new Insets(15));
        Label shoppingCartLabel = new Label("Shopping Cart");
        shoppingCartLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 17px;");
        Line topLine = new Line(20, 100, 330, 100);
        topLine.setStroke(Color.web("#dddddd"));
        topLine.setStrokeWidth(2);
        leftVBox.getChildren().addAll(shoppingCartLabel, topLine);

        // Enhanced empty cart styling (copied from updateCartDisplay)
        VBox centerVBox = new VBox(25);
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setPadding(new Insets(30, 20, 30, 20));
        centerVBox.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #ffffff); " +
                "-fx-background-radius: 15; -fx-border-radius: 15; " +
                "-fx-border-color: #e9ecef; -fx-border-width: 1;");

        VBox emptyContainer = new VBox(15);
        emptyContainer.setAlignment(Pos.CENTER);

        Label emptyIcon = new Label("🛒");
        emptyIcon.setStyle("-fx-font-size: 48px; -fx-opacity: 0.5;");

        Label emptyLabel = new Label("Your cart is empty");
        emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d; -fx-font-weight: 500;");

        Label emptySubLabel = new Label("Add some products to get started!");
        emptySubLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #adb5bd; -fx-font-style: italic;");

        emptyContainer.getChildren().addAll(emptyIcon, emptyLabel, emptySubLabel);

        Line middleLine = new Line(30, 100, 280, 100);
        middleLine.setStroke(Color.web("#3c5688"));
        middleLine.setStrokeWidth(2);
        middleLine.setStyle("-fx-opacity: 0.3;");

        Label totalLabel = new Label("Total: $0.00");
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #495057;");

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setPrefHeight(40);
        checkoutButton.setPrefWidth(250);
        checkoutButton.setStyle("-fx-background-color: #3c5688;-fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 10;-fx-border-radius: 10;-fx-border-color: transparent; -fx-opacity: 0.6;");
        checkoutButton.setDisable(true);

        centerVBox.getChildren().addAll(emptyContainer, middleLine, totalLabel, checkoutButton);

        mainVBox.getChildren().addAll(leftVBox, centerVBox);
        return mainVBox;
    }

    private ObservableList<Product> executeQuery(String query) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                if (rs.getBoolean("isActive")) {
                    Product p = new Product(
                            rs.getInt("prodID"),
                            rs.getString("prodName"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getDate("expire_Date"),
                            rs.getInt("catID"),
                            rs.getInt("wID"),
                            rs.getString("photoPath")
                            , rs.getBoolean("isActive")
                    );
                    products.add(p);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Executing Query");
        }
        return products;
    }
}