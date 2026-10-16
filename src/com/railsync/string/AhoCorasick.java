package com.railsync.string;

import java.util.*;

public class AhoCorasick {

    private static class Node {

        Node[] children = new Node[26];
        Node fail;
        List<String> outputs = new ArrayList<>();
    }

    private Node root = new Node();

    public void addPattern(String pattern) {

        Node current = root;

        String value = pattern.toLowerCase();

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

        current.outputs.add(pattern);
    }

    public void buildFailureLinks() {

        Queue<Node> queue = new LinkedList<>();

        root.fail = root;

        for (Node child : root.children) {

            if (child != null) {
                child.fail = root;
                queue.add(child);
            }
        }

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            for (int i = 0; i < 26; i++) {

                Node child = current.children[i];

                if (child == null) {
                    continue;
                }

                Node failure = current.fail;

                while (failure != root && failure.children[i] == null) {
                    failure = failure.fail;
                }

                if (failure.children[i] != null && failure.children[i] != child) {
                    child.fail = failure.children[i];
                } else {
                    child.fail = root;
                }

                child.outputs.addAll(child.fail.outputs);

                queue.add(child);
            }
        }
    }

    public List<String> search(String text) {

        List<String> found = new ArrayList<>();

        Node current = root;

        String value = text.toLowerCase();

        for (char c : value.toCharArray()) {

            if (c < 'a' || c > 'z') {
                current = root;
                continue;
            }

            int index = c - 'a';

            while (current != root && current.children[index] == null) {
                current = current.fail;
            }

            if (current.children[index] != null) {
                current = current.children[index];
            }

            found.addAll(current.outputs);
        }

        return found;
    }
}