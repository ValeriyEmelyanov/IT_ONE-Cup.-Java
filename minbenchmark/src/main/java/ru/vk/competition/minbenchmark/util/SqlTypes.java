package ru.vk.competition.minbenchmark.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlTypes {
    private static final Map<String, Pattern> TYPES = new HashMap<>();
    private static final Pattern COMMON = Pattern.compile("[A-Z][ A-Z0-9(),.]+");

    private SqlTypes() {
    }

    static {
        Set<String> typesSet = typesSet();
        typesSet.forEach(s -> TYPES.put(s, s.contains("\\") ? Pattern.compile(s) : null));
    }

    private static Set<String> typesSet() {
        return Set.of(
                // strings
                "CHAR\\s*\\(\\s*\\d+\\s*\\)",
                "CHAR",
                "CHARACTER\\s*\\(\\s*\\d+\\s*\\)",
                "CHARACTER",
                "VARCHAR\\s*\\(\\s*\\d+\\s*\\)",
                "VARCHAR",
                "NVARCHAR\\s*\\(\\s*\\d+\\s*\\)",
                "NVARCHAR",
                "TINYTEXT",
                "TEXT",
                "BLOB",
                "TINYBLOB",
                "MEDIUMTEXT",
                "MEDIUMBLOB",
                "LONGTEXT",
                "LONGBLOB",
                "ENUM\\s*\\(\\s*'[^']+'(\\s*,?\\s*'[^']+')*?\\s*\\)",
                "SET",
                // integers
                "TINYINT\\s*\\(\\s*\\d+\\s*\\)",
                "TINYINT",
                "SMALLINT\\s*\\(\\s*\\d+\\s*\\)",
                "SMALLINT",
                "MEDIUMINT\\s*\\(\\s*\\d+\\s*\\)",
                "MEDIUMINT",
                "INT\\s*\\(\\s*\\d+\\s*\\)",
                "BIGINT",
                "B1GINT",
                "SERIAL",
                "BIGSERIAL",
                "INT",
                "INTEGER",
                "INT2",
                "INT4",
                "INT8",
                // float
                "FLOAT\\s*\\(\\s*\\d+,\\s*\\d+\\s*\\)",
                "FLOAT4",
                "REAL",
                "DOUBLE\\s*\\(\\s*\\d+,\\s*\\d+\\s*\\)",
                "DECIMAL\\s*\\(\\s*\\d+,\\s*\\d+\\s*\\)",
                "NUMERIC\\s*\\(\\s*\\d+,\\s*\\d+\\s*\\)",
                "MONEY",
                // date and time
                "DATE()",
                "DATE",
                "DATETIME()",
                "DATETIME",
                "TIMESTAMP()",
                "TIMESTAMP",
                "TIME()",
                "TIME",
                "YEAR()",
                "YEAR",
                //
                "BINARY\\s*\\(\\s*\\d+\\s*\\)",
                "BINARY",
                "BIT"
        );
    }

    public static boolean isValid(String type) {
        if (TYPES.containsKey(type)) {
            return true;
        }

        Matcher matcher = COMMON.matcher(type);
        if (!matcher.matches()) {
            return false;
        }

        for (Map.Entry<String, Pattern> entry : TYPES.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            Matcher m = entry.getValue().matcher(type);
            if (m.matches()) {
                return true;
            }
        }

        return false;
    }
}
