import java.util.Random;

public class PancakeShopNonConcurrent {
    public static void main(String[] args) {
        int totalEaten = 0;
        int totalWasted = 0;
        int unmetOrders = 0;
        Random random = new Random();


        for (int i = 0; i < 5; i++) {
            int pancakesToMake = random.nextInt(12) + 1;
            int userOrder1 = random.nextInt(6);
            int userOrder2 = random.nextInt(6);
            int userOrder3 = random.nextInt(6);

            System.out.println("Starting time of 30-second slot: " + i * 30 + " seconds");
            System.out.println("Ending time of 30-second slot: " + (i + 1) * 30 + " seconds");

            System.out.println("Shopkeeper making " + pancakesToMake + " pancakes.");

            if (userOrder1 + userOrder2 + userOrder3 <= pancakesToMake) {
                int totalOrdered = userOrder1 + userOrder2 + userOrder3;
                System.out.println("All users' orders met.");
                System.out.println("Users ordered: " + totalOrdered + " pancakes.");
                totalEaten = totalOrdered;
                totalWasted = pancakesToMake - totalOrdered;
            } else {
                System.out.println("Shopkeeper could not meet all orders.");
                int totalOrdered = userOrder1 + userOrder2 + userOrder3;
                System.out.println("Users ordered: " + totalOrdered + " pancakes.");
                totalEaten = pancakesToMake;
                totalWasted = 0;
                unmetOrders = (userOrder1 + userOrder2 + userOrder3) - pancakesToMake;
                System.out.println(unmetOrders + " pancake orders were not met.");
            }

            System.out.println("Total pancakes eaten: " + totalEaten);
            System.out.println("Total pancakes wasted: " + totalWasted);
            System.out.println();
        }
    }
}
