import java.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CalendarController {

    @FXML
    private ComboBox<Integer> yearComboBox;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private Label calendarTitle;

    @FXML
    private GridPane calendarGrid;

    private Map<String, String> appointments = new TreeMap<>();
    private int currentYear;
    private int currentMonth;
    private final  int MAX_WEEK = 6;
    private final  int TEN_YEARS = 10;
    private final String[] MONTH_NAMES = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private final String[] DAY_NAMES = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupDayHeaders();
        setupEventHandlers();

        Calendar today = Calendar.getInstance();
        currentYear = today.get(Calendar.YEAR);
        currentMonth = today.get(Calendar.MONTH);

        yearComboBox.setValue(currentYear);
        monthComboBox.setValue(MONTH_NAMES[currentMonth]);

        updateCalendarView();
    }

    private void setupComboBoxes() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer[] years = new Integer[TEN_YEARS*2+1];
        for (int i = 0; i < years.length; i++) {
            years[i] = currentYear - TEN_YEARS + i;
        }
        yearComboBox.setItems(FXCollections.observableArrayList(years));

        monthComboBox.setItems(FXCollections.observableArrayList(MONTH_NAMES));
    }

    private void setupDayHeaders() {
        for (int i = 0; i < DAY_NAMES.length; i++) {
            Label label = new Label(DAY_NAMES[i]);
            label.setFont(Font.font("System", FontWeight.BOLD, 14));
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setPadding(new Insets(5));
            calendarGrid.add(label, i, 0);
        }
    }

    private void setupEventHandlers() {
        yearComboBox.setOnAction(event -> {
            currentYear = yearComboBox.getValue();
            updateCalendarView();
        });

        monthComboBox.setOnAction(event -> {
            String selectedMonth = monthComboBox.getValue();
            for (int i = 0; i < MONTH_NAMES.length; i++) {
                if (MONTH_NAMES[i].equals(selectedMonth)) {
                    currentMonth = i;
                    break;
                }
            }
            updateCalendarView();
        });
    }

    private void updateCalendarView() {
        calendarGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        calendarTitle.setText(MONTH_NAMES[currentMonth] + " " + currentYear);

        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);

        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int row = 1;
        int col = firstDayOfMonth;

        for (int day = 1; day <= daysInMonth; day++) {
            Button dayButton = createDayButton(day);
            calendarGrid.add(dayButton, col, row);

            col++;

            if (col > MAX_WEEK) {
                col = 0;
                row++;
            }
        }
    }

    private Button createDayButton(int day) {
        Button button = new Button(String.valueOf(day));
        button.setPrefSize(80, 60);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHgrow(button, Priority.ALWAYS);
        GridPane.setVgrow(button, Priority.ALWAYS);
        String dateKey = createDateKey(currentYear, currentMonth, day);
        String appointmentsText = appointments.get(dateKey);

        if (appointmentsText != null && !appointmentsText.isEmpty()) {
            button.setStyle("-fx-background-color: #ff9999;");
        }

        button.setOnAction(event -> showAppointmentDialog(day));

        return button;
    }

    private void showAppointmentDialog(int day) {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Meetings");
        dialog.setHeaderText("Meetings on " + MONTH_NAMES[currentMonth] + " "  + day + ", " + currentYear);

        ButtonType saveButtonType = new ButtonType("save");
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextArea textArea = new TextArea();
        textArea.setPrefHeight(200);

        String dateKey = createDateKey(currentYear, currentMonth, day);
        String appointmentsText = appointments.get(dateKey);
        if (appointmentsText != null) {
            textArea.setText(appointmentsText);
        }

        VBox content = new VBox(10);
        content.getChildren().add(new Label("Please enter your appointments:"));
        content.getChildren().add(textArea);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(appointmentsText2 -> {
            if (appointmentsText2.trim().isEmpty()) {
                appointments.remove(dateKey);
            } else {
                appointments.put(dateKey, appointmentsText2);
            }

            updateCalendarView();
        });
    }

    private String createDateKey(int year, int month, int day) {
        return year + "-" + (month + 1) + "-" + day;
    }
}
