package com.cj.dynamicjson;

import java.math.BigDecimal;
import java.util.*;

public class AbstractSyntaxTree {
    public interface JsonAst {
        default String aString() {
            throw new RuntimeException(String.format("Can not convert %s to a String", this));
        }

        default Optional<String> oString() {
            return Optional.ofNullable(aString());
        }

        default BigDecimal aBigDecimal() {
            throw new RuntimeException(String.format("Can not convert %s to a BigDecimal", this));
        }

        default Optional<BigDecimal> oBigDecimal() {
            return Optional.ofNullable(aBigDecimal());
        }

        default Boolean aBoolean() {
            throw new RuntimeException(String.format("Can not convert %s to a boolean", this));
        }

        default Optional<Boolean> oBoolean() {
            return Optional.ofNullable(aBoolean());
        }

        default boolean isNull() {
            throw new RuntimeException(String.format("Can not convert %s to a null", this));
        }

        default List<JsonAst> list() {
            throw new RuntimeException(String.format("Can not convert %s to an array", this));
        }

        default Map<String, JsonAst> map() {
            throw new RuntimeException(String.format("Can not convert %s to an object", this));
        }
    }

    public static class JsonString implements JsonAst {
        public final String value;

        public JsonString(String value) {
            this.value = value;
        }

        @Override
        public String aString() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("String(%s)", value);
        }
    }

    public static class JsonNumber implements JsonAst {
        public final BigDecimal value;

        public JsonNumber(BigDecimal value) {
            this.value = value;
        }

        @Override
        public BigDecimal aBigDecimal() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("Number(%s)", value);
        }
    }

    public static class JsonBoolean implements JsonAst {
        public final boolean value;

        public JsonBoolean(boolean value) {
            this.value = value;
        }

        @Override
        public Boolean aBoolean() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("Boolean(%s)", value);
        }
    }

    public static class JsonNull implements JsonAst {
        public static final JsonNull instance = new JsonNull();

        private JsonNull() {
        }

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public String aString() {
            return null;
        }

        @Override
        public BigDecimal aBigDecimal() {
            return null;
        }

        @Override
        public Boolean aBoolean() {
            return null;
        }

        @Override
        public List<JsonAst> list() {
            return Collections.emptyList();
        }

        @Override
        public Map<String, JsonAst> map() {
            return Collections.emptyMap();
        }

        @Override
        public String toString() {
            return "null";
        }
    }

    public static class JsonArray implements JsonAst {
        private final List<JsonAst> array;

        public JsonArray(List<JsonAst> array) {
            this.array = Collections.unmodifiableList(new ArrayList<>(array));
        }

        @Override
        public List<JsonAst> list() {
            return array;
        }

        @Override
        public String toString() {
            String contents = array.stream().map(Object::toString).reduce((left, right) -> left + ", " + right).orElse("");
            return String.format("Array(%s)", contents);
        }
    }

    public static class JsonObject implements JsonAst {
        private final Map<String, JsonAst> object;

        public JsonObject(Map<String, JsonAst> object) {
            this.object = Collections.unmodifiableMap(new HashMap<>(object));
        }

        @Override
        public Map<String, JsonAst> map() {
            return object;
        }

        @Override
        public String toString() {
            //Sort the keys to make toString deterministic
            List<String> sortedKeys = new ArrayList<>();
            sortedKeys.addAll(object.keySet());
            Collections.sort(sortedKeys);

            String contents = sortedKeys.stream().map(key -> {
                JsonAst value = object.get(key);
                return String.format("%s -> %s", key, value);
            }).reduce((left, right) -> left + ", " + right).orElse("");

            return String.format("Object(%s)", contents);
        }
    }
}
