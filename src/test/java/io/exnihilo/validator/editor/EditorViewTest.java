package io.exnihilo.validator.editor;

import io.exnihilo.validator.WebAutomationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Selenium driver automation tests for SustainabilityView
 *
 * @author Anand Varkey Philips
 * @since 1.0.0
 */
@Slf4j
public class EditorViewTest {
    private static WebAutomationService webAutomationService;
    private static WebDriver webDriver;

    static private final String EDITOR_HTML_ID = "editor";
    static private final String APPLICATION_URL = "http://localhost:4200";

    @BeforeClass
    public static void setUpCommonTestCodeForAllTests() {
        webAutomationService = new WebAutomationService();
        webDriver = webAutomationService.setUp();
    }

    @Before
    public void setUp() {
        webDriver.get(APPLICATION_URL);
        new WebDriverWait(webDriver, 20).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    @AfterClass
    public static void tearDown() {
        webDriver.quit();
    }

    @Test
    public void validateYamlService_whenValidYaml_returnTrue() throws Exception {
        String inputData = new String(Files.readAllBytes(Paths.get("src/test/resources/valid-yaml.yml")), StandardCharsets.UTF_8);

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue(\"" + inputData + "\", 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("validateYamlData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"),"textContent","Valid YAML!!!"));
    }

    @Test
    public void validateYamlService_whenInvalidYaml_returnFalse() throws Exception {
        String inputData = new String(Files.readAllBytes(Paths.get("src/test/resources/invalid-yaml.yml")), StandardCharsets.UTF_8);

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue(\"" + inputData + "\", 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("validateYamlData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"), "class", "validation-message-failure"));
        Assert.assertEquals("mapping values are not allowed here\n" + " in 'string', line 7, column 27:\n"
                + "       pid.fail-on-write-error: true\n" + "                              ^\n", webDriver.findElement(By.id("validation-result-block")).getAttribute("textContent"));
    }

    @Test
    public void validateJsonService_whenValidJson_returnTrue() throws Exception {
        String inputData = new String(Files.readAllBytes(Paths.get("src/test/resources/valid-json.json")), StandardCharsets.UTF_8);

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue('" + inputData + "', 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("validateJsonData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"),"textContent","Valid JSON!!!"));
    }

    @Test
    public void validateJsonService_whenInvalidJson_returnFalse() throws Exception {
        String inputData = new String(Files.readAllBytes(Paths.get("src/test/resources/invalid-json.json")), StandardCharsets.UTF_8);

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue('" + inputData + "', 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("validateJsonData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"), "class", "validation-message-failure"));
        Assert.assertEquals("Expected a ',' or '}' at 129 [character 21 line 7]", webDriver.findElement(By.id("validation-result-block")).getAttribute("textContent"));
    }

    @Test
    public void formatJsonService_whenValidJson_returnTrue() throws Exception {
        String inputData = new String(Files.readAllBytes(Paths.get("src/test/resources/valid-json.json")), StandardCharsets.UTF_8);

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue('" + inputData + "', 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("formatJsonData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"),"textContent","Formatted JSON!!!"));
    }

    @Test
    public void formatJsonService_whenInvalidJson_returnFalse() throws Exception {
        String inputData = new String(Files.readAllBytes(Paths.get("src/test/resources/invalid-json.json")), StandardCharsets.UTF_8);

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue('" + inputData + "', 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("formatJsonData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"), "class", "validation-message-failure"));
        Assert.assertEquals("Expected a ',' or '}' at 129 [character 21 line 7]", webDriver.findElement(By.id("validation-result-block")).getAttribute("textContent"));
    }

    @Test
    public void formatXmlService_whenValidXml_returnTrue() throws Exception {
        String inputData = new String(Files.readAllBytes(Paths.get("src/test/resources/valid-xml.xml")), StandardCharsets.UTF_8);

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue('" + inputData + "', 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("formatXmlData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"),"textContent","Formatted XML!!!"));
    }

    @Test
    public void formatXmlService_whenInvalidXml_returnFalse() throws Exception {
        String inputData = new String(Files.readAllBytes(Paths.get("src/test/resources/invalid-xml.xml")), StandardCharsets.UTF_8);

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue('" + inputData + "', 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("formatXmlData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"), "class", "validation-message-failure"));
        Assert.assertEquals("Element type \"pages1020\" must be followed by either attribute specifications, \">\" or \"/>\".", webDriver.findElement(By.id("validation-result-block")).getAttribute("textContent"));
    }

    @Test
    public void encodeBase64_whenValidContent_returnTrue() throws Exception {
        String inputData = "Anand";

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue(\"" + inputData + "\", 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("encodeData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"),"textContent","Encode Successful!!!"));
    }

    @Test
    public void decodeBase64_whenValidContent_returnTrue() throws Exception {
        String inputData = "QW5hbmQ=";

        inputData = inputData.replace("\n", "\\n");
        String script = "editor = ace.edit(\"" + EDITOR_HTML_ID + "\");\n" +
                "editor.setValue(\"" + inputData + "\", 0);\n" +
                "editor.clearSelection();";
        ((JavascriptExecutor) webDriver).executeScript(script);
        webDriver.findElement(By.id("decodeData")).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(20).getSeconds())
                .until(ExpectedConditions.attributeContains(By.id("validation-result-block"),"textContent","Decode Successful!!!"));
    }
}