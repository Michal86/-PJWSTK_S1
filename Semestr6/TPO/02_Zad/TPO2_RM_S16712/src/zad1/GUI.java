package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;


public class GUI extends JFrame implements Runnable {
    private final Service service;

    public GUI(Service service) {
        super("WEBCLIENT - weathermap, exchangerate and NBP");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
            //System.out.println(ex);
        }

        this.service = service;
        guiInit();
    }

    private void guiInit() {
        Weather weather = service.getWeather();
        int width = 1024;
        int height = 768;

        setPreferredSize(new Dimension(width, height));
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return (width/3);
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 50;
            }
        });

        //INFO - pogoda + NBP
        JComponent jComponentInfo = new JPanel();
        initializeWeatherTab(jComponentInfo, jTabbedPane, weather);
        jTabbedPane.setBackgroundAt(0, Color.LIGHT_GRAY);

        //WIKI - Strona wiki z opisem miasta.
        JComponent jComponentWiki = new JPanel();
        jTabbedPane.addTab("Wiki: " + weather.getCity(), null, jComponentWiki, "Strona wiki z opisem miasta");

        final JFXPanel fxPanel = new JFXPanel();
        jComponentWiki.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            StackPane root = new StackPane();
            Scene scene = new Scene(root, jComponentWiki.getWidth(), jComponentWiki.getHeight() - 10);

            webEngine.load("https://wikipedia.org/wiki/" + weather.getCity());
            root.getChildren().add(webView);

            fxPanel.setScene(scene);
        });

        add(jTabbedPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeWeatherTab(JComponent topTab, JTabbedPane tabbedPane, Weather weather) {

        tabbedPane.addTab("Weather: " + weather.getCity(), null, topTab,
                "Weather in " + weather.getCity() + "," + weather.getCountry());

        topTab.setLayout(new BorderLayout());
        topTab.setBorder(new EmptyBorder(50, 50, 10, 10));
        topTab.setBackground(Color.LIGHT_GRAY);
        String strCity = "Weather information about: " + weather.getCity(),
               strCountry = weather.getCountry();

        JLabel jlCity = new JLabel(strCity);
        jlCity.setFont(new Font("Courier New", Font.PLAIN, 40));
        jlCity.setHorizontalAlignment(SwingConstants.LEFT);
        topTab.add(jlCity, BorderLayout.NORTH);

        //info
        JComponent contentLeft = new JPanel();
        contentLeft.setLayout(new BoxLayout(contentLeft, BoxLayout.Y_AXIS));
        contentLeft.setBackground(Color.LIGHT_GRAY);
        contentLeft.setBorder(new EmptyBorder(50, 10, 10, 10));

        JLabel jlCountry = new JLabel("Country: " + strCountry);
        jlCountry.setHorizontalAlignment(SwingConstants.LEFT);
        jlCountry.setFont(new Font("Courier New", Font.PLAIN, 20));
        contentLeft.add(jlCountry);

        String strTemperature = String.format("%.1f", weather.getTemperature()) + " \u00b0C";
        JLabel jlTemperature = new JLabel("Temperature: " + strTemperature);
        jlTemperature.setHorizontalAlignment(SwingConstants.LEFT);
        jlTemperature.setFont(new Font("Courier New", Font.PLAIN, 20));
        contentLeft.add(jlTemperature);

        String strDesc = weather.getSimpleDesc() + ", " + weather.getAdvanceDesc();
        JLabel jlDescription = new JLabel("Description: " + strDesc);
        jlDescription.setHorizontalAlignment(SwingConstants.LEFT);
        jlDescription.setFont(new Font("Courier New", Font.PLAIN, 20));
        contentLeft.add(jlDescription);

        String strDPress = weather.getAtmosphericPressure() + " Pa";
        JLabel jlPress = new JLabel("Pressure: " + strDPress);
        jlPress.setHorizontalAlignment(SwingConstants.LEFT);
        jlPress.setFont(new Font("Courier New", Font.PLAIN, 20));
        contentLeft.add(jlPress);

        String strExRate = "1 " + weather.getCountryCurrencyCode() + " = " + service.getdRate() + " " + service.getStrRateName();
        JLabel jlExchangeRate = new JLabel("Exchange Rate: " + strExRate);
        jlExchangeRate.setHorizontalAlignment(SwingConstants.CENTER);
        jlExchangeRate.setFont(new Font("Courier New", Font.PLAIN, 20));
        contentLeft.add(jlExchangeRate);

        String strNBPRate = service.getJsonNBPRate().optString("kurs_sredni") + " PLN" + " = " + service.getJsonNBPRate().optString("przelicznik") + " " + service.getWeather().getCountryCurrencyCode();
        JLabel jlNBPRate = new JLabel("NBP Rate: " + strNBPRate);
        jlNBPRate.setHorizontalAlignment(SwingConstants.CENTER);
        jlNBPRate.setFont(new Font("Courier New", Font.PLAIN, 20));
        contentLeft.add(jlNBPRate);

        topTab.add(contentLeft, BorderLayout.WEST);
    }

    @Override
    public void run() {
        setVisible(true);
    }
}
