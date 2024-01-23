package com.daineka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HashMapTest {

    private HashMap<String, Integer> hashMap;

    @BeforeEach
    void setUp() {
        hashMap = new HashMap<>();
    }


    @org.junit.jupiter.api.Test
    void get() {

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
            // Проверяем get для каждого ключа
            assertEquals(value, hashMap.get(key));
        }
    }

    @org.junit.jupiter.api.Test
    void put() {

        // Генерируем случайные ключи
        Random random = new Random();
        int dataSize = 1000000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            // Выполняем put для каждого ключа
            hashMap.put(key, value);
        }

        // Проверяем, что размер HashMap соответствует ожидаемому
        assertEquals(dataSize, hashMap.size());
    }

    // Метод для генерации случайной строки
    private String generateRandomString(int length) {
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    @Test
    void size() {

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
        }

        // Проверяем, что размер HashMap соответствует ожидаемому
        assertEquals(dataSize, hashMap.size());
    }

    @Test
    void isEmpty() {
        // Создаем пустую HashMap
        HashMap<String, Integer> emptyHashMap = new HashMap<>();

        // Проверяем, что isEmpty возвращает true для пустой HashMap
        assertTrue(emptyHashMap.isEmpty());

        // Создаем непустую HashMap
        HashMap<String, Integer> nonEmptyHashMap = new HashMap<>();
        nonEmptyHashMap.put("Key", 42);

        // Проверяем, что isEmpty возвращает false для непустой HashMap
        assertFalse(nonEmptyHashMap.isEmpty());
    }

    @Test
    void containsKey() {

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
        }

        // Выбираем случайный ключ из HashMap
        String randomKey = getRandomKey(hashMap);

        // Проверяем, что containsKey возвращает true для существующего ключа
        assertTrue(hashMap.containsKey(randomKey));

        // Создаем ключ, которого точно нет в HashMap
        String nonExistentKey = generateRandomString(10);

        // Проверяем, что containsKey возвращает false для отсутствующего ключа
        assertFalse(hashMap.containsKey(nonExistentKey));
    }

    private <K, V> K getRandomKey(HashMap<K, V> map) {
        int size = map.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for (K key : map.keySet()) {
            if (i == item) {
                return key;
            }
            i++;
        }
        throw new IllegalArgumentException("Map is empty");
    }

    @Test
    void containsValue() {

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
        }

        // Выбираем случайное значение из HashMap
        int randomValue = getRandomValue(hashMap);

        // Проверяем, что containsValue возвращает true для существующего значения
        assertTrue(hashMap.containsValue(randomValue));

        // Создаем значение, которого точно нет в HashMap
        int nonExistentValue = -1;

        // Проверяем, что containsValue возвращает false для отсутствующего значения
        assertFalse(hashMap.containsValue(nonExistentValue));
    }

    // Метод для получения случайного значения из Map
    private <K, V> V getRandomValue(HashMap<K, V> map) {
        int size = map.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for (V value : map.values()) {
            if (i == item) {
                return value;
            }
            i++;
        }
        throw new IllegalArgumentException("Map is empty");
    }

    @Test
    void remove() {

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
        }

        // Выбираем случайный ключ из HashMap
        String randomKey = getRandomKey(hashMap);

        // Проверяем, что ключ присутствует в HashMap перед вызовом remove
        assertTrue(hashMap.containsKey(randomKey));

        // Выполняем remove для выбранного ключа
        Integer removedValue = hashMap.remove(randomKey);

        // Проверяем, что remove вернул корректное значение
        assertNotNull(removedValue);

        // Проверяем, что ключ больше не присутствует в HashMap после вызова remove
        assertFalse(hashMap.containsKey(randomKey));
    }

    @Test
    void clear() {

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
        }

        // Проверяем, что HashMap не пустая перед вызовом clear
        assertFalse(hashMap.isEmpty());

        // Выполняем clear
        hashMap.clear();

        // Проверяем, что HashMap стала пустой после вызова clear
        assertTrue(hashMap.isEmpty());
    }

    @Test
    void keySet() {

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
        }

        // Получаем keySet
        Set<String> keySet = hashMap.keySet();

        // Проверяем, что размер keySet равен размеру HashMap
        assertEquals(dataSize, keySet.size());

        // Проверяем, что все ключи из HashMap присутствуют в keySet
        for (String key : hashMap.keySet()) {
            assertTrue(keySet.contains(key));
        }

        // Проверяем, что keySet не пустой
        assertFalse(keySet.isEmpty());
    }

    @Test
    void values() {

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
        }

        // Получаем values
        Collection<Integer> values = hashMap.values();

        // Проверяем, что размер values равен размеру HashMap
        assertEquals(dataSize, values.size());

        // Проверяем, что все значения из HashMap присутствуют в values
        for (Integer value : hashMap.values()) {
            assertTrue(values.contains(value));
        }

        // Проверяем, что values не пустой
        assertFalse(values.isEmpty());
    }

    @Test
    void entrySet() {
        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 1000;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            hashMap.put(key, value);
        }

        // Получаем values
        Set<Map.Entry<String, Integer>> entries = hashMap.entrySet();

        // Проверяем, что размер values равен размеру HashMap
        assertEquals(dataSize, entries.size());


        // Проверяем, что все значения из HashMap присутствуют в values
        for (Map.Entry<String, Integer> value : entries) {
            assertTrue(entries.contains(value));
            System.out.println(value.getKey());
        }

        // Проверяем, что values не пустой
        assertFalse(entries.isEmpty());
    }

    @Test
    void putAll() {
        // Создаем первую HashMap
        HashMap<String, Integer> sourceHashMap = new HashMap<>();

        // Генерируем случайные ключи и значения
        Random random = new Random();
        int dataSize = 500;
        for (int i = 0; i < dataSize; i++) {
            String key = generateRandomString(10);
            int value = random.nextInt(100);
            sourceHashMap.put(key, value);
        }

        // Создаем вторую HashMap
        HashMap<String, Integer> targetHashMap = new HashMap<>();

        // Копируем элементы из первой HashMap во вторую с использованием putAll
        targetHashMap.putAll(sourceHashMap);

        // Проверяем, что размер второй HashMap равен размеру первой HashMap
        assertEquals(sourceHashMap.size(), targetHashMap.size());

        // Проверяем, что все элементы из первой HashMap присутствуют во второй HashMap
        Collection<Integer> values = targetHashMap.values();
        for (Integer value : sourceHashMap.values()) {
            assertTrue(values.contains(value));
        }
        Set<String> keySet = targetHashMap.keySet();
        for (String key : sourceHashMap.keySet()) {
            assertTrue(keySet.contains(key));
        }
    }
}