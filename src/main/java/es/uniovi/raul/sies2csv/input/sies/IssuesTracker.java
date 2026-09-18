package es.uniovi.raul.sies2csv.input.sies;

/**
 * Interface for tracking issues found during validation of student data.
 */
public interface IssuesTracker {

    void notifyMissingEmail(int row);

    void notifyMissingName(int row);

}
