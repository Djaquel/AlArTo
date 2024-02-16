package fr.eni.auctionapp.hmi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        // Log the error details
        logger.error("An error occurred:", e);

        // Add error details to the model
        model.addAttribute("error", e);

        // Forward to the custom error page
        return "pages/error";
    }
}
