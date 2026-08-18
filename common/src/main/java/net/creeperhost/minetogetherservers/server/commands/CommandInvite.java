package net.creeperhost.minetogetherservers.server.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.creeperhost.minetogetherservers.MineTogetherServersServer;
import net.creeperhost.minetogether.lib.web.ApiClientResponse;
import net.creeperhost.minetogether.lib.web.ApiResponse;
import net.creeperhost.minetogetherservers.server.web.SendInviteRequest;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserWhiteList;
import net.minecraft.server.players.UserWhiteListEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandInvite {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleCommandExceptionType INVALID_USERNAME = new SimpleCommandExceptionType(Component.literal("Invalid username"));
    private static final SimpleCommandExceptionType INVALID_GAME_PROFILE = new SimpleCommandExceptionType(Component.literal("Failed to load GameProfile, Username is not valid"));
    private static final DynamicCommandExceptionType ALREADY_WHITELISTED = new DynamicCommandExceptionType(username -> Component.translatable(username + " Is already whitelisted"));


    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("invite").requires(cs -> cs.permissions().hasPermission(Permissions.COMMANDS_ADMIN)).then(Commands.argument("username", StringArgumentType.string()).executes(cs -> execute(cs, StringArgumentType.getString(cs, "username"))));
    }

    private static int execute(CommandContext<CommandSourceStack> cs, String username) throws CommandSyntaxException {
        MinecraftServer minecraftServer = cs.getSource().getServer();
        if (username.isEmpty()) throw INVALID_USERNAME.create();

        NameAndId profile = minecraftServer.services().nameToIdCache().get(username).orElse(null);
        if (profile == null) {
            throw INVALID_GAME_PROFILE.create();
        }

        if (minecraftServer.getPlayerList().getWhiteList().isWhiteListed(profile)) {
            throw ALREADY_WHITELISTED.create(username);
        }

        UserWhiteListEntry userWhiteListEntry = new UserWhiteListEntry(profile);
        minecraftServer.getPlayerList().getWhiteList().add(userWhiteListEntry);
        minecraftServer.getPlayerList().reloadWhiteList();
        sendUserInvite(profile, minecraftServer);

        cs.getSource().sendSuccess(() -> Component.literal(username + " Added to whitelist"), false);
        return 0;
    }

    public static void sendUserInvite(NameAndId profile, MinecraftServer server) {
        UserWhiteList whitelistedPlayers = server.getPlayerList().getWhiteList();
        ArrayList<String> tempHash = new ArrayList<>();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(whitelistedPlayers.get(profile).toString().getBytes(StandardCharsets.UTF_8));
            tempHash.add(Arrays.toString(digest.digest(hash)));
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.fatal("SHA256 not supported??", ex);
        }

        try {
            ApiClientResponse<ApiResponse> resp = MineTogetherServers.API.execute(new SendInviteRequest(MineTogetherServersServer.inviteId, tempHash));
            LOGGER.debug("Response from add endpoint " + resp.apiResponse().getStatus() + " " + resp.apiResponse().getMessageOrNull());
        } catch (IOException e) {
            LOGGER.error("Failed to send invite.");
        }

    }
}
