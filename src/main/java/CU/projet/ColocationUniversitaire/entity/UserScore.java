package CU.projet.ColocationUniversitaire.entity;

public class UserScore {
    private User user;
    private double score;

    public UserScore(User user, double score) {
        this.user = user;
        this.score = score;
    }
    public User getUser() { return user; }
    public double getScore() { return score; }
}
