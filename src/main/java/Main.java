import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static String currentRoom = "Entrance";
    private static final ArrayList<String> inventory = new ArrayList<>();
    private static int playerHealth = 100;

    private static final HashMap<String, String[]> rooms = new HashMap<>();
    private static final HashMap<String, String> roomDescriptions = new HashMap<>();

    private static boolean npcHintGiven = false;
    private static boolean enemyDefeated = false;

    public static void main(String[] args) {
        setupGame();

        System.out.println("Welcome to the Adventure Game!");
        System.out.println("Type 'help' for a list of commands.");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nCurrent Room: " + currentRoom);
            System.out.println(roomDescriptions.get(currentRoom));
            System.out.print("> ");
            String input = scanner.nextLine().toLowerCase();

            if (input.startsWith("go ")) {
                move(input.substring(3));
            } else if (input.equals("check inventory")) {
                checkInventory();
            } else if (input.equals("talk")) {
                talk();
            } else if (input.equals("attack")) {
                attack();
            } else if (input.equals("run")) {
                run();
            } else if (input.equals("help")) {
                displayHelp();
            } else if (input.equals("quit")) {
                System.out.println("Thanks for playing!");
                break;
            } else {
                System.out.println("Invalid command. Type 'help' for a list of commands.");
            }

            if (playerHealth <= 0) {
                System.out.println("Game Over! You have been defeated.");
                break;
            }
        }
        scanner.close();
    }

    private static void setupGame() {
        rooms.put("Entrance", new String[]{"north"});
        rooms.put("Forest", new String[]{"south", "east"});
        rooms.put("Dungeon", new String[]{"west"});
        rooms.put("Treasure Room", new String[]{"west"});

        roomDescriptions.put("Entrance", "You are at the entrance. Exits: north.");
        roomDescriptions.put("Forest", "You are in a dark forest. Exits: south, east.");
        roomDescriptions.put("Dungeon", "You are in a dungeon. There's an enemy here! Exits: west.");
        roomDescriptions.put("Treasure Room", "You have found the treasure room! Exits: west.");
    }

    private static void move(String direction) {
        switch (currentRoom) {
            case "Entrance":
                if (direction.equals("north")) {
                    currentRoom = "Forest";
                } else {
                    System.out.println("You can't go that way.");
                }
                break;
            case "Forest":
                if (direction.equals("south")) {
                    currentRoom = "Entrance";
                } else if (direction.equals("east")) {
                    if (!enemyDefeated) {
                        currentRoom = "Dungeon";
                    } else {
                        currentRoom = "Treasure Room";
                    }
                } else {
                    System.out.println("You can't go that way.");
                }
                break;
            case "Dungeon", "Treasure Room":
                if (direction.equals("west")) {
                    currentRoom = "Forest";
                } else {
                    System.out.println("You can't go that way.");
                }
                break;
            default:
                System.out.println("You can't go that way.");
        }

        if (currentRoom.equals("Treasure Room") && enemyDefeated) {
            System.out.println("You found the treasure! You Win!");
            System.exit(0);
        }
    }

    private static void checkInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory: " + String.join(", ", inventory));
        }
    }

    private static void talk() {
        if (currentRoom.equals("Forest") && !npcHintGiven) {
            System.out.println("You meet an old hermit. He gives you a key and a hint: 'Defeat the enemy to unlock the treasure.'");
            inventory.add("key");
            npcHintGiven = true;
        } else {
            System.out.println("There's no one to talk to here.");
        }
    }

    private static void attack() {
        if (currentRoom.equals("Dungeon")) {
            System.out.println("You engage in combat with the enemy!");
            playerHealth -= 20;
            if (playerHealth > 0) {
                System.out.println("You defeated the enemy!");
                enemyDefeated = true;
            } else {
                System.out.println("The enemy defeated you.");
            }
        } else {
            System.out.println("There's nothing to attack here.");
        }
    }

    private static void run() {
        if (currentRoom.equals("Dungeon")) {
            System.out.println("You run back to the forest.");
            currentRoom = "Forest";
        } else {
            System.out.println("There's nothing to run from here.");
        }
    }

    private static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("go [direction] - Move in a direction (north, south, east, west).");
        System.out.println("check inventory - Display your inventory.");
        System.out.println("talk - Talk to an NPC if one is present.");
        System.out.println("attack - Attack an enemy if present.");
        System.out.println("run - Flee from combat.");
        System.out.println("help - Display this help message.");
        System.out.println("quit - Quit the game.");
    }
}
