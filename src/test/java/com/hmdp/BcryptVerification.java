package com.hmdp;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class BcryptVerification {
    @Test
    void testBcrypt() {

        // 存储在数据库中的哈希密码
        String storedHash = "$2a$10$wwwzuhjqmMRPr6SnPhODu.wbfEBCx8wU062kW9x1wrYwKZGySdWcy";


        String password = "a5c6e78db67eff715afeac98de4a9b3c";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("Hashed password: " + hashedPassword);

        boolean passwordMatch = BCrypt.checkpw(password, storedHash);

        System.out.println("Password match: " + passwordMatch);
        System.out.println("Password match: " + BCrypt.checkpw("a5c6e78db67eff715afeac98de4a9b3c", "$2a$10$wwwzuhjqmMRPr6SnPhODu.wbfEBCx8wU062kW9x1wrYwKZGySdWcy"));
    }
}
