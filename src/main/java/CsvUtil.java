final class CsvUtil {
    private CsvUtil() {}


    
    static String[] parseLine(String line) {
        if (line == null) throw new IllegalArgumentException("CSV line cannot be null");

        String[] fields = new String[Math.max(1, line.length() + 1)];
        int fieldCount = 0;
        StringBuilder field = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (c == ',' && !quoted) {
                fields[fieldCount++] = field.toString().trim();
                field.setLength(0);
            } else {
                field.append(c);
            }
        }

        if (quoted) {
            throw new IllegalArgumentException("Unclosed quoted CSV field");
        }

        fields[fieldCount++] = field.toString().trim();

        String[] result = new String[fieldCount];
        System.arraycopy(fields, 0, result, 0, fieldCount);
        return result;
    }

    static String quote(String value) {
        if (value == null) return "";
        if (value.indexOf(',') >= 0 || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
