import java.util.HashMap;
import java.util.Map;

public class Spreadsheet {
    private Map<String, Cell> cells;

    public Spreadsheet() {
        cells = new HashMap<>();
    }

    public void setCellValue(String cellId, Object value) {
        if (!cells.containsKey(cellId)) {
            cells.put(cellId, new Cell(value));
        } else {
            cells.get(cellId).setValue(value);
        }
    }

    public int getCellValue(String cellId) {
        if (!cells.containsKey(cellId)) {
            return 0; // Default value if cell doesn't exist
        }
        return cells.get(cellId).evaluate(this);
    }

    private static class Cell {
        private Object value;

        public Cell(Object value) {
            this.value = value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public int evaluate(Spreadsheet spreadsheet) {
            if (value instanceof String && ((String) value).startsWith("=")) {
                String expression = (String) value;
                String[] tokens = expression.substring(1).split("\\+");
                int sum = 0;
                for (String token : tokens) {
                    if (token.matches("[A-Z]\\d+")) {
                        sum += spreadsheet.getCellValue(token);
                    } else {
                        try {
                            sum += Integer.parseInt(token);
                        } catch (NumberFormatException e) {
                            // Invalid expression, return 0
                            return 0;
                        }
                    }
                }
                return sum;
            } else if (value instanceof Integer) {
                return (Integer) value;
            } else {
                return 0; // Default value if not recognized
            }
        }
    }

    public static void main(String[] args) {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellValue("A1", 13);
        spreadsheet.setCellValue("A2", 14);
        System.out.println("A1: " + spreadsheet.getCellValue("A1")); // 13
        spreadsheet.setCellValue("A3", "=A1+A2");
        System.out.println("A3: " + spreadsheet.getCellValue("A3")); // 27
        spreadsheet.setCellValue("A4", "=A1+A2+A3");
        System.out.println("A4: " + spreadsheet.getCellValue("A4")); // 54
    }
}
