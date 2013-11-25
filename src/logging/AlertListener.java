package logging;

public interface AlertListener {
    /**
     * Called when an alert occurs.
     * @param alert The alert that happened.
     */
    public void alertOccurred(Alert alert);
}
