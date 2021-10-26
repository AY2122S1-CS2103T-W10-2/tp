package seedu.teachbook;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.teachbook.commons.core.Config;
import seedu.teachbook.commons.core.LogsCenter;
import seedu.teachbook.commons.core.Version;
import seedu.teachbook.commons.exceptions.DataConversionException;
import seedu.teachbook.commons.util.ConfigUtil;
import seedu.teachbook.commons.util.StringUtil;
import seedu.teachbook.logic.Logic;
import seedu.teachbook.logic.LogicManager;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.ModelManager;
import seedu.teachbook.model.ReadOnlyTeachBook;
import seedu.teachbook.model.ReadOnlyUserPrefs;
import seedu.teachbook.model.TeachBook;
import seedu.teachbook.model.UserPrefs;
import seedu.teachbook.model.util.SampleDataUtil;
import seedu.teachbook.storage.JsonTeachBookStorage;
import seedu.teachbook.storage.JsonUserPrefsStorage;
import seedu.teachbook.storage.Storage;
import seedu.teachbook.storage.StorageManager;
import seedu.teachbook.storage.TeachBookStorage;
import seedu.teachbook.storage.UserPrefsStorage;
import seedu.teachbook.ui.Ui;
import seedu.teachbook.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 3, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing TeachBook ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        TeachBookStorage teachBookStorage = new JsonTeachBookStorage(userPrefs.getTeachBookFilePath());
        storage = new StorageManager(teachBookStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s teachbook and {@code userPrefs}. <br>
     * The data from the sample teachbook will be used instead if {@code storage}'s teachbook is not found,
     * or an empty teachbook will be used instead if errors occur when reading {@code storage}'s teachbook.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyTeachBook> teachBookOptional;
        ReadOnlyTeachBook initialData;
        try {
            teachBookOptional = storage.readTeachBook();
            if (teachBookOptional.isEmpty()) {
                logger.info("Data file not found. Will be starting with a sample TeachBook");
            }
            initialData = teachBookOptional.orElseGet(SampleDataUtil::getSampleTeachBook);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty TeachBook");
            initialData = new TeachBook();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty TeachBook");
            initialData = new TeachBook();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty TeachBook");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting TeachBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping TeachBook ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
