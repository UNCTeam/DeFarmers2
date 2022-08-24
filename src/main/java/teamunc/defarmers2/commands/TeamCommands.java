package teamunc.defarmers2.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.GameAnnouncer;

public class TeamCommands extends AbstractCommandExecutor{
    public TeamCommands(Defarmers2 plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {

            if (cmd.getName().equalsIgnoreCase("team")) {
                String teamColor = "";
                switch (args.length) {
                    case 0: // no subcommand
                        GameAnnouncer.sendMessage(sender, "Team Commands:");
                        GameAnnouncer.sendMessage(sender, "/team");
                        GameAnnouncer.sendMessage(sender, "/team <join> <playerName> <teamColor>");
                        GameAnnouncer.sendMessage(sender, "/team <leave> <playerName> <teamColor>");
                        GameAnnouncer.sendMessage(sender, "/team <list>");
                        GameAnnouncer.sendMessage(sender, "/team <create> <teamColor>");
                        GameAnnouncer.sendMessage(sender, "/team <info> <teamColor>");
                        GameAnnouncer.sendMessage(sender, "/team <delete> <teamColor>");
                        GameAnnouncer.sendMessage(sender, "/team <score> <add> <teamColor> <number>");
                        GameAnnouncer.sendMessage(sender, "/team <score> <set> <teamColor> <number>");
                        GameAnnouncer.sendMessage(sender, "/team <score> <remove> <teamColor> <number>");
                        GameAnnouncer.sendMessage(sender, "/team <money> <add> <teamColor> <number>");
                        GameAnnouncer.sendMessage(sender, "/team <money> <set> <teamColor> <number>");
                        GameAnnouncer.sendMessage(sender, "/team <money> <remove> <teamColor> <number>");
                        break;

                    case 1: // subcommand list
                        if (args[0].equalsIgnoreCase("list")) {
                            StringBuilder teamList = new StringBuilder();
                            for (Team team : plugin.getGameManager().getTeamManager().getTeamStates().getAllTeams()) {
                                teamList.append(team.getName()).append(", ");
                            }
                            GameAnnouncer.sendMessage(sender, "Teams: " + teamList);
                        } else {
                            GameAnnouncer.sendMessage(sender, "Team Command not ok ! type /team to see more help !");
                        }
                        break;

                    case 2: // subcommand create / delete
                        teamColor = args[1].toUpperCase();
                        if (args[0].equalsIgnoreCase("create")) {
                            plugin.getGameManager().getTeamManager().addTeam(teamColor);
                            GameAnnouncer.sendMessage(sender, "Team " + teamColor + " created !");
                        } else if (args[0].equalsIgnoreCase("delete")) {
                            plugin.getGameManager().getTeamManager().removeTeam(teamColor);
                            GameAnnouncer.sendMessage(sender, "Team " + teamColor + " deleted !");
                        } else if (args[0].equalsIgnoreCase("info")) {
                            GameAnnouncer.sendMessage(sender, "Team " + teamColor + " info:");
                            GameAnnouncer.sendMessage(sender, "- Score: " + plugin.getGameManager().getTeamManager().getTeamScore(teamColor));
                            GameAnnouncer.sendMessage(sender, "- Money: " + plugin.getGameManager().getTeamManager().getTeamMoney(teamColor));
                        } else {
                            GameAnnouncer.sendMessage(sender, "Team Command not ok ! type /team to see more help !");
                        }
                        break;

                    case 3: // subcommand join / leave
                        teamColor = args[2].toUpperCase();
                        if (args[0].equalsIgnoreCase("join")) {
                            plugin.getGameManager().getTeamManager().joinTeam(args[1], teamColor);
                            GameAnnouncer.sendMessage(sender, "Player " + args[1] + " joined team " + teamColor + " !");

                        } else if (args[0].equalsIgnoreCase("leave")) {
                            plugin.getGameManager().getTeamManager().leaveTeam(args[1], teamColor);
                            GameAnnouncer.sendMessage(sender, "Player " + args[1] + " left team " + teamColor + " !");

                        } else {
                            GameAnnouncer.sendMessage(sender, "Team Command not ok ! type /team to see more help !");
                        }
                        break;

                    case 4: // subcommand score / money
                        teamColor = args[2].toUpperCase();
                        if (args[0].equalsIgnoreCase("score")) {
                            if (args[1].equalsIgnoreCase("add")) {
                                plugin.getGameManager().getTeamManager().addTeamScore(teamColor, Integer.parseInt(args[3]));
                                GameAnnouncer.sendMessage(sender, "Team " + teamColor + " score added " + args[3] + " !");
                            } else if (args[1].equalsIgnoreCase("set")) {
                                plugin.getGameManager().getTeamManager().setTeamScore(teamColor, Integer.parseInt(args[3]));
                                GameAnnouncer.sendMessage(sender, "Team " + teamColor + " score set to " + args[3] + " !");
                            } else if (args[1].equalsIgnoreCase("remove")) {
                                plugin.getGameManager().getTeamManager().removeTeamScore(teamColor, Integer.parseInt(args[3]));
                                GameAnnouncer.sendMessage(sender, "Team " + teamColor + " score removed " + args[3] + " !");
                            } else {
                                GameAnnouncer.sendMessage(sender, "Team Command not ok ! type /team to see more help !");
                            }
                        } else if (args[0].equalsIgnoreCase("money")) {
                            if (args[1].equalsIgnoreCase("add")) {
                                plugin.getGameManager().getTeamManager().addTeamMoney(teamColor, Integer.parseInt(args[3]));
                                GameAnnouncer.sendMessage(sender, "Team " + teamColor + " money added " + args[3] + " !");
                            } else if (args[1].equalsIgnoreCase("set")) {
                                plugin.getGameManager().getTeamManager().setTeamMoney(teamColor, Integer.parseInt(args[3]));
                                GameAnnouncer.sendMessage(sender, "Team " + teamColor + " money set to " + args[3] + " !");
                            } else if (args[1].equalsIgnoreCase("remove")) {
                                plugin.getGameManager().getTeamManager().removeTeamMoney(teamColor, Integer.parseInt(args[3]));
                                GameAnnouncer.sendMessage(sender, "Team " + teamColor + " money removed " + args[3] + " !");
                            } else {
                                GameAnnouncer.sendMessage(sender, "Team Command not ok ! type /team to see more help !");
                            }
                        } else {
                            GameAnnouncer.sendMessage(sender, "Team Command not ok ! type /team to see more help !");
                        }
                        break;
                }
            }
        } catch (Exception e) {
            GameAnnouncer.sendMessage(sender, "Team Command not ok ! type /team to see more help !");
        } finally {
            return true;
        }
    }
}
