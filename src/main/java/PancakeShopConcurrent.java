import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeShopConcurrent {
    public static void main(String[] args) {
        AtomicInteger totalEaten = new AtomicInteger();
        AtomicInteger totalWasted = new AtomicInteger();
        AtomicInteger unmetOrders = new AtomicInteger();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int pancakesToMake = random.nextInt(12) + 1;

            System.out.println("Starting time of 30-second slot: " + i * 30 + " seconds");
            System.out.println("Ending time of 30-second slot: " + (i + 1) * 30 + " seconds");

            CompletableFuture<Integer> user1 = CompletableFuture.supplyAsync(() -> {
                int userOrder1 = random.nextInt(6);
                return Math.min(userOrder1, 5);
            });

            CompletableFuture<Integer> user2 = CompletableFuture.supplyAsync(() -> {
                int userOrder2 = random.nextInt(6);
                return Math.min(userOrder2, 5);
            });

            CompletableFuture<Integer> user3 = CompletableFuture.supplyAsync(() -> {
                int userOrder3 = random.nextInt(6);
                return Math.min(userOrder3, 5);
            });

            CompletableFuture<Void> allOf = CompletableFuture.allOf(user1, user2, user3);

            allOf.thenRun(() -> {
                int userOrder1 = user1.join();
                int userOrder2 = user2.join();
                int userOrder3 = user3.join();

                System.out.println("Shopkeeper making " + pancakesToMake + " pancakes.");

                if (userOrder1 + userOrder2 + userOrder3 <= pancakesToMake) {
                    int totalOrdered = userOrder1 + userOrder2 + userOrder3;
                    System.out.println("All users' orders met.");
                    System.out.println("Users ordered: " + totalOrdered + " pancakes.");
                    totalEaten.getAndSet(totalOrdered);
                    totalWasted.getAndSet(pancakesToMake - totalOrdered);
                } else {
                    System.out.println("Shopkeeper could not meet all orders.");
                    int totalOrdered = userOrder1 + userOrder2 + userOrder3;
                    System.out.println("Users ordered: " + totalOrdered + " pancakes.");
                    totalEaten.getAndSet(pancakesToMake);
                    totalWasted.getAndSet(0);
                    unmetOrders.getAndSet((userOrder1 + userOrder2 + userOrder3) - pancakesToMake);
                    System.out.println(unmetOrders + " pancake orders were not met.");
                }

                System.out.println("Total pancakes eaten: " + totalEaten);
                System.out.println("Total pancakes wasted: " + totalWasted);
                System.out.println();
            });

            try {
                allOf.get(); // Wait for all CompletableFuture tasks to complete
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
