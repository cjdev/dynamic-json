package com.cj.dynamicjson;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            try {
                return Optional.ofNullable(aBigDecimal());
            }catch (NumberFormatException e){
                return Optional.empty();
            }

        }

        default Boolean aBoolean() {
            throw new RuntimeException(String.format("Can not convert %s to a boolean", this));
        }

        default Optional<Boolean> oBoolean() {
            return Optional.ofNullable(aBoolean());
        }

        default boolean isNull() {
            return false;
        }

        /**
         * This may be deprecated soon.  Consider using either listOf() or stream() instead 
         * because you'll probably find that it makes your code less verbose.
         * @return
         */
        default List<JsonAst> list() {
            throw new RuntimeException(String.format("Can not convert %s to an array", this));
        }
        
        /**
         * @return
         * @deprecated This may become a private method soon because it is not useful.  Use object() instead.
         */
        @Deprecated
        default Map<String, JsonAst> map() {
            throw new RuntimeException(String.format("Can not convert %s to an object", this));
        }

        default JsonObject object(){
            return new JsonObject(map());
        }
        
        /* ***********Convenience Methods For Accessing Primitives ***************************** */
        default Optional<Integer> oInteger() {return oBigDecimal().map(BigDecimal::intValue);}
        default Optional<Long> oLong() {return oBigDecimal().map(BigDecimal::longValue);}
        default Optional<Double> oDouble() {return oBigDecimal().map(BigDecimal::doubleValue);}
        default Optional<Float> oFloat() {return oBigDecimal().map(BigDecimal::floatValue);}
        default <T> List<T> listOf(Function<JsonAst,T> mapper){
            return stream().map(mapper).collect(Collectors.toList());
        }
        default Stream<JsonAst> stream(){return list().stream();}
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

        @Override
        public BigDecimal aBigDecimal() {
            return new BigDecimal(value);
        }

        @Override
        public Boolean aBoolean() {
            return Boolean.parseBoolean(value);
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

        @Override
        public String aString() {
            return value.toString();
        }

        @Override
        public Boolean aBoolean() {
            return !BigDecimal.ZERO.equals(value);
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

        @Override
        public String aString() {
            return Boolean.valueOf(value).toString();
        }

        @Override
        public BigDecimal aBigDecimal() {
            if(value) {
                return BigDecimal.ONE;
            } else {
                return BigDecimal.ZERO;
            }
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

        @Override @Deprecated
        public Map<String, JsonAst> map() {
            return object;
        }
        
        public JsonAst get(String key){
            return oGet(key).orElse(JsonNull.instance);
        }
        
        public Set<String> keys(){
            return object.keySet();
        }

        public Optional<JsonAst> oGet(String key){
            return Optional.ofNullable(map().get(key));
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
