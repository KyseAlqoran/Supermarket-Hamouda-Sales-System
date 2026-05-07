package d.demo6;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class ProductScene extends BorderPane {
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private VBox cartPanel;
    private Branch currentBranch;
    private int personID;

    public ProductScene(ObservableList<Product> productList, VBox cartPanel,
                        ObservableList<CartItem> cartItems, Branch currentBranch, int personID) {
        this.cartPanel = cartPanel;
        this.cartItems = cartItems;
        this.currentBranch = currentBranch;
        this.personID = personID;

        HBox topHBox = new HBox(550);
        Label allProductsLabel = new Label("All Products");
        allProductsLabel.setStyle("-fx-font-weight: bold; " + "-fx-font-size: 18px;");
        topHBox.getChildren().addAll(allProductsLabel);

        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(20);
        gp.setVgap(20);

        VBox middleVBox = new VBox(20);
        int j = 0, h = 0;
        for (int i = 0; i < productList.size(); i++) {
            if (j % 3 == 0 && j != 0) {
                j = 0;
                h++;
            }
            gp.add(productDesign(productList.get(i)), j, h);
            j++;
        }
        middleVBox.getChildren().addAll(topHBox, gp);
        setTop(middleVBox);
    }

    private StackPane productDesign(Product product) {
        StackPane[] sp = new StackPane[2];
        for (int i = 0; i < sp.length; i++) {
            sp[i] = new StackPane();
        }
        ImageView image = new ImageView();
        File imgFile = new File(product.getPhotoPath());
        try {
            Image img = new Image(imgFile.getName().trim());
            image.setImage(img);
        } catch (Exception e) {
            image.setImage(new Image("noProd.png"));
        }
        image.setFitHeight(50);
        image.setFitWidth(50);
        Rectangle[] r = new Rectangle[2];
        Integer[] size = {275, 250, 80, 80};
        for (int i = 0, j = 0; i < r.length; i++) {
            r[i] = new Rectangle(size[j++], size[j++]);
            r[i].setArcWidth(30);
            r[i].setArcHeight(30);
        }
        r[0].setStroke(Color.web("#cccccc"));
        r[0].setFill(Color.WHITE);
        r[0].setStrokeWidth(1.5);
        r[1].setStyle("-fx-fill: #FFD580;");
        sp[1].getChildren().addAll(r[1], image);
        Label[] l = new Label[2];
        String[] names = {product.getProdName(), "$ " + product.getPrice()};
        for (int i = 0; i < l.length; i++) {
            l[i] = new Label(names[i]);
            l[i].setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill:#000000;");
        }
        VBox productVBox = new VBox(10);
        productVBox.setAlignment(Pos.CENTER);
        Button b = new Button("Add to Cart");
        b.setPrefHeight(30);
        b.setPrefWidth(250);
        b.setStyle("-fx-background-color: #3c5688;" + "-fx-text-fill: white;" + "-fx-font-weight: bold;"
                + "-fx-background-radius: 10;" + "-fx-border-radius: 10;" + "-fx-border-color: transparent;");
        productVBox.getChildren().addAll(sp[1], l[0],l[1], b);
        sp[0].getChildren().addAll(r[0], productVBox);
        b.setOnAction(e -> {
            addToCart(product);
        });
        return sp[0];
    }

    private void addToCart(Product product) {
        CartItem existingItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getProdID() == product.getProdID()) {
                existingItem = item;
                break;
            }
        }
        if (existingItem != null) {
            existingItem.incrementQuantity();
        } else {
            cartItems.add(new CartItem(product, 1));
        }
        updateCartDisplay();
    }

    private void updateCartDisplay() {
        if (cartPanel.getChildren().size() < 2) return;
        VBox cartContent = (VBox) cartPanel.getChildren().get(1);
        if (cartContent.getChildren().size() < 2) return;
        VBox centerVBox = (VBox) cartContent.getChildren().get(1);
        centerVBox.getChildren().clear();
        if (cartItems.isEmpty()) {
            VBox emptyContainer = new VBox(15);
            emptyContainer.setAlignment(Pos.CENTER);
            Label[] labels = {
                    new Label("🛒"),
                    new Label("Your cart is empty"),
                    new Label("Add some products to get started!")
            };
            String[] styles = {
                    "-fx-font-size: 48px; -fx-opacity: 0.5;",
                    "-fx-font-size: 16px; -fx-text-fill: #6c757d; -fx-font-weight: 500;",
                    "-fx-font-size: 12px; -fx-text-fill: #adb5bd; -fx-font-style: italic;"
            };
            for (int i = 0; i < labels.length; i++)
                labels[i].setStyle(styles[i]);
            emptyContainer.getChildren().addAll(labels);
            Line line = new Line(30, 100, 280, 100);
            line.setStroke(Color.web("#3c5688"));
            line.setStrokeWidth(2);
            line.setOpacity(0.3);
            Label totalLabel = new Label("Total: $0.00");
            totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #495057;");
            Button checkout = new Button("Checkout");
            checkout.setPrefSize(250, 40);
            checkout.setStyle("-fx-background-color: #3c5688;-fx-text-fill: white;-fx-font-weight: bold;" +
                    "-fx-background-radius: 10;-fx-border-radius: 10;-fx-border-color: transparent; -fx-opacity: 0.6;");
            checkout.setDisable(true);
            centerVBox.setSpacing(25);
            centerVBox.setAlignment(Pos.CENTER);
            centerVBox.setPadding(new Insets(30, 20, 30, 20));
            centerVBox.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #ffffff); " +
                    "-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #e9ecef; -fx-border-width: 1;");
            centerVBox.getChildren().addAll(emptyContainer, line, totalLabel, checkout);
        } else {
            centerVBox.setSpacing(8);
            centerVBox.setAlignment(Pos.TOP_CENTER);
            centerVBox.setPadding(new Insets(10, 8, 15, 8));
            centerVBox.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f9fa);");
            HBox header = new HBox();
            header.setAlignment(Pos.CENTER);
            header.setPadding(new Insets(5, 0, 10, 0));
            Label itemCount = new Label(cartItems.size() + " item" + (cartItems.size() > 1 ? "s" : "") + " in cart");
            itemCount.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d; -fx-font-weight: 500;");
            header.getChildren().add(itemCount);
            VBox itemsContainer = new VBox(8);
            itemsContainer.setAlignment(Pos.TOP_CENTER);
            itemsContainer.setPadding(new Insets(0, 2, 0, 2));
            for (CartItem item : cartItems)
                itemsContainer.getChildren().add(createEnhancedCartItem(item));
            Line separator = new Line(20, 0, 310, 0);
            separator.setStroke(Color.web("#3c5688"));
            separator.setStrokeWidth(3);
            separator.setEffect(new javafx.scene.effect.DropShadow(4, Color.rgb(60, 86, 136, 0.25)));
            VBox totalSection = new VBox(12);
            totalSection.setAlignment(Pos.CENTER);
            totalSection.setPadding(new Insets(15, 10, 10, 10));
            totalSection.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8f9fa, #e9ecef); " +
                    "-fx-background-radius: 15; -fx-border-radius: 15; " +
                    "-fx-border-color: #dee2e6; -fx-border-width: 1; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

            // Calculate total using basic loop instead of stream
            double total = 0.0;
            for (CartItem item : cartItems) {
                total += item.getTotalPrice();
            }

            // Calculate total items using basic loop instead of stream
            int totalItems = 0;
            for (CartItem item : cartItems) {
                totalItems += item.getQuantity();
            }

            Label totalLabel = new Label("Total: $" + String.format("%.2f", total));
            totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #3c5688;");
            Label summaryLabel = new Label("(" + totalItems + " item" + (totalItems > 1 ? "s" : "") + ")");
            summaryLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");
            totalSection.getChildren().addAll(totalLabel, summaryLabel);
            Button checkout = new Button("Checkout");
            checkout.setPrefSize(250, 40);
            checkout.setStyle("-fx-background-color: #3c5688;-fx-text-fill: white;-fx-font-weight: bold;" +
                    "-fx-background-radius: 10;-fx-border-radius: 10;-fx-border-color: transparent;");
            checkout.setOnAction(e -> {
                if (currentBranch == null) processOnlineOrder();
                else processInBranchSale();
            });
            centerVBox.getChildren().addAll(header, itemsContainer, separator, totalSection, checkout);
        }
    }

    private void processOnlineOrder() {
        new OrderDialog().showAndWait().ifPresent(details -> {
            try (Connection conn = MainApplication.dbConn.connectDB();
                 Statement stmt = conn.createStatement()) {
                String date = LocalDate.now().toString();
                String address = details.address.replace("'", "''");

                stmt.executeUpdate("INSERT INTO Delivery (dDate, dStatus, Address) VALUES ('" + date + "', true, '" + address + "')");
                ResultSet rsD = stmt.executeQuery("SELECT MAX(dID) FROM Delivery");
                rsD.next();
                int dID = rsD.getInt(1);

                stmt.executeUpdate("INSERT INTO Orders (oDate, status, cID, dID) VALUES ('" + date + "', true, " + personID + ", " + dID + ")");
                ResultSet rsO = stmt.executeQuery("SELECT MAX(oID) FROM Orders");
                rsO.next();
                int oID = rsO.getInt(1);

                for (CartItem item : cartItems)
                    stmt.executeUpdate("INSERT INTO Order_Detail (oID, prodID, quantity) VALUES (" +
                            oID + "," + item.getProduct().getProdID() + "," + item.getQuantity() + ")");
                cartItems.clear();
                updateCartDisplay();
                showAlert("Success", "Order placed successfully!", AlertType.INFORMATION);

            } catch (Exception e) {
                showAlert("Database Error", "Failed to create order: " + e.getMessage(), AlertType.ERROR);
            }
        });
    }

    private void processInBranchSale() {
        try (Connection conn = MainApplication.dbConn.connectDB();
             Statement stmt = conn.createStatement()) {

            String saleDate = LocalDate.now().toString();
            String insertSale = "INSERT INTO Sale (bID, pID, sale_date) VALUES (" +
                    currentBranch.getId() + ", " + personID + ", '" + saleDate + "')";
            stmt.executeUpdate(insertSale);

            ResultSet rs = stmt.executeQuery("SELECT MAX(saleID) FROM Sale");
            if (rs.next()) {
                int saleID = rs.getInt(1);
                for (CartItem item : cartItems) {
                    String insertDetail = "INSERT INTO Sale_Details (saleID, prodID, quantity, unitPrice, discount) VALUES (" +
                            saleID + ", " + item.getProduct().getProdID() + ", " + item.getQuantity() + ", " +
                            item.getProduct().getPrice() + ", 0.0)";
                    stmt.executeUpdate(insertDetail);
                }
            }
            cartItems.clear();
            updateCartDisplay();
            showAlert("Success", "Sale completed successfully!", AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Database Error", "Failed to create sale: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox createEnhancedCartItem(CartItem item) {
        String[] styles = {
                "-fx-background-color: linear-gradient(to bottom right, #ffffff, #f8f9fc); " +
                        "-fx-border-color: linear-gradient(to right, #e3f2fd, #f3e5f5); -fx-border-width: 1.5; " +
                        "-fx-border-radius: 12; -fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);",

                "-fx-background-color: linear-gradient(to bottom right, #f8f9fc, #e8eaf6); " +
                        "-fx-border-color: linear-gradient(to right, #3c5688, #5e72a3); -fx-border-width: 2; " +
                        "-fx-border-radius: 12; -fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(60,86,136,0.15), 10, 0, 0, 3);"
        };

        VBox main = new VBox(8);
        main.setPadding(new Insets(12));
        main.setStyle(styles[0]);
        main.setOnMouseEntered(e -> main.setStyle(styles[1]));
        main.setOnMouseExited(e -> main.setStyle(styles[0]));

        ImageView imgView = new ImageView();
        try {
            imgView.setImage(new Image(new File(item.getProduct().getPhotoPath()).getName().trim()));
        } catch (Exception e) {
            imgView.setImage(new Image("noProd.png"));
        }
        imgView.setFitHeight(45);
        imgView.setFitWidth(45);
        imgView.setPreserveRatio(true);

        Rectangle imgBg = new Rectangle(50, 50, Color.web("#f8f9fa"));
        imgBg.setStroke(Color.web("#dee2e6"));
        imgBg.setStrokeWidth(1);
        imgBg.setArcWidth(10);
        imgBg.setArcHeight(10);

        StackPane imgPane = new StackPane(imgBg, imgView);

        Label[] labels = {
                new Label(item.getProduct().getProdName()),
                new Label("$" + (item.getProduct().getPrice() * 100 / 100.0) + " each"),
                new Label(item.getQuantity() + ""),
                new Label("$" + (item.getTotalPrice() * 100 / 100.0)),
                new Label("total")
        };

        labels[0].setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #212529;");
        labels[0].setWrapText(true);
        labels[0].setMaxWidth(180);
        labels[1].setStyle("-fx-font-size: 11px; -fx-text-fill: #6c757d;");
        labels[2].setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #495057; -fx-min-width: 25; -fx-alignment: center;");
        labels[3].setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #3c5688;");
        labels[4].setStyle("-fx-font-size: 10px; -fx-text-fill: #6c757d; -fx-font-style: italic;");

        VBox productDetails = new VBox(4, labels[0], labels[1]);
        productDetails.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(productDetails, Priority.ALWAYS);
        HBox top = new HBox(12, imgPane, productDetails);
        top.setAlignment(Pos.CENTER_LEFT);

        Button[] buttons = {
                createQtyButton("−", "#dc3545", () -> removeFromCart(item)),
                createQtyButton("+", "#28a745", () -> {
                    item.incrementQuantity();
                    updateCartDisplay();
                })
        };

        HBox qtyBox = new HBox(8, buttons[0], labels[2], buttons[1]);
        qtyBox.setAlignment(Pos.CENTER);
        qtyBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 20; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 20; -fx-padding: 4 8;");

        VBox priceBox = new VBox(2, labels[3], labels[4]);
        priceBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bottom = new HBox(12, qtyBox, spacer, priceBox);
        bottom.setAlignment(Pos.CENTER);

        main.getChildren().addAll(top, bottom);
        return main;
    }

    private Button createQtyButton(String text, String color, Runnable action) {
        Button btn = new Button(text);
        String[] styles = {
                "-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-font-size: 14px; " +
                        "-fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 15; -fx-border-radius: 15; -fx-padding: 2 8;",

                "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; " +
                        "-fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 15; -fx-border-radius: 15; -fx-padding: 2 8;"
        };
        btn.setStyle(styles[0]);
        btn.setPrefSize(30, 25);
        btn.setOnMouseEntered(e -> btn.setStyle(styles[1]));
        btn.setOnMouseExited(e -> btn.setStyle(styles[0]));
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void removeFromCart(CartItem itemToRemove) {
        if (itemToRemove.getQuantity() > 1) {
            itemToRemove.decrementQuantity();
        } else {
            cartItems.remove(itemToRemove);
        }
        updateCartDisplay();
    }

    private static class OrderDetails {
        private String address;
        private String shippingMethod;
        private String paymentMethod;
        private String visaNumber;

        public OrderDetails(String address, String shippingMethod,
                            String paymentMethod, String visaNumber) {
            this.address = address;
            this.shippingMethod = shippingMethod;
            this.paymentMethod = paymentMethod;
            this.visaNumber = visaNumber;
        }

        public String address() {
            return address;
        }

        public String shippingMethod() {
            return shippingMethod;
        }

        public String paymentMethod() {
            return paymentMethod;
        }

        public String visaNumber() {
            return visaNumber;
        }
    }

    private class OrderDialog extends Dialog<OrderDetails> {
        public OrderDialog() {
            setTitle("Order Details");
            setHeaderText("Please provide your order information");

            ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(confirmBtn, ButtonType.CANCEL);

            TextField addressField = new TextField();
            addressField.setPromptText("Delivery Address");
            addressField.setPrefWidth(250);

            ComboBox<String> shippingBox = new ComboBox<>();
            shippingBox.getItems().addAll("Standard", "Express");
            shippingBox.setValue("Standard");
            shippingBox.setPrefWidth(250);

            ToggleGroup paymentGroup = new ToggleGroup();
            RadioButton cashOption = new RadioButton("Cash on Delivery");
            RadioButton visaOption = new RadioButton("Visa");
            cashOption.setToggleGroup(paymentGroup);
            visaOption.setToggleGroup(paymentGroup);
            cashOption.setSelected(true);

            TextField visaField = new TextField();
            visaField.setPromptText("Visa Number (16 digits)");
            visaField.setDisable(true);
            visaField.setVisible(false);
            visaField.setPrefWidth(250);

            visaOption.selectedProperty().addListener((obs, oldV, newV) -> {
                visaField.setDisable(!newV);
                visaField.setVisible(newV);
            });

            visaField.textProperty().addListener((obs, oldV, newV) -> {
                visaField.setText(newV.replaceAll("[^\\d]", ""));
                if (visaField.getText().length() > 16) visaField.setText(oldV);
            });

            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(20);
            grid.setPadding(new Insets(25));
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setHalignment(HPos.RIGHT);
            grid.getColumnConstraints().addAll(col1, new ColumnConstraints());

            grid.add(new Label("Delivery Address:"), 0, 0);
            grid.add(addressField, 1, 0);
            grid.add(new Separator(), 0, 1, 2, 1);
            grid.add(new Label("Shipping Method:"), 0, 2);
            grid.add(shippingBox, 1, 2);
            grid.add(new Separator(), 0, 3, 2, 1);
            grid.add(new Label("Payment Method:"), 0, 4);
            VBox paymentBox = new VBox(8, cashOption, visaOption, visaField);
            grid.add(paymentBox, 1, 4);

            getDialogPane().setContent(grid);
            Platform.runLater(addressField::requestFocus);

            setResultConverter(btn -> {
                if (btn == confirmBtn) {
                    if (addressField.getText().isEmpty()) {
                        showAlert("Error", "Address is required", AlertType.ERROR);
                        return null;
                    }
                    if (visaOption.isSelected() && visaField.getText().length() != 16) {
                        showAlert("Error", "Visa number must be 16 digits", AlertType.ERROR);
                        return null;
                    }
                    return new OrderDetails(
                            addressField.getText(),
                            shippingBox.getValue(),
                            visaOption.isSelected() ? "Visa" : "Cash",
                            visaOption.isSelected() ? visaField.getText() : null
                    );
                }
                return null;
            });
        }
    }
}