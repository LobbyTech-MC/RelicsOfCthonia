package ne.fnfal113.relicsofcthonia;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import ne.fnfal113.relicsofcthonia.config.ConfigManager;
import ne.fnfal113.relicsofcthonia.items.RelicsItemSetup;
import ne.fnfal113.relicsofcthonia.listeners.MiningListener;
import ne.fnfal113.relicsofcthonia.listeners.MobKillListener;
import ne.fnfal113.relicsofcthonia.listeners.OffHandClickListener;
import ne.fnfal113.relicsofcthonia.listeners.PiglinMainListener;
import ne.fnfal113.relicsofcthonia.listeners.RelicPlaceBreakListener;
import ne.fnfal113.relicsofcthonia.listeners.RelicVoiderListener;

import net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Level;

public final class RelicsOfCthonia extends JavaPlugin implements SlimefunAddon {

    private static RelicsOfCthonia instance;

    private final ConfigManager configManager = new ConfigManager();

    private final RelicsRegistry relicsRegistry = new RelicsRegistry();

    @Override
    public void onEnable() {
        setInstance(this);

        if (!getServer().getPluginManager().isPluginEnabled("GuizhanLibPlugin")) {
            getLogger().log(Level.SEVERE, "本插件需要 鬼斩前置库插件(GuizhanLibPlugin) 才能运行!");
            getLogger().log(Level.SEVERE, "从此处下载: https://50l.cc/gzlib");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new Metrics(this, 15420);

        getLogger().info("************************************************************");
        getLogger().info("               Relics of Cthonia - 克苏尼亚遗物               ");
        getLogger().info("         作者: FN_FAL113  汉化: SlimefunGuguProject           ");
        getLogger().info("                 2022粘液科技附属竞赛参赛作品                    ");
        getLogger().info("************************************************************");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        RelicsItemSetup.INSTANCE.init();

        registerEvents();

        if (getConfig().getBoolean("auto-update", true) && getDescription().getVersion().startsWith("Build")) {
            GuizhanUpdater.start(this, getFile(), "SlimefunGuguProject", "RelicsOfCthonia", "main");
        }

    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(), this);
        getServer().getPluginManager().registerEvents(new PiglinMainListener(), this);
        getServer().getPluginManager().registerEvents(new OffHandClickListener(), this);
        getServer().getPluginManager().registerEvents(new RelicPlaceBreakListener(), this);
        getServer().getPluginManager().registerEvents(new RelicVoiderListener(), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(RelicsOfCthonia.getInstance());
        getLogger().log(Level.INFO, "已取消所有任务");
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/SlimefunGuguProject/RelicsOfCthonia/issues";
    }

    private static void setInstance(RelicsOfCthonia ins) {
        instance = ins;
    }

    public static RelicsOfCthonia getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager(){
        return instance.configManager;
    }

    public RelicsRegistry getRelicsRegistry(){
        return instance.relicsRegistry;
    }
}
