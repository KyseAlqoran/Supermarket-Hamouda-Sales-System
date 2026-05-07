package d.demo6;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminScene {
    ImageView logout = new ImageView("logout.png");
    private Button[] b = new Button[]{
            new Button("Dashboard"), new Button("Products"), new Button("Categories"),
            new Button("Orders"), new Button("Analytics"), new Button("Employees"),
            new Button("", logout), new Button("User Accounts"), new Button("Sales"), new Button("Supplies"),
            new Button("Suppliers")
    };
    private HBox[] hboxes = new HBox[]{
            new ProductFX().productTablePane(),
            new CategoryFX().categoryTablePane(),
            new OrderFX().orderTablePane(),
            new EmployeeFX().employeeTablePane(),
            new UsersFX().userTablePane(),
            new SaleFX().saleTablePane(),
            new SupplyFX().supplyTablePane(),
            new SupplierFX().supplierTablePane()
    };
    private boolean vis;
    private MainWindow mainWindow;
    private Stage mainStage;

    AdminScene(boolean vis, MainWindow mainWindow, Stage s) {
        this.vis = vis;
        this.mainWindow = mainWindow;
        mainStage = s;
    }

    private void setSizes(HBox hb) {
        Region center = (Region) mainWindow.root.getCenter();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        hb.prefHeightProperty().bind(center.heightProperty());
        hb.prefWidthProperty().bind(
                Bindings.createDoubleBinding(() ->
                                Math.min(center.getWidth(), screenBounds.getWidth() - 180),
                        center.widthProperty())
        );
        hb.setMaxWidth(Double.MAX_VALUE);
        hb.setMaxHeight(Double.MAX_VALUE);
    }

    public Pane p() {
        VBox v = new VBox(15);
        v.getChildren().addAll(b);

        if (!vis) {
            v.getChildren().remove(b[5]);
            v.getChildren().remove(b[7]);
        }

        v.setPadding(new Insets(30, 20, 30, 20));
        v.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e);" +
                "-fx-background-radius: 0 15 15 0;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 4, 0);");

        BorderPane bp = new BorderPane();
        bp.setLeft(v);
        bp.setTop(top());
        settingButtons(bp);
        bp.setStyle("-fx-background-color: linear-gradient(to bottom right, #ecf0f1, #d5dbdb);");
        return bp;
    }

    private VBox dashboard() {
        VBox vb = new VBox(new Dashboard().p());
        Region center = (Region) mainWindow.root.getCenter();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        vb.prefHeightProperty().bind(center.heightProperty());
        vb.prefWidthProperty().bind(
                Bindings.createDoubleBinding(() ->
                                Math.min(center.getWidth(), screenBounds.getWidth() - 180),
                        center.widthProperty())
        );
        vb.setMaxWidth(Double.MAX_VALUE);
        vb.setMaxHeight(Double.MAX_VALUE);
        vb.setStyle("-fx-background-color: rgba(255,255,255,0.9);" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        return vb;
    }

    private ScrollPane anaylstics() {
        ScrollPane vb = new Analytics().analyticsProbe();
        Region center = (Region) mainWindow.root.getCenter();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        vb.prefHeightProperty().bind(center.heightProperty());
        vb.prefWidthProperty().bind(
                Bindings.createDoubleBinding(() ->
                                Math.min(center.getWidth(), screenBounds.getWidth() - 180),
                        center.widthProperty())
        );
        vb.setMaxWidth(Double.MAX_VALUE);
        vb.setMaxHeight(Double.MAX_VALUE);
        vb.setStyle("-fx-background-color: rgba(255,255,255,0.9);" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        return vb;
    }

    private void settingButtons(BorderPane bp) {
        String[] buttonShades = {
                "#d6eaf8",
                "#aed6f1",
                "#85c1e9",
                "#5dade2",
                "#3498db",
                "#2980b9",
                "#2471a3",
                "#1f618d",
                "#1a5276",
                "#154360",
                "#154360"
        };

        for (int i = 0; i < b.length; i++) {
            Button button = b[i];
            String buttonColor = buttonShades[i];
            button.setPrefSize(200, 55);
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, " + buttonColor + ", derive(" + buttonColor + ", -15%));" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: 'Segoe UI', 'Arial';" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-border-color: derive(" + buttonColor + ", -25%);" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(52,152,219,0.3), 8, 0, 0, 4);" +
                            "-fx-padding: 12px;" +
                            "-fx-cursor: hand;"
            );

            button.setOnMouseEntered(e -> button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, derive(" + buttonColor + ", 15%), " + buttonColor + ");" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: 'Segoe UI', 'Arial';" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-border-color: derive(" + buttonColor + ", -15%);" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(52,152,219,0.4), 12, 0, 0, 6);" +
                            "-fx-padding: 12px;" +
                            "-fx-cursor: hand;" +
                            "-fx-scale-x: 1.02;" +
                            "-fx-scale-y: 1.02;"
            ));

            button.setOnMouseExited(e -> button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, " + buttonColor + ", derive(" + buttonColor + ", -15%));" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: 'Segoe UI', 'Arial';" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-border-color: derive(" + buttonColor + ", -25%);" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(52,152,219,0.3), 8, 0, 0, 4);" +
                            "-fx-padding: 12px;" +
                            "-fx-cursor: hand;" +
                            "-fx-scale-x: 1.0;" +
                            "-fx-scale-y: 1.0;"
            ));
        }

        b[0].setOnAction(e -> bp.setCenter(dashboard()));
        b[1].setOnAction(e -> {
            setSizes(hboxes[0]);
            bp.setCenter(hboxes[0]);
        });
        b[2].setOnAction(e -> {
            setSizes(hboxes[1]);
            bp.setCenter(hboxes[1]);
        });
        b[3].setOnAction(e -> {
            setSizes(hboxes[2]);
            bp.setCenter(hboxes[2]);
        });
        b[4].setOnAction(e -> bp.setCenter(anaylstics()));
        b[5].setOnAction(e -> {
            setSizes(hboxes[3]);
            bp.setCenter(hboxes[3]);
        });
        b[6].setOnAction(e -> {
            mainWindow.root.setCenter(new LoginFX(mainWindow, mainStage).loginScene());
        });
        b[7].setOnAction(e -> {
            bp.setCenter(hboxes[4]);
            setSizes(hboxes[4]);
        });
        b[8].setOnAction(e -> {
            bp.setCenter(hboxes[5]);
            setSizes(hboxes[5]);
        });
        b[9].setOnAction(e -> {
            bp.setCenter(hboxes[6]);
            setSizes(hboxes[6]);
        });
        b[10].setOnAction(e -> {
            bp.setCenter(hboxes[7]);
            setSizes(hboxes[7]);
        });

        b[5].setVisible(vis);
        b[7].setVisible(vis);

        b[0].fire();

        b[6].setPrefSize(55, 55);
        b[6].setStyle(
                "-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b);" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(231,76,60,0.4), 8, 0, 0, 3);" +
                        "-fx-border-color: #a93226;" +
                        "-fx-border-width: 2;"
        );

        b[6].setOnMouseEntered(e -> b[6].setStyle(
                "-fx-background-color: linear-gradient(to bottom, #c0392b, #922b21);" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(192,57,43,0.5), 12, 0, 0, 4);" +
                        "-fx-border-color: #7b241c;" +
                        "-fx-border-width: 2;" +
                        "-fx-scale-x: 1.05;" +
                        "-fx-scale-y: 1.05;"
        ));

        b[6].setOnMouseExited(e -> b[6].setStyle(
                "-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b);" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(231,76,60,0.4), 8, 0, 0, 3);" +
                        "-fx-border-color: #a93226;" +
                        "-fx-border-width: 2;" +
                        "-fx-scale-x: 1.0;" +
                        "-fx-scale-y: 1.0;"
        ));

        logout.setFitHeight(32);
        logout.setFitWidth(32);
        logout.setPreserveRatio(true);
    }

    private HBox top() {
        Label[] l = new Label[]{new Label("Hamoda Market"), new Label("Admin Dashboard"), new Label("Employee Dashboard")};
        l[0].setStyle("-fx-text-fill: #ecf0f1;" +
                "-fx-font-family: 'Segoe UI', 'Arial';" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 1, 1);");

        for (int i = 1; i < l.length; i++) {
            l[i].setStyle("-fx-text-fill: #bdc3c7;" +
                    "-fx-font-family: 'Segoe UI', 'Arial';" +
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 1, 0, 0, 1);");
        }

        VBox vb = new VBox(8, l[0]);
        if (vis) {
            vb.getChildren().add(l[1]);
        } else {
            vb.getChildren().add(l[2]);
        }
        vb.setPadding(new Insets(15, 0, 15, 25));

        HBox topBar = new HBox(25, b[6], vb);
        topBar.setPadding(new Insets(20, 25, 20, 25));
        topBar.setStyle("-fx-background-color: linear-gradient(to right, #34495e, #2c3e50);" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);" +
                "-fx-border-color: #1b2631;" +
                "-fx-border-width: 0 0 2 0;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
    }
}