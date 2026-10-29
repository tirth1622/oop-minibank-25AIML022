package minibank.model.interfaces;

/**
 * Practical 6: Capability interface with default methods for interest calculations.
 */
public interface InterestBearing {
    double interestRate();
    long getBalance();

    /**
     * Default method calculating annual interest based on interest rate and balance.
     */
    default double yearlyInterest() {
        return (getBalance() * interestRate()) / 100.0;
    }

    /**
     * Supplementary Practical 6: Second default method calculating estimated monthly interest.
     */
    default double monthlyInterestEstimate() {
        return yearlyInterest() / 12.0;
    }
}
