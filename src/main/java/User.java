import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class User {
    public static void main(String[] args) {
        // todo: Input for Username, Email, Password, and Date of Birth
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
        Validate result = validateFields(username, email, password, String.valueOf(dob));

        // todo:Check if all validations passed
        if (result.isValid()) {
            System.out.println("Registration Successful!");
        } else {
            System.out.println("Registration Failed. Validation Errors:");
            System.out.println(result.getErrorMessages());
        }

        scanner.close();
    }
    // todo:Validate all fields concurrently
    private static Validate validateFields (String username, String email, String password, String dob){
        Validate res = new Validate();

        // todo:Validation username
        if (username == null || username.length() < 4) {
            res.addError("Username: should not be empty and must have at least 4 characters");
        }

        // todo: Validate email (valid format)
        String emailRegex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        if (!Pattern.matches(emailRegex, email) || email == null) {
            res.addError("Email: should not be empty and must be a valid email format");
        }

        // todo:Validate password (min 8 characters, 1 upper case, 1 special character, 1 number)
        String passwordRegex = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}$";
        if (!Pattern.matches(passwordRegex, password) || password == null) {
            res.addError("Password: should not be empty and must be valid format");
        }

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
