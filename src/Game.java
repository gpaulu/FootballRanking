
public class Game {
	private String home;
	private String away;
	private int homeScore;
	private int awayScore;
	public String getHome() {
		return home;
	}
	public String getAway() {
		return away;
	}
	public int getHomeScore() {
		return homeScore;
	}
	public int getAwayScore() {
		return awayScore;
	}
	public int getScoreDiff(){
		return Math.max(homeScore, awayScore) - Math.min(homeScore, awayScore);
	}
	public String winner(){
		if(this.homeScore >= this.awayScore){
			return this.home;
		}
		return this.away;
	}
	public String loser(){
		if(this.homeScore >= this.awayScore){
			return this.away;
		}
		return this.home;
	}
	public Game(String home, int homeScore, String away, int awayScore){
		this.home = home;
		this.homeScore = homeScore;
		this.away = away;
		this.awayScore = awayScore;
	}
}
