import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class JwtVerification {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username (min 4 characters): ");
        String username = scanner.nextLine();
        System.out.print("Email (valid format): ");
        String email = scanner.nextLine();
        System.out.print("Password (min 8 characters, 1 upper case, 1 special character, 1 number): ");
        String password = scanner.nextLine();
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        // todo:Validate all fields concurrently
        CompletableFuture<Boolean> usernameValidation = CompletableFuture.supplyAsync(() -> isValidUsername(username));
        CompletableFuture<Boolean> emailValidation = CompletableFuture.supplyAsync(() -> isValidEmail(email));
        CompletableFuture<Boolean> passwordValidation = CompletableFuture.supplyAsync(() -> isValidPassword(password));
        CompletableFuture<Boolean> dobValidation = CompletableFuture.supplyAsync(() -> isValidDOB(dob));


        try {
            // todo:Combine the results and check if all validations passed
            boolean isValid = usernameValidation.get() && emailValidation.get() && passwordValidation.get() && dobValidation.get();

            if (isValid) {
                // todo:Generate a JWT token upon successful validation
                String jwt = generateJWT(username);
                System.out.println("Registration Successful!");
                System.out.println("JWT Token: " + jwt);

                // todo:Verify the generated JWT token
                String verificationResult = verifyToken(jwt);
                System.out.println("Token Verification Result: " + verificationResult);
            } else {
                System.out.println("Registration Failed. Validation Errors:");
                if (!usernameValidation.get()) {
                    System.out.println("Username: min 4 characters");
                }
                if (!emailValidation.get()) {
                    System.out.println("Email: invalid format");
                }
                if (!passwordValidation.get()) {
                    System.out.println("Password: invalid format");
                }
                if (!dobValidation.get()) {
                    System.out.println("Date of Birth: invalid format or must be 16 years or older");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    private static boolean isValidUsername(String username) {
        // todo:Validation username (min 4 characters)

        Validate res = new Validate();
        if (username.length() < 4) {
            res.addError("Username: minimum of 4 characters");
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        // todo: Validate email (valid format)
        Validate res = new Validate();
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            res.addError("Email: invalid format");
        }
        return true;
    }

    private static boolean isValidPassword(String password) {
        // todo:Validate password (min 8 characters, 1 upper case, 1 special character, 1 number)
        Validate res = new Validate();
        String passwordRegex = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}$";
        if (!Pattern.matches(passwordRegex, password)) {
            res.addError("Password: invalid format");
        }
        return true;
    }

    private static boolean isValidDOB(String dob) {
        // todo:Validate password
        Validate res = new Validate();
        if (dob.isEmpty()) {
            res.addError("Date of Birth: cannot be empty");
        } else {
            try {
                String[] parts = dob.split("-");
                if (parts.length != 3) {
                    res.addError("Date of Birth: invalid format");
                } else {
                    int year = Integer.parseInt(parts[0]);
                    int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                    if (currentYear - year < 16) {
                        res.addError("Date of Birth: must be 16 years or older");
                    }
                }
            } catch (NumberFormatException e) {
                res.addError("Date of Birth: invalid format");
            }
        }
        return true;
    }


    // todo:Generate a JWT token with a secure key
    private static String generateJWT(String username) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        long currentTimeMillis = System.currentTimeMillis();
        Date expiration = new Date(currentTimeMillis + 3600_000); // Token valid for 1 hour

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }


    // todo:Verify a JWT token
    private static String verifyToken(String token) {
        try {
            // todo:peaParse and verify the token using the same key used for signing
            Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

//            System.out.println("Decoded Token: " + claims.toString());

            // todo:Check token expiration
            Date expiration = claims.getExpiration();
            Date now = new Date();
            if (expiration != null && expiration.before(now)) {
                return "verification fails: Token has expired";
            }
            return "verification pass";
        } catch (Exception e) {
            return "verification fails: " + e.getMessage();
        }
    }
}
