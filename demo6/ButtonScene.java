package d.demo6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ButtonScene extends VBox {
    private HBox middleHBox = new HBox(175);
    private VBox centerLeft = new VBox();
    private ProductScene productScene;
    private VBox cartPanel;
    private ObservableList<CartItem> cartItems;
    private Branch currentBranch;
    private int personID;

    public ButtonScene(VBox cartPanel, ObservableList<CartItem> cartItems, Branch currentBranch, int personID) {
        this.cartItems = cartItems;
        this.cartPanel = cartPanel;
        this.currentBranch = currentBranch;
        this.personID = personID;
        GridPane categoriesGridPane = new GridPane();
        categoriesGridPane.setVgap(10);
        categoriesGridPane.setPadding(new Insets(10));
        Button allCategories = new Button("All Products");
        allCategories.setOnMouseClicked(e -> {
            ProductFX.productTable.setItems(ProductFX.productList);
            middleHBox.getChildren().clear();
            productScene = new ProductScene(ProductFX.productList, cartPanel, cartItems, currentBranch, personID);
            middleHBox.getChildren().addAll(centerLeft, productScene);
        });
        settingStyle(allCategories);
        categoriesGridPane.add(allCategories, 0, 0);
        for (int i = 1, j = 0; i < CategoryFX.categoryList.size(); i++, j++) {
            categoriesGridPane.add(categoryButtonMaker(CategoryFX.categoryList.get(j)), 0, i);
        }
        Label categoriesLabel = new Label("           Categories");
        categoriesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        centerLeft.getChildren().addAll(categoriesLabel, categoriesGridPane);

        productScene = new ProductScene(ProductFX.productList, cartPanel, cartItems, currentBranch, personID);
        middleHBox.getChildren().addAll(centerLeft, productScene);
        getChildren().addAll(middleHBox);
    }

    private Button categoryButtonMaker(Category category) {
        ImageView image = new ImageView();
        try {
            File imgFile = new File(category.getPhotoPath());
            Image img = new Image(imgFile.getName().trim());
            image.setImage(img);
        } catch (Exception e) {
            image.setImage(new Image("noProd.png"));
        }
        image.setFitHeight(50);
        image.setFitWidth(50);
        Button b = new Button(category.getCatName(), image);
        settingStyle(b);
        b.setOnAction(e -> {
            String query = "SELECT * FROM Product WHERE catID = (" +
                    "SELECT catID FROM Category WHERE catName = '" + category.getCatName() + "')";
            try (Connection con = MainApplication.dbConn.connectDB();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
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
                        filteredProducts.add(p);
                    }
                }
                if (filteredProducts.isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "There Are No Products In This Category").show();
                    return;
                }
                middleHBox.getChildren().clear();
                productScene = new ProductScene(filteredProducts, cartPanel, cartItems, currentBranch, personID);
                middleHBox.getChildren().addAll(centerLeft, productScene);
                ProductFX.productTable.setItems(filteredProducts);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error filtering products by category");
            }
        });

        return b;
    }

    private void settingStyle(Button b) {
        b.setPrefHeight(50);
        b.setPrefWidth(200);
        b.setStyle(getNormalTextStyle());
        b.setContentDisplay(ContentDisplay.LEFT);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(5, 10, 5, 10));
        b.setGraphicTextGap(30);
        b.setOnMouseEntered(e -> b.setStyle(getHoverTextStyle()));
        b.setOnMouseExited(e -> b.setStyle(getNormalTextStyle()));
    }

    private String getNormalTextStyle() {
        return "-fx-background-color: linear-gradient(to bottom, #ffffff, #e6e6e6);"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-border-color: #cccccc;"
                + "-fx-border-width: 1.5;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 15px;"
                + "-fx-text-fill: black;";
    }

    private String getHoverTextStyle() {
        return "-fx-background-color: linear-gradient(to bottom, #ffffff, #e6e6e6);"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-border-color: #0078D7;"
                + "-fx-border-width: 2;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 15px;"
                + "-fx-text-fill: black;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,120,215,0.7), 10, 0, 0, 0);";
    }

    public void updateProducts(String filter) {
        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        String lowerFilter = filter.toLowerCase().trim();
        for (int i = 0; i < ProductFX.productList.size(); i++) {
            Product p = ProductFX.productList.get(i);
            if (p.getProdName().toLowerCase().contains(lowerFilter) || p.getDescription().toLowerCase().contains(lowerFilter)) {
                filteredList.add(p);
            }
        }
        middleHBox.getChildren().remove(productScene);
        productScene = new ProductScene(filteredList, cartPanel, cartItems, currentBranch, personID);
        middleHBox.getChildren().add(productScene);
        ProductFX.productTable.setItems(filteredList);
    }

    public void sortProducts(ObservableList<Product> sortedList) {
        middleHBox.getChildren().remove(productScene);
        productScene = new ProductScene(sortedList, cartPanel, cartItems, currentBranch, personID);
        middleHBox.getChildren().add(productScene);
        ProductFX.productTable.setItems(sortedList);
    }
}
