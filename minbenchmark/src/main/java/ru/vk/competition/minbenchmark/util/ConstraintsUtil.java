package ru.vk.competition.minbenchmark.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstraintsUtil {
    public static final String IDENTIFIER_PATTERN = "([^\\s,;]+)|(\"[^\"\u0000]+\")";
    public static final int TABLE_NAME_MAX_SIZE = 50;
    public static final int SQL_MAX_SIZE = 120;

    private static final Set<Pattern> NOT_ALLOWED_SQL_PATTERNS = new HashSet<>();

    static {
        NOT_ALLOWED_SQL_PATTERNS.add(
                Pattern.compile("(?i)(.*[;\\s])?DROP\\s+DATABASE\\s.*"));
        NOT_ALLOWED_SQL_PATTERNS.add(
                Pattern.compile("(?i)(.*[;\\s])?DROP\\s+SCHEMA\\s+(IF\\s+EXISTS\\s+)?APP_TABLES(\\s.*)?"));
        NOT_ALLOWED_SQL_PATTERNS.add
                (Pattern.compile("(?i)(.*[;\\s])?APP_TABLES\\..*"));
        NOT_ALLOWED_SQL_PATTERNS.add
                (Pattern.compile("(?i).*\\sDEFAULT_SCHEMA\\s*=\\s*APP_TABLES.*"));
    }

    private ConstraintsUtil() {
    }

    public static boolean isSqlSafety(String sql) {
        for (Pattern pattern : NOT_ALLOWED_SQL_PATTERNS) {
            Matcher matcher = pattern.matcher(sql);
            if (matcher.matches()) {
                return false;
            }
        }

        return true;
    }
}
