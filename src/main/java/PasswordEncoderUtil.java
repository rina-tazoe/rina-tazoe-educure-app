import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String tanakaPassword = "1234QWER!";
        String suzukiPassword = "2345WERT!";

        String encodedTanakaPassword = encoder.encode(tanakaPassword);
        String encodedSuzukiPassword = encoder.encode(suzukiPassword);

        System.out.println("tanaka encoded password: " + encodedTanakaPassword);
        System.out.println("suzuki encoded password: " + encodedSuzukiPassword);

        // 生成されたハッシュ値の例:
        // Tanaka's encoded password: $2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        // Suzuki's encoded password: $2a$10$YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
    }
}