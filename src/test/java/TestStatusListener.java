import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestStatusListener extends BaseTest implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);
        captureScreenshot(result.getName());
        System.out.println(String.format("Test failed: %s", result.getName()));
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
        captureScreenshot(result.getName());
        System.out.println(String.format("Test failed with timeout: %s", result.getName()));
    }
}