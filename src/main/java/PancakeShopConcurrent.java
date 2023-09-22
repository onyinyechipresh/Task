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

            CompletableFuture<Integer> user1Future = CompletableFuture.supplyAsync(() -> {
                int userOrder1 = random.nextInt(6); // User 1 can order up to 5 pancakes
                return Math.min(userOrder1, 5); // Limit order to 5 pancakes
            });

            CompletableFuture<Integer> user2Future = CompletableFuture.supplyAsync(() -> {
                int userOrder2 = random.nextInt(6); // User 2 can order up to 5 pancakes
                return Math.min(userOrder2, 5); // Limit order to 5 pancakes
            });

            CompletableFuture<Integer> user3Future = CompletableFuture.supplyAsync(() -> {
                int userOrder3 = random.nextInt(6); // User 3 can order up to 5 pancakes
                return Math.min(userOrder3, 5); // Limit order to 5 pancakes
            });

            CompletableFuture<Void> allOf = CompletableFuture.allOf(user1Future, user2Future, user3Future);

            allOf.thenRun(() -> {
                int user1Order = user1Future.join();
                int user2Order = user2Future.join();
                int user3Order = user3Future.join();

                System.out.println("Shopkeeper making " + pancakesToMake + " pancakes.");

                if (user1Order + user2Order + user3Order <= pancakesToMake) {
                    int totalOrdered = user1Order + user2Order + user3Order;
                    System.out.println("All users' orders met.");
                    System.out.println("Users ordered: " + totalOrdered + " pancakes.");
                    totalEaten.addAndGet(totalOrdered);
                    totalWasted.addAndGet(pancakesToMake - totalOrdered);
                } else {
                    System.out.println("Shopkeeper could not meet all orders.");
                    int totalOrdered = pancakesToMake;
                    System.out.println("Users ordered: " + totalOrdered + " pancakes.");
                    totalEaten.addAndGet(totalOrdered);
                    totalWasted.addAndGet(0);
                    unmetOrders.addAndGet((user1Order + user2Order + user3Order) - pancakesToMake);
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
