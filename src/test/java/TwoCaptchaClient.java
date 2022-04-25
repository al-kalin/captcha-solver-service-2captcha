import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;

import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.ReCaptcha;

public class TwoCaptchaClient {

    /**
     * Solver for captcha.
     *
     * @param url - Site url with captcha.
     */
    public static void reCaptcha(String url) throws Exception {

        //get api key from: https://2captcha.com/enterpage
        final String TWO_CAPTCHA_API_KEY = "TWO_CAPTCHA_API_KEY_HERE";

        //get data-SiteKey from captcha
        String dataSiteKey;
        dataSiteKey = $x("//*[@id='recaptcha']").getAttribute("data-sitekey");

        //init API with your access api key
        TwoCaptcha solver = new TwoCaptcha(TWO_CAPTCHA_API_KEY);

        //init captcha with parameters
        ReCaptcha captcha = new ReCaptcha();
        captcha.setSiteKey(dataSiteKey);
        captcha.setUrl(url);
        captcha.setInvisible(true);
        captcha.setAction("verify");
        //twocaptchauser@email.com and twoCaptchaUserPassword - login and password for 2captcha service account
        captcha.setProxy("HTTPS", "twocaptchauser@email.com:twoCaptchaUserPassword@123.123.123.123:3128");

        //check account balance
        double balance = solver.balance();

        //get the result of solving captcha and handle errors
        String result = null;

        try {
            solver.solve(captcha);
            System.out.println("Captcha solved: " + captcha.getCode());
            result = captcha.getCode();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        //set the captcha solution answer in g-recaptcha-response
        executeJavaScript("document.getElementById('g-recaptcha-response').value='" + result + "'");
        executeJavaScript("googleRecaptchaCallback()");
    }
}
