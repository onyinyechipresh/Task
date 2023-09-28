import io.jsonwebtoken.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class VerifyJwt {

    static BufferedReader bufferedReader;
    static BufferedReader bufferedReaderKey;

    static {
        try {
            bufferedReader = new BufferedReader(new FileReader("jwt_token.txt"));
            bufferedReaderKey = new BufferedReader(new FileReader("key_token.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException {
        String jwt = String.valueOf(readJWTFromFile());
//        System.out.println(jwt);

        if (jwt != null && verifyJWT(jwt)) {
            System.out.println("Verification pass");
        } else {
            System.out.println("Verification fails");
        }

    }

    private static String readJWTFromFile() throws IOException {
        try {
            String expirationDateStr = bufferedReader.readLine();
            return expirationDateStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            bufferedReader.close();
        }
    }

    private static String readKeyFromFile() throws IOException {
        try {
            String key = bufferedReaderKey.readLine();
            return key;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            bufferedReaderKey.close();
        }
    }

    private static boolean verifyJWT(String jwt) throws IOException {

        String jwtToken = jwt;
        String secretKey = readKeyFromFile();
//        System.out.println(secretKey);

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();


        try {
            Claims claims = jwtParser
                    .parseClaimsJws(jwtToken)
                    .getBody();

            Long expirationTimestamp = claims.get("exp", Long.class);

            if (expirationTimestamp != null) {
                Calendar calendar = Calendar.getInstance();
                Date currentTime = calendar.getTime();
                Date expirationDate = new Date(expirationTimestamp * 1000);

                if (expirationDate.before(currentTime)) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (ExpiredJwtException e) {
        } catch (JwtException e) {
            System.out.println("JWT parsing error: " + e.getMessage());
        }
        return false;
    }
}

