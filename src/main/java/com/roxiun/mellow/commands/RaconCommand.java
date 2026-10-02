package com.roxiun.mellow.commands;

import com.mojang.authlib.GameProfile;
import com.roxiun.mellow.api.racon.RaconApi;
import com.roxiun.mellow.api.racon.RaconTag;
import com.roxiun.mellow.config.MellowOneConfig;
import com.roxiun.mellow.core.async.AsyncExecutor;
import com.roxiun.mellow.core.async.MainThreadDispatcher;
import com.roxiun.mellow.util.ChatUtils;
import com.roxiun.mellow.util.formatting.FormattingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RaconCommand extends CommandBase {

    private final RaconApi raconApi;
    private final MellowOneConfig config;

    public RaconCommand(RaconApi raconApi, MellowOneConfig config) {
        this.raconApi = raconApi;
        this.config = config;
    }

    @Override
    public String getCommandName() {
        return "racon";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/racon <username>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("mracon");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            ChatUtils.sendCommandMessage(
                sender,
                "§cInvalid usage! Use /racon <username>"
            );
            return;
        }

        String username = args[0];
        AsyncExecutor.getInstance().command(() -> {
            try {
                List<RaconTag> tags = raconApi.fetchRaconTags(
                    null,
                    username,
                    config.raconKey
                );

                if (tags == null || tags.isEmpty()) {
                    MainThreadDispatcher.run(() ->
                        ChatUtils.sendCommandMessage(
                            sender,
                            "§aNo Racon tags found for: §r" + username
                        )
                    );
                } else {
                    String formattedTags = FormattingUtils.formatRaconTags(
                        tags
                    );
                    String raconMessage =
                        "§c" + username + " is tagged for: " + formattedTags;
                    MainThreadDispatcher.run(() ->
                        ChatUtils.sendCommandMessage(sender, raconMessage)
                    );
                }
            } catch (IOException e) {
                MainThreadDispatcher.run(() ->
                    ChatUtils.sendCommandMessage(
                        sender,
                        "§cCould not fetch Racon tags for " +
                            username +
                            ": " +
                            e.getMessage()
                    )
                );
            }
        });
    }

    @Override
    public List<String> addTabCompletionOptions(
        ICommandSender sender,
        String[] args,
        BlockPos pos
    ) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(
                args,
                Minecraft.getMinecraft()
                    .getNetHandler()
                    .getPlayerInfoMap()
                    .stream()
                    .map(NetworkPlayerInfo::getGameProfile)
                    .map(GameProfile::getName)
                    .toArray(String[]::new)
            );
        }
        return null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
