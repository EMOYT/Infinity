package infinity;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.TextButton.TextButtonStyle;
import arc.scene.ui.layout.Table;
import arc.util.CommandHandler;
import arc.util.Http;
import arc.util.Log;
import arc.util.Threads;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.serialization.Jval;
import java.util.Iterator;
import java.util.Objects;
import mindustry.Vars;
import mindustry.core.Logic;
import mindustry.core.World;
import mindustry.ctype.ContentType;
import mindustry.game.Team;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.mod.Mod;
import mindustry.mod.Mods.LoadedMod;
import mindustry.type.Item;
import mindustry.type.StatusEffect;
import mindustry.ui.Styles;
import mindustry.ui.Links.LinkEntry;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.PlanetDialog;
import mindustry.world.modules.ItemModule;
import infinity.infinity.1;
import infinity.util.ui.FeatureLog;
import infinity.util.ui.TableFunc;
import infinity.util.ui.dialog.NewFeatureDialog;

public class infinity extends Mod {
   public static boolean DEBUGGING = false;
   public static final boolean DEBUGGING_SPRITE = false;
   protected static boolean contentLoadComplete = false;
   public static final String MOD_RELEASES = "https://github.com/EMOYT/Infinity/releases";
   public static final String MOD_REPO = "Yuria-Shikibe/NewHorizonMod";
   public static final String MOD_GITHUB_URL = "https://github.com/EMOYT/Infinity";
   public static final String MOD_NAME = "infinity";
   private static boolean showed = false;
   public static LoadedMod MOD;
   public static LinkEntry[] links;

   public static void debugLog(Object obj) {
      if (DEBUGGING) {
         Log.info(obj);
      }

   }

   public static final boolean loadedComplete() {
      return contentLoadComplete;
   }

   public static String name(String name) {
      return "infinity-" + name;
   }

   public static FeatureLog[] getUpdateContent() {
      return new FeatureLog[]{new FeatureLog(ProductionBlocks.resonanceMiningFacility), new FeatureLog(ProductionBlocks.beamMiningFacility), new FeatureLog(ProductionBlocks.implosionMiningFacility), new FeatureLog(ProductionBlocks.refineModule), new FeatureLog(ProductionBlocks.speedModule), new FeatureLog(ProductionBlocks.deliveryModule)};
   }

   private static void showAbout() {
      if (links == null) {
         links = new LinkEntry[]{new LinkEntry("mod.ccs", "https://github.com/Yuria-Shikibe/NewHorizonMod/wiki/Cutscene-Script-Custom-Guide", Icon.settings, Pal.heal), new LinkEntry("mod.discord", "https://discord.gg/yNmbMcuwyW", Icon.discord, Color.valueOf("7289da")), new LinkEntry("mod.github", "https://github.com/Yuria-Shikibe/NewHorizonMod.git", Icon.github, Color.valueOf("24292e")), new LinkEntry("mod.guide", "https://github.com/Yuria-Shikibe/NewHorizonMod#mod-guide", Icon.bookOpen, Pal.accent), new LinkEntry("yuria.plugin", "https://github.com/Yuria-Shikibe/RangeImager", Icon.export, NHColor.thurmixRed)};
      }

      BaseDialog dialog = new BaseDialog("@links");
      dialog.cont.pane((table) -> {
         LinkEntry[] var1 = links;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            LinkEntry entry = var1[var3];
            TableFunc.link(table, entry);
         }

      }).grow().row();
      Table var10000 = dialog.cont;
      TextureRegionDrawable var10002 = Icon.left;
      TextButtonStyle var10003 = Styles.cleart;
      Objects.requireNonNull(dialog);
      var10000.button("@back", var10002, var10003, dialog::hide).size(240.0F, 60.0F);
      dialog.addCloseListener();
      dialog.show();
   }

   public static void showNew() {
      NewFeatureDialog newFeatureDialog = new NewFeatureDialog();
      newFeatureDialog.show();
   }

   public static void startLog() {
      if (!showed) {
         showed = true;
         Runnable runnable = () -> {
            Core.app.post(() -> {
               Core.app.post(() -> {
                  Core.settings.getBoolOnce("I-first-load", () -> {
                     (new 1("CAUTION")).show();
                  });
               });
            });
         };
         BaseDialog dialog = new 2("", runnable);
         dialog.closeOnBack(runnable);
         dialog.cont.pane((inner) -> {
            inner.pane((table) -> {
               table.table((t) -> {
                  t.image(NHContent.icon2).fill();
               }).center().growX().fillY().row();
               table.image().fillX().height(4.3636365F).pad(4.0F).color(Color.white).row();
               table.pane((p) -> {
                  p.add("[white]<< Powered by Infinity Mod >>", Styles.techLabel).row();
               }).fillY().growX().row();
               table.image().fillX().height(4.3636365F).pad(4.0F).color(Color.white).row();
               table.add("").row();
            }).growX().center().row();
            inner.table((table) -> {
               if (!Vars.mobile) {
                  table.table((t) -> {
                  }).grow();
               }

               table.table((t) -> {
                  TextureRegionDrawable var10002 = Icon.left;
                  TextButtonStyle var10003 = Styles.cleart;
                  Objects.requireNonNull(dialog);
                  t.button("@back", var10002, var10003, dialog::hide).growX().height(60.0F).padLeft(12.0F).padRight(12.0F).row();
                  t.button("@links", Icon.link, Styles.cleart, NewHorizon::showAbout).growX().height(60.0F).padLeft(12.0F).padRight(12.0F).row();
                  t.button("@hide-setting", Icon.settings, Styles.cleart, () -> {
                     Core.settings.put("I_hide_starting_log", true);
                  }).disabled((b) -> {
                     return Core.settings.getBool("I_hide_starting_log", false);
                  }).growX().height(60.0F).padLeft(12.0F).padRight(12.0F).row();
                  t.button(Core.bundle.get("servers.remote") + "\n(" + Core.bundle.get("waves.copy") + ")", Icon.host, Styles.cleart, () -> {
                     Core.app.setClipboardText("175.178.22.6:6666");
                  }).growX().height(60.0F).padLeft(12.0F).padRight(12.0F).row();
               }).grow();
               if (!Vars.mobile) {
                  table.table((t) -> {
                  }).grow();
               }

            }).fill();
         }).grow();
         dialog.show();
      }
   }

   public infinity() {
      DEBUGGING = NHSetting.getBool("I_debugging");
      if (DEBUGGING) {
         PlanetDialog.debugSelect = true;
         Events.run(Trigger.universeDrawEnd, DebugFunc::renderSectorId);
      }

      IInputListener.registerModBinding();
      Events.on(ClientLoadEvent.class, (e) -> {
         Core.app.post(NHUI::init);
         this.updateServer();
         this.fetchNewRelease();
         this.showNewDialog();
         this.showStartLog();
         Time.run(10.0F, () -> {
         });
      });
      Events.run(Trigger.draw, () -> {
         if (ISetting.getBool("I_terrain_mode")) {
            IModCore.control.terrainSelect();
         }

      });
   }

   public void init() {
      Vars.netServer.admins.addChatFilter((player, text) -> {
         return text.replace("jvav", "java");
      });
      Core.app.addListener(new IModCore());
      NIVars.worldData = new IWorldData();
      ICSS_UI.init();
      if (!Vars.headless) {
         ISetting.loadUI();
         EffectDrawer.drawer.init();
         if (ISetting.getBool("I_debug_panel")) {
            TableFunc.tableMain();
         }

      }
   }

   public void registerClientCommands(CommandHandler handler) {
      handler.register("applystatus", "Apply a status to player's unit", (args, player) -> {
         if (!player.admin()) {
            player.sendMessage("[VIOLET]Admin Only");
         } else if (args.length != 0 && !args[0].isEmpty()) {
            try {
               player.unit().apply((StatusEffect)Vars.content.getByID(ContentType.status, Integer.parseInt(args[0])), 7200.0F);
            } catch (NumberFormatException var4) {
               player.sendMessage("[VIOLET]Failed, the param must be a <Number>");
            }
         } else {
            Iterator var2 = Vars.content.statusEffects().iterator();

            while(var2.hasNext()) {
               StatusEffect s = (StatusEffect)var2.next();
               player.sendMessage(s.name + "|" + s.id);
            }
         }

      });
      handler.register("runwave", "<num>", "Run Wave (Admin Only)", (args, player) -> {
         if (!player.admin()) {
            player.sendMessage("[VIOLET]Admin Only");
         } else if (args.length != 0 && !args[0].isEmpty()) {
            try {
               for(int i = 0; i < Integer.parseInt(args[0]); ++i) {
                  float var10000 = (float)i * 90.0F;
                  Logic var10001 = Vars.logic;
                  Objects.requireNonNull(var10001);
                  Time.run(var10000, var10001::runWave);
               }
            } catch (NumberFormatException var3) {
               player.sendMessage("[VIOLET]Failed, the param must be a <Number>");
            }
         } else {
            Vars.logic.runWave();
         }

      });
      handler.register("runevent", "<id>", "Trigger Event (Admin Only)", (args, player) -> {
         if (!player.admin()) {
            player.sendMessage("[VIOLET]Admin Only");
         } else if (args.length == 0) {
            player.sendMessage("[VIOLET]Failed, pls type ID");
         } else {
            try {
               WorldEvent event = (WorldEvent)NHGroups.events.getByID(Integer.parseInt(args[0]));
               event.type.triggerNet(event);
               player.sendMessage("Triggered: " + event);
            } catch (NumberFormatException var3) {
               player.sendMessage("[VIOLET]Failed, the ID must be a <Number>");
            }
         }

      });
      handler.register("setupevent", "<name> [team] [x] [y]", "Setup Event (Admin Only)", (args, player) -> {
         if (!player.admin()) {
            player.sendMessage("[VIOLET]Admin Only");
         } else if (args.length == 0) {
            player.sendMessage("[VIOLET]Failed, pls type ID");
         } else {
            try {
               WorldEventType event = WorldEventType.getStdType(args[0]);
               WorldEvent e = (WorldEvent)event.eventProv.get();
               e.type = event;
               e.init();
               e.add();
               if (event.initPos != -1 && event.hasCoord) {
                  Tmp.p1.set(Point2.unpack(event.initPos));
                  e.set((float)(Tmp.p1.x * 8), (float)(Tmp.p1.y * 8));
               }

               if (args.length >= 2) {
                  e.team = Team.get(Integer.parseInt(args[1]));
                  if (args.length >= 4) {
                     e.set((float)(Integer.parseInt(args[2]) * 8 + 4), (float)(Integer.parseInt(args[3]) * 8 + 4));
                  }
               }

               if (args.length >= 2) {
                  Core.app.post(() -> {
                     e.team = Team.get(Integer.parseInt(args[1]));
                  });
               }

               player.sendMessage("Setup: " + event + " | " + e.team + " | (" + World.toTile(e.x) + ", " + World.toTile(e.y) + ")");
            } catch (NumberFormatException var5) {
               player.sendMessage("[VIOLET]Undefined<Number>");
            }
         }

      });
      handler.register("fill", "<id>", "Trigger Event (Admin Only)", (args, player) -> {
         if (!player.admin()) {
            player.sendMessage("[VIOLET]Admin Only");
         } else {
            ItemModule module;
            Iterator var3;
            Item i;
            if (args.length != 0 && !args[0].isEmpty()) {
               module = Team.get(Integer.parseInt(args[0])).core().items;
               var3 = Vars.content.items().iterator();

               while(var3.hasNext()) {
                  i = (Item)var3.next();
                  module.set(i, 1000000);
               }
            } else {
               module = player.team().core().items;
               var3 = Vars.content.items().iterator();

               while(var3.hasNext()) {
                  i = (Item)var3.next();
                  module.set(i, 1000000);
               }
            }
         }

      });
      handler.register("killBelow", "<health>", "Trigger Event (Admin Only)", (args, player) -> {
         if (!player.admin()) {
            player.sendMessage("[VIOLET]Admin Only");
         } else if (args.length != 0 && !args[0].isEmpty()) {
            Groups.unit.each((b) -> {
               return b.type.health < (float)Integer.parseInt(args[0]);
            }, (b) -> {
               float var10000 = (float)Mathf.random(60, 300);
               Objects.requireNonNull(b);
               Time.run(var10000, b::kill);
            });
         } else {
            Groups.unit.each((b) -> {
               return b.type.health < 800.0F;
            }, (b) -> {
               float var10000 = (float)Mathf.random(60, 300);
               Objects.requireNonNull(b);
               Time.run(var10000, b::kill);
            });
         }

      });
      handler.register("killteam", "<id>", "Destroy The Team (Admin Only)", (args, player) -> {
         Cons<Team> destroyer = (t) -> {
            Groups.build.each((b) -> {
               return b.team == t;
            }, (b) -> {
               float var10000 = (float)Mathf.random(60, 300);
               Objects.requireNonNull(b);
               Time.run(var10000, b::kill);
            });
            Groups.unit.each((b) -> {
               return b.team == t;
            }, (b) -> {
               float var10000 = (float)Mathf.random(60, 300);
               Objects.requireNonNull(b);
               Time.run(var10000, b::kill);
            });
         };
         if (!player.admin()) {
            player.sendMessage("[VIOLET]Admin Only");
         } else if (args.length != 0 && !args[0].isEmpty()) {
            destroyer.get(Team.get(Integer.parseInt(args[0])));
            player.sendMessage("Killed: [accent]" + Team.get(Integer.parseInt(args[0])));
         } else {
            destroyer.get(player.team());
         }

      });
      handler.register("events", "List all cutscene events in the map.", (args, player) -> {
         if (NHGroups.events.isEmpty()) {
            player.sendMessage("No Event Available");
         } else {
            StringBuilder builder = new StringBuilder();
            builder.append("[accent]Events: [lightgray]\n");
            NHGroups.events.each((e) -> {
               builder.append(e).append('\n');
            });
            player.sendMessage(builder.toString());
         }

      });
      handler.register("eventtypes", "List all cutscene event types in the map.", (args, player) -> {
         if (WorldEventType.allTypes.isEmpty()) {
            player.sendMessage("No EventTypes Available");
         } else {
            StringBuilder builder = new StringBuilder();
            builder.append("[accent]Events: [lightgray]\n");
            WorldEventType.allTypes.each((k, e) -> {
               builder.append(e.getClass().getSuperclass().getSimpleName()).append("->").append(k).append('\n');
            });
            player.sendMessage(builder.toString());
         }

      });
      handler.register("eventtriggers", "List all event triggers in the map.", (args, player) -> {
         if (NHGroups.autoEventTrigger.isEmpty()) {
            player.sendMessage("No Trigger Available");
         } else {
            StringBuilder builder = new StringBuilder();
            NHGroups.autoEventTrigger.each((e) -> {
               builder.append("\n======================\n");
               builder.append("[royal]").append(e.toString()).append(" | ").append(e.eventType.name).append(" | [lightgray]").append('\n').append(e.desc()).append('\n').append("Meet Requirements?: ").append(e.meet() ? "[heal]Yes[]" : "[#ff7b69]No[]").append("[lightgray]\n").append("Reload: ").append(e.getReload()).append("\nSpacing: ").append(e.getSpacing()).append('\n');
               builder.append("Percentage: [accent]").append((int)(e.getReload() / e.getSpacing() * 100.0F)).append("[]%\n");
               builder.append("======================\n");
            });
            NHCall.infoDialog(builder.toString(), player.con);
         }

      });
      handler.register("getscale", "Check Auto Event Trigger Time Scale", (args, player) -> {
         player.sendMessage("Scale: " + AutoEventTrigger.timeScale);
      });
      handler.register("setscale", "<Scale>", "Set Auto Event Trigger Time Scale (Admin Only)", (args, player) -> {
         if (!player.admin()) {
            player.sendMessage("[VIOLET]Admin Only");
         } else if (args.length == 0) {
            AutoEventTrigger.setScale(0.8F);
            player.sendMessage("Set to: [accent]0.8");
         } else {
            try {
               AutoEventTrigger.setScale(Float.parseFloat(args[0]));
               player.sendMessage("Set to: [accent]" + Float.parseFloat(args[0]));
            } catch (NumberFormatException var3) {
               player.sendMessage(var3.toString());
            }
         }

      });
   }

   public void loadContent() {
      contentLoadComplete = false;
      Log.info("Debug Mode: " + DEBUGGING);
      Log.info("Process Texture Mode: " + IPixmap.isDebugging());
      Time.mark();
      MOD = Vars.mods.getMod(this.getClass());
      EntityRegister.load();
      IRegister.load();
      IContent.loadPriority();
      ISounds.load();
      if (!Vars.headless) {
         NHShaders.init();
      }

      IContent.loadBeforeContentLoad();
      IStatusEffects.load();
      IItems.load();
      ILiquids.load();
      IBullets.load();
      IUnitTypes.load();
      Recipes.load();
      IBlocks.load();
      IWeathers.load();
      IPlanets.load();
      ISectorPresents.load();
      ITechTree.load();
      IInbuiltEvents.load();
      ISetting.load();
      IOverride.load();
      if (Vars.headless || ISetting.getBool("nh_overridecost")) {
         IOverride.loadOptional();
      }

      IContent.loadLast();
      contentLoadComplete = true;
      Log.info(MOD.meta.displayName + " Loaded Complete: " + MOD.meta.version + " | Cost Time: " + Time.elapsed() / 60.0F + " sec.");
   }

   private void updateServer() {
      Vars.defaultServers.add(new 3(this));
   }


   private void showNewDialog() {
      Time.runTask(10.0F, () -> {
         if (!Core.settings.get("I-lastver", -1).equals(MOD.meta.version)) {
            showNew();
         }

         Core.settings.put("I-lastver", MOD.meta.version);
      });
   }

   private void showStartLog() {
      if (!Core.settings.getBool("I_hide_starting_log")) {
         Core.app.post(Time.runTask(10.0F, infinity::startLog));
      }

   }
}
