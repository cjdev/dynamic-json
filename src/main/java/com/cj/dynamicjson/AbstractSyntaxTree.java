package com.cj.dynamicjson;

import java.math.BigDecimal;
import java.util.*;

public class AbstractSyntaxTree {
    public abstract static class JsonAst {
        public String asString() {
            throw new RuntimeException(String.format("Can not convert %s to a String", this));
        }

        public BigDecimal asBigDecimal() {
            throw new RuntimeException(String.format("Can not convert %s to a BigDecimal", this));
        }

        public boolean asBoolean() {
            throw new RuntimeException(String.format("Can not convert %s to a boolean", this));
        }

        public boolean isNull() {
            throw new RuntimeException(String.format("Can not convert %s to a null", this));
        }

        public List<JsonAst> asList() {
            throw new RuntimeException(String.format("Can not convert %s to an array", this));
        }

        public Map<String, JsonAst> asMap() {
            throw new RuntimeException(String.format("Can not convert %s to an object", this));
        }
    }

    public static class JsonString extends JsonAst {
        public final String value;

        public JsonString(String value) {
            this.value = value;
        }

        @Override
        public String asString() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("String(%s)", value);
        }
    }

    public static class JsonNumber extends JsonAst {
        public final BigDecimal value;

        public JsonNumber(BigDecimal value) {
            this.value = value;
        }

        @Override
        public BigDecimal asBigDecimal() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("Number(%s)", value);
        }
    }

    public static class JsonBoolean extends JsonAst {
        public final boolean value;

        public JsonBoolean(boolean value) {
            this.value = value;
        }

        @Override
        public boolean asBoolean() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("Boolean(%s)", value);
        }
    }

    public static class JsonNull extends JsonAst {
        public static final JsonNull instance = new JsonNull();

        private JsonNull() {
        }

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public String toString() {
            return "null";
        }
    }

    public static class JsonArray extends JsonAst {
        private final List<JsonAst> array;

        public JsonArray(List<JsonAst> array) {
            this.array = Collections.unmodifiableList(new ArrayList<>(array));
        }

        @Override
        public List<JsonAst> asList() {
            return array;
        }

        @Override
        public String toString() {
            String contents = array.stream().map(Object::toString).reduce((left, right) -> left + ", " + right).orElse("");
            return String.format("Array(%s)", contents);
        }
    }

    public static class JsonObject extends JsonAst {
        private final Map<String, JsonAst> object;

        public JsonObject(Map<String, JsonAst> object) {
            this.object = Collections.unmodifiableMap(new HashMap<>(object));
        }

        @Override
        public Map<String, JsonAst> asMap() {
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
