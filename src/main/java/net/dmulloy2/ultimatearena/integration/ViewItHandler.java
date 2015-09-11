package net.dmulloy2.ultimatearena.integration;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.dmulloy2.integration.DependencyProvider;
import net.dmulloy2.ultimatearena.UltimateArena;
import net.dmulloy2.ultimatearena.arenas.Arena;
import net.dmulloy2.ultimatearena.types.ArenaPlayer;
import net.t7seven7t.viewit.ViewItPlugin;
import net.t7seven7t.viewit.replacer.Replacer;
import net.t7seven7t.viewit.replacer.Replacers;
import net.t7seven7t.viewit.scoreboard.ScoreboardElement;
import net.t7seven7t.viewit.scoreboard.ScoreboardService;
import net.t7seven7t.viewit.supply.FrameSupply;
import net.t7seven7t.viewit.supply.Supply;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import static net.t7seven7t.viewit.scoreboard.ScoreboardElement.Priority.HIGH;

/**
 * Handle scoreboard integration
 *
 * @author t7seven7t
 */
public class ViewItHandler extends DependencyProvider<ViewItPlugin> {

    private static UltimateArena plugin;

    private List<ScoreboardElement> ingameElements;
    private List<ScoreboardElement> lobbyElements;

    public ViewItHandler(UltimateArena plugin) {
        super(plugin, "ViewIt");
    }

    @Override
    public void onEnable() {
        plugin = (UltimateArena) handler;

        registerReplacer("UA_remaining_time", Functions.REMAINING_TIME);
        registerReplacer("UA_game_status", Functions.GAME_STATUS);
        registerReplacer("UA_game_name", Functions.GAME_NAME);
        registerReplacer("UA_player_class", Functions.PLAYER_CLASS);
        registerReplacer("UA_player_deaths", Functions.PLAYER_DEATHS);
        registerReplacer("UA_player_kdr", Functions.PLAYER_KDR);
        registerReplacer("UA_player_kills", Functions.PLAYER_KILLS);
        registerReplacer("UA_player_killstreak", Functions.PLAYER_KILLSTREAK);

        lobbyElements = Lists.newArrayList();
        lobbyElements.add(makeElement(HIGH(13), 10000L, Supply.of("&8[-------&4UA&8-------]")));
        lobbyElements.add(makeElement(HIGH(12), 600L, Supply.of("&6Arena: &a%UA_game_name%")));
        lobbyElements.add(makeElement(HIGH(11), 20L, Supply.of("&6Status: &a%UA_game_status%")));
        lobbyElements.add(
                makeElement(HIGH(10), 20L, Supply.of("&6Time left: &9%UA_remaining_time%")));
        lobbyElements.add(makeElement(HIGH(5), 10000L, Supply.of("&8[-------&4UA&8-------]")));

        ingameElements = Lists.newArrayList();
        ingameElements.addAll(lobbyElements);
        ingameElements.add(makeElement(HIGH(9), 20L, Supply.of("&6Kills: &a%UA_player_kills%")));
        ingameElements.add(makeElement(HIGH(8), 20L, Supply.of("&6Deaths: &a%UA_player_deaths%")));
        ingameElements.add(makeElement(HIGH(7), 20L,
                Supply.of("&6Killstreak: &a%UA_player_killstreak%")));
        ingameElements.add(makeElement(HIGH(6), 20L, Supply.of("&6Class: &a%UA_player_class%")));
    }

    public void addLobbyElements(Player player) {
        getInstance().addElements(player,
                lobbyElements.toArray(new ScoreboardElement[lobbyElements.size()]));
    }

    public void removeAllElements(Player player) {
        getInstance().removeElements(player,
                ingameElements.toArray(new ScoreboardElement[ingameElements.size()]));
    }

    public void addIngameElements(Player player) {
        getInstance().addElements(player,
                ingameElements.toArray(new ScoreboardElement[ingameElements.size()]));
    }

    private void registerReplacer(String replace, final Function<Player, String> function) {
        Replacers.registerReplacer(new Replacer(replace) {
            @Override
            public String getResult(Player player, Player player1) {
                return function.apply(player);
            }
        });
    }

    private ScoreboardElement makeElement(ScoreboardElement.Priority priority, long updateDelay,
                                          FrameSupply... contents) {
        return ViewItPlugin.getInstance().of(plugin, priority.intValue(), updateDelay,
                Arrays.asList(contents));
    }

    private ScoreboardElement makeElement(ScoreboardElement.Priority priority, long updateDelay,
                                          List<FrameSupply> contents) {
        return ViewItPlugin.getInstance().of(plugin, priority.intValue(), updateDelay, contents);
    }

    private ScoreboardService getInstance() {
        return ViewItPlugin.getInstance().getScoreboardService();
    }

    private static class Functions {
        private static Function<Player, String> REMAINING_TIME = new Function<Player, String>() {
            @Nullable
            @Override
            public String apply(Player player) {
                return getArena(player).getRemainingTime() + "s";
            }
        };

        private static Function<Player, String> GAME_NAME = new Function<Player, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Player player) {
                return getArena(player).getName();
            }
        };

        private static Function<Player, String> GAME_STATUS = new Function<Player, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Player player) {
                return getArena(player).getStatus();
            }
        };

        private static Function<Player, String> PLAYER_KDR = new Function<Player, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Player player) {
                return getArenaPlayer(player).getKDR() + "";
            }
        };

        private static Function<Player, String> PLAYER_KILLS = new Function<Player, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Player player) {
                return getArenaPlayer(player).getKills() + "";
            }
        };

        private static Function<Player, String> PLAYER_DEATHS = new Function<Player, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Player player) {
                return getArenaPlayer(player).getDeaths() + "";
            }
        };

        private static Function<Player, String> PLAYER_KILLSTREAK = new Function<Player, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Player player) {
                return getArenaPlayer(player).getKillStreak() + "";
            }
        };

        private static Function<Player, String> PLAYER_CLASS = new Function<Player, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Player player) {
                return getArenaPlayer(player).getArenaClass().getName();
            }
        };

        private static Arena getArena(Player player) {
            return plugin.getArena(player);
        }

        private static ArenaPlayer getArenaPlayer(Player player) {
            return plugin.getArenaPlayer(player);
        }
    }

}
