package exception;

public class GetRecommendationHttpException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    public GetRecommendationHttpException() {
        super();
    }

    public GetRecommendationHttpException(String message) {
        super(message);
    }

    public GetRecommendationHttpException(Throwable e) {
        super(e);
    }

    public GetRecommendationHttpException(String message, Throwable e) {
        super(message, e);
    }
}
