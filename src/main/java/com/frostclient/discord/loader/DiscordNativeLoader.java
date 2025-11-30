package com.frostclient.discord.loader;

import java.io.*;
import java.nio.file.*;

public class DiscordNativeLoader {
    private static boolean loaded = false;

    public static synchronized void load() {
        if (loaded) return;

        try {
            String os = getOS();

            String libName = getLibraryName(os);
            String sdkName = getPartnerSDKName(os);

            Path tempDir = Files.createTempDirectory("discord-natives");

            Path libFile = tempDir.resolve(libName);
            Path sdkFile = tempDir.resolve(sdkName);

            extractLibrary("discord", tempDir);
            extractLibrary("discord_partner_sdk", tempDir);

            System.load(libFile.toAbsolutePath().toString());
            System.load(sdkFile.toAbsolutePath().toString());

            loaded = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Discord native library", e);
        }
    }

    private static void extractLibrary(String name, Path tempDir) throws IOException {
        String resourcePath = "/native/windows/" + name;

        InputStream in = DiscordNativeLoader.class.getResourceAsStream(resourcePath);
        if (in == null) {
            System.err.println("Warning: " + name + " not found in JAR");
            return;
        }

        Path tempFile = tempDir.resolve(name);
        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        in.close();
    }

    private static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return "windows";
        if (os.contains("nix") || os.contains("nux")) return "linux";
        if (os.contains("mac")) return "macos";
        throw new UnsupportedOperationException("Unsupported OS: " + os);
    }

    private static String getPartnerSDKName(String os) {
        return "discord_partner_sdk." + getSuffix(os);
    }

    private static String getLibraryName(String os) {
        return "discord." + getSuffix(os);
    }

    private static String getSuffix(String os) {
        return switch (os) {
            case "windows" -> "dll";
            case "linux" -> "so";
            case "macos" -> "dylib";
            default -> "";
        };
    }
}