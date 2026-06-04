package org.firstinspires.ftc.teamcode;

// See https://www.desmos.com/calculator/qagcguclh6 for data and calculations
public class ShooterMap {
    enum PredictionMode {
        LINEAR,
        QUADRATIC
    }

    private final PredictionMode predictionMode = PredictionMode.QUADRATIC;

    // Linear Regression Parameters
    private final double lineCoeff1 = 0.0;
    private final double lineCoeff0 = 0.0;

    // Quadratic Regression Parameters
    private final double quadCoeff2 = 0.0;
    private final double quadCoeff1 = 0.0;
    private final double quadCoeff0 = 0.0;

   private double linearRegression(double x) {
       return lineCoeff1 * x + lineCoeff0;
   }

   private double quadraticRegression(double x) {
       return Math.pow(quadCoeff2 * x, 2) + quadCoeff1 * x + quadCoeff0;
   }

   public double predictPower(double distance) {
        switch (predictionMode) {
            case LINEAR:
                return linearRegression(distance);
            case QUADRATIC:
                return quadraticRegression(distance);
            default:
                throw new RuntimeException("Invalid Prediction Mode");
        }
   }
}
