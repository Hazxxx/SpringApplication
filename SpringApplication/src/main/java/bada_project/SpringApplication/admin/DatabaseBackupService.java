package bada_project.SpringApplication.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseBackupService {

    private final JdbcTemplate jdbc;

    public DatabaseBackupService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public byte[] createBackup() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8);

        writer.println("-- Database Backup");
        writer.println("-- Generated: " + java.time.LocalDateTime.now());
        writer.println("-- Alpha&Omega Motors - Car Salon Management System");
        writer.println();

        // List of tables to backup (in order of dependencies - parent tables first)
        String[] tables = {
                "SYSTEM_FLAGS",
                "MARKI",
                "MODELE",
                "ADRESY",
                "FIRMY_PARTNERSKIE",
                "SALONY_SAMOCHODOWE",
                "STANOWISKA",
                "PRACOWNICY",
                "KLIENCI",
                "POJAZDY",
                "OFERTY",
                "SPRZEDAWCY",
                "SPRZEDAZE",
                "DETALY_SPRZEDAZY",
                "WYNAGRODZENIA",
                "ZLECENIA_SERWISOWE",
                "MECHANICY",
                "WARSZTATY"
        };

        for (String table : tables) {
            try {
                backupTable(table, writer);
            } catch (Exception e) {
                writer.println("-- Error backing up table " + table + ": " + e.getMessage());
                writer.println();
            }
        }

        writer.println("-- Backup completed successfully");
        writer.flush();
        return baos.toByteArray();
    }

    private void backupTable(String tableName, PrintWriter writer) {
        writer.println("-- ========================================");
        writer.println("-- Table: " + tableName);
        writer.println("-- ========================================");

        // Get all rows from the table
        List<Map<String, Object>> rows;
        try {
            rows = jdbc.queryForList("SELECT * FROM " + tableName);
        } catch (Exception e) {
            writer.println("-- Table " + tableName + " does not exist or cannot be accessed");
            writer.println();
            return;
        }

        if (rows.isEmpty()) {
            writer.println("-- No data in " + tableName);
            writer.println();
            return;
        }

        writer.println("-- Records: " + rows.size());
        writer.println();

        // Get column names from first row
        Map<String, Object> firstRow = rows.get(0);
        String columns = String.join(", ", firstRow.keySet());

        for (Map<String, Object> row : rows) {
            StringBuilder values = new StringBuilder();
            int i = 0;
            for (Object value : row.values()) {
                if (i > 0) values.append(", ");

                if (value == null) {
                    values.append("NULL");
                } else if (value instanceof Number) {
                    values.append(value);
                } else if (value instanceof java.sql.Date) {
                    values.append("TO_DATE('").append(value.toString()).append("', 'YYYY-MM-DD')");
                } else if (value instanceof java.sql.Timestamp) {
                    values.append("TO_TIMESTAMP('").append(value.toString()).append("', 'YYYY-MM-DD HH24:MI:SS.FF')");
                } else if (value instanceof Boolean) {
                    values.append(((Boolean) value) ? "1" : "0");
                } else {
                    // Escape single quotes for string values
                    String strValue = value.toString().replace("'", "''");
                    values.append("'").append(strValue).append("'");
                }
                i++;
            }

            writer.println("INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ");");
        }

        writer.println();
    }

    @Transactional
    public void clearDatabase() {
        // Delete in reverse order of dependencies to avoid foreign key violations
        // Child tables first, parent tables last
        String[] tables = {
                "WARSZTATY",
                "MECHANICY",
                "ZLECENIA_SERWISOWE",
                "WYNAGRODZENIA",
                "DETALY_SPRZEDAZY",
                "SPRZEDAZE",
                "SPRZEDAWCY",
                "OFERTY",
                "POJAZDY",
                "KLIENCI",
                "PRACOWNICY",
                "STANOWISKA",
                "SALONY_SAMOCHODOWE",
                "FIRMY_PARTNERSKIE",
                "ADRESY",
                "MODELE",
                "MARKI"
                // Note: SYSTEM_FLAGS is intentionally not cleared to preserve system settings
        };

        int totalDeleted = 0;

        for (String table : tables) {
            try {
                int deleted = jdbc.update("DELETE FROM " + table);
                totalDeleted += deleted;
                System.out.println("Cleared " + deleted + " records from " + table);
            } catch (Exception e) {
                // Log error but continue with other tables
                System.err.println("Failed to clear table " + table + ": " + e.getMessage());
                throw new RuntimeException("Failed to clear table " + table + ": " + e.getMessage(), e);
            }
        }

        System.out.println("Total records deleted: " + totalDeleted);
    }
}