import java.util.Scanner;
import java.util.regex.Pattern;

public class User {
    public static void main(String[] args) {
        // todo: Input for Username, Email, Password, and Date of Birth
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
        Validate result = validateFields(username, email, password, dob);

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

        // todo:Validation username (min 4 characters)
        if (username.length() < 4) {
            res.addError("Username: minimum ofon 4 characters");
        }

        // todo: Validate email (valid format)
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            res.addError("Email: invalid format");
        }

        // todo:Validate password (min 8 characters, 1 upper case, 1 special character, 1 number)
        String passwordRegex = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}$";
        if (!Pattern.matches(passwordRegex, password)) {
            res.addError("Password: invalid format");
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
                    int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
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

}
