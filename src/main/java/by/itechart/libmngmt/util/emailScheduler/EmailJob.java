package by.itechart.libmngmt.util.emailScheduler;

import by.itechart.libmngmt.dto.ReaderCardDto;
import by.itechart.libmngmt.service.ReaderCardService;
import by.itechart.libmngmt.service.impl.ReaderCardServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.List;

/**
 * Provides method executing jobs.
 */
public class EmailJob implements Job {
    public static final int SEVEN_DAYS_BEFORE = 7;
    public static final int ONE_DAY_BEFORE = 1;
    public static final int ONE_DAY_AFTER = -1;
    private ReaderCardService readerCardService = ReaderCardServiceImpl.getInstance();
    private MailTemplate mailTemplate = MailTemplate.getInstance();
    private EmailSender emailSender = EmailSender.getInstance();

    /**
     * Invokes methods that send emails for users using different templates.
     *
     * @param context JobExecutionContext object
     */
    public void execute(JobExecutionContext context) {
        sendSevenDaysExpiringEmails();
        sendOneDayExpiringEmails();
        sendOneDayExpiredEmails();
    }

    private void sendSevenDaysExpiringEmails() {
        List<ReaderCardDto> sevenDaysBeforeReaderCards = readerCardService.getExpiringReaderCards(SEVEN_DAYS_BEFORE);
        if (sevenDaysBeforeReaderCards != null) {
            for (ReaderCardDto readerCard : sevenDaysBeforeReaderCards) {
                emailSender.sendEmail(readerCard.getReaderEmail(),
                        mailTemplate.getMessageSevenDaysBefore(readerCard.getBookTitle(), readerCard.getReaderName()));
            }
        }
    }

    private void sendOneDayExpiringEmails() {
        List<ReaderCardDto> oneDayBeforeReaderCards = readerCardService.getExpiringReaderCards(ONE_DAY_BEFORE);
        if (oneDayBeforeReaderCards != null) {
            for (ReaderCardDto readerCard : oneDayBeforeReaderCards) {
                emailSender.sendEmail(readerCard.getReaderEmail(),
                        mailTemplate.getMessageOneDayBefore(readerCard.getBookTitle(), readerCard.getReaderName()));
            }
        }
    }

    private void sendOneDayExpiredEmails() {
        List<ReaderCardDto> oneDayAfterReaderCards = readerCardService.getExpiringReaderCards(ONE_DAY_AFTER);
        if (oneDayAfterReaderCards != null) {
            for (ReaderCardDto readerCard : oneDayAfterReaderCards) {
                emailSender.sendEmail(readerCard.getReaderEmail(),
                        mailTemplate.getMessageOneDayAfter(readerCard.getBookTitle(), readerCard.getReaderName()));
            }
        }
    }
}
