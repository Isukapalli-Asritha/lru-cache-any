package com.lru;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter cache capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine().trim());

        LRUCache cache = new LRUCache(capacity);
        System.out.println("LRU Cache created with capacity " + capacity + ".");
        System.out.println("Commands: put <key> <value> | get <key> | quit");

        while (scanner.hasNextLine()) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            switch (parts[0].toLowerCase()) {
                case "put" -> {
                    if (parts.length != 3) {
                        System.out.println("Usage: put <key> <value>");
                        break;
                    }
                    cache.put(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    System.out.println("Inserted (" + parts[1] + ", " + parts[2] + ")");
                }
                case "get" -> {
                    if (parts.length != 2) {
                        System.out.println("Usage: get <key>");
                        break;
                    }
                    int result = cache.get(Integer.parseInt(parts[1]));
                    System.out.println(result == -1 ? "Key not found" : "Value: " + result);
                }
                case "quit", "exit" -> {
                    System.out.println("Bye.");
                    return;
                }
                default -> System.out.println("Unknown command. Use put, get, or quit.");
            }
        }
    }
}
