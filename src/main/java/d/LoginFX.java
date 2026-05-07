package d.demo6;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.sql.*;
import java.time.LocalDate;

public class LoginFX {
    private Text mainT;
    private MainWindow mainWindow;
    private Stage mainStage;


    private Button[] b = new Button[2];
    private Label[] l = new Label[2];
    private TextField[] tf = new TextField[1];
    private PasswordField pf;

    private Button[] cb = new Button[2];
    private Label[] cl = new Label[6];
    private TextField[] ctf = new TextField[5];
    private PasswordField cpf;

    public LoginFX(MainWindow mainWindow, Stage stage) {
        this.mainWindow = mainWindow;
        this.mainStage = stage;
        mainStage.setMaximized(false);
        mainStage.setWidth(500);
        mainStage.setHeight(600);
        centerStage();

        initArrays();
    }

    private void centerStage() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        mainStage.setX((screenBounds.getWidth() - mainStage.getWidth()) / 2);
        mainStage.setY((screenBounds.getHeight() - mainStage.getHeight()) / 2);
    }

    private void initArrays() {
        b[0] = new Button("Sign in");
        b[1] = new Button("Create Account");
        l[0] = new Label("Username:");
        l[1] = new Label("Password:");
        tf[0] = new TextField();
        pf = new PasswordField();
        cb[0] = new Button("Back");
        cb[1] = new Button("Create Account");
        String[] labels = {"First Name:", "Last Name:", "Phone Number:",
                "Email:", "Username:", "Password:"};
        for (int i = 0; i < cl.length; i++) {
            cl[i] = new Label(labels[i]);
        }
        for (int i = 0; i < ctf.length; i++) {
            ctf[i] = new TextField();
        }
        cpf = new PasswordField();
    }

    public VBox loginScene() {
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-background-color: #ecf0f1;");
        VBox vb2 = new VBox(25);
        vb2.setAlignment(Pos.CENTER);
        vb2.setPadding(new Insets(40, 30, 40, 30));
        vb2.setPrefWidth(320);
        vb2.setMaxWidth(320);
        vb2.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        Text t = new Text("LOGIN");
        t.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        t.setFill(Color.web("#2c3e50"));
        VBox vb3 = new VBox(15);
        vb3.setAlignment(Pos.CENTER);
        setStyles();
        mainT = new Text();
        mainT.setFont(Font.font("Arial", 12));
        mainT.setFill(Color.FIREBRICK);
        HBox hb1 = new HBox(20);
        hb1.setAlignment(Pos.CENTER);
        hb1.getChildren().addAll(b);
        vb3.getChildren().addAll(l[0], tf[0], l[1], pf, mainT, hb1);
        vb2.getChildren().addAll(t, vb3);
        vb.getChildren().add(vb2);
        setupLoginEvents();
        return vb;
    }

    public VBox createAccountScene() {
        VBox vb1 = new VBox();
        vb1.setAlignment(Pos.CENTER);
        vb1.setStyle("-fx-background-color: #ecf0f1;");
        VBox vb2 = new VBox(25);
        vb2.setAlignment(Pos.CENTER);
        vb2.setPadding(new Insets(40, 30, 40, 30));
        vb2.setPrefWidth(340);
        vb2.setMaxWidth(340);
        vb2.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        Text t = new Text("CREATE ACCOUNT");
        t.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        t.setFill(Color.web("#2c3e50"));
        VBox vb3 = new VBox(12);
        vb3.setAlignment(Pos.CENTER);
        setCAStyles();
        for (int i = 0; i < 5; i++) {
            vb3.getChildren().addAll(cl[i], ctf[i]);
        }
        vb3.getChildren().addAll(cl[5], cpf);
        HBox hb1 = new HBox(20);
        hb1.setAlignment(Pos.CENTER);
        hb1.getChildren().addAll(cb);
        vb3.getChildren().add(hb1);
        vb2.getChildren().addAll(t, vb3);
        vb1.getChildren().add(vb2);
        setupCAEvents();
        return vb1;
    }

    private void setStyles() {
        b[0].setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 24;" +
                        "-fx-cursor: hand;"
        );
        b[1].setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #3498db;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 2;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 10 16;" +
                        "-fx-cursor: hand;"
        );
        String style =
                "-fx-background-color: white;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12;" +
                        "-fx-font-size: 14px;" +
                        "-fx-pref-width: 240;";

        tf[0].setStyle(style);
        pf.setStyle(style);
        for (int i = 0; i < l.length; i++) {
            l[i].setStyle(
                    "-fx-text-fill: #2c3e50;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;"
            );
        }
    }

    private void setCAStyles() {
        cb[0].setStyle(
                "-fx-background-color: #95a5a6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;"
        );
        cb[1].setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;"
        );
        String style = "-fx-background-color: white;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12;" +
                        "-fx-font-size: 14px;" +
                        "-fx-pref-width: 260;";

        for (int i = 0; i < ctf.length; i++) {
            ctf[i].setStyle(style);
        }
        cpf.setStyle(style);
        for (int i = 0; i < cl.length; i++) {
            cl[i].setStyle(
                    "-fx-text-fill: #2c3e50;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;"
            );
        }
    }

    private void setupLoginEvents() {
        b[0].setOnAction(e -> {
            String[] s = {tf[0].getText(), pf.getText()};
            User user = verify(s[0], s[1]);
            if (user != null) {
                if (user.isActiveStatus()) {
                    changeScene(user);
                } else {
                    mainT.setText("Your Account Is Not Active\nTry Contacting The Store Support Team");
                }
            } else {
                mainT.setText("Invalid username or password");
            }
        });

        b[1].setOnAction(e -> {
            mainStage.setWidth(800);
            mainStage.setHeight(900);
            centerStage();
            mainWindow.root.setCenter(createAccountScene());
        });
    }

    private void setupCAEvents() {
        cb[0].setOnAction(e -> {
            mainStage.setWidth(500);
            mainStage.setHeight(600);
            centerStage();
            mainWindow.root.setCenter(loginScene());
        });

        cb[1].setOnAction(e -> {
            mainWindow.root.setCenter(loginScene());
            try (Connection conn = MainApplication.dbConn.connectDB();
                 Statement stmt = conn.createStatement()) {
                String firstName = ctf[0].getText();
                String lastName = ctf[1].getText();
                String phone = ctf[2].getText();
                String email = ctf[3].getText();
                String username = ctf[4].getText();
                String password = cpf.getText();
                String personSQL = "INSERT INTO Person (pFirstName, pLastName, pPhoneNumber, pEmail) " +
                        "VALUES ('" + firstName + "', '" + lastName + "', '" + phone + "', '" + email + "')";
                stmt.executeUpdate(personSQL);
                ResultSet rs = stmt.executeQuery("SELECT MAX(pID) FROM Person");
                if (rs.next()) {
                    int pID = rs.getInt(1);
                    String userSQL = "INSERT INTO User (uname, password, accountType, activeStatus, pID) " +
                            "VALUES ('" + username + "', '" + password + "', 'Customer', true, " + pID + ")";
                    stmt.executeUpdate(userSQL);
                    String customerSQL = "INSERT INTO Customer (cID, LastPurchaseDate) " +
                            "VALUES (" + pID + ", '" + LocalDate.now() + "')";
                    stmt.executeUpdate(customerSQL);
                }
                new Alert(Alert.AlertType.INFORMATION,"A New Customer Account Has Been Made!");
                mainStage.setWidth(500);
                mainStage.setHeight(600);
                centerStage();
                MainApplication.readDB();
            } catch (SQLIntegrityConstraintViolationException ex) {
                new Alert(Alert.AlertType.WARNING,"This Username Is Already Used!\\nTry Another Name\"");
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private User verify(String username, String password) {
        for (User user : UsersFX.userList) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void changeScene(User user){
        switch (user.getAccountType()) {
            case "Admin":
                mainStage.setMaximized(true);
                mainWindow.root.setCenter(new AdminScene(true, mainWindow, mainStage).p());
                break;
            case "Employee":
                mainStage.setMaximized(true);
                mainWindow.root.setCenter(new AdminScene(false, mainWindow, mainStage).p());
                break;
            case "Customer":
                mainWindow.root.setCenter(branchSelection(user));
                break;
        }
    }

    public VBox branchSelection(User user){
        VBox vb1 = new VBox();
        vb1.setAlignment(Pos.CENTER);
        vb1.setStyle("-fx-background-color: #ecf0f1;");
        vb1.setPadding(new Insets(30));

        VBox vb2 = new VBox(25);
        vb2.setAlignment(Pos.CENTER);
        vb2.setPadding(new Insets(40, 30, 40, 30));
        vb2.setPrefWidth(500);
        vb2.setMaxWidth(500);
        vb2.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );

        Text t = new Text("SELECT BRANCH");
        t.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        t.setFill(Color.web("#2c3e50"));

        ScrollPane sp = new ScrollPane();
        sp.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;"
        );
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setPrefHeight(400);
        sp.setFitToWidth(true);

        VBox vb3 = new VBox(15);
        vb3.setAlignment(Pos.CENTER);
        vb3.setPadding(new Insets(10));
        VBox vb4 = new VBox(12);
        vb4.setAlignment(Pos.CENTER_LEFT);
        vb4.setPadding(new Insets(20));
        vb4.setPrefWidth(420);
        vb4.setMaxWidth(420);
        vb4.setStyle(
                "-fx-background-color: #e8f5e8;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #27ae60;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(39,174,96,0.2), 6, 0, 0, 2);"
        );

        Label l1 = new Label("Branch: Online Services");
        l1.setStyle(
                "-fx-text-fill: #27ae60;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        Label l2 = new Label("Location: Available Everywhere");
        l2.setStyle(
                "-fx-text-fill: #2d8a3e;" +
                        "-fx-font-size: 14px;"
        );

        Label l3 = new Label("Phone: 24/7 Digital Support");
        l3.setStyle(
                "-fx-text-fill: #2d8a3e;" +
                        "-fx-font-size: 14px;"
        );

        Button b = new Button("Select Online");
        b.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-pref-width: 160;"
        );

        b.setOnMouseEntered(e -> {
            if (!b.getText().contains("✓")) {
                b.setStyle(
                        "-fx-background-color: #219a52;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;" +
                                "-fx-padding: 10 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-pref-width: 160;"
                );
            }
        });

        b.setOnMouseExited(e -> {
            if (!b.getText().contains("✓")) {
                b.setStyle(
                        "-fx-background-color: #27ae60;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;" +
                                "-fx-padding: 10 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-pref-width: 160;"
                );
            }
        });

        b.setOnAction(e -> {
            b.setText("✓ Selected");
            b.setStyle(
                    "-fx-background-color: #1e8449;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 10 20;" +
                            "-fx-cursor: hand;" +
                            "-fx-pref-width: 160;"
            );
            mainStage.setMaximized(true);
            mainWindow.root.setCenter(new CustomerScene(mainWindow, mainStage,null,user.getpID()));
        });

        VBox vb5 = new VBox(8);
        vb5.getChildren().addAll(l1, l2, l3);

        HBox hb1 = new HBox();
        hb1.setAlignment(Pos.CENTER_RIGHT);
        hb1.getChildren().add(b);

        vb4.getChildren().addAll(vb5, hb1);
        vb3.getChildren().add(vb4);

        String query = "SELECT bID, bName, location, bPhoneNumber FROM Branch";

        try (Connection conn = MainApplication.dbConn.connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Branch branch = new Branch(
                        rs.getInt("bID"),
                        rs.getString("bName"),
                        rs.getString("location"),
                        rs.getString("bPhoneNumber")
                );

                VBox vb6 = new VBox(12);
                vb6.setAlignment(Pos.CENTER_LEFT);
                vb6.setPadding(new Insets(20));
                vb6.setPrefWidth(420);
                vb6.setMaxWidth(420);
                vb6.setStyle(
                        "-fx-background-color: #f8f9fa;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-color: #e9ecef;" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-radius: 10;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0, 0, 2);"
                );

                Label l4 = new Label("Branch: " + branch.getName());
                l4.setStyle(
                        "-fx-text-fill: #2c3e50;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;"
                );

                Label l5 = new Label("Location: " + branch.getLocation());
                l5.setStyle(
                        "-fx-text-fill: #34495e;" +
                                "-fx-font-size: 14px;"
                );

                Label l6 = new Label("Phone: " + branch.getPhoneNumber());
                l6.setStyle(
                        "-fx-text-fill: #34495e;" +
                                "-fx-font-size: 14px;"
                );

                Button b2 = new Button("Select This Branch");
                b2.setStyle(
                        "-fx-background-color: #3498db;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;" +
                                "-fx-padding: 10 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-pref-width: 160;"
                );
                b2.setOnMouseEntered(e -> {
                    if (!b2.getText().contains("✓")) {
                        b2.setStyle(
                                "-fx-background-color: #2980b9;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-size: 14px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-background-radius: 8;" +
                                        "-fx-padding: 10 20;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-pref-width: 160;"
                        );
                    }
                });

                b2.setOnMouseExited(e -> {
                    if (!b2.getText().contains("✓")) {
                        b2.setStyle(
                                "-fx-background-color: #3498db;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-size: 14px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-background-radius: 8;" +
                                        "-fx-padding: 10 20;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-pref-width: 160;"
                        );
                    }
                });

                b2.setOnAction(e -> {
                    b2.setText("✓ Selected");
                    b2.setStyle(
                            "-fx-background-color: #27ae60;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-size: 14px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-background-radius: 8;" +
                                    "-fx-padding: 10 20;" +
                                    "-fx-cursor: hand;" +
                                    "-fx-pref-width: 160;"
                    );
                    mainStage.setMaximized(true);
                    mainWindow.root.setCenter(new CustomerScene(mainWindow, mainStage,branch, user.getpID()));
                });

                VBox vb7 = new VBox(8);
                vb7.getChildren().addAll(l4, l5, l6);

                HBox buttonSection = new HBox();
                buttonSection.setAlignment(Pos.CENTER_RIGHT);
                buttonSection.getChildren().add(b2);

                vb6.getChildren().addAll(vb7, buttonSection);
                vb3.getChildren().add(vb6);
            }

            sp.setContent(vb3);
            vb2.getChildren().addAll(t, sp);
            vb1.getChildren().add(vb2);

            return vb1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}