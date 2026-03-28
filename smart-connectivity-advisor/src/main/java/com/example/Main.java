package com.example;

import java.util.EnumSet;
import java.util.Set;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Front-end entry point for the Smart Connectivity Advisor.
 */
public class Main extends Application {

    private final ConnectivityAIService aiService = new ConnectivityAIService();

    @Override
    public void start(Stage stage) {
        TextField budgetField = new TextField("60");
        budgetField.setPromptText("Enter monthly budget");
        budgetField.getStyleClass().add("text-input-friendly");

        Spinner<Integer> householdSpinner = new Spinner<>(1, 12, 4);
        Spinner<Integer> devicesSpinner = new Spinner<>(1, 50, 8);
        householdSpinner.setEditable(true);
        devicesSpinner.setEditable(true);
        householdSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        devicesSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        CheckBox schoolBox = new CheckBox("Schoolwork or homework support");
        CheckBox remoteWorkBox = new CheckBox("Remote work or video meetings");
        CheckBox streamingBox = new CheckBox("Streaming shows and movies");
        CheckBox gamingBox = new CheckBox("Online gaming");
        CheckBox browsingBox = new CheckBox("General browsing and social media");

        schoolBox.setSelected(true);
        remoteWorkBox.setSelected(true);
        streamingBox.setSelected(true);
        browsingBox.setSelected(true);

        styleCheckBox(schoolBox);
        styleCheckBox(remoteWorkBox);
        styleCheckBox(streamingBox);
        styleCheckBox(gamingBox);
        styleCheckBox(browsingBox);

        CheckBox reliabilityBox = new CheckBox("A stable connection is really important here");
        CheckBox outagesBox = new CheckBox("We have frequent outages right now");
        CheckBox slowSpeedsBox = new CheckBox("Our current service feels slow");

        reliabilityBox.setSelected(true);
        styleCheckBox(reliabilityBox);
        styleCheckBox(outagesBox);
        styleCheckBox(slowSpeedsBox);

        Label statusPill = new Label("Ready to personalize");
        statusPill.getStyleClass().add("status-pill");

        Label planValue = createValueLabel("Complete the profile to see your best-fit plan.");
        Label speedValue = createValueLabel("--");
        Label affordabilityValue = createValueLabel("--");
        Label reliabilityRiskValue = createValueLabel("--");
        Label communityNeedValue = createValueLabel("--");
        Label backupValue = createValueLabel("--");

        Label summaryHeadline = new Label("Your household recommendation will appear here.");
        summaryHeadline.getStyleClass().add("summary-headline");
        summaryHeadline.setWrapText(true);

        Label summarySubheadline = new Label("Use the form to compare speed, price fit, and reliability guidance in one place.");
        summarySubheadline.getStyleClass().add("summary-subheadline");
        summarySubheadline.setWrapText(true);

        Label summaryFootnote = new Label("Tip: affordability is better when the score is higher, and reliability risk is better when the score is lower.");
        summaryFootnote.getStyleClass().add("summary-footnote");
        summaryFootnote.setWrapText(true);

        TextArea explanationArea = new TextArea();
        explanationArea.setEditable(false);
        explanationArea.setWrapText(true);
        explanationArea.setPrefRowCount(6);
        explanationArea.setText("Once you generate a recommendation, we will explain why the plan fits your home.");
        explanationArea.getStyleClass().add("explanation-area");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);

        Button recommendButton = new Button("See My Recommendation");
        recommendButton.setDefaultButton(true);
        recommendButton.getStyleClass().add("primary-button");

        recommendButton.setOnAction(event -> {
            try {
                int budget = parseBudget(budgetField.getText());
                Set<UsageType> usageTypes = collectUsageTypes(
                        schoolBox,
                        remoteWorkBox,
                        streamingBox,
                        gamingBox,
                        browsingBox
                );

                UserProfile profile = new UserProfile(
                        budget,
                        householdSpinner.getValue(),
                        devicesSpinner.getValue(),
                        usageTypes,
                        reliabilityBox.isSelected(),
                        outagesBox.isSelected(),
                        slowSpeedsBox.isSelected()
                );

                RecommendationResult result = aiService.recommend(profile);
                renderResult(
                        result,
                        statusPill,
                        summaryHeadline,
                        summarySubheadline,
                        planValue,
                        speedValue,
                        affordabilityValue,
                        reliabilityRiskValue,
                        communityNeedValue,
                        backupValue,
                        explanationArea
                );

                errorLabel.setText("");
            } catch (IllegalArgumentException ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("details-grid");
        formGrid.setHgap(18);
        formGrid.setVgap(18);
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPercentWidth(48);
        ColumnConstraints valueColumn = new ColumnConstraints();
        valueColumn.setPercentWidth(52);
        valueColumn.setHgrow(Priority.ALWAYS);
        formGrid.getColumnConstraints().addAll(labelColumn, valueColumn);

        formGrid.add(createFieldBlock("Monthly budget", "How much you can comfortably spend each month."), 0, 0);
        formGrid.add(budgetField, 1, 0);
        formGrid.add(createFieldBlock("Household size", "People sharing the connection at home."), 0, 1);
        formGrid.add(householdSpinner, 1, 1);
        formGrid.add(createFieldBlock("Connected devices", "Phones, TVs, laptops, tablets, consoles, and more."), 0, 2);
        formGrid.add(devicesSpinner, 1, 2);

        VBox usageBox = new VBox(
                10,
                createHintLabel("Choose all the activities that matter most in this household."),
                schoolBox,
                remoteWorkBox,
                streamingBox,
                gamingBox,
                browsingBox
        );

        VBox situationBox = new VBox(
                10,
                createHintLabel("These help the recommendation weigh resilience and backup options."),
                reliabilityBox,
                outagesBox,
                slowSpeedsBox
        );

        VBox tipsBox = new VBox(
                10,
                createTipRow("The recommendation tries to stay within budget before suggesting a more expensive plan."),
                createTipRow("If no activities are selected, the app assumes light browsing needs."),
                createTipRow("Mobile backup is suggested when uptime matters most or outages are happening.")
        );

        VBox quickStartBox = new VBox(
                8,
                createHintLabel("Start here"),
                createIntroText("Enter your monthly budget, choose the activities your home depends on, and then click the button to see a recommendation.")
        );

        VBox inputContent = new VBox(
                18,
                createHeroCard(),
                quickStartBox,
                createCard("Household Snapshot", formGrid),
                createCard("How You Use The Internet", usageBox),
                createCard("Reliability & Pain Points", situationBox),
                createCard("Helpful Notes", tipsBox),
                errorLabel,
                recommendButton
        );
        inputContent.getStyleClass().add("panel");
        inputContent.setPadding(new Insets(28));
        inputContent.setPrefWidth(430);

        ScrollPane inputScroll = new ScrollPane(inputContent);
        inputScroll.setFitToWidth(true);
        inputScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        inputScroll.getStyleClass().add("panel-scroll");
        inputScroll.setPrefWidth(450);

        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(16);
        metricsGrid.setVgap(16);
        ColumnConstraints metricColumn = new ColumnConstraints();
        metricColumn.setPercentWidth(50);
        metricColumn.setHgrow(Priority.ALWAYS);
        metricsGrid.getColumnConstraints().addAll(metricColumn, metricColumn);
        metricsGrid.add(createMetricCard("Recommended Plan", planValue), 0, 0);
        metricsGrid.add(createMetricCard("Estimated Speed", speedValue), 1, 0);
        metricsGrid.add(createMetricCard("Affordability Score", affordabilityValue), 0, 1);
        metricsGrid.add(createMetricCard("Reliability Risk", reliabilityRiskValue), 1, 1);
        metricsGrid.add(createMetricCard("Community Need", communityNeedValue), 0, 2);
        metricsGrid.add(createMetricCard("Mobile Backup", backupValue), 1, 2);

        VBox summaryCard = new VBox(10, statusPill, summaryHeadline, summarySubheadline, summaryFootnote);
        summaryCard.getStyleClass().add("summary-card");

        VBox resultsPanel = new VBox(
                18,
                createResultsHeader(),
                summaryCard,
                metricsGrid,
                createCard("Why This Recommendation Works", explanationArea)
        );
        resultsPanel.getStyleClass().add("panel");
        resultsPanel.setPadding(new Insets(28));
        VBox.setVgrow(explanationArea, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-shell");
        root.setLeft(inputScroll);
        root.setCenter(resultsPanel);

        Scene scene = new Scene(root, 1260, 820);
        scene.getStylesheets().add(
                Main.class.getResource("/com/example/app.css").toExternalForm()
        );

        stage.setMinWidth(1100);
        stage.setMinHeight(760);
        stage.setTitle("Smart Connectivity Advisor");
        stage.setScene(scene);
        stage.show();

        recommendButton.fire();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static int parseBudget(String rawBudget) {
        try {
            int budget = Integer.parseInt(rawBudget.trim());
            if (budget <= 0) {
                throw new IllegalArgumentException("Monthly budget must be greater than 0.");
            }
            return budget;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Enter a valid whole-number monthly budget.");
        }
    }

    private static Set<UsageType> collectUsageTypes(CheckBox schoolBox,
                                                    CheckBox remoteWorkBox,
                                                    CheckBox streamingBox,
                                                    CheckBox gamingBox,
                                                    CheckBox browsingBox) {
        EnumSet<UsageType> usageTypes = EnumSet.noneOf(UsageType.class);

        if (schoolBox.isSelected()) {
            usageTypes.add(UsageType.SCHOOL);
        }
        if (remoteWorkBox.isSelected()) {
            usageTypes.add(UsageType.REMOTE_WORK);
        }
        if (streamingBox.isSelected()) {
            usageTypes.add(UsageType.STREAMING);
        }
        if (gamingBox.isSelected()) {
            usageTypes.add(UsageType.GAMING);
        }
        if (browsingBox.isSelected()) {
            usageTypes.add(UsageType.BROWSING);
        }

        if (usageTypes.isEmpty()) {
            usageTypes.add(UsageType.BROWSING);
        }

        return usageTypes;
    }

    private static void renderResult(RecommendationResult result,
                                     Label statusPill,
                                     Label summaryHeadline,
                                     Label summarySubheadline,
                                     Label planValue,
                                     Label speedValue,
                                     Label affordabilityValue,
                                     Label reliabilityRiskValue,
                                     Label communityNeedValue,
                                     Label backupValue,
                                     TextArea explanationArea) {
        Plan plan = result.getRecommendedPlan();
        planValue.setText(plan.getName() + "  |  $" + plan.getMonthlyPrice() + "/mo");
        speedValue.setText(result.getEstimatedSpeedNeed() + " Mbps");
        affordabilityValue.setText(result.getAffordabilityScore() + "/100");
        reliabilityRiskValue.setText(result.getReliabilityRiskScore() + "/100");
        communityNeedValue.setText(result.getCommunityNeedScore() + "/100");
        backupValue.setText(result.isRecommendMobileBackup() ? "Recommended" : "Not needed");
        explanationArea.setText(result.getExplanation());

        statusPill.setText(result.isRecommendMobileBackup() ? "Plan plus backup guidance" : "Plan recommendation ready");
        summaryHeadline.setText("Best fit: " + plan.getName());
        summarySubheadline.setText(
                "This option balances your budget, expected speed needs, and reliability priorities for the household profile you entered."
        );
    }

    private static VBox createHeroCard() {
        Label eyebrow = new Label("Smart Connectivity Advisor");
        eyebrow.getStyleClass().add("hero-eyebrow");

        Label title = new Label("Find a home internet plan that feels practical, stable, and easy to trust.");
        title.setWrapText(true);
        title.getStyleClass().add("hero-title");

        Label subtitle = new Label("Share a few household details and we will turn them into a clearer recommendation with budget and reliability context.");
        subtitle.setWrapText(true);
        subtitle.getStyleClass().add("hero-subtitle");

        HBox chips = new HBox(
                10,
                createChip("Budget-aware"),
                createChip("Family-friendly"),
                createChip("Reliability-focused")
        );

        VBox hero = new VBox(12, eyebrow, title, subtitle, chips);
        hero.getStyleClass().add("hero-card");
        hero.setPadding(new Insets(24));
        return hero;
    }

    private static VBox createResultsHeader() {
        Label title = new Label("Recommendation Dashboard");
        title.getStyleClass().add("section-title");

        Label subtitle = new Label("A simpler view of what your household likely needs and why.");
        subtitle.getStyleClass().add("section-subtitle");

        Separator separator = new Separator();
        separator.getStyleClass().add("soft-separator");

        VBox box = new VBox(6, title, subtitle, separator);
        return box;
    }

    private static VBox createCard(String titleText, javafx.scene.Node content) {
        Label title = new Label(titleText);
        title.getStyleClass().add("card-title");

        VBox card = new VBox(14, title, content);
        card.getStyleClass().add("content-card");
        card.setPadding(new Insets(20));
        return card;
    }

    private static VBox createMetricCard(String labelText, Label valueLabel) {
        Label label = new Label(labelText);
        label.getStyleClass().add("metric-label");

        VBox box = new VBox(10, label, valueLabel);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(18));
        box.setMinHeight(118);
        box.getStyleClass().add("metric-card");
        return box;
    }

    private static VBox createFieldBlock(String title, String description) {
        Label titleLabel = createSectionLabel(title);
        Label descriptionLabel = createHintLabel(description);
        return new VBox(4, titleLabel, descriptionLabel);
    }

    private static Label createValueLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("value-label");
        return label;
    }

    private static Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("field-label");
        return label;
    }

    private static Label createHintLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("hint-label");
        return label;
    }

    private static Label createIntroText(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("intro-text");
        return label;
    }

    private static HBox createTipRow(String text) {
        Label dot = new Label("•");
        dot.getStyleClass().add("tip-dot");

        Label content = new Label(text);
        content.setWrapText(true);
        content.getStyleClass().add("tip-text");

        HBox row = new HBox(10, dot, content);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    private static Label createChip(String text) {
        Label chip = new Label(text);
        chip.getStyleClass().add("hero-chip");
        return chip;
    }

    private static void styleCheckBox(CheckBox checkBox) {
        checkBox.getStyleClass().add("friendly-check");
        checkBox.setWrapText(true);
        checkBox.setMaxWidth(Double.MAX_VALUE);
    }
}
