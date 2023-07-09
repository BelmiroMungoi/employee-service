package com.bbm.employeeservice.utils;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token) {
        return "Olá " + name + ",\n\nA sua conta foi criada com successo. Por favor clique no link abaixo" +
                "para verificar a sua conta.\n\n" + getVerificationUrl(host, token) +
                "\n\nA equipe de Suporte.";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/api/v1/auth/verify?token=" + token;
    }
}
