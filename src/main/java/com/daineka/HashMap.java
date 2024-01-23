package com.daineka;

import java.util.*;

/**
 * HashMap - простая реализация java.util.HashMap.
 * Использует массив связанных узлов для обработки коллизий.
 *
 * @param <K> тип ключей, хранящихся в карте.
 * @param <V> тип значений, хранящихся в карте.
 */
public class HashMap<K, V>{
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;
    private Node<K, V>[] nodes;
    private int capacity;
    private final float loadFactory;
    private int size = 0;

    /**
     * Класс, представляющий узел в цепочке карты.
     *
     * @param <K> тип ключа.
     * @param <V> тип значения.
     */
    private static class Node<K, V> implements Map.Entry<K,V>{
        final int hash;
        final K key;
        V value;
        Node<K, V> nextNode;

        /**
         * Приватный конструктор для создания нового узла.
         *
         * @param hash     хеш-код ключа узла.
         * @param key      ключ узла.
         * @param value    значение узла.
         * @param nextNode следующий узел в цепочке связанных узлов.
         */
        private Node(int hash, K key, V value, Node<K, V> nextNode) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        /**
         * Устанавливает новое значение для узла и возвращает старое значение.
         *
         * @param newValue новое значение для узла.
         * @return старое значение узла.
         */
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    /**
     * Создает новый экземпляр HashMap с емкостью и коэффициентом загрузки по умолчанию.
     */
    public HashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTORY);
    }

    /**
     * Создает новый экземпляр HashMap с указанной начальной емкостью и коэффициентом загрузки по умолчанию.
     *
     * @param capacity начальная емкость карты.
     */
    public HashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTORY);
    }

    /**
     * Создает новый экземпляр HashMap с указанной начальной емкостью и указанным коэффициентом загрузки.
     *
     * @param capacity    начальная емкость карты.
     * @param loadFactory коэффициент загрузки карты.
     */
    public HashMap(int capacity, float loadFactory) {
        if (capacity < 0) throw new IllegalArgumentException("the capacity is incorrectly initialized" + capacity);
        if (loadFactory <= 0)
            throw new IllegalArgumentException("the loading factor is incorrectly initialized" + loadFactory);
        this.nodes = new Node[capacity];
        this.capacity = capacity;
        this.loadFactory = loadFactory;
    }

    /**
     * Возвращает значение с указанным ключом,
     * или {@code null}, если в карте отсутствует данный ключа.
     *
     * @param key ключ, для которого будет возвращено значение.
     * @return значение, связанное с указанным ключом, или {@code null}, если такого значения нет.
     */
    public V get(Object key) {
        int hashCode = getHashCode(key);
        int index = hashCode & (this.capacity - 1);
        if (!isEmptyBucket(index)) {
            Node<K, V> tempNode = this.nodes[index];
            while (true) {
                if (tempNode.hash == hashCode && (key == null && tempNode.key == null) || tempNode.key.equals(key)) {
                    return tempNode.value;
                }
                if (isLastNodeInChain(tempNode)) break;
                tempNode = tempNode.nextNode;
            }
        }
        return null;
    }

    /**
     * Возвращает перезаписываемое значение, если в карте было значение с указанным ключом,
     * или {@code null}, если в карте отсутствовал данный ключ.
     * Добавляет элемент в карту с указанным ключом и значениме.
     * Если карта раньше содержала данный ключ, старое значение заменяется.
     *
     * @param key   ключ, с которым будет связано указанное значение.
     * @param value значение, которое будет связано с указанным ключом.
     * @return значение, перезаписываемое в указанном ключе, или {@code null}, если такого значения не было.
     */
    public V put(Object key, Object value) {
        if (this.loadFactory <= (float) this.size / this.capacity) {
            resize();
        }
        int hashCode = getHashCode(key);

        int indexInput = hashCode & (this.capacity - 1);
        Node<K, V> newNode = (Node<K, V>) new Node<>(hashCode, key, value, null);
        if (isEmptyBucket(indexInput)) {
            this.nodes[indexInput] = newNode;
        } else {
            Node<K, V> tempNode = this.nodes[indexInput];
            while (true) {
                if ((tempNode.hash == hashCode) && (key == null && tempNode.key == null) || tempNode.key.equals(key)) {
                    V lastValue = tempNode.value;
                    tempNode.setValue((V) value);
                    return lastValue;
                }
                if (isLastNodeInChain(tempNode)) break;
                tempNode = tempNode.nextNode;
            }
            tempNode.nextNode = newNode;
        }
        this.size++;
        return null;
    }

    /**
     * Возвращает количество записей (пар ключ-значение) в карте.
     *
     * @return количество записей в карте.
     */
    public int size() {
        return this.size;
    }

    /**
     * Проверяет, пуста ли карта.
     *
     * @return {@code true}, если карта пуста, {@code false} в противном случае.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Проверяет, содержится ли указанный ключ в карте.
     *
     * @param key ключ, который требуется проверить на наличие в карте.
     * @return {@code true}, если карта содержит указанный ключ, {@code false} в противном случае.
     */
    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    /**
     * Проверяет, содержится ли указанное значение в карте.
     *
     * @param value значение, которое требуется проверить на наличие в карте.
     * @return {@code true}, если карта содержит указанное значение, {@code false} в противном случае.
     */
    public boolean containsValue(Object value) {
        for (Node<K, V> node : nodes) {
            for (; node != null; node = node.nextNode) {
                if (Objects.equals(node.value, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Удаляет ключ и значение, связанное с ключем из этой карты, если оно присутствует.
     *
     * @param key ключ, который требуется удалить из карты.
     * @return значение, удаляемое с указанным ключом, или {@code null}, если такого значения не было.
     */
    public V remove(Object key) {
        int hashCode = getHashCode(key);
        int index = hashCode & (this.capacity - 1);
        if (!isEmptyBucket(index)) {
            Node<K, V> tempNode = this.nodes[index];
            if (tempNode.nextNode == null) {
                if ((tempNode.hash == hashCode) && (key == null && tempNode.key == null) || tempNode.key.equals(key)) {
                    return removeSingleNodeFromBucket(tempNode, index);
                }
            } else {
                Node<K, V> tempNodeNext = tempNode.nextNode;
                while (true) {
                    if ((tempNodeNext.hash == hashCode) && (key == null && tempNodeNext.key == null) || tempNodeNext.key.equals(key)) {
                        return deleteNodeInBucket(tempNodeNext, tempNode);
                    }
                    if (isLastNodeInChain(tempNodeNext)) break;
                    tempNode = tempNode.nextNode;
                    tempNodeNext = tempNode.nextNode;
                }
            }
        }
        return null;
    }

    /**
     * Очищает все записи из карты, делая ее пустой.
     */
    public void clear() {
        this.nodes = new Node[capacity];
        this.size = 0;
    }

    /**
     * Возвращает представление множества ключей, содержащихся в карте.
     *
     * @return представление множества ключей в карте.
     */
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Node<K, V> node : nodes) {
            for (; node != null; node = node.nextNode) {
                keySet.add(node.key);
            }
        }
        return keySet;
    }

    /**
     * Возвращает представление коллекции значений, содержащихся в карте.
     *
     * @return представление коллекции значений в карте.
     */
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        for (Node<K, V> node : nodes) {
            for (; node != null; node = node.nextNode) {
                values.add(node.value);
            }
        }
        return values;
    }

    /**
     * Возвращает представление множества записей содержащихся в карте.
     *
     * @return представление множества записей в карте.
     */
    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> entries = new HashSet<>();
        for (Node<K, V> node : nodes) {
            for (; node != null; node = node.nextNode) {
                entries.add(node);
            }
        }
        return entries;
    }

    /**
     * Копирует все записи из указанной карты в текущую карту.
     * Если в текущей карте уже существует запись с тем же ключом, она будет заменена.
     *
     * @param map карта, чьи записи копируются в текущую карту.
     */
    public void putAll(HashMap<K, V> map) {
        for (Node<K, V> node : map.nodes) {
            for (; node != null; node = node.nextNode) {
                this.put(node.key, node.value);
            }
        }
    }

    /**
     * Удаляет узел из корзины, содержащей только один элемент.
     * Возвращает значение удаленного узла. Если корзина пуста, возвращает null.
     *
     * @param tempNode узел, который будет удален из корзины.
     * @param index    индекс корзины в массиве узлов.
     * @return значение удаленного узла или null, если корзина пуста.
     */
    private V removeSingleNodeFromBucket(Node<K, V> tempNode, int index) {
        V lastValue = tempNode.value;
        this.nodes[index] = null;
        this.size--;
        return lastValue;
    }

    /**
     * Удаляет указанный узел из корзины.
     *
     * @param tempNodeNext следующий узел после удаляемого узла.
     * @param tempNode     удаляемый узел.
     * @return значение удаленного узла.
     */
    private V deleteNodeInBucket(Node<K, V> tempNodeNext, Node<K, V> tempNode) {
        tempNode.nextNode = (tempNodeNext.nextNode == null) ? null : tempNodeNext.nextNode;
        this.size--;
        return tempNodeNext.value;
    }

    /**
     * Проверяет, является ли указанная корзина пустой.
     *
     * @param indexInput индекс корзины, которую нужно проверить.
     * @return true, если корзина пуста, false в противном случае.
     */
    private boolean isEmptyBucket(int indexInput) {
        return this.nodes[indexInput] == null;
    }

    /**
     * Изменяет размер карты, увеличивая ее вдвое.
     * Перехеширует существующие записи в новую карту с увеличенной емкостью.
     * Обновляет текущую карту данными из новой карты.
     */
    private void resize() {
        HashMap<K, V> newHashMap = new HashMap<>(this.capacity * 2);
        for (int i = 0; i < this.capacity; i++) {
            if (isEmptyBucket(i)) continue;
            Node<K, V> tempNode = this.nodes[i];
            while (true) {
                newHashMap.put(tempNode.key, tempNode.value);
                if (isLastNodeInChain(tempNode)) break;
                tempNode = tempNode.nextNode;
            }
        }
        this.nodes = newHashMap.nodes;
        this.capacity = newHashMap.capacity;
    }

    /**
     * Возвращает хеш-код для указанного ключа.
     * Если ключ равен null, возвращает 0.
     *
     * @param key ключ, для которого нужно получить хеш-код.
     * @param <K> тип ключа.
     * @return хеш-код ключа.
     */
    private static <K> int getHashCode(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    /**
     * Проверяет, является ли узел последним в цепочке связанных узлов.
     *
     * @param tempNode узел, для которого нужно выполнить проверку.
     * @param <K>      тип ключа.
     * @param <V>      тип значения.
     * @return true, если узел последний в цепочке, false в противном случае.
     */
    private static <K, V> boolean isLastNodeInChain(Node<K, V> tempNode) {
        return tempNode.nextNode == null;
    }
}
