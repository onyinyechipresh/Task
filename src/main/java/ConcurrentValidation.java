import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class ConcurrentValidation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password (min 8 characters, 1 upper case, 1 special character, 1 number): ");
        String password = scanner.nextLine();

        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();
        LocalDateTime dob = parseDateOfBirth(dateOfBirth);

        // todo:Validate all fields concurrently
        CompletableFuture<Validate> usernameValidation = CompletableFuture.supplyAsync(() -> isValidUsername(username));
        CompletableFuture<Validate> emailValidation = CompletableFuture.supplyAsync(() -> isValidEmail(email));
        CompletableFuture<Validate> passwordValidation = CompletableFuture.supplyAsync(() -> isValidPassword(password));
        CompletableFuture<Validate> dobValidation = CompletableFuture.supplyAsync(() -> isValidDOB(String.valueOf(dob)));

        try {
            // todo:Combine the results and check if all validations passed
            boolean isValid = usernameValidation.get().isValid()
                    && emailValidation.get().isValid()
                    && passwordValidation.get().isValid()
                    && dobValidation.get().isValid();

            if (isValid) {
                System.out.println("Registration Successful!");
            } else {
                System.out.println("Registration Failed. Validation Errors:");
                if (!usernameValidation.get().isValid()) {
                    System.out.println(usernameValidation.get().getErrorMessages());
                }
                if (!emailValidation.get().isValid()) {
                    System.out.println(emailValidation.get().getErrorMessages());
                }
                if (!passwordValidation.get().isValid()) {
                    System.out.println(passwordValidation.get().getErrorMessages());
                }
                if (!dobValidation.get().isValid()) {
                    System.out.println(dobValidation.get().getErrorMessages());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        scanner.close();

    }


    private static Validate isValidUsername(String username) {
        // todo:Validation username (min 4 characters)

        Validate res = new Validate();
        if (username == null || username.length() < 4) {
            res.addError("Username: should not be empty and must have at least 4 characters");
        }
        return res;
    }

    private static Validate isValidEmail(String email) {
        // todo: Validate email (valid format)
        Validate res = new Validate();
        String emailRegex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        if (!Pattern.matches(emailRegex, email) || email == null) {
            res.addError("Email: should not be empty and must be a valid email format");
        }
        return res;
    }

    private static Validate isValidPassword(String password) {
        // todo:Validate password (min 8 characters, 1 upper case, 1 special character, 1 number)
        Validate res = new Validate();
        String passwordRegex = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}$";
        if (!Pattern.matches(passwordRegex, password) || password == null) {
            res.addError("Password: should not be empty and must be valid format");
        }
        return res;
    }

    private static Validate isValidDOB(String dob) {
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
                    int currentYear = java.time.Year.now().getValue();
                    if (currentYear - year < 16) {
                        res.addError("Date of Birth: must be 16 years or older");
                    }
                }
            } catch (NumberFormatException e) {
                res.addError("Date of Birth: invalid format");
            }
        }
        return res;
    }

    public static LocalDateTime parseDateOfBirth(String dobString) {
        try {
            return LocalDateTime.parse(dobString + "T00:00:00");
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
