package com.softgesign.devintensive.utils;


import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static String getValidatedPhone(String phoneStr) {
        String normalizedPhone = phoneStr.trim();
        if (normalizedPhone.charAt(0) != '+') {
            return null;
        }
        normalizedPhone = normalizedPhone.replaceAll("[^\\d]", "");
        if (normalizedPhone.length() >= 11 && normalizedPhone.length() <= 20) {
            return "+" + normalizedPhone;
        }
        return null;
    }

    public static String formatRegularPhone(String phoneStrNorm) {
        if (phoneStrNorm.length() == 12) {
            StringBuilder phoneBuilder = new StringBuilder();

            phoneBuilder.append(phoneStrNorm.substring(0, 2));
            phoneBuilder.append(" ");
            phoneBuilder.append(phoneStrNorm.substring(2, 5));
            phoneBuilder.append(" ");
            phoneBuilder.append(phoneStrNorm.substring(5, 8));
            phoneBuilder.append("-");
            phoneBuilder.append(phoneStrNorm.substring(8, 10));
            phoneBuilder.append("-");
            phoneBuilder.append(phoneStrNorm.substring(10, 12));

            return phoneBuilder.toString();
        }
        return phoneStrNorm;
    }


    @Nullable
    public static String getValidatedEmail(String emailStr) {
        String regExp = "^[\\w\\-\\.]{3,}@[\\w\\-\\.]{2,}\\.[\\w]{2,}$";
        Pattern pattern = Pattern.compile(regExp);
        String normalizedEmail = emailStr.trim();
        Matcher matcher = pattern.matcher(normalizedEmail);
        if (matcher.matches()) {
            return normalizedEmail;
        }
        return null;
    }

    @Nullable
    public static String getValidatedVkUrl(String vkUrlStr) {
        return getTruncatedUrl(vkUrlStr, "vk.com");
    }

    @Nullable
    public static String getValidatedGitUrl(String gitUrlStr) {
        return getTruncatedUrl(gitUrlStr, "github.com");
    }


    @Nullable
    private static String getTruncatedUrl(String url, String domain) {
        String normalizedUrl = url.trim();
        String protocol = "https://";
        if (normalizedUrl.startsWith(protocol)) {
            normalizedUrl = normalizedUrl.substring(protocol.length());
        }
        if (normalizedUrl.startsWith(domain + "/")) {
            Pattern pattern = Pattern.compile("[\\w\\-\\./]+");
            Matcher matcher = pattern.matcher(normalizedUrl.substring(domain.length() + 1));
            if (matcher.matches()) {
                return normalizedUrl;
            }
        }
        return null;
    }
}