package es.uniovi.raul.sies2csv.main;

import es.uniovi.raul.sies2csv.input.sies.IssuesTracker;

/**
 * A simple implementation of the IssuesTracker interface that counts the number of issues.
 */
public final class IssuesCounter implements IssuesTracker {

    private int missingEmailCount = 0;
    private int missingNameCount = 0;

    @Override
    public void notifyMissingEmail(int row) {
        missingEmailCount++;
    }

    @Override
    public void notifyMissingName(int row) {
        missingNameCount++;
    }

    public int getMissingEmailCount() {
        return missingEmailCount;
    }

    public int getMissingNameCount() {
        return missingNameCount;
    }

    public boolean hasIssues() {
        return (missingEmailCount + missingNameCount) > 0;
    }

}
