package com.railsync.string;

import java.util.ArrayList;
import java.util.List;

public class Trie {

    private static class Node {
        Node[] children = new Node[26];
        boolean isEnd;
        List<String> words = new ArrayList<>();
    }

    private Node root = new Node();

    public void insert(String word) {

        String value = word.toLowerCase();

        Node current = root;

        for (char c : value.toCharArray()) {

            if (c < 'a' || c > 'z') {
                continue;
            }

            int index = c - 'a';

            if (current.children[index] == null) {
                current.children[index] = new Node();
            }

            current = current.children[index];
        }

        current.isEnd = true;

        if (!current.words.contains(word)) {
            current.words.add(word);
        }
    }

    public List<String> searchPrefix(String prefix) {

        String value = prefix.toLowerCase();

        Node current = root;

        for (char c : value.toCharArray()) {

            if (c < 'a' || c > 'z') {
                continue;
            }

            int index = c - 'a';

            if (current.children[index] == null) {
                return new ArrayList<>();
            }

            current = current.children[index];
        }

        List<String> result = new ArrayList<>();

        collectWords(current, result);

        return result;
    }

    private void collectWords(Node node, List<String> result) {

        result.addAll(node.words);

        for (Node child : node.children) {

            if (child != null) {
                collectWords(child, result);
            }
        }
    }
}