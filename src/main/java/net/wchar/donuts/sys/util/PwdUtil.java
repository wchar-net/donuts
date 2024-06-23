package net.wchar.donuts.sys.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

/**
 * 密码工具类
 *
 * @author Elijah
 */
public class PwdUtil {

    // 不同类型字符集合
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz"; // 小写字母
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 大写字母
    private static final String DIGITS = "0123456789"; // 数字
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?"; // 特殊字符

    /**
     * 生成随机密码。
     *
     * @param length         密码长度
     * @param useLowercase   是否包含小写字母
     * @param useUppercase   是否包含大写字母
     * @param useDigits      是否包含数字
     * @param useSpecialChars 是否包含特殊字符
     * @return 生成的随机密码
     */
    public static String generatePassword(int length, boolean useLowercase, boolean useUppercase, boolean useDigits, boolean useSpecialChars) {
        // 用于构建密码的 StringBuilder
        StringBuilder password = new StringBuilder();
        // 随机数生成器
        Random random = new Random();

        // 根据参数设定生成密码
        while (password.length() < length) {
            if (useLowercase) {
                password.append(LOWERCASE_CHARS.charAt(random.nextInt(LOWERCASE_CHARS.length())));
            }
            if (useUppercase) {
                password.append(UPPERCASE_CHARS.charAt(random.nextInt(UPPERCASE_CHARS.length())));
            }
            if (useDigits) {
                password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
            }
            if (useSpecialChars) {
                password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
            }
        }

        // 如果密码超过指定长度，进行修剪
        if (password.length() > length) {
            password.delete(length, password.length());
        }

        // 打乱密码中字符的顺序，增加密码的随机性
        for (int i = 0; i < length; i++) {
            int swapWith = random.nextInt(length);
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(swapWith));
            password.setCharAt(swapWith, temp);
        }

        return password.toString();
    }

    /**
     * 主方法，演示如何使用 generatePassword 方法生成随机密码。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main1(String[] args) {
        // 示例：生成一个包含所有字符类型，长度为12的密码
        String password = generatePassword(12, true, true, true, true);
        System.out.println("生成的密码：" + password);
    }


    public static void main(String[] args) {
        // 假设从数据库中获取的密码
        String storedPasswordFromDatabase = "$2a$10$kABB5yRLTtSwlhu7CUQ4gOWTvHZEG78TGwZ4Ft8ebUygnSbFFRc7i";

        // 用户输入的密码
        String userInputPassword = "123456";

        // 创建 BCryptPasswordEncoder 实例
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 验证密码是否匹配
        boolean passwordMatches = passwordEncoder.matches(userInputPassword, storedPasswordFromDatabase);

        if (passwordMatches) {
            System.out.println("Password matches!");
        } else {
            System.out.println("Password does not match!");
        }
    }

    public static String encoder(String pwd) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(pwd);
    }

    public static boolean matches(String rawPwd, String dbPwd) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPwd, dbPwd);
    }
}
