package nl.inholland.BankingAPI.exception;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message,
                           String description)
{
    public int getStatusCode()
    {
        return statusCode;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public String getMessage()
    {
        return message;
    }

    public String getDescription()
    {
        return description;
    }
}