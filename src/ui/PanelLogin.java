package ui;

import administrador.Administrador;
import administrador.GestorAdministradores;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;

/**
 * Panel de inicio: solicita ID y contraseña del administrador.
 */
public class PanelLogin {

    private PanelLogin() {
    }

    public static void mostrar(Stage stage, LoginCallback alIngresar) {
        GestorAdministradores gestor = GestorAdministradores.cargar();
        // Para crear un admin nuevo: descomenta, ajusta ID/contraseña/nombre, ejecuta
        // la app una vez (no hace falta entrar), y vuelve a comentar.
        // gestor.registrarAdministrador(new Administrador("recepcion", "1048441197", "Ismael Recepcion"));

        Label titulo = new Label("Hotel Sol del Mar");
        titulo.getStyleClass().add("section-title");

        Label subtitulo = new Label("Acceso al sistema de gestión");
        subtitulo.getStyleClass().add("login-subtitle");

        TextField txtId = new TextField();
        txtId.setPromptText("ID de usuario");
        txtId.setMaxWidth(Double.MAX_VALUE);

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");
        txtContrasena.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints colLabel = new ColumnConstraints(100);
        ColumnConstraints colField = new ColumnConstraints();
        colField.setHgrow(Priority.ALWAYS);
        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setMaxWidth(Double.MAX_VALUE);
        form.getColumnConstraints().addAll(colLabel, colField);
        form.add(new Label("ID:"), 0, 0);
        form.add(txtId, 1, 0);
        form.add(new Label("Contraseña:"), 0, 1);
        form.add(txtContrasena, 1, 1);

        Button btnEntrar = new Button("Ingresar");
        btnEntrar.getStyleClass().add("btn-primary");
        btnEntrar.setDefaultButton(true);
        btnEntrar.setMaxWidth(Double.MAX_VALUE);

        Label ayuda = new Label("Primera vez: ID admin · contraseña admin123");
        ayuda.getStyleClass().add("login-hint");
        ayuda.setWrapText(true);

        btnEntrar.setOnAction(e -> intentarAcceso(
                gestor, txtId, txtContrasena, alIngresar));

        txtContrasena.setOnAction(e -> intentarAcceso(
                gestor, txtId, txtContrasena, alIngresar));

        VBox card = new VBox(14, titulo, subtitulo, form, btnEntrar, ayuda);
        card.getStyleClass().add("login-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(28));
        card.setMaxWidth(380);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("login-root");
        root.setPadding(new Insets(24));

        Scene scene = new Scene(root, 460, 360);
        scene.getStylesheets().add(
                new File("resources/styles/hotel.css").toURI().toString());
        stage.setScene(scene);
        stage.setTitle("Proyecto Hotel - Acceso");
        stage.show();
    }

    private static void intentarAcceso(GestorAdministradores gestor,
            TextField txtId, PasswordField txtContrasena, LoginCallback alIngresar) {
        String id = txtId.getText().trim();
        String contrasena = txtContrasena.getText();
        if (id.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Complete el ID y la contraseña.");
            return;
        }
        Administrador admin = gestor.buscarPorCredenciales(id, contrasena);
        if (admin != null) {
            alIngresar.onLogin(admin);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "ID o contraseña incorrectos.");
            txtContrasena.clear();
        }
    }

    private static void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.showAndWait();
    }

    public interface LoginCallback {
        void onLogin(Administrador administrador);
    }
}
