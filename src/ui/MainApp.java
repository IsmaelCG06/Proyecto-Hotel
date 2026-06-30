package ui;

import administrador.Administrador;
import hotel.Hotel;
import cliente.Cliente;
import cliente.Empresario;
import cliente.Natural;
import factura.Factura;
import habitacion.Doble;
import habitacion.Habitacion;
import habitacion.Matrimonial;
import habitacion.Sencilla;
import reserva.Reserva;
import reserva.Servicio;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.control.TextFormatter;

/**
 * Aplicación JavaFX del sistema de gestión hotelera.
 */
public class MainApp extends Application {

    private Hotel hotel;
    private Administrador administradorActivo;
    private StackPane contentPane;
    private Button btnHabitaciones;
    private Button btnClientes;
    private Button btnReservas;
    private Button btnServicios;
    private Button btnFacturas;

    private TableView<Habitacion> tablaHabitaciones;
    private TableView<Cliente> tablaClientes;
    private TableView<Reserva> tablaReservas;
    private TableView<Servicio> tablaServicios;
    private ImageView previewImagen;
    private HBox panelMiniaturas;
    private Label lblDetalleHabitacion;
    private StackPane contenedorImagen;
    private String imagenActual;

    @Override
    public void start(Stage stage) {
        PanelLogin.mostrar(stage, admin -> iniciarSistema(stage, admin));
    }

    private void iniciarSistema(Stage stage, Administrador administrador) {
        administradorActivo = administrador;
        hotel = Hotel.cargarDesdeArchivo();
        // Para cambiar el nombre SIN borrar datos
        // ejecuta la app una vez, verifica el encabezado, y vuelve a comentarlas
        // hotel.setNombre("Hotel Sol del Mar");
        // hotel.guardarEnArchivo();
        hotel.cargarDatosIniciales();

        BorderPane root = new BorderPane();
        root.setTop(crearHeader());
        root.setLeft(crearSidebar());
        contentPane = new StackPane();
        contentPane.getStyleClass().add("content-area");
        root.setCenter(contentPane);

        mostrarPanelHabitaciones();

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(
                new File("resources/styles/hotel.css").toURI().toString());
        stage.setTitle("Proyecto Hotel - Gestión");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> hotel.guardarEnArchivo());
        stage.show();
    }

    private VBox crearHeader() {
        VBox header = new VBox(4);
        header.getStyleClass().add("header-bar");
        Label titulo = new Label(hotel.getNombre());
        titulo.getStyleClass().add("header-title");
        Label subtitulo = new Label("Sistema de reservas y facturación · "
                + administradorActivo.getNombre() + " (" + administradorActivo.getId() + ")");
        subtitulo.getStyleClass().add("header-subtitle");
        header.getChildren().addAll(titulo, subtitulo);
        return header;
    }

    private VBox crearSidebar() {
        VBox sidebar = new VBox(8);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(200);

        btnHabitaciones = crearMenuBtn("🏨 Habitaciones", () -> mostrarPanelHabitaciones());
        btnClientes = crearMenuBtn("👤 Clientes", () -> mostrarPanelClientes());
        btnReservas = crearMenuBtn("📅 Reservas", () -> mostrarPanelReservas());
        btnServicios = crearMenuBtn("🍽 Servicios", () -> mostrarPanelServicios());
        btnFacturas = crearMenuBtn("🧾 Facturas", () -> mostrarPanelFacturas());

        sidebar.getChildren().addAll(btnHabitaciones, btnClientes, btnReservas,
                btnServicios, btnFacturas);
        return sidebar;
    }

    private Button crearMenuBtn(String texto, Runnable accion) {
        Button btn = new Button(texto);
        btn.getStyleClass().add("menu-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            marcarActivo(btn);
            accion.run();
        });
        return btn;
    }

    private void marcarActivo(Button activo) {
        for (Button b : new Button[]{btnHabitaciones, btnClientes, btnReservas,
                btnServicios, btnFacturas}) {
            b.getStyleClass().remove("menu-button-active");
            if (!b.getStyleClass().contains("menu-button")) {
                b.getStyleClass().add("menu-button");
            }
        }
        activo.getStyleClass().remove("menu-button");
        activo.getStyleClass().add("menu-button-active");
    }

    // --- Panel Habitaciones ---

    private void mostrarPanelHabitaciones() {
        Label titulo = new Label("Gestión de Habitaciones");
        titulo.getStyleClass().add("section-title");

        tablaHabitaciones = new TableView<>();
        TableColumn<Habitacion, Integer> colNum = new TableColumn<>("Núm.");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numHabitacion"));
        TableColumn<Habitacion, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getTipo()));
        TableColumn<Habitacion, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(
                c.getValue().getPrecioNoche()).asObject());
        TableColumn<Habitacion, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().estaDisponible() ? "Disponible" : "Ocupada"));
        TableColumn<Habitacion, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDescripcionBase()));
        TableColumn<Habitacion, String> colCaract = new TableColumn<>("Características");
        colCaract.setCellValueFactory(new PropertyValueFactory<>("caracteristica"));
        tablaHabitaciones.getColumns().addAll(colNum, colTipo, colPrecio, colEstado, colDesc, colCaract);
        tablaHabitaciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaHabitaciones.setPrefHeight(320);
        tablaHabitaciones.setMinHeight(320);
        tablaHabitaciones.setMaxHeight(320);
        HBox.setHgrow(tablaHabitaciones, Priority.ALWAYS);
        tablaHabitaciones.setMaxWidth(Double.MAX_VALUE);
        actualizarTablaHabitaciones();

        previewImagen = new ImageView();
        previewImagen.setPreserveRatio(true);
        previewImagen.getStyleClass().add("room-image");
        previewImagen.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && imagenActual != null) {
                abrirVentanaImagen(imagenActual);
            }
        });

        contenedorImagen = new StackPane(previewImagen);
        contenedorImagen.setAlignment(Pos.CENTER);
        contenedorImagen.getStyleClass().add("image-center-box");
        contenedorImagen.setMinHeight(220);
        contenedorImagen.setPrefHeight(220);
        VBox.setVgrow(contenedorImagen, Priority.ALWAYS);
        previewImagen.fitWidthProperty().bind(contenedorImagen.widthProperty().subtract(24));
        previewImagen.fitHeightProperty().bind(contenedorImagen.heightProperty().subtract(24));

        panelMiniaturas = new HBox(8);
        panelMiniaturas.setAlignment(Pos.CENTER);
        ScrollPane scrollMiniaturas = new ScrollPane(panelMiniaturas);
        scrollMiniaturas.setFitToHeight(true);
        scrollMiniaturas.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollMiniaturas.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollMiniaturas.setPrefHeight(58);
        scrollMiniaturas.setMinHeight(58);
        scrollMiniaturas.setStyle("-fx-background-color: transparent;");

        lblDetalleHabitacion = new Label("Selecciona una habitación para ver fotos y detalles.");
        lblDetalleHabitacion.getStyleClass().add("room-detail-label");
        lblDetalleHabitacion.setWrapText(true);
        lblDetalleHabitacion.setMaxHeight(72);

        VBox panelGaleria = new VBox(8, contenedorImagen, scrollMiniaturas, lblDetalleHabitacion);
        panelGaleria.getStyleClass().add("room-gallery");
        panelGaleria.setAlignment(Pos.TOP_CENTER);
        panelGaleria.setMinHeight(320);
        panelGaleria.setPrefHeight(320);
        panelGaleria.setMaxHeight(320);
        HBox.setHgrow(panelGaleria, Priority.ALWAYS);
        panelGaleria.setMaxWidth(Double.MAX_VALUE);
        lblDetalleHabitacion.maxWidthProperty().bind(panelGaleria.widthProperty().subtract(16));

        tablaHabitaciones.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> mostrarImagenHabitacion(sel));

        HBox filaSuperior = new HBox(16, tablaHabitaciones, panelGaleria);
        filaSuperior.setAlignment(Pos.TOP_CENTER);
        filaSuperior.setFillHeight(true);
        filaSuperior.setPrefHeight(320);
        filaSuperior.setMinHeight(320);

        GridPane form = crearFormularioHabitacion();
        form.setMaxWidth(Double.MAX_VALUE);

        HBox acciones = new HBox(10);
        Button btnAgregar = new Button("Agregar");
        btnAgregar.getStyleClass().add("btn-primary");
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.getStyleClass().add("btn-danger");
        Button btnFiltrar = new Button("Ver disponibles");
        btnFiltrar.getStyleClass().add("btn-secondary");
        Button btnOcupadas = new Button("Ver ocupadas");
        btnOcupadas.getStyleClass().add("btn-secondary");
        Button btnTodas = new Button("Ver todas");
        btnTodas.getStyleClass().add("btn-secondary");
        acciones.getChildren().addAll(btnAgregar, btnEliminar, btnFiltrar, btnOcupadas, btnTodas);
        acciones.setPadding(new Insets(4, 0, 16, 0));

        Label lblFiltroTipo = new Label("Filtrar por tipo:");
        lblFiltroTipo.getStyleClass().add("label-field");
        Button btnSencillas = new Button("Sencillas");
        btnSencillas.getStyleClass().add("btn-secondary");
        Button btnDobles = new Button("Dobles");
        btnDobles.getStyleClass().add("btn-secondary");
        Button btnMatrimoniales = new Button("Matrimoniales");
        btnMatrimoniales.getStyleClass().add("btn-secondary");
        HBox filtroTipo = new HBox(8, lblFiltroTipo, btnSencillas, btnDobles, btnMatrimoniales);
        filtroTipo.setPadding(new Insets(0, 0, 8, 0));

        btnAgregar.setOnAction(e -> agregarHabitacion(form));
        btnEliminar.setOnAction(e -> eliminarHabitacionSeleccionada());
        btnFiltrar.setOnAction(e -> filtrarDisponibles());
        btnOcupadas.setOnAction(e -> filtrarOcupadas());
        btnTodas.setOnAction(e -> actualizarTablaHabitaciones());
        btnSencillas.setOnAction(e -> tablaHabitaciones.setItems(
                FXCollections.observableArrayList(hotel.listarHabitacionesPorTipo("Sencilla"))));
        btnDobles.setOnAction(e -> tablaHabitaciones.setItems(
                FXCollections.observableArrayList(hotel.listarHabitacionesPorTipo("Doble"))));
        btnMatrimoniales.setOnAction(e -> tablaHabitaciones.setItems(
                FXCollections.observableArrayList(hotel.listarHabitacionesPorTipo("Matrimonial"))));

        VBox panelCompleto = new VBox(16, titulo, filaSuperior, form, filtroTipo, acciones);
        panelCompleto.setFillWidth(true);
        contentPane.getChildren().setAll(envolverConScroll(panelCompleto));
    }

    private GridPane crearFormularioHabitacion() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("card");
        grid.setHgap(14);
        grid.setVgap(10);
        grid.setPadding(new Insets(14));
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints colLabel1 = new ColumnConstraints(110);
        ColumnConstraints colField1 = new ColumnConstraints();
        colField1.setHgrow(Priority.ALWAYS);
        colField1.setMinWidth(120);
        ColumnConstraints colLabel2 = new ColumnConstraints(120);
        ColumnConstraints colField2 = new ColumnConstraints();
        colField2.setHgrow(Priority.ALWAYS);
        colField2.setMinWidth(120);
        grid.getColumnConstraints().addAll(colLabel1, colField1, colLabel2, colField2);

        ComboBox<String> cboTipo = new ComboBox<>(FXCollections.observableArrayList(
                "Sencilla", "Doble", "Matrimonial"));
        cboTipo.setValue("Sencilla");
        cboTipo.setMaxWidth(Double.MAX_VALUE);
        TextField txtNum = new TextField();
        TextField txtPrecio = new TextField();
        TextField txtCaract = new TextField();
        txtCaract.setMaxWidth(Double.MAX_VALUE);
        Spinner<Integer> spnCamas = new Spinner<>(1, 4, 1);
        CheckBox chkBanio = new CheckBox("Baño privado");
        CheckBox chkVistaMar = new CheckBox("Vista al mar");
        CheckBox chkJacuzzi = new CheckBox("Jacuzzi");
        for (CheckBox cb : new CheckBox[]{chkBanio, chkVistaMar, chkJacuzzi}) {
            cb.setMinWidth(Region.USE_PREF_SIZE);
            cb.getStyleClass().add("form-checkbox");
        }

        Label lblOpciones = new Label("Opciones:");
        lblOpciones.getStyleClass().add("label-field");
        HBox opcionesChk = new HBox(16, chkBanio, chkVistaMar, chkJacuzzi);
        opcionesChk.setAlignment(Pos.CENTER_LEFT);
        GridPane.setHgrow(opcionesChk, Priority.ALWAYS);

        cboTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
            switch (newVal) {
                case "Sencilla" -> {
                    chkVistaMar.setDisable(true);
                    chkVistaMar.setSelected(false);
                    chkJacuzzi.setDisable(true);
                    chkJacuzzi.setSelected(false);
                    chkBanio.setDisable(false);
                }
                case "Doble" -> {
                    chkVistaMar.setDisable(false);
                    chkJacuzzi.setDisable(true);
                    chkJacuzzi.setSelected(false);
                    chkBanio.setDisable(true);
                    chkBanio.setSelected(false);
                }
                case "Matrimonial" -> {
                    chkVistaMar.setDisable(false);
                    chkJacuzzi.setDisable(false);
                    chkBanio.setDisable(true);
                    chkBanio.setSelected(false);
                }
            }
        });
        cboTipo.getSelectionModel().select(0);

        VBox boxImagenes = new VBox(4);
        boxImagenes.setFillWidth(true);
        ArrayList<CheckBox> chkImagenes = new ArrayList<>();
        for (String img : listarImagenesDisponibles()) {
            CheckBox cb = new CheckBox(img);
            cb.setMinWidth(Region.USE_PREF_SIZE);
            cb.setMaxWidth(Double.MAX_VALUE);
            cb.getStyleClass().add("form-checkbox");
            chkImagenes.add(cb);
            boxImagenes.getChildren().add(cb);
        }
        if (!chkImagenes.isEmpty()) {
            chkImagenes.get(0).setSelected(true);
        }
        ScrollPane scrollImagenes = new ScrollPane(boxImagenes);
        scrollImagenes.setFitToWidth(true);
        scrollImagenes.setPrefHeight(120);
        scrollImagenes.setMinHeight(120);
        scrollImagenes.setStyle("-fx-background-color: white;");
        GridPane.setHgrow(scrollImagenes, Priority.ALWAYS);
        scrollImagenes.setMaxWidth(Double.MAX_VALUE);

        grid.add(new Label("Tipo:"), 0, 0);
        grid.add(cboTipo, 1, 0);
        grid.add(new Label("Número:"), 2, 0);
        grid.add(txtNum, 3, 0);
        grid.add(new Label("Características:"), 0, 1);
        grid.add(txtCaract, 1, 1, 3, 1);
        grid.add(new Label("Precio/noche:"), 0, 2);
        grid.add(txtPrecio, 1, 2);
        grid.add(new Label("Camas:"), 0, 3);
        grid.add(spnCamas, 1, 3);
        grid.add(lblOpciones, 0, 4);
        grid.add(opcionesChk, 1, 4, 3, 1);
        grid.add(new Label("Imágenes:"), 0, 5);
        grid.add(scrollImagenes, 1, 5, 3, 1);

        grid.setUserData(new Object[]{cboTipo, txtNum, txtPrecio, txtCaract,
                spnCamas, chkBanio, chkVistaMar, chkJacuzzi, chkImagenes});
        return grid;
    }

    private void agregarHabitacion(GridPane form) {
        Object[] campos = (Object[]) form.getUserData();
        try {
            String tipo = ((ComboBox<String>) campos[0]).getValue();
            int num = Integer.parseInt(((TextField) campos[1]).getText().trim());
            if (num <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "El número de la habitación debe ser mayor a cero.");
                return;
            }
            CheckBox chkBanio = (CheckBox) campos[5];
            CheckBox chkVistaMar = (CheckBox) campos[6];
            CheckBox chkJacuzzi = (CheckBox) campos[7];
            StringBuilder sbDesc = new StringBuilder();
            if (chkBanio.isSelected()) sbDesc.append("Baño privado, ");
            if (chkVistaMar.isSelected()) sbDesc.append("Vista al mar, ");
            if (chkJacuzzi.isSelected()) sbDesc.append("Jacuzzi, ");
            int camas = ((Spinner<Integer>) campos[4]).getValue();
            String desc = sbDesc.length() > 0 ? sbDesc.substring(0, sbDesc.length() - 2) : "";
            if (!desc.isEmpty()) desc += ", ";
            desc += camas + " cama(s)";
            double precio = Double.parseDouble(((TextField) campos[2]).getText().trim());
            if (precio <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "El precio por noche debe ser mayor que cero.");
                return;
            }
            String caract = ((TextField) campos[3]).getText().trim();
            ArrayList<CheckBox> chkImagenes = (ArrayList<CheckBox>) campos[8];
            ArrayList<String> imagenesSel = new ArrayList<>();
            for (CheckBox cb : chkImagenes) {
                if (cb.isSelected()) {
                    imagenesSel.add(cb.getText());
                }
            }
            String imagen = imagenesSel.isEmpty() ? "sin_imagen.jpg"
                    : String.join(",", imagenesSel);

            Habitacion hab;
            switch (tipo) {
                case "Doble" -> hab = new Doble(num, desc, precio, true, caract, imagen,
                        camas, chkVistaMar.isSelected());
                case "Matrimonial" -> hab = new Matrimonial(num, desc, precio, true, caract, imagen,
                        camas, chkVistaMar.isSelected(), chkJacuzzi.isSelected());
                default -> hab = new Sencilla(num, desc, precio, true, caract, imagen,
                        camas, chkBanio.isSelected());
            }

            if (hotel.registrarHabitacion(hab)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Habitación registrada.");
                actualizarTablaHabitaciones();
                limpiarFormularioHabitacion(form);
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Ya existe una habitación con ese número.");
            }
        } catch (Exception e) {
            //--Cursor IA: sin validar campos vacíos o parsear mal, la app crashea al agregar habitación
            mostrarAlerta(Alert.AlertType.ERROR, "Revisa los datos ingresados: " + e.getMessage());
        }
    }

    private void eliminarHabitacionSeleccionada() {
        Habitacion sel = tablaHabitaciones.getSelectionModel().getSelectedItem();
        if (sel != null && hotel.eliminarHabitacion(sel.getNumHabitacion())) {
            actualizarTablaHabitaciones();
        }
    }

    private void limpiarFormularioHabitacion(GridPane form) {
        Object[] campos = (Object[]) form.getUserData();
        ((ComboBox<String>) campos[0]).setValue("Sencilla");
        ((TextField) campos[1]).clear();
        ((TextField) campos[2]).clear();
        ((TextField) campos[3]).clear();
        ((Spinner<Integer>) campos[4]).getValueFactory().setValue(1);
        ((CheckBox) campos[5]).setSelected(false);
        ((CheckBox) campos[6]).setSelected(false);
        ((CheckBox) campos[7]).setSelected(false);
        ArrayList<CheckBox> chkImagenes = (ArrayList<CheckBox>) campos[8];
        for (CheckBox cb : chkImagenes) {
            cb.setSelected(false);
        }
        if (!chkImagenes.isEmpty()) {
            chkImagenes.get(0).setSelected(true);
        }
        previewImagen.setImage(null);
        panelMiniaturas.getChildren().clear();
        lblDetalleHabitacion.setText("Selecciona una habitación para ver fotos y detalles.");
    }

    private void filtrarDisponibles() {
        tablaHabitaciones.setItems(FXCollections.observableArrayList(
                hotel.listarHabitacionesDisponibles()));
    }

    private void filtrarOcupadas() {
        tablaHabitaciones.setItems(FXCollections.observableArrayList(
                hotel.listarHabitacionesOcupadas()));
    }

    private void actualizarTablaHabitaciones() {
        tablaHabitaciones.setItems(FXCollections.observableArrayList(
                hotel.listarTodasHabitaciones()));
    }

    private void mostrarImagenHabitacion(Habitacion hab) {
        panelMiniaturas.getChildren().clear();
        previewImagen.setImage(null);
        if (hab == null) {
            lblDetalleHabitacion.setText("Selecciona una habitación para ver fotos y detalles.");
            return;
        }
        lblDetalleHabitacion.setText(
                "Características: " + (hab.getCaracteristica().isEmpty() ? "—" : hab.getCaracteristica())
                + "\nFotos: " + (hab.getImagenesTexto().isEmpty() ? "sin imágenes" : hab.getImagenesTexto()));

        ArrayList<String> fotos = hab.getImagenes();
        if (fotos.isEmpty()) {
            return;
        }
        for (String nombre : fotos) {
            ImageView mini = crearMiniatura(nombre);
            panelMiniaturas.getChildren().add(mini);
        }
        cargarImagenGrande(fotos.get(0));
    }

    private ImageView crearMiniatura(String nombre) {
        ImageView mini = new ImageView();
        mini.setFitWidth(70);
        mini.setFitHeight(52);
        mini.setPreserveRatio(true);
        mini.getStyleClass().add("thumbnail");
        try {
            File img = new File("resources/images/" + nombre);
            if (img.exists()) {
                mini.setImage(new Image(img.toURI().toString(), 70, 52, true, true));
                mini.setOnMouseClicked(e -> cargarImagenGrande(nombre));
            }
        } catch (Exception e) {
            //--Cursor IA: miniaturas también pueden fallar si el archivo no existe
        }
        return mini;
    }

    private void cargarImagenGrande(String nombre) {
        imagenActual = nombre;
        try {
            File img = new File("resources/images/" + nombre);
            if (img.exists()) {
                previewImagen.setImage(new Image(img.toURI().toString(), true));
            } else {
                previewImagen.setImage(null);
            }
        } catch (Exception e) {
            previewImagen.setImage(null);
        }
    }

    private void abrirVentanaImagen(String nombre) {
        Stage ventana = new Stage();
        ventana.setTitle("Imagen - " + nombre);
        ImageView imgView = new ImageView();
        imgView.setPreserveRatio(true);
        try {
            File img = new File("resources/images/" + nombre);
            if (img.exists()) {
                Image imagen = new Image(img.toURI().toString(), true);
                imgView.setImage(imagen);
                ventana.setWidth(Math.min(imagen.getWidth() + 760, 1400));
                ventana.setHeight(Math.min(imagen.getHeight() + 800, 1000));
            }
        } catch (Exception e) {
            imgView.setImage(null);
        }
        StackPane root = new StackPane(imgView);
        root.setStyle("-fx-background-color: #1a1a1a;");
        Scene scene = new Scene(root);
        ventana.getIcons().add(previewImagen.getImage());
        ventana.setScene(scene);
        ventana.show();
    }

    // --- Panel Clientes ---

    private void mostrarPanelClientes() {
        VBox panel = new VBox(16);
        Label titulo = new Label("Gestión de Clientes");
        titulo.getStyleClass().add("section-title");

        tablaClientes = new TableView<>();
        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getNombre()));
        TableColumn<Cliente, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getClass().getSimpleName()));
        TableColumn<Cliente, String> colContacto = new TableColumn<>("Contacto");
        colContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        TableColumn<Cliente, Boolean> colHabitual = new TableColumn<>("Habitual");
        colHabitual.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(
                c.getValue().isEsHabitual()).asObject());
        TableColumn<Cliente, Double> colDesc = new TableColumn<>("Descuento %");
        colDesc.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(
                c.getValue().getDescuento()).asObject());
        tablaClientes.getColumns().addAll(colNombre, colTipo, colContacto, colHabitual, colDesc);
        VBox.setVgrow(tablaClientes, Priority.ALWAYS);
        actualizarTablaClientes();

        GridPane form = new GridPane();
        form.getStyleClass().add("card");
        form.setHgap(10);
        form.setVgap(8);
        ComboBox<String> cboTipo = new ComboBox<>(FXCollections.observableArrayList(
                "Natural", "Empresario"));
        TextField txtNombre = new TextField();
        TextField txtContacto = new TextField();
        TextField txtId = new TextField();
        
        txtId.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
            return change;
            }
            return null;
            }));
        
        TextField txtRazon = new TextField();
        txtRazon.setDisable(true);

        cboTipo.valueProperty().addListener((obs, oldValue, newValue) -> {
            boolean esEmpresario = "Empresario".equals(newValue);

            txtRazon.setDisable(!esEmpresario);

            if (!esEmpresario) {
                txtRazon.clear();
            }
        });
        
        
        CheckBox chkHabitual = new CheckBox("Cliente habitual");
        Spinner<Double> spnDesc = new Spinner<>(0.0, 50.0, 0.0, 5.0);
        spnDesc.setDisable(true);

        chkHabitual.selectedProperty().addListener((obs, oldVal, newVal) -> {
            spnDesc.setDisable(!newVal);
            if (!newVal) {
                spnDesc.getValueFactory().setValue(0.0);
            }
        });

        form.add(new Label("Tipo:"), 0, 0);
        form.add(cboTipo, 1, 0);
        form.add(new Label("Nombre:"), 0, 1);
        form.add(txtNombre, 1, 1);
        form.add(new Label("Contacto:"), 0, 2);
        form.add(txtContacto, 1, 2);
        form.add(new Label("Cédula/NIT:"), 0, 3);
        form.add(txtId, 1, 3);
        form.add(new Label("Razón social:"), 0, 4);
        form.add(txtRazon, 1, 4);
        form.add(chkHabitual, 0, 5);
        form.add(new Label("Descuento:"), 0, 6);
        form.add(spnDesc, 1, 6);

        Button btnAgregar = new Button("Registrar cliente");
        btnAgregar.getStyleClass().add("btn-primary");
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.getStyleClass().add("btn-danger");
        Button btnHabituales = new Button("Ver habituales");
        btnHabituales.getStyleClass().add("btn-accent");
        Button btnTodosClientes = new Button("Ver todos");
        btnTodosClientes.getStyleClass().add("btn-secondary");

        btnAgregar.setOnAction(e -> {
            try {

                if (cboTipo.getValue() == null) {
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "Debe seleccionar el tipo de cliente.");
                    return;
                }

                if (txtNombre.getText().trim().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "El nombre es obligatorio.");
                    return;
                }

                if (txtContacto.getText().trim().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "El contacto es obligatorio.");
                    return;
                }

                if (txtId.getText().trim().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "La cédula o NIT es obligatoria.");
                    return;
                }

                int identificacion = Integer.parseInt(txtId.getText());

                if (identificacion <= 0) {
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "La cédula o NIT debe ser mayor que cero.");
                    return;
                }

                Cliente c;

                if ("Empresario".equals(cboTipo.getValue())) {
                    c = new Empresario(
                            txtNombre.getText(),
                            "NIT",
                            txtContacto.getText(),
                            chkHabitual.isSelected(),
                            identificacion,
                            txtRazon.getText() // opcional
                    );
                } else {
                    c = new Natural(
                            txtNombre.getText(),
                            "Cédula",
                            txtContacto.getText(),
                            chkHabitual.isSelected(),
                            identificacion
                    );
                }

                if (chkHabitual.isSelected()) {
                    c.setDescuento(spnDesc.getValue());
                }

                String msg = hotel.registrarCliente(c);

                if (msg.startsWith("Ya existe")) {
                    mostrarAlerta(Alert.AlertType.WARNING, msg);
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, msg);
                    actualizarTablaClientes();

                    txtNombre.clear();
                    txtContacto.clear();
                    txtId.clear();
                    txtRazon.clear();
                    chkHabitual.setSelected(false);
                    spnDesc.getValueFactory().setValue(0.0);
                }

            } catch (Exception ex) {
                mostrarAlerta(Alert.AlertType.ERROR,
                        "Datos inválidos: " + ex.getMessage());
            }
        });
        btnEliminar.setOnAction(e -> {
            Cliente sel = tablaClientes.getSelectionModel().getSelectedItem();
            if (sel != null) {
                int idx = hotel.listarClientes().indexOf(sel);
                if (idx >= 0) {
                    hotel.eliminarCliente(idx);
                    actualizarTablaClientes();
                }
            }
        });
        btnHabituales.setOnAction(e -> tablaClientes.setItems(
                FXCollections.observableArrayList(hotel.listarClientesHabituales())));
        btnTodosClientes.setOnAction(e -> actualizarTablaClientes());

        panel.getChildren().addAll(titulo, tablaClientes, form,
                new HBox(10, btnAgregar, btnEliminar, btnHabituales, btnTodosClientes));
        contentPane.getChildren().setAll(panel);
    }

    private void actualizarTablaClientes() {
        tablaClientes.setItems(FXCollections.observableArrayList(hotel.listarClientes()));
    }

    // --- Panel Reservas ---

    private void mostrarPanelReservas() {
        VBox panel = new VBox(16);
        Label titulo = new Label("Gestión de Reservas");
        titulo.getStyleClass().add("section-title");

        tablaReservas = new TableView<>();
        TableColumn<Reserva, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        TableColumn<Reserva, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getCliente().getNombre()));
        TableColumn<Reserva, LocalDate> colFecha = new TableColumn<>("Inicio");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        TableColumn<Reserva, Integer> colDias = new TableColumn<>("Días");
        colDias.setCellValueFactory(new PropertyValueFactory<>("diasOcupacion"));
        TableColumn<Reserva, String> colHabitaciones = new TableColumn<>("Habitación(es)");
        colHabitaciones.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getHabitacionesDetalle()));
        colHabitaciones.setPrefWidth(200);
        tablaReservas.getColumns().addAll(colId, colCliente, colHabitaciones, colFecha, colDias);
        VBox.setVgrow(tablaReservas, Priority.ALWAYS);
        tablaReservas.setItems(FXCollections.observableArrayList(hotel.listarReservasActivas()));

        ComboBox<Cliente> cboCliente = new ComboBox<>(
                FXCollections.observableArrayList(hotel.listarClientes()));
        ComboBox<Habitacion> cboHabitacion = new ComboBox<>(
                FXCollections.observableArrayList(hotel.listarTodasHabitaciones()));
        DatePicker dpInicio = new DatePicker(LocalDate.now());
        Spinner<Integer> spnDias = new Spinner<>(1, 30, 1);

        GridPane form = new GridPane();
        form.getStyleClass().add("card");
        form.setHgap(10);
        form.setVgap(8);
        form.add(new Label("Cliente:"), 0, 0);
        form.add(cboCliente, 1, 0);
        form.add(new Label("Habitación:"), 0, 1);
        form.add(cboHabitacion, 1, 1);
        form.add(new Label("Fecha inicio:"), 0, 2);
        form.add(dpInicio, 1, 2);
        form.add(new Label("Días:"), 0, 3);
        form.add(spnDias, 1, 3);

        Button btnCrear = new Button("Crear reserva");
        btnCrear.getStyleClass().add("btn-primary");
        Button btnEliminarId = new Button("Eliminar por ID");
        btnEliminarId.getStyleClass().add("btn-danger");
        TextField txtIdEliminar = new TextField();
        txtIdEliminar.setPromptText("ID reserva");
        Button btnEliminarHab = new Button("Eliminar por hab.");
        btnEliminarHab.getStyleClass().add("btn-danger");
        TextField txtNumHab = new TextField();
        txtNumHab.setPromptText("N° habitación");

        btnCrear.setOnAction(e -> {
            if (cboCliente.getValue() == null || cboHabitacion.getValue() == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Selecciona cliente y habitación.");
                return;
            }
            ArrayList<Habitacion> habs = new ArrayList<>();
            habs.add(cboHabitacion.getValue());
            String msg = hotel.crearReserva(cboCliente.getValue(), habs,
                    dpInicio.getValue(), spnDias.getValue());
            mostrarAlerta(Alert.AlertType.INFORMATION, msg);
            tablaReservas.setItems(FXCollections.observableArrayList(hotel.listarReservasActivas()));
            if (tablaHabitaciones != null) {
                actualizarTablaHabitaciones();
            }
        });

        btnEliminarId.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtIdEliminar.getText().trim());
                hotel.eliminarReservaPorId(id);
                tablaReservas.setItems(FXCollections.observableArrayList(hotel.listarReservasActivas()));
                if (tablaHabitaciones != null) {
                    actualizarTablaHabitaciones();
                }
            } catch (Exception ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "ID inválido.");
            }
        });

        btnEliminarHab.setOnAction(e -> {
            try {
                int num = Integer.parseInt(txtNumHab.getText().trim());
                hotel.eliminarReservaPorHabitacion(num);
                tablaReservas.setItems(FXCollections.observableArrayList(hotel.listarReservasActivas()));
                if (tablaHabitaciones != null) {
                    actualizarTablaHabitaciones();
                }
            } catch (Exception ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Número inválido.");
            }
        });

        HBox eliminarBox = new HBox(10, txtIdEliminar, btnEliminarId, txtNumHab, btnEliminarHab);
        panel.getChildren().addAll(titulo, tablaReservas, form, btnCrear, eliminarBox);
        contentPane.getChildren().setAll(panel);
    }

    // --- Panel Servicios ---

    private void mostrarPanelServicios() {
        VBox panel = new VBox(16);
        Label titulo = new Label("Servicios del Hotel");
        titulo.getStyleClass().add("section-title");

        tablaServicios = new TableView<>();
        TableColumn<Servicio, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Servicio, String> colNom = new TableColumn<>("Nombre");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Servicio, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colDesc.setPrefWidth(220);
        TableColumn<Servicio, Double> colCosto = new TableColumn<>("Costo");
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
        tablaServicios.getColumns().addAll(colId, colNom, colDesc, colCosto);
        VBox.setVgrow(tablaServicios, Priority.ALWAYS);
        actualizarTablaServicios();

        TextField txtNom = new TextField();
        TextField txtDesc = new TextField();
        TextField txtCosto = new TextField();
        ComboBox<Reserva> cboReserva = new ComboBox<>(
                FXCollections.observableArrayList(hotel.listarReservasActivas()));
        ComboBox<Servicio> cboServicio = new ComboBox<>(
                FXCollections.observableArrayList(hotel.listarServicios()));

        GridPane form = new GridPane();
        form.getStyleClass().add("card");
        form.setHgap(10);
        form.setVgap(8);
        form.add(new Label("Nombre:"), 0, 0);
        form.add(txtNom, 1, 0);
        form.add(new Label("Descripción:"), 0, 1);
        form.add(txtDesc, 1, 1);
        form.add(new Label("Costo:"), 0, 2);
        form.add(txtCosto, 1, 2);

        Button btnRegistrar = new Button("Registrar servicio");
        btnRegistrar.getStyleClass().add("btn-primary");
        Button btnEliminarServ = new Button("Eliminar servicio");
        btnEliminarServ.getStyleClass().add("btn-danger");

        btnRegistrar.setOnAction(e -> {
            try {
                String msg = hotel.crearServicio(txtNom.getText(), txtDesc.getText(),
                        Double.parseDouble(txtCosto.getText()));
                if (msg.startsWith("Ya hay")) {
                    mostrarAlerta(Alert.AlertType.WARNING, msg);
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, msg);
                    actualizarTablaServicios();
                    cboServicio.setItems(FXCollections.observableArrayList(hotel.listarServicios()));
                    txtNom.clear();
                    txtDesc.clear();
                    txtCosto.clear();
                }
            } catch (Exception ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Datos inválidos.");
            }
        });

        btnEliminarServ.setOnAction(e -> {
            Servicio sel = tablaServicios.getSelectionModel().getSelectedItem();
            if (sel != null) {
                hotel.eliminarServicio(sel.getId());
                actualizarTablaServicios();
                cboServicio.setItems(FXCollections.observableArrayList(hotel.listarServicios()));
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Selecciona un servicio para eliminar.");
            }
        });

        Button btnCargar = new Button("Cargar a reserva");
        btnCargar.getStyleClass().add("btn-accent");
        btnCargar.setOnAction(e -> {
            if (cboReserva.getValue() != null && cboServicio.getValue() != null) {
                String msg = hotel.agregarServicioAReserva(
                        cboReserva.getValue().getIdReserva(),
                        cboServicio.getValue().getId());
                mostrarAlerta(Alert.AlertType.INFORMATION, msg);
            }
        });

        GridPane cargarForm = new GridPane();
        cargarForm.getStyleClass().add("card");
        cargarForm.setHgap(10);
        cargarForm.add(new Label("Reserva:"), 0, 0);
        cargarForm.add(cboReserva, 1, 0);
        cargarForm.add(new Label("Servicio:"), 0, 1);
        cargarForm.add(cboServicio, 1, 1);

        panel.getChildren().addAll(titulo, tablaServicios, form,
                new HBox(10, btnRegistrar, btnEliminarServ), cargarForm, btnCargar);
        contentPane.getChildren().setAll(panel);
    }

    private void actualizarTablaServicios() {
        tablaServicios.setItems(FXCollections.observableArrayList(hotel.listarServicios()));
    }

    // --- Panel Facturas ---

    private void mostrarPanelFacturas() {
        VBox panel = new VBox(16);
        Label titulo = new Label("Facturas Informativas");
        titulo.getStyleClass().add("section-title");

        TextArea areaFactura = new TextArea();
        areaFactura.setEditable(false);
        areaFactura.setPrefRowCount(20);
        areaFactura.getStyleClass().add("factura-area");
        VBox.setVgrow(areaFactura, Priority.ALWAYS);

        ComboBox<Reserva> cboReserva = new ComboBox<>(
                FXCollections.observableArrayList(hotel.listarReservasActivas()));
        Button btnGenerar = new Button("Generar factura");
        btnGenerar.getStyleClass().add("btn-primary");
        btnGenerar.setOnAction(e -> {
            if (cboReserva.getValue() != null) {
                Factura f = hotel.generarFactura(cboReserva.getValue().getIdReserva());
                if (f != null) {
                    areaFactura.setText(f.generarFactura());
                }
            }
        });

        panel.getChildren().addAll(titulo, cboReserva, btnGenerar, areaFactura);
        contentPane.getChildren().setAll(panel);
    }

    // --- Utilidades ---

    private ScrollPane envolverConScroll(javafx.scene.Node contenido) {
        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setPannable(true);
        scroll.getStyleClass().add("panel-scroll");
        return scroll;
    }

    private javafx.collections.ObservableList<String> listarImagenesDisponibles() {
        ArrayList<String> imagenes = new ArrayList<>();
        File carpeta = new File("resources/images");
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File f : archivos) {
                    String nombre = f.getName().toLowerCase();
                    if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")
                            || nombre.endsWith(".png") || nombre.endsWith(".gif")) {
                        imagenes.add(f.getName());
                    }
                }
            }
        }
        if (imagenes.isEmpty()) {
            imagenes.add("sin_imagen.jpg");
        }
        return FXCollections.observableArrayList(imagenes);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
