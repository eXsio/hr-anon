package com.exsio.hranon;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

class HrStringAnonymizer {

    private static final String SEED_SOURCE = "str_seed_src.txt";

    private static final Map<Integer, List<String>> SEED = new HashMap<>();

    static {
        try {
            init();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String anonymize(String source) {
        if (source == null) {
            return null;
        }
        var tokens = Jsoup.parse(source).text().split("[^a-zA-Z0-9']+");
        for (var token : tokens) {
            var replacement = copyCase(token, findReplacement(token));
            source = source.replaceFirst("\\b" + Pattern.quote(token) + "\\b", replacement);
        }
        return source;
    }

    private static String copyCase(String token, String replacement) {
        if (token.length() != replacement.length()) {
            return replacement;
        }
        var sb = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
            if (Character.isUpperCase(token.charAt(i))) {
                sb.append(Character.toUpperCase(replacement.charAt(i)));
            } else {
                sb.append(replacement.charAt(i));
            }
        }
        return sb.toString();
    }

    private static String findReplacement(String token) {
        if (token.length() == 0) {
            return token;
        }
        try {
            Integer.parseInt(token);
            return HrNumberAnonymizer.anonymizeNumber(token);
        } catch (NumberFormatException e) {
            if (!SEED.containsKey(token.length())) {
                return combineReplacement(token);
            }
            var set = SEED.get(token.length());
            int randomIndex = ThreadLocalRandom.current().nextInt(set.size());
            return SEED.get(token.length()).get(randomIndex);
        }
    }

    private static String combineReplacement(String token) {
        var maxLength = Collections.max(SEED.keySet());
        var rest = token.substring(maxLength - 1, token.length() - 1);
        var randomIndex = ThreadLocalRandom.current().nextInt(SEED.get(maxLength).size());
        return SEED.get(maxLength).get(randomIndex) + findReplacement(rest);

    }

    private static void init() throws URISyntaxException, IOException {
        if (!SEED.isEmpty()) {
            return;
        }
        var seed = readSeedFile();
        var map = new HashMap<Integer, Set<String>>();
        for (var word : seed.split("\\s+")) {
            if (!map.containsKey(word.length())) {
                map.put(word.length(), new LinkedHashSet<>());
            }
            map.get(word.length()).add(word);
        }
        map.forEach((len, tokens) -> SEED.put(len, new ArrayList<>(tokens)));
    }

    private static String readSeedFile() throws URISyntaxException, IOException {
        var resource = HrStringAnonymizer.class.getClassLoader().getResource(SEED_SOURCE);
        if (resource == null) {
            throw new RuntimeException("Unable to find String Seed Source File");
        }
        var reader = new BufferedReader(new FileReader(new File(resource.toURI())));
        var stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(" ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        return stringBuilder.toString().toLowerCase();
    }


}
